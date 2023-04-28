package com.physmo.garnet.drawablebatch;

import com.physmo.garnet.graphics.Graphics;

public interface DrawableElement {
    int SPRITE = 1;
    int LINE = 2;
    int CIRCLE = 3;
    int OTHER = 0;

    void render(Graphics graphics);

    /**
     * Higher draw order elements get drawn on top of lower order elements.
     *
     * @return
     */
    int getDrawOrder();

    void setDrawOrder(int drawOrder);

    int getTextureId();

    boolean hasTexture();

    int getClipRect();

    int getType();
}
