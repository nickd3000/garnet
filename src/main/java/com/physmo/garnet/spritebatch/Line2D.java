package com.physmo.garnet.spritebatch;

import com.physmo.garnet.graphics.Graphics;

public class Line2D implements BatchElement {

    int drawOrder;

    @Override
    public void render(Graphics graphics) {

    }

    @Override
    public int getDrawOrder() {
        return drawOrder;
    }

    @Override
    public void setDrawOrder(int drawOrder) {
        this.drawOrder = drawOrder;
    }

    @Override
    public int getTextureId() {
        return 0;
    }

    @Override
    public boolean hasTexture() {
        return false;
    }
}
