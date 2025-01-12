package com.physmo.garnet.graphics;

/*
    A description of an area of an image, this does not contain image data.
    As an example, it can be used to describe the position and size of a single sprite
    in a tile sheet.
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

    public void configure(Texture texture, int x, int y, int w, int h) {
        this.texture = texture;
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }
}
