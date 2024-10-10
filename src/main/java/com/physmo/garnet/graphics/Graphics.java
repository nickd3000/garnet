package com.physmo.garnet.graphics;

import com.physmo.garnet.ColorUtils;
import com.physmo.garnet.Display;
import com.physmo.garnet.drawablebatch.Circle2D;
import com.physmo.garnet.drawablebatch.DrawableBatch;
import com.physmo.garnet.drawablebatch.DrawableElement;
import com.physmo.garnet.drawablebatch.Line2D;
import com.physmo.garnet.drawablebatch.Shape2D;
import com.physmo.garnet.drawablebatch.Sprite2D;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.lwjgl.opengl.GL11.GL_SCISSOR_TEST;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glScissor;

/**
 * The Graphics class is responsible for managing and rendering 2D graphics within the application,
 * including handling texture drawing, camera management, and various rendering settings.
 */
public class Graphics {

    private final Display display;
    private final Map<Integer, Texture> textures;
    private final DrawableBatch drawableBatch;

    private final CameraManager cameraManager;
    ObjectPool<Sprite2D> sprite2DObjectPool;

    private int currentTextureId = 0;
    private int color;
    private int currentDrawOrder;
    private int currentlyBoundTextureId;
    private int backgroundColor = 0;
    private double xo = 0;
    private double yo = 0;
    private int clipRectHash = 0;
    private int activeCameraId = 0;

    public Graphics(Display display) {
        this.display = display;
        drawableBatch = new DrawableBatch();
        textures = new HashMap<>();

        resetSettings();

        sprite2DObjectPool = new ObjectPool<>(Sprite2D.class, () -> new Sprite2D());
        cameraManager = new CameraManager(display.getWindowWidth(), display.getWindowHeight());

    }

    public void resetSettings() {
        //zoom = 1;
        color = ColorUtils.rgb(0xff, 0xff, 0xff, 0xff);
        currentDrawOrder = 0;
        currentlyBoundTextureId = 0;
    }

    public CameraManager getCameraManager() {
        return cameraManager;
    }

    public void render() {
        drawCameraDebugInfo();
        drawableBatch.render(this);
        releaseBatch();
        drawableBatch.clear();

    }

    private void drawCameraDebugInfo() {
        int prevCameraId = cameraManager.getActiveCamera().getId();
        double prevScale = this.getZoom();
        int prevColor = this.getColor();


        setActiveCamera(CameraManager.DEBUG_CAMERA);
        this.setZoom(1);

        Camera camera;
        int[] clipRect;
        for (int i = 0; i < 10; i++) {
            camera = cameraManager.getCamera(i);
            if (!camera.isDrawDebugInfo()) continue;
            clipRect = camera.getClipRect();

            setColor(camera.getDebugInfoColor());
            for (int j = 0; j < 5; j++) {
                this.drawRect(clipRect[0] + j, clipRect[1] + j, clipRect[2] - (j * 2), clipRect[3] - (j * 2));
            }
            //this.drawRect(camera.getWindowX(), camera.getWindowY(), camera.getWidth(), camera.getHeight());
        }
        //this.setZoom(prevScale);
        setActiveCamera(prevCameraId);
        setColor(prevColor);
    }


    public void releaseBatch() {
        List<DrawableElement> elements = drawableBatch.getElements();
        for (DrawableElement element : elements) {
            if (element.getType() == DrawableElement.SPRITE) sprite2DObjectPool.releaseObject((Sprite2D) element);
        }

    }

    /**
     * Retrieves the current zoom level of the active camera.
     *
     * @return the zoom level of the active camera
     */
    public double getZoom() {
        return cameraManager.getActiveCamera().getZoom();
    }

    /**
     * Set the zoom level of the currently active camera.
     *
     * @param zoom
     */
    public void setZoom(double zoom) {
        cameraManager.getActiveCamera().setZoom(zoom);
    }

    public int getColor() {
        return color;
    }

    public void setActiveCamera(int id) {
        if (id == activeCameraId) return;
        activeCameraId = id;
        cameraManager.setActiveCamera(id);
        Camera camera = cameraManager.getActiveCamera();
        xo = camera.getWindowX() - camera.getX();
        yo = camera.getWindowY() - camera.getY();
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
        Line2D line = new Line2D(x1, y1, x2, y2);
        line.setColor(color);
        line.setDrawOrder(currentDrawOrder);
        line.setCamera(cameraManager.getActiveCamera());
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

        sprite2D.setCommonValues(cameraManager.getActiveCamera(), currentDrawOrder, color);

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

        sprite2D.setCommonValues(cameraManager.getActiveCamera(), currentDrawOrder, color);

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
        sprite2D.setCommonValues(cameraManager.getActiveCamera(), currentDrawOrder, color);

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

        sprite2D.setCommonValues(cameraManager.getActiveCamera(), currentDrawOrder, color);

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

        circle.setCommonValues(cameraManager.getActiveCamera(), currentDrawOrder, color);

        drawableBatch.add(circle);
    }

    public void filledCircle(float x, float y, float w, float h) {
        Circle2D circle = new Circle2D(x, y, w, h);
        circle.setFilled(true);

        circle.setCommonValues(cameraManager.getActiveCamera(), currentDrawOrder, color);

        drawableBatch.add(circle);
    }

    public int getDrawOrder() {
        return currentDrawOrder;
    }

    public void setDrawOrder(int i) {
        currentDrawOrder = i;
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
        shape2D.setCamera(cameraManager.getActiveCamera());
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
     * @param camera
     */
    public void _activateClipRect(Camera camera) {

        if (!camera.isClipActive()) {
            if (clipRectHash != 0) {
                glDisable(GL_SCISSOR_TEST);
                clipRectHash = 0;
            }
        } else if (clipRectHash == camera.getClipRectHash()) {
            // Do nothing:
            // - clip rect hash matches the last cameras clip rect that was applied.
        } else {
            double[] windowToPixelsScale = display.getWindowToBufferScale();
            int[] windowSize = display.getBufferSize();
            glEnable(GL_SCISSOR_TEST);
            int[] clipRect = camera.getClipRect(); //clipRects.get(clipRectId);
            int x = (int) (clipRect[0] * windowToPixelsScale[0]);
            int y = (int) ((windowSize[1] - (clipRect[3] * windowToPixelsScale[1])) - (clipRect[1] * windowToPixelsScale[1]));
            int w = (int) (clipRect[2] * windowToPixelsScale[0]);
            int h = (int) (clipRect[3] * windowToPixelsScale[1]);
            glScissor(x, y, w, h);
            clipRectHash = camera.getClipRectHash();

        }

    }

}
