package com.physmo.garnet;

import com.physmo.garnet.graphics.Graphics;
import com.physmo.garnet.regularfont.RegularFont;

import java.util.HashMap;
import java.util.Map;

public class DebugDrawer {
    private RegularFont regularFont;
    private final Map<String, String> userStrings = new HashMap<>();
    private double fps;
    private final int textColor = 0xffdd00ff;
    private final int shadowColor = 0x000000d0;
    private boolean visible = true;
    private boolean drawFps = true;
    private final int lineHeight = 10;

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public void setDrawFps(boolean drawFps) {
        this.drawFps = drawFps;
    }

    public void init() {
        //visible=false;
        regularFont = new RegularFont(Utils.getPathForResource(this, "drake_10x10.png"), 10, 10);
        regularFont.setHorizontalPad(-1);
    }

    public void draw(Graphics g) {
        if (!visible) return;

        int y = 5;

        int prevColor = g.getColor();
        double prevScale = g.getScale();
        g.setScale(1);

        if (drawFps) y += drawString(g, "FPS: " + fps, y);
        y += drawUserStrings(g, y);

        g.setColor(prevColor);
        g.setScale(prevScale);

    }

    public int drawString(Graphics g, String str, int y) {

        g.setDrawOrder(100);
        g.setColor(shadowColor);
        regularFont.drawText(g, str, 5 + 1, y + 1);
        regularFont.drawText(g, str, 5 + 2, y + 2);

        g.setDrawOrder(101);
        g.setColor(textColor);
        regularFont.drawText(g, str, 5, y);

        return lineHeight;
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
}
