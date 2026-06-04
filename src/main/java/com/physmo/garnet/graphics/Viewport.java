package com.physmo.garnet.graphics;

/**
 * Represents a viewport that defines a rectangular region of the screen for rendering.
 * <p>
 * A viewport controls where on screen content is drawn (via window position and size),
 * how the world is scrolled (via scrollX/scrollY), and the zoom level applied to content.
 * Multiple viewports can be used to create split-screen effects or UI overlays.
 * <p>
 * Setters return {@code this} to allow method chaining.
 */
public class Viewport {
    private final int id;
    int[] clipRect = new int[4];
    double[] visibleRect = new double[4];
    private double scrollX; // Scroll x position
    private double scrollY; // Scroll y position
    private int width;
    private int height;
    private int windowX; // Window screen position
    private int windowY; // Window screen position
    private boolean clipActive;
    private int clipRectHash = 0;
    private boolean drawDebugInfo = false;
    private int debugInfoColor = 0xff00ffa0;
    private double zoom = 1;

    /**
     * Creates a new Viewport with the given id and dimensions.
     *
     * @param id     unique identifier for this viewport
     * @param width  width of the viewport in pixels
     * @param height height of the viewport in pixels
     */
    public Viewport(int id, int width, int height) {
        this.id = id;
        this.width = width;
        this.height = height;
        this.windowX = 0;
        this.windowY = 0;
        this.scrollX = 0;
        this.scrollY = 0;
        recalculate();
    }

    /**
     * Recalculates the clip rectangle and its hash based on the current window position and size.
     * Called automatically whenever position, size, or clip state changes.
     */
    public void recalculate() {
        clipRect[0] = windowX;
        clipRect[1] = windowY;
        clipRect[2] = width;
        clipRect[3] = height;
        clipRectHash = (id * 123) * (width * 234) * (height * 135) * (windowX * 311) * (windowY * 212);
        clipRectHash += (id * 123) + (width * 234) + (height * 135) + (windowX * 311) + (windowY * 212);
        clipRectHash &= 0xffffffff;
    }

    /**
     * Returns the current zoom level of this viewport.
     *
     * @return the zoom level, where 1.0 is normal size
     */
    public double getZoom() {
        return zoom;
    }

    /**
     * Sets the zoom level of this viewport.
     * A value of 1.0 is normal size; values greater than 1.0 zoom in, less than 1.0 zoom out.
     *
     * @param zoom the zoom level to apply
     * @return this viewport, for method chaining
     */
    public Viewport setZoom(double zoom) {
        this.zoom = zoom;
        recalculate();
        return this;
    }

    /**
     * Returns the color used to draw the debug border for this viewport.
     *
     * @return the debug info color as a packed RGBA integer
     */
    public int getDebugInfoColor() {
        return debugInfoColor;
    }

    /**
     * Sets the color used to draw the debug border for this viewport.
     *
     * @param debugInfoColor the color as a packed RGBA integer
     * @return this viewport, for method chaining
     */
    public Viewport setDebugInfoColor(int debugInfoColor) {
        this.debugInfoColor = debugInfoColor;
        return this;
    }

    /**
     * Returns whether debug information (a colored border) is drawn for this viewport.
     *
     * @return {@code true} if debug info is drawn
     */
    public boolean isDrawDebugInfo() {
        return drawDebugInfo;
    }

    /**
     * Sets whether a debug border should be drawn around this viewport.
     *
     * @param drawDebugInfo {@code true} to enable debug border rendering
     * @return this viewport, for method chaining
     */
    public Viewport setDrawDebugInfo(boolean drawDebugInfo) {
        this.drawDebugInfo = drawDebugInfo;
        return this;
    }

    /**
     * Returns a hash of the current clip rectangle, used to detect changes and avoid redundant GL state updates.
     *
     * @return the clip rectangle hash
     */
    public int getClipRectHash() {
        return clipRectHash;
    }

    /**
     * Returns the clip rectangle for this viewport as an array of four integers:
     * {@code [x, y, width, height]} in screen pixels.
     *
     * @return the clip rectangle array
     */
    public int[] getClipRect() {
        return clipRect;
    }

