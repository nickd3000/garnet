package com.physmo.garnet.drawablebatch;

import com.physmo.garnet.ColorUtils;
import com.physmo.garnet.graphics.Graphics;
import com.physmo.garnet.graphics.Viewport;

import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glScalef;
import static org.lwjgl.opengl.GL11.glTranslatef;

public abstract class DrawableElement {
    public static final int SPRITE = 1;
    public static final int LINE = 2;
    public static final int CIRCLE = 3;
    public static final int SHAPE = 4;
    public static final int OTHER = 0;

    int drawOrder = 0;
    int color = 0xffffffff;
    float[] colorFloats = new float[4];
    Viewport viewport = null;
    private BlendMode blendMode = BlendMode.NORMAL;
    private boolean colorOverride = false;

    public BlendMode getBlendMode() {
        return blendMode;
    }

    public DrawableElement setBlendMode(BlendMode blendMode) {
        this.blendMode = blendMode;
        return this;
    }

    public boolean isColorOverride() {
        return colorOverride;
    }

    /**
     * When enabled, every visible pixel of this element is rendered using the current
     * vertex colour instead of the texture colour.  The texture's alpha channel is still
     * used to determine which pixels are transparent, so the sprite's silhouette is
     * preserved.  Useful for hit-flash effects (e.g. flash white on damage).
     *
     * @param override true to replace texture RGB with the vertex colour; false for normal rendering
     * @return this element (for chaining)
     */
    public DrawableElement setColorOverride(boolean override) {
        this.colorOverride = override;
        return this;
    }

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

    public abstract int getType();

    public final void setColor(float[] c) {
        setColor(ColorUtils.floatToRgb(c[0], c[1], c[2], c[3]));
    }

    public final void setColor(float r, float g, float b, float a) {
        setColor(ColorUtils.floatToRgb(r, g, b, a));
    }

    public void pushViewportTransform() {
        glPushMatrix();
        double z = viewport.getZoom();
        float xo = (float) (viewport.getWindowX() - (viewport.getX() * z));
        float yo = (float) (viewport.getWindowY() - (viewport.getY() * z));

        glTranslatef(xo, yo, 0);
        glScalef((float) z, (float) z, 1);
    }

    public void popViewportTransform() {
        glPopMatrix();
    }

}
