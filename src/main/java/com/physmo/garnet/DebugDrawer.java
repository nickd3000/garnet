package com.physmo.garnet;

import com.physmo.garnet.graphics.CameraManager;
import com.physmo.garnet.graphics.Graphics;
import com.physmo.garnet.input.Input;
import com.physmo.garnet.text.RegularFont;

import java.util.HashMap;
import java.util.Map;

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

    public void init() {
        //visible = false;
        regularFont = new RegularFont(DEBUG_FONT_NAME, 10, 10);
        regularFont.setHorizontalPad(-1);
    }

    public void draw(Graphics g) {
        if (!visible) return;

        int y = 5;

        int prevColor = g.getColor();
        double prevScale = g.getZoom();
        int prevDrawOrder = g.getDrawOrder();
        int prevCameraId = g.getCameraManager().getActiveCameraId();

        g.setZoom(scale);
        g.setActiveCamera(CameraManager.DEBUG_CAMERA);
        if (drawFps) y += drawString(g, "FPS: " + fps, y);
        if (drawMouseCoords) y += drawString(g, getMouseCoordsString(), y);
        drawUserStrings(g, y);

        g.setColor(prevColor);
        g.setZoom(prevScale);
        g.setDrawOrder(prevDrawOrder);
        g.setActiveCamera(prevCameraId);
    }

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

    public void setUserString(String name, String value) {
        userStrings.put(name, value);
    }

    public void clearUserString(String name) {
        userStrings.remove(name);
    }

    public void setFPS(double fps) {
        this.fps = fps;
    }

    public void setColor(int i) {
        textColor = i;
    }
}
