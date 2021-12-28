package com.physmo.garnet.regularfont;

import com.physmo.garnet.Texture;
import com.physmo.garnet.spritebatch.Sprite2D;
import com.physmo.garnet.spritebatch.SpriteBatch;

/*
    Image requirements: a png containing 16x16 character grid in ascii format.
 */
public class RegularFont {

    SpriteBatch spriteBatch;
    private Texture texture;

    private int charWidth;
    private int charHeight;

    public RegularFont(String imageFile, int charWidth, int charHeight) {
        this.charWidth = charWidth;
        this.charHeight = charHeight;
        texture = Texture.loadTexture(imageFile);
        spriteBatch = new SpriteBatch(texture);
    }

    public SpriteBatch getSpriteBatch() {
        return spriteBatch;
    }

    public void clearSpriteBatch() {
        spriteBatch.clear();
    }

    public void drawText(String text, int x, int y, int scale) {
        TextObject textObject = new TextObject(text, x, y, scale);
        renderTextObject(textObject, spriteBatch);
    }

    private void renderTextObject(TextObject textObject, SpriteBatch sb) {
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

    public void renderChar(SpriteBatch sb, char c, int x, int y, int scale) {

        int cy = ((int) c) / 16;
        int cx = ((int) c) % 16;

        sb.add(Sprite2D.build(
                x, y, charWidth * scale, charHeight * scale,
                cx * charWidth, cy * charHeight, charWidth, charHeight));
    }

    public void drawTextToSpriteBatch(SpriteBatch sb, String text, int x, int y, int scale) {
        TextObject textObject = new TextObject(text, x, y, scale);
        renderTextObject(textObject, sb);
    }

    public void render() {
        render(1);
    }

    public void render(float scale) {
        spriteBatch.render(scale);
        spriteBatch.clear();
    }

    public int getCount() {
        return spriteBatch.size();
    }
}

