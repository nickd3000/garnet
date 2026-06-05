package com.physmo.garnet.drawablebatch;

import com.physmo.garnet.ColorUtils;
import com.physmo.garnet.graphics.Graphics;
import com.physmo.garnet.graphics.ShaderProgram;
import com.physmo.garnet.graphics.Viewport;

import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glScalef;
import static org.lwjgl.opengl.GL11.glTranslatef;

/**
 * Base class for all renderable elements managed by {@link DrawableBatch}.
 * <p>
 * Subclasses represent concrete drawable types (sprites, lines, circles, etc.)
 * and implement {@link #render(Graphics)} to issue the actual GL draw calls.
 * Common properties such as draw order, colour, blend mode, viewport, and an
 * optional {@link ShaderProgram} are stored here and applied by the batch
 * renderer before each element is drawn.
 */
public abstract class DrawableElement {
    /**
     * Element type constant for a sprite.
     */
    public static final int SPRITE = 1;
    /** Element type constant for a line. */
    public static final int LINE = 2;
    /** Element type constant for a circle. */
    public static final int CIRCLE = 3;
    /** Element type constant for a filled shape. */
    public static final int SHAPE = 4;
    /** Element type constant for an unclassified element. */
    public static final int OTHER = 0;

    int drawOrder = 0;
    int color = 0xffffffff;
    float[] colorFloats = new float[4];
    Viewport viewport = null;
    private BlendMode blendMode = BlendMode.NORMAL;
    private boolean colorOverride = false;
    private ShaderProgram shader = null;

    /**
     * Restores mutable render state that must not leak between pooled element uses.
     * Geometry and texture fields remain the responsibility of concrete subclasses.
     */
    protected void resetCommonState() {
        blendMode = BlendMode.NORMAL;
        colorOverride = false;
        shader = null;
    }

    /** Returns the blend mode used when rendering this element. */
    public BlendMode getBlendMode() {
        return blendMode;
    }

    /**
     * Sets the blend mode for this element.
     *
     * @param blendMode the desired {@link BlendMode}
     * @return this element (for chaining)
     */
    public DrawableElement setBlendMode(BlendMode blendMode) {
        this.blendMode = blendMode;
        return this;
    }

    /**
     * Returns the {@link ShaderProgram} assigned to this element, or {@code null} if none.
     */
    public ShaderProgram getShader() {
        return shader;
    }

    /**
     * Assigns a {@link ShaderProgram} to this element.  The shader will be
     * activated by the batch renderer just before this element is drawn.
     *
     * @param shader the shader to use, or {@code null} for the default pipeline
     * @return this element (for chaining)
     */
    public DrawableElement setShader(ShaderProgram shader) {
        this.shader = shader;
        return this;
    }

    /**
     * Removes any assigned shader, reverting to the default rendering pipeline.
     *
     * @return this element (for chaining)
     */
    public DrawableElement clearShader() {
        this.shader = null;
        return this;
    }

    /** Returns {@code true} if colour-override mode is active for this element. */
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

    /**
     * Sets the common rendering properties shared by all element types.
     * Called internally by the graphics system when a draw call is issued.
     *
     * @param viewport  the viewport to render this element within
     * @param drawOrder the draw order (higher values render on top)
     * @param color     the tint colour as a packed RGBA integer
     */
    public void setCommonValues(Viewport viewport, int drawOrder, int color) {
        this.viewport = viewport;
        this.drawOrder = drawOrder;
        setColor(color);

    }

    /**
     * Sets the tint colour from a packed RGBA integer (0xRRGGBBAA).
     *
     * @param rgba packed colour value
     */
    public final void setColor(int rgba) {
        color = rgba;
        ColorUtils.rgbToFloat(color, colorFloats);
    }


    /** Returns the viewport this element is rendered within. */
    public Viewport getViewport() {
        return viewport;
    }

    /**
     * Overrides the viewport for this element.
     *
     * @param viewport the viewport to use
     */
    public void setViewport(Viewport viewport) {
        this.viewport = viewport;
    }

    /**
     * Issues the GL draw calls for this element.
     * Called by {@link DrawableBatch} during the render pass.
     *
     * @param graphics the active {@link Graphics} context
     */
    abstract void render(Graphics graphics);

    /**
     * Returns the draw order for this element.
     * Higher draw order elements are drawn on top of lower order elements.
     *
     * @return the draw order value
     */
    public final int getDrawOrder() {
        return drawOrder;
    }

    /**
     * Sets the draw order for this element.
     *
     * @param drawOrder the draw order value; higher values render on top
     */
    public final void setDrawOrder(int drawOrder) {
        this.drawOrder = drawOrder;
    }

    /** Returns the GL texture ID used by this element, or 0 if none. */
    abstract int getTextureId();

    /**
     * Returns the element type constant (e.g. {@link #SPRITE}, {@link #LINE}).
     *
     * @return element type
     */
    public abstract int getType();

    /**
     * Sets the tint colour from a float array {@code [r, g, b, a]} in the range 0–1.
     *
     * @param c float array with four components: red, green, blue, alpha
     */
    public final void setColor(float[] c) {
        setColor(ColorUtils.floatToRgb(c[0], c[1], c[2], c[3]));
    }

    /**
     * Sets the tint colour from individual float components in the range 0–1.
     *
     * @param r red component
     * @param g green component
     * @param b blue component
     * @param a alpha component
     */
    public final void setColor(float r, float g, float b, float a) {
        setColor(ColorUtils.floatToRgb(r, g, b, a));
    }

    /**
     * Pushes the current GL matrix and applies the viewport's scroll and zoom transform.
     * Must be paired with a call to {@link #popViewportTransform()}.
     */
    public void pushViewportTransform(Graphics graphics) {
        glPushMatrix();
        double z = viewport.getZoom();

        float xo, yo;

        xo = (float) (viewport.getWindowX() - (viewport.getScrollX() * z));
        yo = (float) (viewport.getWindowY() - (viewport.getScrollY() * z));

        glTranslatef(xo, yo, 0);
        glScalef((float) z, (float) z, 1);
    }

    /** Pops the GL matrix pushed by {@link #pushViewportTransform()}. */
    public void popViewportTransform() {
        glPopMatrix();
    }

}
