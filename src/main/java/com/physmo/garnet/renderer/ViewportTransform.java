package com.physmo.garnet.renderer;

import com.physmo.garnet.graphics.Viewport;

/**
 * Converts coordinates between world space and a viewport's screen-space batch coordinates.
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

    public static double worldX(Viewport viewport, double screenX) {
        return ((screenX - viewport.getWindowX()) / viewport.getZoom()) + viewport.getScrollX();
    }

    public static double worldY(Viewport viewport, double screenY) {
        return ((screenY - viewport.getWindowY()) / viewport.getZoom()) + viewport.getScrollY();
    }
}