    /**
     * Returns the x position of this viewport on the screen.
     *
     * @return the screen x position in pixels
     */
    public int getWindowX() {
        return windowX;
    }

    /**
     * Sets the x position of this viewport on the screen.
     *
     * @param windowX the screen x position in pixels
     * @return this viewport, for method chaining
     */
    public Viewport setWindowX(int windowX) {
        this.windowX = windowX;
        recalculate();
        return this;
    }

    /**
     * Returns the y position of this viewport on the screen.
     *
     * @return the screen y position in pixels
     */
    public int getWindowY() {
        return windowY;
    }

    /**
     * Sets the y position of this viewport on the screen.
     *
     * @param windowY the screen y position in pixels
     * @return this viewport, for method chaining
     */
    public Viewport setWindowY(int windowY) {
        this.windowY = windowY;
        recalculate();
        return this;
    }

    /**
     * Returns the unique identifier for this viewport.
     *
     * @return the viewport id
     */
    public int getId() {
        return id;
    }

    /**
     * Returns the horizontal scroll position of this viewport.
     *
     * @return the scroll x position in world units
     */
    public double getScrollX() {
        return scrollX;
    }

    /**
     * Sets the horizontal scroll position of this viewport, effectively moving the camera along the x axis.
     *
     * @param x the scroll x position in world units
     * @return this viewport, for method chaining
     */
    public Viewport setScrollX(double x) {
        this.scrollX = x;
        return this;
    }

    /**
     * Returns the vertical scroll position of this viewport.
     *
     * @return the scroll y position in world units
     */
    public double getScrollY() {
        return scrollY;
    }

    /**
     * Sets the vertical scroll position of this viewport, effectively moving the camera along the y axis.
     *
     * @param y the scroll y position in world units
     * @return this viewport, for method chaining
     */
    public Viewport setScrollY(double y) {
        this.scrollY = y;
        return this;
    }

    /**
     * Returns the width of this viewport in pixels.
     *
     * @return the viewport width
     */
    public int getWidth() {
        return width;
    }

    /**
     * Sets the width of this viewport in pixels.
     *
     * @param width the new width
     * @return this viewport, for method chaining
     */
    public Viewport setWidth(int width) {
        this.width = width;
        recalculate();
        return this;
    }

    /**
     * Returns the height of this viewport in pixels.
     *
     * @return the viewport height
     */
    public int getHeight() {
        return height;
    }

    /**
     * Sets the height of this viewport in pixels.
     *
     * @param height the new height
     * @return this viewport, for method chaining
     */
    public Viewport setHeight(int height) {
        this.height = height;
        recalculate();
        return this;
    }

    /**
     * Returns whether scissor clipping is active for this viewport.
     * When active, rendering is clipped to the viewport's clip rectangle.
     *
     * @return {@code true} if clipping is enabled
     */
    public boolean isClipActive() {
        return clipActive;
    }

    /**
     * Enables or disables scissor clipping for this viewport.
     * When enabled, only content within the viewport's clip rectangle will be rendered.
     *
     * @param clipActive {@code true} to enable clipping
     * @return this viewport, for method chaining
     */
    public Viewport setClipActive(boolean clipActive) {
        this.clipActive = clipActive;
        recalculate();
        return this;
    }

    /**
     * Returns the visible world rectangle for this viewport as an array:
     * {@code [scrollX, scrollY, visibleWidth, visibleHeight]}.
     * The visible dimensions are the viewport size divided by the zoom level.
     *
     * @return the visible world rectangle
     */
    public double[] getVisibleRect() {
        visibleRect[0] = scrollX;
        visibleRect[1] = scrollY;
        visibleRect[2] = width / zoom;
        visibleRect[3] = height / zoom;

        return visibleRect;
    }

    /**
     * Scrolls the viewport by the given amounts, adding to the current scroll position.
     *
     * @param x the amount to scroll horizontally
     * @param y the amount to scroll vertically
     */
    public void scroll(double x, double y) {
        this.scrollX += x;
        this.scrollY += y;
    }
}
