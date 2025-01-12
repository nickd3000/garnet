package com.physmo.garnet.tilegrid;

import com.physmo.garnet.graphics.Graphics;
import com.physmo.garnet.graphics.SubImage;
import com.physmo.garnet.graphics.TileSheet;
import com.physmo.garnet.graphics.Viewport;

/**
 * The TileGridDrawer class is responsible for rendering a grid of tiles
 * based on a defined TileSheet and TileGridData configuration. It allows
 * setting the tile properties, viewport configuration, and drawing the
 * visible portion of the grid onto a Graphics object.
 */
public class TileGridDrawer {

    private int tileWidth, tileHeight;
    private int viewportId = -1;
    private TileSheet tileSheet;
    private TileGridData tileGridData;
    SubImage tileSubImage = new SubImage();

    public TileGridDrawer() {
        tileWidth = 16;
        tileHeight = 16;
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

    public void draw(Graphics g, int drawPosX, int drawPosY) {
        int prevViewport = g.getViewportManager().getActiveViewportId();
        g.setActiveViewport(viewportId);

        int[] tCoords;

        Viewport viewport = g.getViewportManager().getViewport(viewportId);

        double[] visibleRect = viewport.getVisibleRect();

        int xStart = (int) (visibleRect[0] / tileWidth) - 2;
        int yStart = (int) (visibleRect[1] / tileHeight) - 2;
        int xSize = (int) ((visibleRect[2]) / tileWidth) + 3;
        int ySize = (int) ((visibleRect[3]) / tileHeight) + 3;


        for (int y = yStart; y <= yStart + ySize; y++) {
            for (int x = xStart; x <= xStart + xSize; x++) {
                int tileId = tileGridData.getTileId(x, y);
                if (tileId == -1) continue;
                tCoords = tileSheet.getTileCoordsFromIndex(tileId);
                tileSheet.getSubImage(tCoords[0], tCoords[1], tileSubImage);
                g.drawImage(tileSubImage,
                        drawPosX + ((x) * (tileWidth)),
                        drawPosY + ((y) * (tileHeight)));
            }
        }

        g.setActiveViewport(prevViewport);
    }


    public int getViewportId() {
        return viewportId;
    }

    public TileGridDrawer setViewportId(int viewportId) {
        this.viewportId = viewportId;
        return this;
    }

}
