package com.physmo.garnet.graphics;

import com.physmo.garnet.Display;
import com.physmo.garnet.Utils;
import com.physmo.garnet.drawablebatch.*;

import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL11.*;

public class Graphics {

    private final Display display;
    private final Map<Integer, Texture> textures;
    private final DrawableBatch drawableBatch;
    private final Map<Integer, Integer[]> clipRects;
    private double scale;
    private int currentTextureId = 0;
    private int color;
    private int currentDrawOrder;
    private int currentlyBoundTextureId;
    private int backgroundColor = 0;
    private int activeClipRect;
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
        color = Utils.rgb(0xff, 0xff, 0xff, 0xff);
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


    public int getCurrentTextureId() {
        return currentTextureId;
    }

    public void setCurrentTextureId(int currentTextureId) {
        this.currentTextureId = currentTextureId;
    }

    public Sprite2D drawImage(TileSheet tileSheet, double x, double y, int tileX, int tileY, double rotation) {
        Sprite2D sprite2D = drawImage(tileSheet, x, y, tileX, tileY);
        sprite2D.addAngle((float) rotation);
        return sprite2D;
    }

    public Sprite2D drawImage(TileSheet tileSheet, double x, double y, int tileX, int tileY) {
        int tileWidth = tileSheet.getTileWidth();
        int tileHeight = tileSheet.getTileHeight();
        // texture coords
        int tx = tileX * tileWidth;
        int ty = tileY * tileHeight;
        Texture texture = tileSheet.getTexture();
        Sprite2D sprite2D = new Sprite2D((int) (x * scale), (int) (y * scale), (int) (tileWidth * scale), (int) (tileHeight * scale), tx, ty, tileWidth, tileHeight);
        sprite2D.setTextureId(texture.getId());
        sprite2D.setColor(color);
        sprite2D.setTextureScale(1.0f / texture.getWidth(), 1.0f / texture.getHeight());
        sprite2D.setDrawOrder(currentDrawOrder);
        sprite2D.setClipRect(activeClipRect);
        drawableBatch.add(sprite2D);
        return sprite2D;
    }

    public Sprite2D drawImage(SubImage subImage, double x, double y) {
        // texture coords
        int tx = subImage.x;
        int ty = subImage.y;
        Texture texture = subImage.texture;
        Sprite2D sprite2D = new Sprite2D((int) (x * scale), (int) (y * scale), (int) (subImage.w * scale), (int) (subImage.h * scale), tx, ty, subImage.w, subImage.h);
        sprite2D.setTextureId(texture.getId());
        sprite2D.setColor(color);
        sprite2D.setTextureScale(1.0f / texture.getWidth(), 1.0f / texture.getHeight());
        sprite2D.setDrawOrder(currentDrawOrder);
        sprite2D.setClipRect(activeClipRect);
        drawableBatch.add(sprite2D);
        return sprite2D;
    }

    public void drawImage(Texture texture, float[] vertexCoords, float[] texCoords) {

        for (int i = 0; i < vertexCoords.length; i++) {
            vertexCoords[i] = vertexCoords[i] * (float) scale;
        }

        // TODO: make font register texture
        if (!textures.containsKey(texture.getId())) this.addTexture(texture);

        Sprite2D sprite2D = new Sprite2D(vertexCoords, texCoords);
        sprite2D.setTextureId(texture.getId());
        sprite2D.setColor(color);
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

    public void drawImage(Texture texture, int x, int y) {

        int tileWidth = texture.getWidth();
        int tileHeight = texture.getHeight();
        // texture coords
        int tx = 0;
        int ty = 0;

        Sprite2D sprite2D = new Sprite2D((int) (x * scale), (int) (y * scale), (int) (tileWidth * scale), (int) (tileHeight * scale), tx, ty, tileWidth, tileHeight);
        sprite2D.setTextureId(texture.getId());
        sprite2D.setColor(color);
        sprite2D.setTextureScale(1.0f / texture.getWidth(), 1.0f / texture.getHeight());
        sprite2D.setDrawOrder(currentDrawOrder);
        sprite2D.setClipRect(activeClipRect);
        drawableBatch.add(sprite2D);
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

    public void drawCircle(float x, float y, float w, float h) {
        Circle2D circle = new Circle2D((float) (x * scale), (float) (y * scale), (float) (w * scale), (float) (h * scale));
        circle.setColor(color);
        circle.setDrawOrder(currentDrawOrder);
        drawableBatch.add(circle);
    }

    public void filledCircle(float x, float y, float w, float h) {
        Circle2D circle = new Circle2D((float) (x * scale), (float) (y * scale), (float) (w * scale), (float) (h * scale));
        circle.setColor(color);
        circle.setFilled(true);
        circle.setDrawOrder(currentDrawOrder);
        drawableBatch.add(circle);
    }

    public int getDrawOrder() {
        return currentDrawOrder;
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
        line.setColor(color);
        line.setDrawOrder(currentDrawOrder);
        drawableBatch.add(line);
    }

    public void filledRect(float _x, float _y, float _w, float _h) {
        float[] coords = new float[8];
        float x = (float) (_x * scale);
        float y = (float) (_y * scale);
        float w = (float) (_w * scale);
        float h = (float) (_h * scale);

        coords[0] = x;
        coords[1] = y;
        coords[2] = x + w;
        coords[3] = y;
        coords[4] = x + w;
        coords[5] = y + h;
        coords[6] = x;
        coords[7] = y + h;

        Shape2D shape2D = new Shape2D(coords);
        shape2D.setColor(color);
        shape2D.setDrawOrder(currentDrawOrder);
        drawableBatch.add(shape2D);
    }

    public int getColor() {
        return color;
    }

    public void setColor(int col) {
        color = col;
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
            double[] windowToPixelsScale = display.getWindowToBufferScale();
            int[] windowSize = display.getBufferSize();
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

    public int getAvailableClipRectId() {
        // Start at 100 to leave space for user defined clip rects.
        for (int i = 100; i < 5000; i++) {
            if (!clipRects.containsKey(i)) return i;
        }
        throw new RuntimeException("Too many clip rects.");
    }
}
