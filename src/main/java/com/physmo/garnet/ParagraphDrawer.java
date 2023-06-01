package com.physmo.garnet;


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
        List<WordMetrics> wordInfos = analyzeText(font, text);

        int rx = 0; // rolling x
        int ry = 0; // rolling y

        for (WordMetrics wordInfo : wordInfos) {

            if (wordIsCarriageReturn(wordInfo.text)) {
                rx = 0;
                ry += font.getLineHeight() + padY;
                continue;
            }

            if (rx + wordInfo.width > width) {
                rx = 0;
                ry += font.getLineHeight() + padY;
            }

            font.drawText(graphics, wordInfo.text, x + rx, y + ry);
            rx += wordInfo.width;
            rx += font.getSpaceWidth();
        }

        ry += font.getLineHeight() + padY;
        return ry;

    }

    private boolean wordIsCarriageReturn(String str) {
        if (str.equals(("\n"))) return true;
        return str.equals(System.lineSeparator());
    }

    /*
        Split string into words and measure the display size of each word.
     */
    private List<WordMetrics> analyzeText(DrawableFont font, String text) {
        List<WordMetrics> words = new ArrayList<>();
        String[] s = text.split(" ");
        for (int i = 0; i < s.length; i++) {
            words.add(new WordMetrics(s[i], font.getStringWidth(s[i])));
        }
        return words;
    }

    private class WordMetrics {
        String text;
        int width;

        public WordMetrics(String _text, int _width) {
            text = _text;
            width = _width;
        }
    }
}
