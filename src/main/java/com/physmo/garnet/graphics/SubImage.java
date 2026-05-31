package com.physmo.garnet.graphics;

/**
 * Describes a rectangular region within a {@link Texture}.
 * Does not hold pixel data — it is a lightweight descriptor used to identify
 * a sub-region such as a single sprite within a tile sheet.
 */
public class SubImage {
    public Texture texture;
    public int x;
    public int y;
    public int w;
    public int h;

    public SubImage() {
        texture = null;
        x = 0;
        y = 0;
        w = 0;
        h = 0;
    }

    public SubImage(Texture texture, int x, int y, int w, int h) {
        this.texture = texture;
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    /**
     * Reconfigures this SubImage to point to a new region within a texture.
     * Useful when reusing a SubImage instance to avoid allocation.
     *
     * @param texture the source texture
     * @param x       the x-coordinate of the region's top-left corner in the texture
     * @param y       the y-coordinate of the region's top-left corner in the texture
     * @param w       the width of the region in pixels
     * @param h       the height of the region in pixels
     */
    public void configure(Texture texture, int x, int y, int w, int h) {
        this.texture = texture;
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }
}
