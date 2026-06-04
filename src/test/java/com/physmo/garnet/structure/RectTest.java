package com.physmo.garnet.structure;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RectTest {

    private static final double DELTA = 0.0001;

    @Test
    void defaultConstructorInitializesPropertiesToZero() {
        Rect rect = new Rect();

        assertEquals(0, rect.x);
        assertEquals(0, rect.y);
        assertEquals(0, rect.w);
        assertEquals(0, rect.h);
    }

    @Test
    void constructorInitializesProperties() {
        Rect rect = new Rect(1, 2, 3, 4);

        assertEquals(1, rect.x);
        assertEquals(2, rect.y);
        assertEquals(3, rect.w);
        assertEquals(4, rect.h);
    }

    @Test
    void setUpdatesProperties() {
        Rect rect = new Rect();

        rect.set(5, 6, 7, 8);

        assertEquals(5, rect.x);
        assertEquals(6, rect.y);
        assertEquals(7, rect.w);
        assertEquals(8, rect.h);
    }

    @Test
    void intersectDetectsRectangleOverlap() {
        Rect rect1 = new Rect(0, 0, 10, 10);
        Rect rect2 = new Rect(5, 5, 10, 10);
        Rect rect3 = new Rect(20, 20, 5, 5);

        assertTrue(rect1.intersect(rect2));
        assertFalse(rect1.intersect(rect3));
        assertFalse(rect2.intersect(rect3));
    }

    @Test
    void intersectTreatsTouchingEdgesAsIntersection() {
        Rect rect = new Rect(0, 0, 10, 10);

        assertTrue(rect.intersect(new Rect(10, 0, 5, 5)));
        assertTrue(rect.intersect(new Rect(-5, 0, 5, 5)));
        assertTrue(rect.intersect(new Rect(0, 10, 5, 5)));
        assertTrue(rect.intersect(new Rect(0, -5, 5, 5)));
    }

    @Test
    void intersectDetectsContainedIdenticalAndNegativeCoordinateRectangles() {
        Rect rect = new Rect(-10, -10, 20, 20);

        assertTrue(rect.intersect(new Rect(-5, -5, 2, 2)));
        assertTrue(rect.intersect(new Rect(-10, -10, 20, 20)));
        assertTrue(rect.intersect(new Rect(-15, -15, 6, 6)));
        assertFalse(rect.intersect(new Rect(-30, -30, 5, 5)));
    }

    @Test
    void overlapReportsRightAndBottomOverlap() {
        Rect rect1 = new Rect(0, 0, 10, 10);
        Rect rect2 = new Rect(5, 5, 10, 10);
        double[] overlap = new double[4];

        rect1.overlap(rect2, overlap);

        assertArrayEquals(new double[]{0, 5, 5, 0}, overlap, DELTA);
    }

    @Test
    void overlapReportsTopRightBottomAndLeftDirectionalValues() {
        Rect rect = new Rect(0, 0, 10, 10);

        assertOverlap(rect, new Rect(0, -5, 10, 10), new double[]{5, 0, 0, 10});
        assertOverlap(rect, new Rect(5, 0, 10, 10), new double[]{0, 5, 10, 0});
        assertOverlap(rect, new Rect(0, 5, 10, 10), new double[]{0, 0, 5, 10});
        assertOverlap(rect, new Rect(-5, 0, 10, 10), new double[]{0, 0, 10, 5});
    }

    private static void assertOverlap(Rect rect, Rect other, double[] expected) {
        double[] overlap = new double[4];

        rect.overlap(other, overlap);

        assertArrayEquals(expected, overlap, DELTA);
    }

    @Test
    void overlapLeavesArrayUnchangedWhenEdgesTouch() {
        Rect rect1 = new Rect(0, 0, 10, 10);
        Rect rect2 = new Rect(10, 0, 10, 10);
        double[] overlap = new double[]{1, 2, 3, 4};

        rect1.overlap(rect2, overlap);

        assertArrayEquals(new double[]{1, 2, 3, 4}, overlap, DELTA);
    }

    @Test
    void overlapReportsSmallVerticalOverlap() {
        Rect rect1 = new Rect(0, 0.1, 10, 10);
        Rect rect2 = new Rect(0, 10, 10, 10);
        double[] overlap = new double[4];

        rect1.overlap(rect2, overlap);

        assertArrayEquals(new double[]{0, 0, 0.1, 10}, overlap, DELTA);
    }

    @Test
    void overlapLeavesArrayUnchangedWhenRectanglesDoNotOverlap() {
        Rect rect1 = new Rect(0, 0, 10, 10);
        Rect rect2 = new Rect(20, 0, 10, 10);
        double[] overlap = new double[]{1, 2, 3, 4};

        rect1.overlap(rect2, overlap);

        assertArrayEquals(new double[]{1, 2, 3, 4}, overlap, DELTA);
    }
}
