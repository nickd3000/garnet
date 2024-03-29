package com.physmo.garnet.text;

import com.physmo.garnet.graphics.Graphics;

/**
 * Interface that both our font classes support
 * in order to be used by the paragraph drawer
 */
public interface DrawableFont {
    void drawText(Graphics graphics, String text, int x, int y);

    int getLineHeight();

    int getStringWidth(String text);

    int getSpaceWidth();

    void setScale(double scale);
}
