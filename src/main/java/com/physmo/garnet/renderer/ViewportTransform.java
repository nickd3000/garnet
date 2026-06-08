package com.physmo.garnet.renderer;

import com.physmo.garnet.graphics.Viewport;

/**
 * Converts world coordinates into the active viewport's screen-space batch coordinates.
 */
public final class ViewportTransform {
    private ViewportTransform() {
    }

    public static float screenX(Viewport viewport, float worldX) {
        double zoom = viewport.getZoom();
        return (float) (viewport.getWindowX() - (viewport.getScrollX() * zoom) + (worldX * zoom));
    }

    public static float screenY(Viewport viewport, float worldY) {
        double zoom = viewport.getZoom();
        return (float) (viewport.getWindowY() - (viewport.getScrollY() * zoom) + (worldY * zoom));
    }
}
