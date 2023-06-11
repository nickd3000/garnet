package com.physmo.garnet.text;


import com.physmo.garnet.graphics.Graphics;

import java.util.ArrayList;
import java.util.List;

/**
 * Draw a paragraph of text that wraps lines according to the
 * supplied width.
 * TODO: cache analyzed strings
 */
public class ParagraphDrawer {
    DrawableFont font;
    int padY = 0;
    private final boolean showLineBreaks = false;
    public ParagraphDrawer(DrawableFont font) {
        this.font = font;
    }

    /**
     * Set how much to pad the vertical space between lines by.
     *
     * @param val
     */
    public void setPadY(int val) {
        padY = val;
    }

    /**
     * Draw the supplied string to the display, constrained to the supplied dimensions.
     *
     * @param graphics The graphics context to draw the paragraph to
     * @param text     The text to draw
     * @param width    Width of the window to format the text within
     * @param height   Height of the window to format text within
     * @param x        X coordinate of the drawn text
     * @param y        Y coordinate of the drawn text
     * @return The total height of the drawn paragraph.
     */
    public int drawParagraph(Graphics graphics, String text, int width, int height, int x, int y) {
        List<Token> wordInfos = analyzeText(font, text);

        int rx = 0; // rolling x
        int ry = 0; // rolling y
        boolean newLineSpaceConsume = true;
        for (Token token : wordInfos) {

            if (token.tokenType == TOKEN_TYPE.NEWLINE) {
                if (showLineBreaks) font.drawText(graphics, "<br>", x + rx, y + ry);
                rx = 0;
                ry += font.getLineHeight() + padY;
                newLineSpaceConsume = true;
                continue;
            }

            if (token.tokenType == TOKEN_TYPE.SPACE) {
                if (newLineSpaceConsume) continue;
                rx += font.getSpaceWidth();
                continue;
            }

            if (token.tokenType == TOKEN_TYPE.IGNORE) {
                continue;
            }

            if (token.tokenType == TOKEN_TYPE.WORD) {
                // Will this word wit within bounds?
                if (rx + token.width > width) {
                    rx = 0;
                    ry += font.getLineHeight() + padY;
                }

                font.drawText(graphics, token.text, x + rx, y + ry);
                rx += token.width;

                newLineSpaceConsume = false;
            }


            //rx += font.getSpaceWidth();
        }

        ry += font.getLineHeight() + padY;
        return ry;

    }

    /*
        Split string into words and measure the display size of each word.
     */
    private List<Token> analyzeText(DrawableFont font, String text) {
        List<Token> words = new ArrayList<>();

        int seekPosition = 0;
        while (seekPosition < text.length()) {
            Token newToken = getNextToken(text, seekPosition);
            if (newToken.tokenType == TOKEN_TYPE.WORD) newToken.width = font.getStringWidth(newToken.text);
            seekPosition += newToken.consumedCharacters;
            words.add(newToken);
        }

        return words;
    }

    public Token getNextToken(String text, int seekPosition) {
        if (seekPosition >= text.length()) {
            return new Token("", 1, TOKEN_TYPE.IGNORE);
        }

        if (text.charAt(seekPosition) == ' ') {
            return new Token(" ", 1, TOKEN_TYPE.SPACE);
        }

        if (text.charAt(seekPosition) == '\r') { // Ignore \r - part of new line on windows.
            return new Token("", 1, TOKEN_TYPE.IGNORE);
        }

        if (text.charAt(seekPosition) == '\n') {
            return new Token("", 1, TOKEN_TYPE.NEWLINE);
        }

        int pos = 0;
        StringBuilder word = new StringBuilder();
        //word.append(text.charAt(seekPosition));
        while (!isCharSeparator(text, seekPosition + pos)) {
            word.append(text.charAt(seekPosition + pos));
            pos++;
        }
        return new Token(word.toString(), pos, TOKEN_TYPE.WORD);
    }

    public boolean isCharSeparator(String text, int seekPosition) {
        if (seekPosition >= text.length()) return true;
        char c = text.charAt(seekPosition);
        if (c == ' ') return true;
        if (c == '\r') return true;
        return c == '\n';
    }

    enum TOKEN_TYPE {WORD, SPACE, NEWLINE, IGNORE}

    private class Token {

        String text;
        int width; // Number of characters to move scanning head by.
        TOKEN_TYPE tokenType;
        int consumedCharacters;

        public Token(String _text, int consumedCharacters, TOKEN_TYPE tokenType) {
            text = _text;
            width = 0;
            this.consumedCharacters = consumedCharacters;
            this.tokenType = tokenType;
        }
    }
}
