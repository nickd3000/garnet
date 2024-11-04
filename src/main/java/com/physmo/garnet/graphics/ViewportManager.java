package com.physmo.garnet.graphics;

public class ViewportManager {
    public static final int MAX_VIEWPORTS = 101;
    public static final int DEBUG_VIEWPORT = MAX_VIEWPORTS - 1;
    public double xOffset;
    public double yOffset;
    Viewport[] viewports;
    int windowWidth;
    int windowHeight;
    int activeViewportId = -1;


    public ViewportManager(int windowWidth, int windowHeight) {
        this.windowWidth = windowWidth;
        this.windowHeight = windowHeight;
        viewports = new Viewport[MAX_VIEWPORTS];

        for (int i = 0; i < MAX_VIEWPORTS; i++) {
            viewports[i] = new Viewport(i, windowWidth, windowHeight);
        }

        setActiveViewport(0);
    }

    public void refreshCurrentValues() {
        Viewport vp = getViewport(activeViewportId);
        xOffset = vp.getX();
        yOffset = vp.getY();
    }

    public Viewport getViewport(int id) {
        if (id < 0 || id >= MAX_VIEWPORTS) throw new RuntimeException("Max viewports exceeded: " + MAX_VIEWPORTS);
        return viewports[id];
    }

    public Viewport getActiveViewport() {
        return getViewport(activeViewportId);
    }

    void setActiveViewport(int id) {
        if (id == activeViewportId) return;
        if (id >= MAX_VIEWPORTS) throw new RuntimeException("Max viewports exceeded: " + MAX_VIEWPORTS);
        activeViewportId = id;
        refreshCurrentValues();
    }

    public int getActiveViewportId() {
        return activeViewportId;
    }
}
