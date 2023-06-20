package com.physmo.garnet.graphics;

public class Camera {
    private final int id;
    int[] clipRect = new int[4];
    double[] visibleRect = new double[4];
    private double x; // Scroll x
    private double y; // Scroll y
    private int width;
    private int height;
    private int windowX; // Window screen position
    private int windowY; // Window screen position
    private boolean clipActive;
    private int clipRectHash = 0;
    private boolean drawDebugInfo = false;
    private int debugInfoColor = 0xff00ffa0;
    private double zoom = 1;

    public Camera(int id, int width, int height) {
        this.id = id;
        this.width = width;
        this.height = height;
        this.windowX = 0;
        this.windowY = 0;
        this.x = 0;
        this.y = 0;
        recalculate();
    }

    public double getZoom() {
        return zoom;
    }

    public Camera setZoom(double zoom) {
        this.zoom = zoom;
        recalculate();
        return this;
    }

    public void recalculate() {
        clipRect[0] = windowX;
        clipRect[1] = windowY;
        clipRect[2] = width;
        clipRect[3] = height;
        clipRectHash = (id * 123) * (width * 234) * (height * 135) * (windowX * 311) * (windowY * 212);
        clipRectHash += (id * 123) + (width * 234) + (height * 135) + (windowX * 311) + (windowY * 212);
        clipRectHash &= 0xffffffff;
    }

    public int getDebugInfoColor() {
        return debugInfoColor;
    }

    public Camera setDebugInfoColor(int debugInfoColor) {
        this.debugInfoColor = debugInfoColor;
        return this;
    }

    public boolean isDrawDebugInfo() {
        return drawDebugInfo;
    }

    public Camera setDrawDebugInfo(boolean drawDebugInfo) {
        this.drawDebugInfo = drawDebugInfo;
        return this;
    }

    public int getClipRectHash() {
        return clipRectHash;
    }

    public int[] getClipRect() {
        return clipRect;
    }

    public int getWindowX() {
        return windowX;
    }

    public Camera setWindowX(int windowX) {
        this.windowX = windowX;
        recalculate();
        return this;
    }

    public int getWindowY() {
        return windowY;
    }

    public Camera setWindowY(int windowY) {
        this.windowY = windowY;
        recalculate();
        return this;
    }

    public int getId() {
        return id;
    }

    public double getX() {
        return x;
    }

    /**
     * Set the target or scroll position of the camara.
     *
     * @param x
     * @return
     */
    public Camera setX(double x) {
        this.x = x;
        return this;
    }

    public double getY() {
        return y;
    }

    /**
     * Set the target or scroll position of the camara.
     *
     * @param y
     * @return
     */
    public Camera setY(double y) {
        this.y = y;
        return this;
    }

    public int getWidth() {
        return width;
    }

    public Camera setWidth(int width) {
        this.width = width;
        recalculate();
        return this;
    }

    public int getHeight() {
        return height;
    }

    public Camera setHeight(int height) {
        this.height = height;
        recalculate();
        return this;
    }

    public boolean isClipActive() {
        return clipActive;
    }

    public Camera setClipActive(boolean clipActive) {
        this.clipActive = clipActive;
        recalculate();
        return this;
    }

    public double[] getVisibleRect() {
        visibleRect[0] = x;
        visibleRect[1] = y;
        visibleRect[2] = width / zoom;
        visibleRect[3] = height / zoom;

        return visibleRect;
    }

    public void scroll(double x, double y) {
        this.x += x;
        this.y += y;
    }
}
