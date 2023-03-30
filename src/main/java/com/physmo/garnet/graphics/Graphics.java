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

import static org.lwjgl.opengl.GL11.GL_SCISSOR_TEST;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glScissor;

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
    Map<Integer, Integer[]> clipRects;
    int activeClipRect;
    private int appliedClipRect = 0; // The clip rect set active in openGl.

    public Graphics(Display display) {
        this.display = display;
        drawableBatch = new DrawableBatch();
        textures = new HashMap<>();
        clipRects = new HashMap<>();
        resetSettings();
    }

    public void resetSettings() {
        scale = 1;
        currentColor = Utils.rgb(0xff, 0xff, 0xff, 0xff);
        currentDrawOrder = 0;
        currentlyBoundTextureId = 0;
        activeClipRect = 0; // 0 means none.
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
        Texture texture = tileSheet.getTexture();
        Sprite2D sprite2D = new Sprite2D((int) (x * scale), (int) (y * scale),
                (int) (tileWidth * scale), (int) (tileHeight * scale), tx, ty, tileWidth, tileHeight);
        sprite2D.setTextureId(texture.getId());
        sprite2D.addColor(currentColor);
        sprite2D.setTextureScale(1.0f / texture.getWidth(), 1.0f / texture.getHeight());
        sprite2D.setDrawOrder(currentDrawOrder);
        sprite2D.setClipRect(activeClipRect);
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
        sprite2D.setClipRect(activeClipRect);
        drawableBatch.add(sprite2D);
    }

    public void drawImage(Texture texture, int x, int y) {

        int tileWidth = texture.getWidth();
        int tileHeight = texture.getHeight();
        // texture coords
        int tx = 0;
        int ty = 0;

        Sprite2D sprite2D = new Sprite2D((int) (x * scale), (int) (y * scale),
                (int) (tileWidth * scale), (int) (tileHeight * scale), tx, ty, tileWidth, tileHeight);
        sprite2D.setTextureId(texture.getId());
        sprite2D.addColor(currentColor);
        sprite2D.setTextureScale(1.0f / texture.getWidth(), 1.0f / texture.getHeight());
        sprite2D.setDrawOrder(currentDrawOrder);
        sprite2D.setClipRect(activeClipRect);
        drawableBatch.add(sprite2D);
    }

    public void addTexture(Texture texture) {
        if (textures.containsKey(texture.getId())) {
            //System.out.println("Registered texture id: " + texture.getId());
            return;
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

    /**
     * Register a clipping rectangle with Graphics.
     * The location is in screen pixels and ignores any scaling applied to graphics.
     *
     * @param index
     * @param x
     * @param y
     * @param w
     * @param h
     */
    public void addClipRect(int index, int x, int y, int w, int h) {
        Integer[] clipRect = new Integer[]{x, y, w, h};
        clipRects.put(index, clipRect);
    }

    public void disableClipRect() {
        setActiveClipRect(0);
    }

    /**
     * Internal function, not for user use.
     *
     * @param clipRectId
     */
    public void _activateClipRect(int clipRectId) {
        // TODO: this was disabled because the rect was not being
        // recalculated after window size change. Is doing this all
        // the time bad for performance?
        //if (appliedClipRect == clipRectId) return;

        appliedClipRect = clipRectId;

        if (clipRectId == 0) {
            glDisable(GL_SCISSOR_TEST);
        } else {
            double[] windowToPixelsScale = display.getWindowToPixelsScale();
            int[] windowSize = display.getWindowSize();
            glEnable(GL_SCISSOR_TEST);
            Integer[] clipRect = clipRects.get(clipRectId);
            int x = (int) (clipRect[0] * windowToPixelsScale[0]);
            int y = (int) ((windowSize[1] - (clipRect[3] * windowToPixelsScale[1])) - (clipRect[1] * windowToPixelsScale[1]));
            int w = (int) (clipRect[2] * windowToPixelsScale[0]);
            int h = (int) (clipRect[3] * windowToPixelsScale[1]);
            glScissor(x, y, w, h);

        }
    }

    public boolean clipRectActive() {
        return activeClipRect != 0;
    }

    public int getActiveClipRect() {
        return activeClipRect;
    }

    public void setActiveClipRect(int id) {
        activeClipRect = id;
    }
}
