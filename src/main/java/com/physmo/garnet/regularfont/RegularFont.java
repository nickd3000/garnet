package com.physmo.garnet.regularfont;

import com.physmo.garnet.Texture;
import com.physmo.garnet.graphics.Graphics;
import com.physmo.garnet.spritebatch.TileSheet;

/**
 * RegularFont is a simpler font drawer.  It requires a font image file arranged in
 * a specific grid pattern to match the ascii character system.
 */
public class RegularFont {

    private final TileSheet tileSheet;
    private final Texture texture;
    private final int charWidth;
    private final int charHeight;
    private int horizontalPad = 0;

    public RegularFont(String imageFile, int charWidth, int charHeight) {
        this.charWidth = charWidth;
        this.charHeight = charHeight;
        texture = Texture.loadTexture(imageFile);
        tileSheet = new TileSheet(texture, charWidth, charHeight);
    }

    public int getHorizontalPad() {
        return horizontalPad;
    }

    public void setHorizontalPad(int horizontalPad) {
        this.horizontalPad = horizontalPad;
    }

    public void drawText(Graphics graphics, String text, int x, int y) {
        if (!graphics.hasTexture(texture.getId()))
            graphics.addTexture(texture);
        TextObject textObject = new TextObject(text, x, y);
        renderTextObject(graphics, textObject);
    }

    private void renderTextObject(Graphics graphics, TextObject textObject) {
        String str = textObject.text;
        if (str == null) return;

        int textLength = str.length();
        int xPos = textObject.x, yPos = textObject.y;
        for (int i = 0; i < textLength; i++) {
            char c = str.charAt(i);
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

