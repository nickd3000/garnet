package com.physmo.garnet.drawablebatch;

import com.physmo.garnet.Texture;

/**
 * Define the layout of a regularly spaced sprite sheet.
 */
public class TileSheet {
    private final int tilesWide;
    private final int tilesHigh;
    Texture texture;
    int tileWidth;
    int tileHeight;

    public TileSheet(Texture texture, int tileWidth, int tileHeight) {
        this.texture = texture;
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
        this.tilesWide = texture.getWidth() / tileWidth;
        this.tilesHigh = texture.getHeight() / tileHeight;
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

    public int[] getTileCoordsFromIndex(int index) {
        int y = index / tilesHigh;
        int x = index % tilesWide;
        return new int[]{x, y};
    }

    public int getTileIndexFromCoords(int x, int y) {
        return x + (y * tilesWide);
    }
}
