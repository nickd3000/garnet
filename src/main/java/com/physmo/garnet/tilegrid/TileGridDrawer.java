package com.physmo.garnet.tilegrid;

import com.physmo.garnet.drawablebatch.TileSheet;
import com.physmo.garnet.graphics.Graphics;

/**
 * Note: window position and size will be scaled by the scale value.
 */
public class TileGridDrawer {

    private double scrollX = 0, scrollY = 0;
    private int tileWidth, tileHeight;
    private int scale = 1;
    private int windowWidth = 100; // Measured in unscaled pixels
    private int windowHeight = 100;// Measured in unscaled pixels
    private int clipRectId = -1;
    private int screenOffsetX;
    private int screenOffsetY;
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
        int offsetX = ((int) scrollX % tileWidth);
        int offsetY = ((int) scrollY % tileHeight);
        int[] tCoords;

        graphics.setScale(scale);

        int windowHeightInTiles = windowHeight / tileHeight;
        int windowWidthInTiles = windowWidth / tileWidth;
        screenOffsetX = (int) (drawPosX - scrollX);
        screenOffsetY = (int) (drawPosY - scrollY);

        for (int y = -1; y <= windowHeightInTiles + 1; y++) {
            for (int x = -1; x <= windowWidthInTiles + 1; x++) {
                int tileId = tileGridData.getTileId(window_xx + x, window_yy + y);
                if (tileId == -1) continue;
                tCoords = tileSheet.getTileCoordsFromIndex(tileId);
                graphics.drawImage(tileSheet,
                        drawPosX + ((x) * (tileWidth)) - offsetX,
                        drawPosY + ((y) * (tileHeight)) - offsetY,
                        tCoords[0], tCoords[1]);
            }
        }

        graphics.disableClipRect();

    }

    private void setClipRect(Graphics graphics, int x, int y) {
        if (clipRectId == -1) {
            clipRectId = graphics.getAvailableClipRectId();
            System.out.println("id " + clipRectId);
        }
        graphics.addClipRect(clipRectId, x * scale, y * scale, windowWidth * scale, windowHeight * scale);
        graphics.setActiveClipRect(clipRectId);
    }

    public int getClipRectId() {
        return clipRectId;
    }

    public void setScroll(double scrollX, double scrollY) {
        this.scrollX = scrollX;
        this.scrollY = scrollY;
        clampScroll();
    }

    private void clampScroll() {
        int[] scrollExtents = getScrollExtents();
        if (scrollX < 0) scrollX = 0;
        if (scrollY < 0) scrollY = 0;
        if (scrollX >= scrollExtents[0]) scrollX = scrollExtents[0];
        if (scrollY >= scrollExtents[1]) scrollY = scrollExtents[1];
    }

    public int[] getScrollExtents() {
        int maxX = (tileWidth * (tileGridData.width)) - windowWidth;
        int maxY = (tileHeight * (tileGridData.height)) - windowHeight;
//        maxX /= scale;
//        maxY /= scale;
        return new int[]{maxX, maxY};
    }

    public int[] translateMapToScreenPosition(int x, int y) {
        int[] translated = new int[2];
        translated[0] = x + screenOffsetX;
        translated[1] = y + screenOffsetY;
        return translated;
    }
}
