package com.physmo.garnet.drawablebatch;

import com.physmo.garnet.ColorUtils;
import com.physmo.garnet.graphics.Graphics;
import com.physmo.garnet.graphics.Viewport;

import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glScalef;
import static org.lwjgl.opengl.GL11.glTranslatef;

public abstract class DrawableElement {
    public static int SPRITE = 1;
    public static int LINE = 2;
    public static int CIRCLE = 3;
    public static int SHAPE = 4;
    public static int OTHER = 0;

    int drawOrder = 0;
    int color = 0xffffffff;
    float[] colorFloats = new float[4];
    Viewport viewport = null;

    public void setCommonValues(Viewport viewport, int drawOrder, int color) {
        this.viewport = viewport;
        this.drawOrder = drawOrder;
        setColor(color);

    }

    public final void setColor(int rgba) {
        color = rgba;
        ColorUtils.rgbToFloat(color, colorFloats);
    }


    public Viewport getViewport() {
        return viewport;
    }

    public void setViewport(Viewport viewport) {
        this.viewport = viewport;
    }

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

    public abstract int getType();

    public final void setColor(float[] c) {
        setColor(ColorUtils.floatToRgb(c[0], c[1], c[2], c[3]));
    }

    public final void setColor(float r, float g, float b, float a) {
        setColor(ColorUtils.floatToRgb(r, g, b, a));
    }

    //
    public void applyTranslation() {
        glPushMatrix();
        double z = viewport.getZoom();
        float xo = (float) (viewport.getWindowX() - (viewport.getX() * z));
        float yo = (float) (viewport.getWindowY() - (viewport.getY() * z));

        glTranslatef(xo, yo, 0);
        glScalef((float) z, (float) z, 1);
    }

    public void removeTranslation() {
        glPopMatrix();
    }

}
