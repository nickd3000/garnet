package com.physmo.garnet.tilegrid;

import com.physmo.garnet.graphics.Graphics;
import com.physmo.garnet.spritebatch.TileSheet;

// TODO: we need to clip this window
public class TileGridDrawer {

    double scrollX, scrollY;
    int tileWidth, tileHeight;
    int spriteDrawWidth, spriteDrawHeight;
    int scale = 3;
    int windowWidthInTiles = 10; // Measured in tiles
    int windowHeightInTiles = 10;// Measured in tiles
    private TileSheet tileSheet;
    private TileGridData tileGridData;

    public TileGridDrawer() {
        tileWidth = 16;
        tileHeight = 16;
        spriteDrawWidth = 16 * scale;
        spriteDrawHeight = 16 * scale;
    }

    public TileGridDrawer setTileSheet(TileSheet tileSheet) {
        this.tileSheet = tileSheet;
        return this;
    }

    public TileGridDrawer setTileSheet(int scale) {
        this.scale = scale;
        return this;
    }

    public TileGridDrawer setTileSize(int tileWidth, int tileHeight) {
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
        return this;
    }

    public TileGridDrawer setData(TileGridData tileGridData) {
        this.tileGridData = tileGridData;
        return this;
    }

    public TileGridDrawer setWindowSize(int windowWidthInTiles, int windowHeightInTiles) {
        this.windowWidthInTiles = windowWidthInTiles;
        this.windowHeightInTiles = windowHeightInTiles;
        return this;
    }

    public void draw(Graphics graphics, int drawPosX, int drawPosY) {

        int window_xx = ((int) scrollX) / tileWidth;
        int window_yy = ((int) scrollY) / tileHeight;
        int offsetX = (int) (scrollX % 16);
        int offsetY = (int) (scrollY % 16);
        int[] tCoords;

        graphics.setScale(scale);

        for (int y = 0; y <= windowHeightInTiles; y++) {
            for (int x = 0; x <= windowWidthInTiles; x++) {
                int tileId = tileGridData.getTileId(window_xx + x, window_yy + y);

                tCoords = tileSheet.getTileCoordsFromIndex(tileId);
                graphics.drawImage(tileSheet, drawPosX + (x * 16) - offsetX, drawPosY + (y * 16) - offsetY, tCoords[0], tCoords[1]);
            }
        }
    }

    public void setScroll(double scrollX, double scrollY) {
        this.scrollX = scrollX;
        this.scrollY = scrollY;
    }
}
