package com.physmo.garnet.regularfont;

import com.physmo.garnet.Texture;
import com.physmo.garnet.graphics.Graphics;
import com.physmo.garnet.spritebatch.TileSheet;

/*
    Image requirements: a png containing 16x16 character grid in ascii format.
 */
public class RegularFont {

    TileSheet tileSheet;
    private Texture texture;
    private int charWidth;
    private int charHeight;
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
        int xpos = textObject.x, ypos = textObject.y;
        for (int i = 0; i < textLength; i++) {
            char c = str.charAt(i);
            renderChar(graphics, c, xpos, ypos);
            xpos += charWidth + horizontalPad;
        }
    }

    public void renderChar(Graphics graphics, char c, int x, int y) {
        int cy = ((int) c) / 16;
        int cx = ((int) c) % 16;

        graphics.drawImage(tileSheet, x, y, cx, cy);
    }


}

