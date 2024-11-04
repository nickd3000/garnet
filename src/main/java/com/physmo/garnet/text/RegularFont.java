package com.physmo.garnet.text;

import com.physmo.garnet.FileUtils;
import com.physmo.garnet.graphics.Graphics;
import com.physmo.garnet.graphics.Texture;
import com.physmo.garnet.graphics.TileSheet;

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
    double scale = 1;
    private int horizontalPad = 0;

    public RegularFont(String path, int charWidth, int charHeight) {
        this(FileUtils.getFileFromResourceAsStream(path), charWidth, charHeight);
    }

    public RegularFont(InputStream inputStream, int charWidth, int charHeight) {
        this.charWidth = charWidth;
        this.charHeight = charHeight;
        texture = Texture.loadTexture(inputStream);
        tileSheet = new TileSheet(texture, charWidth, charHeight);
    }

    public int getHorizontalPad() {
        return (int) (horizontalPad * scale);
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
        return (int) (charHeight * scale);
    }

    @Override
    public int getStringWidth(String text) {
        if (text == null) return 0;

        int textLength = text.length();
        return (int) (textLength * ((charWidth + horizontalPad) * scale));

    }

    @Override
    public int getSpaceWidth() {
        return (int) ((charWidth + horizontalPad) * scale);
    }

    @Override
    public void setScale(double scale) {
        this.scale = scale;
    }

    private void renderText(Graphics graphics, String text, int x, int y) {
        if (text == null) return;

        int textLength = text.length();
        int xPos = x, yPos = y;
        for (int i = 0; i < textLength; i++) {
            char c = text.charAt(i);
            renderChar(graphics, c, xPos, yPos);
            xPos += (int) ((charWidth + horizontalPad) * scale);
        }
    }


    public void renderChar(Graphics graphics, char c, int x, int y) {
        int cy = ((int) c) / 16;
        int cx = ((int) c) % 16;

        graphics.drawImageScaled(tileSheet.getSubImage(cx, cy), x, y, scale);
    }


}

