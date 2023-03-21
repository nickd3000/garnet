package com.physmo.garnet.graphics;

import com.physmo.garnet.Display;
import com.physmo.garnet.Texture;
import com.physmo.garnet.Utils;
import com.physmo.garnet.spritebatch.DrawableBatch;
import com.physmo.garnet.spritebatch.Line2D;
import com.physmo.garnet.spritebatch.Sprite2D;
import com.physmo.garnet.spritebatch.TileSheet;

import java.util.HashMap;
import java.util.Map;

public class Graphics {
    private final Display display;
    Map<Integer, Texture> textures;
    DrawableBatch drawableBatch;
    double scale;
    float xOffset;
    float yOffset;
    int currentTextureId = 0;
    int currentColor;
    int currentDrawOrder;
    int currentlyBoundTextureId;
    int backgroundColor = 0;

    public Graphics(Display display) {
        this.display = display;
        drawableBatch = new DrawableBatch();
        textures = new HashMap<>();
        resetSettings();
    }

    public void resetSettings() {
        scale = 1;
        currentColor = Utils.rgb(0xff, 0xff, 0xff, 0xff);
        currentDrawOrder = 0;
        currentlyBoundTextureId = 0;
    }

    public void render() {
        drawableBatch.render(this);
        drawableBatch.clear();
    }

    public double getScale() {
        return scale;
    }

    public void setScale(double scale) {
        this.scale = scale;
    }

    public float getxOffset() {
        return xOffset;
    }

    public void setxOffset(float xOffset) {
        this.xOffset = xOffset;
    }

    public float getyOffset() {
        return yOffset;
    }

    public void setyOffset(float yOffset) {
        this.yOffset = yOffset;
    }

    public int getCurrentTextureId() {
        return currentTextureId;
    }

    public void setCurrentTextureId(int currentTextureId) {
        this.currentTextureId = currentTextureId;
    }

    public void drawImage(TileSheet tileSheet, int x, int y, int tileX, int tileY) {
        int tileWidth = tileSheet.getTileWidth();
        int tileHeight = tileSheet.getTileHeight();
        // texture coords
        int tx = tileX * tileWidth;
        int ty = tileY * tileHeight;
        int tw = tileWidth;
        int th = tileHeight;
        Texture texture = tileSheet.getTexture();
        Sprite2D sprite2D = new Sprite2D((int) (x * scale), (int) (y * scale),
                (int) (tileWidth * scale), (int) (tileHeight * scale), tx, ty, tw, th);
        sprite2D.setTextureId(texture.getId());
        sprite2D.addColor(currentColor);
        sprite2D.setTextureScale(1.0f / texture.getWidth(), 1.0f / texture.getHeight());
        sprite2D.setDrawOrder(currentDrawOrder);
        drawableBatch.add(sprite2D);

    }

    public void drawImage(Texture texture, float[] vertexCoords, float[] texCoords) {

        for (int i = 0; i < vertexCoords.length; i++) {
            vertexCoords[i] = vertexCoords[i] * (float) scale;
        }

        // TODO: make font register texture
        if (!textures.containsKey(texture.getId())) this.addTexture(texture);

        Sprite2D sprite2D = new Sprite2D(vertexCoords, texCoords);
        sprite2D.setTextureId(texture.getId());
        sprite2D.addColor(currentColor);
        sprite2D.setTextureScale(1.0f / texture.getWidth(), 1.0f / texture.getHeight());
        sprite2D.setDrawOrder(currentDrawOrder);
        drawableBatch.add(sprite2D);
    }

    public void addTexture(Texture texture) {
        if (textures.containsKey(texture.getId())) {
            System.out.println("Registered texture id: " + texture.getId());
        }
        textures.put(texture.getId(), texture);
        currentTextureId = texture.getId();
    }

    // TODO: only bind if different
    public void bindTexture(int textureId) {
        if (textureId == 0) return;
        if (currentlyBoundTextureId == textureId) return;

        Texture texture = textures.get(textureId);
        if (texture == null) return;

        texture.bind();
        currentlyBoundTextureId = textureId;
    }

    public void setColor(int col) {
        currentColor = col;
    }

    public void setDrawOrder(int i) {
        currentDrawOrder = i;
    }

    public void drawRect(float x, float y, float w, float h) {
        drawLine(x, y, x + w, y);
        drawLine(x + w, y, x + w, y + h);
        drawLine(x + w, y + h, x, y + h);
        drawLine(x, y + h, x, y);
    }

    public void drawLine(float x1, float y1, float x2, float y2) {
        Line2D line = new Line2D((float) (x1 * scale), (float) (y1 * scale), (float) (x2 * scale), (float) (y2 * scale));
        line.addColor(currentColor);
        line.setDrawOrder(currentDrawOrder);
        drawableBatch.add(line);
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(int rgba) {
        backgroundColor = rgba;
    }

    public boolean hasTexture(int id) {
        return textures.containsKey(id);
    }
}
