package com.physmo.garnet;

import com.physmo.garnet.graphics.Graphics;
import com.physmo.garnet.graphics.ViewportManager;
import com.physmo.garnet.input.Input;
import com.physmo.garnet.text.RegularFont;

import java.util.HashMap;
import java.util.Map;

/**
 * Renders an on-screen debug overlay showing FPS, mouse coordinates, and arbitrary
 * user-defined key/value strings.
 * <p>
 * Toggle visibility with {@link #setVisible}. Individual sections (FPS, mouse coords)
 * can be enabled independently. The overlay always renders on top via the debug viewport.
 */
public class DebugDrawer {
    public static String DEBUG_FONT_NAME = "drake_10x10.png";
    private final Map<String, String> userStrings = new HashMap<>();
    private final int shadowColor = 0x000000d0;
    private final int lineHeight = 10;
    Input input;
    private int textColor = 0xffdd00ff;
    private RegularFont regularFont;
    private double fps;
    private boolean visible = false;
    private boolean drawFps = false;
    private boolean drawMouseCoords = false;
    private double scale = 1;

    public DebugDrawer(Input input) {
        this.input = input;
    }

    public double getScale() {
        return scale;
    }

    public void setScale(double scale) {
        this.scale = scale;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public void setDrawFps(boolean drawFps) {
        this.drawFps = drawFps;
    }

    public void setDrawMouseCoords(boolean drawMouseCoords) {
        this.drawMouseCoords = drawMouseCoords;
    }

    /**
     * Initialises the debug font. Must be called before {@link #draw}.
     */
    public void init() {
        //visible = false;
        regularFont = new RegularFont(DEBUG_FONT_NAME, 10, 10);
        regularFont.setHorizontalPad(-1);
        regularFont.setScale(scale);
    }

    /**
     * Draws the debug overlay if {@link #setVisible visible} is {@code true}.
     * Saves and restores all graphics state (colour, zoom, draw order, viewport).
     *
     * @param g the graphics context
     */
    public void draw(Graphics g) {
        if (!visible) return;

        int y = 5;

        int prevColor = g.getColor();
        double prevScale = g.getZoom();
        int prevDrawOrder = g.getDrawOrder();
        int prevViewportId = g.getViewportManager().getActiveViewportId();

        g.setActiveViewport(ViewportManager.DEBUG_VIEWPORT);
        g.getViewportManager().getViewport(ViewportManager.DEBUG_VIEWPORT).setZoom(scale);
        if (drawFps) y += drawString(g, "FPS: " + fps, y);
        if (drawMouseCoords) y += drawString(g, getMouseCoordsString(), y);
        drawUserStrings(g, y);

        g.setColor(prevColor);
        g.setZoom(prevScale);
        g.setDrawOrder(prevDrawOrder);
        g.setActiveViewport(prevViewportId);
    }

    /**
     * Draws a single debug string with a drop shadow at the given y position.
     *
     * @param g   the graphics context
     * @param str the string to draw
     * @param y   the y-coordinate (in debug viewport space)
     * @return the line height advance to use for the next line
     */
    public int drawString(Graphics g, String str, int y) {

        g.setDrawOrder(100);
        g.setColor(shadowColor);
        regularFont.drawText(g, str, 5 + 1, y + 1);
        regularFont.drawText(g, str, 5 + 2, y + 2);

        g.setDrawOrder(101);
        g.setColor(textColor);
        regularFont.drawText(g, str, 5, y);

        return (int) (lineHeight * scale);
    }

    /**
     * Returns a formatted string showing the current mouse canvas coordinates.
     *
     * @return a string of the form {@code "Mouse X:nnn Y:nnn"}
     */
    public String getMouseCoordsString() {
        int[] mousePosition = input.getMouse().getPosition();
        return String.format("Mouse X:%d Y:%d", mousePosition[0], mousePosition[1]);
    }

    private int drawUserStrings(Graphics g, int y) {
        int yy = 0;

        for (String key : userStrings.keySet()) {
            yy += drawString(g, key + " " + userStrings.get(key), y + yy);
        }

        return yy;
    }

    /**
     * Adds or updates a named debug string displayed in the overlay.
     *
     * @param name  the label shown before the value
     * @param value the value string to display
     */
    public void setUserString(String name, String value) {
        userStrings.put(name, value);
    }

    /**
     * Removes a previously added debug string from the overlay.
     *
     * @param name the label of the string to remove
     */
    public void clearUserString(String name) {
        userStrings.remove(name);
    }

    /**
     * Updates the FPS value displayed in the overlay.
     * Called each frame by the main loop.
     *
     * @param fps the current frames-per-second value
     */
    public void setFPS(double fps) {
        this.fps = fps;
    }

    /**
     * Sets the text colour used for debug strings.
     *
     * @param i the packed RGBA colour
     */
    public void setColor(int i) {
        textColor = i;
    }

    public void drawFrameGraphs(Graphics g) {

    }
}
