package com.physmo.garnet;


import com.physmo.garnet.graphics.Graphics;

import java.util.ArrayList;
import java.util.List;

public class ParagraphDrawer {
    DrawableFont font;
    int padY = 0;

    public ParagraphDrawer(DrawableFont font) {
        this.font = font;
    }


    public void setPadY(int val) {
        padY = val;
    }

    // TODO: cache analyzed strings.
    public void drawParagraph(Graphics g, String text, int width, int height, int x, int y) {
        List<WordInfo> wordInfos = analyzeText(font, text);

        int rx = 0; // rolling x
        int ry = 0; // rolling y

        for (WordInfo wordInfo : wordInfos) {
            if (rx + wordInfo.width > width) {
                rx = 0;
                ry += font.getLineHeight() + padY;
            }

            font.drawText(g, wordInfo.text, x + rx, y + ry);
            rx += wordInfo.width;
            rx += font.getSpaceWidth();
        }

    }


    public List<WordInfo> analyzeText(DrawableFont font, String text) {
        List<WordInfo> words = new ArrayList<>();
        String[] s = text.split(" ");
        for (int i = 0; i < s.length; i++) {
            words.add(new WordInfo(s[i], font.getStringWidth(s[i])));
        }
        return words;
    }

    class WordInfo {
        String text;
        int width;

        public WordInfo(String _text, int _width) {
            text = _text;
            width = _width;
        }
    }
}
