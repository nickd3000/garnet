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
import org.lwjgl.opengl.GL;

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
    private final ViewportManager viewportManager;
    private final DrawableBatch drawableBatch;

    private final Map<Integer, Texture> textures;
    private final ObjectPool<Sprite2D> sprite2DObjectPool;
    private final ObjectPool<Line2D> line2DObjectPool;

    private int color;
    private final SubImage subImage = new SubImage();
    private int currentDrawOrder;
    private int backgroundColor;
    private int currentlyBoundTextureId;
    private int currentTextureId;
    private int activeViewportId;
    private int clipRectHash;
    private boolean internalBufferMode;

    /**
     * Creates a new Graphics instance tied to the given display.
     *
     * @param display the {@link Display} this Graphics instance will render to
     */
    public Graphics(Display display) {
        this.display = display;
        viewportManager = new ViewportManager(display.getWindowWidth(), display.getWindowHeight());
        drawableBatch = new DrawableBatch();

        textures = new HashMap<>();
        sprite2DObjectPool = new ObjectPool<>(Sprite2D.class, Sprite2D::new);
        line2DObjectPool = new ObjectPool<>(Line2D.class, Line2D::new);

        resetSettings();
    }

    // Frame lifecycle

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

    /**
     * Resets rendering settings to their defaults: white color, draw order 0, no clip rect, and scissor test disabled.
     */
    public void resetSettings() {
        //zoom = 1;
        color = ColorUtils.rgb(0xff, 0xff, 0xff, 0xff);
        currentDrawOrder = 0;
        currentlyBoundTextureId = 0;
        clipRectHash = 0;
        try {
            GL.getCapabilities();
            glDisable(GL_SCISSOR_TEST);
        } catch (IllegalStateException e) {
            // Ignore - capabilities not set yet
        }
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

    // Viewports

    /**
     * Returns the {@link ViewportManager} used to manage viewports for this Graphics instance.
     *
     * @return the viewport manager
     */
    public ViewportManager getViewportManager() {
        return viewportManager;
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
     * Sets the zoom level of the currently active viewport.
     *
     * @param zoom the zoom level to apply, where 1.0 is normal size
     */
    public void setZoom(double zoom) {
        viewportManager.getActiveViewport().setZoom(zoom);
    }

    /**
     * Sets the active viewport by id. Subsequent draw calls will be rendered within this viewport.
     *
     * @param id the id of the viewport to activate
     */
    public void setActiveViewport(int id) {
        if (id == activeViewportId) return;
        activeViewportId = id;
        viewportManager.setActiveViewport(id);
//        Viewport viewport = viewportManager.getActiveViewport();
//        xo = viewport.getWindowX() - viewport.getScrollX();
//        yo = viewport.getWindowY() - viewport.getScrollY();
    }

    /**
     * Returns whether internal buffer (FBO) mode is active.
     *
     * @return {@code true} if rendering to an internal buffer
     */
    public boolean isInternalBufferMode() {
        return internalBufferMode;
    }

    /**
     * Enables or disables internal buffer (FBO) rendering mode.
     * When enabled, rendering targets an off-screen framebuffer rather than the display directly.
     *
     * @param internalBufferMode {@code true} to enable internal buffer mode
     */
    public void setInternalBufferMode(boolean internalBufferMode) {
        this.internalBufferMode = internalBufferMode;
    }

    // Render state

    /**
     * Returns the current drawing color as a packed RGBA integer.
     *
     * @return the current color
     */
    public int getColor() {
        return color;
    }

    /**
     * Sets the current drawing color.
     *
     * @param col the color as a packed RGBA integer
     */
    public void setColor(int col) {
        color = col;
    }

    /**
     * Returns the background clear color as a packed RGBA integer.
     *
     * @return the background color
     */
    public int getBackgroundColor() {
        return backgroundColor;
    }

    /**
     * Sets the background clear color.
     *
     * @param rgba the background color as a packed RGBA integer
     */
    public void setBackgroundColor(int rgba) {
        backgroundColor = rgba;
    }

    /**
     * Returns the current draw order value. Graphics are painted from lower to higher values.
     *
     * @return the current draw order
     */
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

    // Textures

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

    /**
     * Returns whether a texture with the given id has been registered.
     *
     * @param id the texture id to check
     * @return {@code true} if the texture is registered
     */
    public boolean hasTexture(int id) {
        return textures.containsKey(id);
    }

    /**
     * Returns the id of the most recently registered texture.
     *
     * @return the current texture id
     */
    public int getCurrentTextureId() {
        return currentTextureId;
    }

    /**
     * Sets the current texture id.
     *
     * @param currentTextureId the texture id to set
     */
    public void setCurrentTextureId(int currentTextureId) {
        this.currentTextureId = currentTextureId;
    }

    /**
     * Binds the texture with the given id for rendering, if it is not already bound.
     *
     * @param textureId the id of the texture to bind
     */
    public void bindTexture(int textureId) {
        if (textureId == 0) return;
        if (currentlyBoundTextureId == textureId) return;

        Texture texture = textures.get(textureId);
        if (texture == null) return;

        texture.bind();
        currentlyBoundTextureId = textureId;
    }

    // Images

    /**
     * Draws a full texture at the specified screen coordinates.
     *
     * @param texture the {@link Texture} to draw
     * @param x       the x-coordinate where the texture should be rendered
     * @param y       the y-coordinate where the texture should be rendered
     * @return the {@link Sprite2D} object representing the drawn image
     */
    public Sprite2D drawImage(Texture texture, int x, int y) {
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
        return sprite2D;
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

    /**
     * Draws a scaled image from the specified {@link SubImage} at the given coordinates.
     *
     * @param subImage the SubImage to draw
     * @param x        the x-coordinate where the image should be rendered
     * @param y        the y-coordinate where the image should be rendered
     * @param scale    the scale factor to apply to the image dimensions
     * @return the {@link Sprite2D} object representing the drawn image
     */
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
     * @param x         the x-coordinate where the image should be drawn
     * @param y         the y-coordinate where the image should be drawn
     * @param tileX     the x-coordinate of the tile in the TileSheet
     * @param tileY     the y-coordinate of the tile in the TileSheet
     * @return the Sprite2D object representing the drawn image
     */
    public Sprite2D drawImage(TileSheet tileSheet, double x, double y, int tileX, int tileY) {
        return drawImage(tileSheet.getSubImage(tileX, tileY), x, y);
    }

    /**
     * Draws an image using explicit vertex and texture coordinate arrays.
     * Primarily used for font rendering.
     *
     * @param texture      the {@link Texture} to draw
     * @param vertexCoords the vertex coordinates array
     * @param texCoords    the texture coordinates array
     */
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

    // Primitives

    /**
     * Integer-coordinate overload of {@link #drawLine(float, float, float, float)}.
     */
    public void drawLine(int x1, int y1, int x2, int y2) {
        drawLine((float) x1, (float) y1, (float) x2, (float) y2);
    }

    /**
     * Draws a line between two points using the current color and draw order.
     *
     * @param x1 the x-coordinate of the start point
     * @param y1 the y-coordinate of the start point
     * @param x2 the x-coordinate of the end point
     * @param y2 the y-coordinate of the end point
     */
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

    /**
     * Integer-coordinate overload of {@link #drawRect(float, float, float, float)}.
     */
    public void drawRect(int x, int y, int w, int h) {
        drawRect((float) x, (float) y, (float) w, (float) h);
    }

    /**
     * Draws the outline of a rectangle using four lines.
     *
     * @param x the x-coordinate of the top-left corner
     * @param y the y-coordinate of the top-left corner
     * @param w the width of the rectangle
     * @param h the height of the rectangle
     */
    public void drawRect(float x, float y, float w, float h) {
        drawLine(x, y, x + w, y);
        drawLine(x + w, y, x + w, y + h);
        drawLine(x + w, y + h, x, y + h);
        drawLine(x, y + h, x, y);
    }

    /**
     * Draws a filled rectangle at the specified position and size.
     *
     * @param _x the x-coordinate of the top-left corner
     * @param _y the y-coordinate of the top-left corner
     * @param _w the width of the rectangle
     * @param _h the height of the rectangle
     */
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

    /**
     * Integer-coordinate overload of {@link #filledRect(float, float, float, float)}.
     */
    public void filledRect(int _x, int _y, int _w, int _h) {
        filledRect((float) _x, (float) _y, (float) _w, (float) _h);
    }

    /**
     * Integer-coordinate overload of {@link #drawCircle(float, float, float, float)}.
     */
    public void drawCircle(int x, int y, int w, int h) {
        drawCircle((float) x, (float) y, (float) w, (float) h);
    }

    /**
     * Draws the outline of an ellipse inscribed within the specified bounding rectangle.
     *
     * @param x the x-coordinate of the bounding rectangle
     * @param y the y-coordinate of the bounding rectangle
     * @param w the width of the bounding rectangle
     * @param h the height of the bounding rectangle
     */
    public void drawCircle(float x, float y, float w, float h) {
        Circle2D circle = new Circle2D(x, y, w, h);

        circle.setCommonValues(viewportManager.getActiveViewport(), currentDrawOrder, color);

        drawableBatch.add(circle);
    }

    /**
     * Integer-coordinate overload of {@link #filledCircle(float, float, float, float)}.
     */
    public void filledCircle(int x, int y, int w, int h) {
        filledCircle((float) x, (float) y, (float) w, (float) h);
    }

    /**
     * Draws a filled ellipse inscribed within the specified bounding rectangle.
     *
     * @param x the x-coordinate of the bounding rectangle
     * @param y the y-coordinate of the bounding rectangle
     * @param w the width of the bounding rectangle
     * @param h the height of the bounding rectangle
     */
    public void filledCircle(float x, float y, float w, float h) {
        Circle2D circle = new Circle2D(x, y, w, h);
        circle.setFilled(true);

        circle.setCommonValues(viewportManager.getActiveViewport(), currentDrawOrder, color);

        drawableBatch.add(circle);
    }

    // Clipping internals

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

            glEnable(GL_SCISSOR_TEST);
            int[] clipRect = vp.getClipRect();

            int x, y, w, h;

            if (internalBufferMode) {
                // When rendering to an internal buffer (FBO), coordinates are 1:1 and relative to the FBO (top-left).
                // glScissor expects bottom-left coordinates.
                x = clipRect[0];
                y = clipRect[1];
                w = clipRect[2];
                h = clipRect[3];

                // FBO height is the same as canvas height in our implementation.
                int fboHeight = display.getCanvasSize()[1];
                glScissor(x, fboHeight - h - y, w, h);
            } else {
                w = (int) (clipRect[2] / display.glViewportScale[0]);
                h = (int) (clipRect[3] / display.glViewportScale[1]);
                x = (int) (clipRect[0] / display.glViewportScale[0]);
                y = (int) (clipRect[1] / display.glViewportScale[1]);

                x += display.glViewportOffsets[0];
                y += display.glViewportOffsets[1];

                int[] windowSize = display.getBufferSize();
                glScissor(x, windowSize[1] - h - y, w, h);
            }

            clipRectHash = vp.getClipRectHash();
        }
    }

}
