package com.physmo.garnet.regularfont;

import com.physmo.garnet.DrawableFont;
import com.physmo.garnet.Texture;
import com.physmo.garnet.Utils;
import com.physmo.garnet.drawablebatch.TileSheet;
import com.physmo.garnet.graphics.Graphics;

import java.io.InputStream;

/**
 * RegularFont is a simpler font drawer.  It requires a font image file arranged in
 * a specific grid pattern to match the ascii character system.
 */
public class RegularFont implements DrawableFont {

    private final TileSheet tileSheet;
    private final Texture texture;
    private final int charWidth;
    private final int charHeight;
    private int horizontalPad = 0;

    public RegularFont(String path, int charWidth, int charHeight) {
        this(Utils.getFileFromResourceAsStream(path), charWidth, charHeight);
    }

    public RegularFont(InputStream inputStream, int charWidth, int charHeight) {
        this.charWidth = charWidth;
        this.charHeight = charHeight;
        texture = Texture.loadTexture(inputStream);
        tileSheet = new TileSheet(texture, charWidth, charHeight);
    }

    public int getHorizontalPad() {
        return horizontalPad;
    }

    public void setHorizontalPad(int horizontalPad) {
        this.horizontalPad = horizontalPad;
    }

    @Override
    public void drawText(Graphics graphics, String text, int x, int y) {
        if (!graphics.hasTexture(texture.getId()))
            graphics.addTexture(texture);

        renderText(graphics, text, x, y);
    }

    @Override
    public int getLineHeight() {
        return charHeight;
    }

    @Override
    public int getStringWidth(String text) {
        if (text == null) return 0;

        int textLength = text.length();
        return textLength * (charWidth + horizontalPad);

    }

    @Override
    public int getSpaceWidth() {
        return charWidth;
    }

    private void renderText(Graphics graphics, String text, int x, int y) {

        if (text == null) return;

        int textLength = text.length();
        int xPos = x, yPos = y;
        for (int i = 0; i < textLength; i++) {
            char c = text.charAt(i);
            renderChar(graphics, c, xPos, yPos);
            xPos += charWidth + horizontalPad;
        }
    }


    public void renderChar(Graphics graphics, char c, int x, int y) {
        int cy = ((int) c) / 16;
        int cx = ((int) c) % 16;

        graphics.drawImage(tileSheet, x, y, cx, cy);
    }


}

