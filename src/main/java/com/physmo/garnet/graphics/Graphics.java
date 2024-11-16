package com.physmo.garnet.graphics;

import com.physmo.garnet.ColorUtils;
import com.physmo.garnet.Display;
import com.physmo.garnet.drawablebatch.Circle2D;
import com.physmo.garnet.drawablebatch.DrawableBatch;
import com.physmo.garnet.drawablebatch.DrawableElement;
import com.physmo.garnet.drawablebatch.Line2D;
import com.physmo.garnet.drawablebatch.Shape2D;
import com.physmo.garnet.drawablebatch.Sprite2D;
import com.physmo.garnet.structure.Array;

import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL11.GL_SCISSOR_TEST;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glScissor;

/**
 * The Graphics class is responsible for managing and rendering 2D graphics within the application,
 * including handling texture drawing, viewport management, and various rendering settings.
 */
public class Graphics {

    private final Display display;
    private final Map<Integer, Texture> textures;
    private final DrawableBatch drawableBatch;

    private final ViewportManager viewportManager;
    ObjectPool<Sprite2D> sprite2DObjectPool;
    ObjectPool<Line2D> line2DObjectPool;

    private int currentTextureId = 0;
    private int color;
    private int currentDrawOrder;
    private int currentlyBoundTextureId;
    private int backgroundColor = 0;
    private double xo = 0;
    private double yo = 0;
    private int clipRectHash = 0;
    private int activeViewportId = 0;

    public Graphics(Display display) {
        this.display = display;
        drawableBatch = new DrawableBatch();
        textures = new HashMap<>();

        resetSettings();

        sprite2DObjectPool = new ObjectPool<>(Sprite2D.class, Sprite2D::new);
        line2DObjectPool = new ObjectPool<>(Line2D.class, Line2D::new);
        viewportManager = new ViewportManager(display.getWindowWidth(), display.getWindowHeight());

    }

    public void resetSettings() {
        //zoom = 1;
        color = ColorUtils.rgb(0xff, 0xff, 0xff, 0xff);
        currentDrawOrder = 0;
        currentlyBoundTextureId = 0;
    }

    public ViewportManager getViewportManager() {
        return viewportManager;
    }

    /**
     * Renders the current frame by executing the following steps:
     * 1. Draws the viewport debug information.
     * 2. Renders the drawable batch using the current graphics settings.
     * 3. Releases the current batch of drawable elements back to the object pool.
     * 4. Clears the drawable batch for the next frame.
     */
    public void render() {
        drawViewportDebugInfo();
        drawableBatch.render(this);
        releaseBatch();
        drawableBatch.clear();

    }

    private void drawViewportDebugInfo() {
        int prevViewportId = viewportManager.getActiveViewport().getId();
        double prevScale = this.getZoom();
        int prevColor = this.getColor();


        setActiveViewport(ViewportManager.DEBUG_VIEWPORT);
        this.setZoom(1);

        Viewport viewport;
        int[] clipRect;
        for (int i = 0; i < 10; i++) {
            viewport = viewportManager.getViewport(i);
            if (!viewport.isDrawDebugInfo()) continue;
            clipRect = viewport.getClipRect();

            setColor(viewport.getDebugInfoColor());
            for (int j = 0; j < 5; j++) {
                this.drawRect(clipRect[0] + j, clipRect[1] + j, clipRect[2] - (j * 2), clipRect[3] - (j * 2));
            }
            //this.drawRect(camera.getWindowX(), camera.getWindowY(), camera.getWidth(), camera.getHeight());
        }
        //this.setZoom(prevScale);
        setActiveViewport(prevViewportId);
        setColor(prevColor);
    }


    /**
     * Releases all sprite objects in the current drawable batch back to the object pool.
     * <p>
     * This method iterates through each element in the drawable batch, checks if
     * the element is of type SPRITE, and if so, releases it back to the Sprite2D object pool.
     */
    public void releaseBatch() {

        Array<DrawableElement> elements = drawableBatch.getElements();

        for (DrawableElement element : elements) {
            if (element.getType() == DrawableElement.SPRITE) sprite2DObjectPool.releaseObject((Sprite2D) element);
            if (element.getType() == DrawableElement.LINE) line2DObjectPool.releaseObject((Line2D) element);
        }

    }

    /**
     * Retrieves the current zoom level of the active viewport.
     *
     * @return the zoom level of the active viewport
     */
    public double getZoom() {
        return viewportManager.getActiveViewport().getZoom();
    }

