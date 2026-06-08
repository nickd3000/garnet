package com.physmo.garnet.renderer;

import com.physmo.garnet.graphics.Viewport;

/**
 * CPU equivalent of the legacy fixed-function viewport matrix transform.
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
