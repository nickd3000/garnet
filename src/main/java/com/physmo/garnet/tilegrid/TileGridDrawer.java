package com.physmo.garnet.tilegrid;

import com.physmo.garnet.graphics.Graphics;
import com.physmo.garnet.spritebatch.TileSheet;

// TODO: we need to clip this window
public class TileGridDrawer {

    double scrollX = 0, scrollY = 0;
    int tileWidth, tileHeight;
    int scale = 3;
    int windowWidth = 100; // Measured in unscaled pixels
    int windowHeight = 100;// Measured in unscaled pixels
    private TileSheet tileSheet;
    private TileGridData tileGridData;

    public TileGridDrawer() {
        tileWidth = 16;
        tileHeight = 16;
    }

    public TileGridDrawer setScale(int scale) {
        this.scale = scale;
        return this;
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

    public TileGridDrawer setWindowSize(int windowWidth, int windowHeight) {
        this.windowWidth = windowWidth;
        this.windowHeight = windowHeight;
        return this;
    }

    public void draw(Graphics graphics, int drawPosX, int drawPosY) {
        setClipRect(graphics, drawPosX, drawPosY);
        int window_xx = ((int) scrollX) / tileWidth;
        int window_yy = ((int) scrollY) / tileHeight;
        int offsetX = (int) (scrollX % tileWidth);
        int offsetY = (int) (scrollY % tileHeight);
        int[] tCoords;

        graphics.setScale(scale);

        int windowHeightInTiles = windowHeight / (tileHeight * scale);
        int windowWidthInTiles = windowWidth / (tileWidth * scale);


        for (int y = -1; y <= windowHeightInTiles; y++) {
            for (int x = -1; x <= windowWidthInTiles; x++) {
                int tileId = tileGridData.getTileId(window_xx + x, window_yy + y);
                if (tileId == -1) continue;
                tCoords = tileSheet.getTileCoordsFromIndex(tileId);
                graphics.drawImage(tileSheet,
                        drawPosX + ((x - 2) * 16) - offsetX,
                        drawPosY + ((y - 2) * 16) - offsetY,
                        tCoords[0], tCoords[1]);
            }
        }

        graphics.disableClipRect();

    }

    private void setClipRect(Graphics graphics, int x, int y) {
        graphics.addClipRect(100, x, y, windowWidth, windowHeight);
        graphics.setActiveClipRect(100);
    }

    public void setScroll(double scrollX, double scrollY) {
        this.scrollX = scrollX;
        this.scrollY = scrollY;
        clampScroll();
    }

    public void clampScroll() {
        int maxX = (tileWidth * (tileGridData.width) * scale) - windowWidth;
        int maxY = (tileHeight * (tileGridData.height) * scale) - windowHeight;
        maxX /= scale;
        maxY /= scale;
        if (scrollX < 0) scrollX = 0;
        if (scrollY < 0) scrollY = 0;
        if (scrollX >= maxX) scrollX = maxX;
        if (scrollY >= maxY) scrollY = maxY;
    }
}
