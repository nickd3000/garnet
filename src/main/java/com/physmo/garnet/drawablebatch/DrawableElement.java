package com.physmo.garnet.drawablebatch;

import com.physmo.garnet.Utils;
import com.physmo.garnet.graphics.Graphics;

public abstract class DrawableElement {
    public static int SPRITE = 1;
    public static int LINE = 2;
    public static int CIRCLE = 3;
    public static int SHAPE = 4;
    public static int OTHER = 0;

    int drawOrder = 0;
    int clipRect = 0;
    int color = 0xffffffff;
    float[] colorFloats = new float[4];

    abstract void render(Graphics graphics);

    /**
     * Higher draw order elements get drawn on top of lower order elements.
     *
     * @return
     */
    public final int getDrawOrder() {
        return drawOrder;
    }

    public final void setDrawOrder(int drawOrder) {
        this.drawOrder = drawOrder;
    }

    abstract int getTextureId();

    abstract boolean hasTexture();

    public final int getClipRect() {
        return clipRect;
    }

    public final void setClipRect(int id) {
        clipRect = id;
    }

    public abstract int getType();

    public final void setColor(float[] c) {
        setColor(Utils.floatToRgb(c[0], c[1], c[2], c[3]));
    }

    public final void setColor(int rgba) {
        color = rgba;
        Utils.rgbToFloat(color, colorFloats);
    }

    public final void setColor(float r, float g, float b, float a) {
        setColor(Utils.floatToRgb(r, g, b, a));
    }
}
