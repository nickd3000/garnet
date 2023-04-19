package com.physmo.garnet.drawablebatch;

import com.physmo.garnet.graphics.Graphics;

public interface DrawableElement {
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
}
