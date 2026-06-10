package com.physmo.garnet.renderer;

import com.physmo.garnet.graphics.Viewport;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ViewportTransformTest {

    @Test
    void matchesLegacyTranslateThenScaleViewportFormula() {
        Viewport viewport = new Viewport(1, 320, 200)
                .setWindowX(100)
                .setWindowY(50)
                .setZoom(1.5);
        viewport.setScrollX(20);
        viewport.setScrollY(10);

        assertEquals(85, ViewportTransform.screenX(viewport, 10), 0.0001f);
        assertEquals(42.5f, ViewportTransform.screenY(viewport, 5), 0.0001f);
    }

    @Test
    void convertsScreenCoordinatesBackToWorldCoordinates() {
        Viewport viewport = new Viewport(1, 320, 200)
                .setWindowX(100)
                .setWindowY(50)
                .setZoom(1.5);
        viewport.setScrollX(20);
        viewport.setScrollY(10);

        assertEquals(10, ViewportTransform.worldX(viewport, 85), 0.0001f);
        assertEquals(5, ViewportTransform.worldY(viewport, 42.5), 0.0001f);
    }

    @Test
    void screenAndWorldTransformsRoundTripWithViewportOffsetAndZoom() {
        Viewport viewport = new Viewport(1, 320, 200)
                .setWindowX(25)
                .setWindowY(40)
                .setZoom(4);
        viewport.setScrollX(6);
        viewport.setScrollY(8);

        float screenX = ViewportTransform.screenX(viewport, 30);
        float screenY = ViewportTransform.screenY(viewport, 50);

        assertEquals(30, ViewportTransform.worldX(viewport, screenX), 0.0001f);
        assertEquals(50, ViewportTransform.worldY(viewport, screenY), 0.0001f);
    }
}
