package com.physmo.garnet.spritebatch;

import com.physmo.garnet.Texture;

public class TileSheet {
    Texture texture;
    int tileWidth;
    int tileHeight;

    public TileSheet(Texture texture, int tileWidth, int tileHeight) {
        this.texture = texture;
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
    }

    public Texture getTexture() {
        return texture;
    }

    public int getTileWidth() {
        return tileWidth;
    }

    public int getTileHeight() {
        return tileHeight;
    }

}
