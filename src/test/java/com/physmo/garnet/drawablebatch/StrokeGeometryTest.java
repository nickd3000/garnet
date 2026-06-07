package com.physmo.garnet.drawablebatch;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StrokeGeometryTest {

    @Test
    void createsHorizontalLineQuadCenteredOnSegment() {
        float[] coords = StrokeGeometry.createLineQuad(10, 20, 30, 20, 4);

        assertArrayEquals(new float[]{
                10, 22,
                30, 22,
                30, 18,
                10, 18
        }, coords, 0.0001f);
    }

    @Test
    void createsVerticalLineQuadCenteredOnSegment() {
        float[] coords = StrokeGeometry.createLineQuad(10, 20, 10, 40, 6);

        assertArrayEquals(new float[]{
                7, 20,
                7, 40,
                13, 40,
                13, 20
        }, coords, 0.0001f);
    }

    @Test
    void createsDiagonalLineQuadWithExpectedDistanceFromSegment() {
        float[] coords = StrokeGeometry.createLineQuad(0, 0, 10, 10, 4);

        assertEquals(8, coords.length);
        assertEquals(-1.4142f, coords[0], 0.0001f);
        assertEquals(1.4142f, coords[1], 0.0001f);
        assertEquals(8.5858f, coords[2], 0.0001f);
        assertEquals(11.4142f, coords[3], 0.0001f);
        assertEquals(11.4142f, coords[4], 0.0001f);
        assertEquals(8.5858f, coords[5], 0.0001f);
    }

    @Test
    void zeroLengthLineProducesNoGeometry() {
        assertEquals(0, StrokeGeometry.createLineQuad(5, 5, 5, 5, 4).length);
    }

    @Test
    void createsClosedEllipseRingStrip() {
        float[] coords = StrokeGeometry.createEllipseRingStrip(100, 200, 20, 10, 4, 8);

        assertEquals((8 + 1) * 4, coords.length);
        assertEquals(coords[0], coords[coords.length - 4], 0.0001f);
        assertEquals(coords[1], coords[coords.length - 3], 0.0001f);
        assertEquals(coords[2], coords[coords.length - 2], 0.0001f);
        assertEquals(coords[3], coords[coords.length - 1], 0.0001f);
    }

    @Test
    void ellipseRingClampsInnerRadiiToCenterWhenThicknessExceedsRadius() {
        float[] coords = StrokeGeometry.createEllipseRingStrip(100, 200, 1, 1, 6, 5);

        for (int i = 2; i < coords.length; i += 4) {
            assertEquals(100, coords[i], 0.0001f);
            assertEquals(200, coords[i + 1], 0.0001f);
        }
    }

    @Test
    void ellipseSegmentCountMatchesLegacyMinimumAndScaling() {
        assertEquals(5, StrokeGeometry.calculateEllipseSegments(2, 2));
        assertTrue(StrokeGeometry.calculateEllipseSegments(100, 50) > 5);
    }
}
