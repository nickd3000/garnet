package com.physmo.garnet.tilegrid;

import com.physmo.garnet.Texture;
import com.physmo.garnet.spritebatch.Sprite2D;
import com.physmo.garnet.spritebatch.SpriteBatch;

public class TileGridDrawer {

    double scrollX, scrollY;
    int spriteWidth, spriteHeight;
    int spriteDrawWidth, spriteDrawHeight;
    int drawScale = 3;
    int windowWidth = 10; // Measured in tiles
    int windowHeight = 10;// Measured in tiles
    private Texture texture;
    private SpriteBatch spriteBatch;
    private TileGridData tileGridData;

    public TileGridDrawer() {
        spriteWidth = 16;
        spriteHeight = 16;
        spriteDrawWidth = 16 * drawScale;
        spriteDrawHeight = 16 * drawScale;
    }

    public TileGridDrawer setSpriteBatch(SpriteBatch spriteBatch) {
        this.spriteBatch = spriteBatch;
        return this;
    }

    public TileGridDrawer setData(TileGridData tileGridData) {
        this.tileGridData = tileGridData;
        return this;
    }

    public TileGridDrawer setWindowSize(int width, int height) {
        windowWidth = width;
        windowHeight = height;
        return this;
    }

    public void draw() {

        int window_xx = ((int) scrollX) / spriteWidth;
        int window_yy = ((int) scrollY) / spriteHeight;
        int offsetX = (int) (scrollX % 16);
        int offsetY = (int) (scrollY % 16);

        for (int y = 0; y <= windowHeight; y++) {
            for (int x = 0; x <= windowWidth; x++) {
                Sprite2D sprite2D = new Sprite2D();

                int tileId = tileGridData.getTileId(window_xx + x, window_yy + y);

                sprite2D.setTile(tileId, 0, 16);
                sprite2D.setDrawPosition((x * 16) - offsetX, (y * 16) - offsetY, 16, 16);

                //spriteBatch.add(Sprite2D.build(x * spriteDrawWidth, y * spriteDrawHeight, spriteDrawWidth, spriteDrawHeight, 0, 0, spriteWidth, spriteHeight));
                spriteBatch.add(sprite2D);
            }
        }
    }

    public void setScroll(double scrollX, double scrollY) {
        this.scrollX = scrollX;
        this.scrollY = scrollY;
    }
}