    /**
     * Set the zoom level of the currently active viewport.
     *
     * @param zoom
     */
    public void setZoom(double zoom) {
        viewportManager.getActiveViewport().setZoom(zoom);
    }

    public int getColor() {
        return color;
    }

    public void setActiveViewport(int id) {
        if (id == activeViewportId) return;
        activeViewportId = id;
        viewportManager.setActiveViewport(id);
        Viewport viewport = viewportManager.getActiveViewport();
        xo = viewport.getWindowX() - viewport.getX();
        yo = viewport.getWindowY() - viewport.getY();
    }

    public void drawRect(float x, float y, float w, float h) {
        drawLine(x, y, x + w, y);
        drawLine(x + w, y, x + w, y + h);
        drawLine(x + w, y + h, x, y + h);
        drawLine(x, y + h, x, y);
    }

    public void setColor(int col) {
        color = col;
    }

    public void drawLine(float x1, float y1, float x2, float y2) {
        //Line2D line = new Line2D(x1, y1, x2, y2);
        Line2D line = line2DObjectPool.getFreeObject();
        line.set(x1, y1, x2, y2);
        line.setColor(color);
        line.setDrawOrder(currentDrawOrder);
        line.setViewport(viewportManager.getActiveViewport());
        //line.setScale(zoom);
        drawableBatch.add(line);
    }

    public int getCurrentTextureId() {
        return currentTextureId;
    }

    public void setCurrentTextureId(int currentTextureId) {
        this.currentTextureId = currentTextureId;
    }

    /**
     * Draws an image from a specified tile in the given TileSheet at the specified coordinates,
     * with an additional rotation applied to the drawn image.
     *
     * @param tileSheet the TileSheet containing the tile to be drawn
     * @param x         the x-coordinate where the image should be drawn
     * @param y         the y-coordinate where the image should be drawn
     * @param tileX     the x-coordinate of the tile in the TileSheet
     * @param tileY     the y-coordinate of the tile in the TileSheet
     * @param rotation  the rotation angle to apply to the drawn image
     * @return the Sprite2D object representing the drawn image with the applied rotation
     */
    public Sprite2D drawImage(TileSheet tileSheet, double x, double y, int tileX, int tileY, double rotation) {
        Sprite2D sprite2D = drawImage(tileSheet, x, y, tileX, tileY);
        sprite2D.addAngle((float) rotation);
        return sprite2D;
    }

    /**
     * Draws an image from a specified tile in the given TileSheet at the specified coordinates.
     *
     * @param tileSheet the TileSheet containing the tile to be drawn
     * @param x the x-coordinate where the image should be drawn
     * @param y the y-coordinate where the image should be drawn
     * @param tileX the x-coordinate of the tile in the TileSheet
     * @param tileY the y-coordinate of the tile in the TileSheet
     * @return the Sprite2D object representing the drawn image
     */
    public Sprite2D drawImage(TileSheet tileSheet, double x, double y, int tileX, int tileY) {
        return drawImage(tileSheet.getSubImage(tileX, tileY), x, y);
    }

    /**
     * Draws an image from the specified SubImage at the given x and y coordinates.
     *
     * @param subImage the SubImage containing the texture and dimensions of the image to be drawn
     * @param x the x-coordinate where the image should be rendered
     * @param y the y-coordinate where the image should be rendered
     * @return the Sprite2D object representing the drawn image
     */
    public Sprite2D drawImage(SubImage subImage, double x, double y) {
        // texture coords
        int tx = subImage.x;
        int ty = subImage.y;
        Texture texture = subImage.texture;

        Sprite2D sprite2D = sprite2DObjectPool.getFreeObject();
        sprite2D.reset();
        sprite2D.setCoords((int) x, (int) y, subImage.w, subImage.h, tx, ty, subImage.w, subImage.h);
        sprite2D.setTextureId(texture.getId());
        sprite2D.setTextureScale(1.0f / texture.getWidth(), 1.0f / texture.getHeight());

        sprite2D.setCommonValues(viewportManager.getActiveViewport(), currentDrawOrder, color);

        drawableBatch.add(sprite2D);
        return sprite2D;
    }

