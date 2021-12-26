package com.physmo.garnet.regularfont;

import com.physmo.garnet.Texture;
import com.physmo.garnet.spritebatch.Sprite2D;
import com.physmo.garnet.spritebatch.SpriteBatch;

import java.util.ArrayList;
import java.util.List;

/*
    Image requirements: a png containing 16x16 character grid in ascii format.
 */
public class RegularFont {

    SpriteBatch spriteBatch;
    List<TextObject> textObjects = new ArrayList<>();
    private Texture texture;
    private String imageFile;
    private int charWidth;
    private int charHeight;

    public RegularFont(String imageFile, int charWidth, int charHeight) {
        this.imageFile = imageFile;
        this.charWidth = charWidth;
        this.charHeight = charHeight;
        texture = Texture.loadTexture(imageFile);
        spriteBatch = new SpriteBatch(texture);
    }

    public void clearSpriteBatch() {
        spriteBatch.clear();
    }

    public void drawText(String text, int x, int y, int scale) {
        textObjects.add(new TextObject(text, x, y, scale));
    }

    public void render() {
        for (TextObject txtObj : textObjects) {
            renderTextObject(txtObj);
        }
        spriteBatch.render(1);
        textObjects.clear();

        spriteBatch.clear();
    }

    private void renderTextObject(TextObject textObject) {
        String str = textObject.text;
        if (str == null) return;

        int textLength = str.length();
        int xpos = textObject.x, ypos = textObject.y;
        for (int i = 0; i < textLength; i++) {
            char c = str.charAt(i);
            renderChar(c, xpos, ypos, textObject.scale);
            xpos += charWidth * textObject.scale;
        }


    }

    public void renderChar(char c, int x, int y, int scale) {

        int cy = ((int) c) / 16;
        int cx = ((int) c) % 16;

//        x+=Math.random()*50;
//        y+=Math.random()*50;

        spriteBatch.add(Sprite2D.build(
                x, y, charWidth * scale, charHeight * scale,
                cx * charWidth, cy * charHeight, charWidth, charHeight));
    }

    public int getCount() {
        return spriteBatch.size();
    }
}

class TextObject {
    String text;
    int x, y, scale;

    TextObject(String text, int x, int y, int scale) {
        this.text = text;
        this.x = x;
        this.y = y;
        this.scale = scale;
    }

}
