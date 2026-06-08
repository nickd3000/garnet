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
}