    public Sprite2D drawImageScaled(SubImage subImage, double x, double y, double scale) {
        // texture coords
        int tx = subImage.x;
        int ty = subImage.y;
        Texture texture = subImage.texture;

        Sprite2D sprite2D = sprite2DObjectPool.getFreeObject();
        sprite2D.reset();
        sprite2D.setCoords((int) x, (int) y, (int) (subImage.w * scale), (int) (subImage.h * scale), tx, ty, subImage.w, subImage.h);
        sprite2D.setTextureId(texture.getId());
        sprite2D.setTextureScale(1.0f / texture.getWidth(), 1.0f / texture.getHeight());

        sprite2D.setCommonValues(viewportManager.getActiveViewport(), currentDrawOrder, color);

        drawableBatch.add(sprite2D);
        return sprite2D;
    }

    public void drawImage(Texture texture, float[] vertexCoords, float[] texCoords) {

        // TODO: make font register texture
        if (!textures.containsKey(texture.getId())) this.addTexture(texture);

        Sprite2D sprite2D = sprite2DObjectPool.getFreeObject();
        sprite2D.reset();
        sprite2D.setCoords(vertexCoords, texCoords);
        sprite2D.setTextureId(texture.getId());
        sprite2D.setTextureScale(1.0f / texture.getWidth(), 1.0f / texture.getHeight());
        sprite2D.setCommonValues(viewportManager.getActiveViewport(), currentDrawOrder, color);

        drawableBatch.add(sprite2D);
    }

    /**
     * Adds a texture to the collection of textures if it is not already present.
     * If the texture is already registered, the method does nothing.
     *
     * @param texture the Texture object to be added
     */
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
        int tx = 0;
        int ty = 0;

        Sprite2D sprite2D = sprite2DObjectPool.getFreeObject();
        sprite2D.reset();

        sprite2D.setCoords(x, y, tileWidth, tileHeight, tx, ty, tileWidth, tileHeight);
        sprite2D.setTextureId(texture.getId());

        sprite2D.setTextureScale(1.0f / texture.getWidth(), 1.0f / texture.getHeight());

        sprite2D.setCommonValues(viewportManager.getActiveViewport(), currentDrawOrder, color);

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
        Circle2D circle = new Circle2D(x, y, w, h);

        circle.setCommonValues(viewportManager.getActiveViewport(), currentDrawOrder, color);

        drawableBatch.add(circle);
    }

    public void filledCircle(float x, float y, float w, float h) {
        Circle2D circle = new Circle2D(x, y, w, h);
        circle.setFilled(true);

        circle.setCommonValues(viewportManager.getActiveViewport(), currentDrawOrder, color);

        drawableBatch.add(circle);
    }

    public int getDrawOrder() {
        return currentDrawOrder;
    }

    /**
     * Sets the current draw order for rendering operations.
     * Graphics are painted from lower to higher.
     *
     * @param i the draw order value to set
     */
    public void setDrawOrder(int i) {
        currentDrawOrder = i;
    }

    public void filledRect(int _x, int _y, int _w, int _h) {
        filledRect((float) _x, (float) _y, (float) _w, (float) _h);
    }

    public void filledRect(float _x, float _y, float _w, float _h) {
        float[] coords = new float[8];
        float x = _x;
        float y = _y;
        float w = _w;
        float h = _h;

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
        shape2D.setViewport(viewportManager.getActiveViewport());
        drawableBatch.add(shape2D);
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
     * Internal function, not for user use.
     *
     * @param vp
     */
    public void _activateClipRect(Viewport vp) {

        if (!vp.isClipActive()) {
            if (clipRectHash != 0) {
                glDisable(GL_SCISSOR_TEST);
                clipRectHash = 0;
            }
        } else if (clipRectHash == vp.getClipRectHash()) {
            // Do nothing:
            // - clip rect hash matches the last viewport clip rect that was applied.
        } else {

            int[] windowSize = display.getBufferSize();

            glEnable(GL_SCISSOR_TEST);
            int[] clipRect = vp.getClipRect();
            int w = (int) (clipRect[2] / display.glViewportScale[0]);
            int h = (int) (clipRect[3] / display.glViewportScale[1]);
            int x = (int) (clipRect[0] / display.glViewportScale[0]);
            int y = (int) (clipRect[1] / display.glViewportScale[1]);

            x += display.glViewportOffsets[0];
            y += display.glViewportOffsets[1];

            glScissor(x, windowSize[1] - h - y, w, h);
            clipRectHash = vp.getClipRectHash();

        }


    }

}
