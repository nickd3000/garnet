package com.physmo.garnet.regularfont;

import com.physmo.garnet.Texture;
import com.physmo.garnet.graphics.Graphics;
import com.physmo.garnet.spritebatch.DrawableBatch;
import com.physmo.garnet.spritebatch.Sprite2D;

/*
    Image requirements: a png containing 16x16 character grid in ascii format.
 */
public class RegularFont {

    DrawableBatch spriteBatch;
    private Texture texture;

    private int charWidth;
    private int charHeight;
    Graphics graphics;

    public RegularFont(String imageFile, int charWidth, int charHeight) {
        this.graphics = graphics;
        this.charWidth = charWidth;
        this.charHeight = charHeight;
        texture = Texture.loadTexture(imageFile);
        spriteBatch = new DrawableBatch();
    }

    public DrawableBatch getSpriteBatch() {
        return spriteBatch;
    }

    public void clearSpriteBatch() {
        spriteBatch.clear();
    }

    public void drawText(String text, int x, int y, int scale) {
        TextObject textObject = new TextObject(text, x, y, scale);
        renderTextObject(textObject, spriteBatch);
    }

    public void drawTextToSpriteBatch(DrawableBatch sb, String text, int x, int y, int scale) {
        TextObject textObject = new TextObject(text, x, y, scale);
        renderTextObject(textObject, sb);
    }

    private void renderTextObject(TextObject textObject, DrawableBatch sb) {
        String str = textObject.text;
        if (str == null) return;

        int textLength = str.length();
        int xpos = textObject.x, ypos = textObject.y;
        for (int i = 0; i < textLength; i++) {
            char c = str.charAt(i);
            renderChar(sb, c, xpos, ypos, textObject.scale);
            xpos += charWidth * textObject.scale;
        }
    }

    public void renderChar(DrawableBatch sb, char c, int x, int y, int scale) {

        int cy = ((int) c) / 16;
        int cx = ((int) c) % 16;

        sb.add(Sprite2D.build(
                x, y, charWidth * scale, charHeight * scale,
                cx * charWidth, cy * charHeight, charWidth, charHeight));
    }

    public void render() {
        render(1);
    }

    public void render(float scale) {
        spriteBatch.render(graphics);
        spriteBatch.clear();
    }

    public int getCount() {
        return spriteBatch.size();
    }
}

