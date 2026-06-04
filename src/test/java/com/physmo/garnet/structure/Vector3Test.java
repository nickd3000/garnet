package com.physmo.garnet.structure;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class Vector3Test {

    private static final double DELTA = 0.0001;

    @Test
    void constructorsInitializeValues() {
        Vector3 defaultVector = new Vector3();
        Vector3 vector = new Vector3(1.0, 2.0, 3.0);

        assertVector(defaultVector, 0, 0, 0);
        assertVector(vector, 1, 2, 3);
    }

    @Test
    void copyConstructorCreatesIndependentCopy() {
        Vector3 original = new Vector3(1, 2, 3);
        Vector3 copy = new Vector3(original);

        original.set(4, 5, 6);

        assertNotSame(original, copy);
        assertVector(copy, 1, 2, 3);
    }

    private static void assertVector(Vector3 vector, double x, double y, double z) {
        assertEquals(x, vector.x, DELTA);
        assertEquals(y, vector.y, DELTA);
        assertEquals(z, vector.z, DELTA);
    }

    @Test
    void setUpdatesValues() {
        Vector3 vector = new Vector3();

        vector.set(4.0, 5.0, 6.0);

        assertVector(vector, 4, 5, 6);
    }

    @Test
    void setCopiesValuesFromAnotherVector() {
        Vector3 vector = new Vector3();
        Vector3 other = new Vector3(1, 2, 3);

        vector.set(other);

        assertVector(vector, 1, 2, 3);
    }

    @Test
    void setHandlesSelfAssignment() {
        Vector3 vector = new Vector3(3, 3, 3);

        vector.set(vector);

        assertVector(vector, 3, 3, 3);
    }

    @Test
    void translateAddsAnotherVector() {
        Vector3 v1 = new Vector3(0, 0, 0);
        Vector3 v2 = new Vector3(1, 2, 3);

        v1.translate(v2);

        assertVector(v1, 1, 2, 3);
    }

    @Test
    void translateHandlesMixedSigns() {
        Vector3 v1 = new Vector3(1, 2, 3);
        Vector3 v2 = new Vector3(3, 2, -1);

        v1.translate(v2);

        assertVector(v1, 4, 4, 2);
    }

    @Test
    void addScaledAddsScaledVector() {
        Vector3 v1 = new Vector3(1, 1, 1);
        Vector3 v2 = new Vector3(2, 3, 4);

        v1.addScaled(v2, 2.0);

        assertVector(v1, 5, 7, 9);
    }

    @Test
    void addScaledHandlesFractionalScale() {
        Vector3 v1 = new Vector3(3, 3, 3);
        Vector3 v2 = new Vector3(3, 3, 3);

        v1.addScaled(v2, 0.5);

        assertVector(v1, 4.5, 4.5, 4.5);
    }

    @Test
    void scaleMultipliesEachComponent() {
        Vector3 v1 = new Vector3(1, 2, 3);
        Vector3 v2 = new Vector3(3, 2, -1);

        v1.scale(1);
        v2.scale(2);

        assertVector(v1, 1, 2, 3);
        assertVector(v2, 6, 4, -2);
    }

    @Test
    void subSubtractsAnotherVector() {
        Vector3 v1 = new Vector3(5, 5, 5);
        Vector3 v2 = new Vector3(2, 3, 4);

        v1.sub(v2);

        assertVector(v1, 3, 2, 1);
    }

    @Test
    void toStringFormatsValues() {
        Vector3 vector = new Vector3(1.0, 2.0, 3.0);

        assertEquals("[x:1.00, y:2.00, z:3.00]", vector.toString());
    }

    @Test
    void normaliseScalesVectorToUnitLength() {
        Vector3 vector = new Vector3(3.0, 4.0, 0.0);

        vector.normalise();

        assertVector(vector, 0.6, 0.8, 0.0);
        assertEquals(1.0, vector.length(), DELTA);
    }

    @Test
    void normaliseZeroVectorProducesNaNComponents() {
        Vector3 vector = new Vector3(0, 0, 0);

        vector.normalise();

        assertTrue(Double.isNaN(vector.x));
        assertTrue(Double.isNaN(vector.y));
        assertTrue(Double.isNaN(vector.z));
    }

    @Test
    void getDirectionToReturnsDirectionFromOtherToThis() {
        Vector3 vector = new Vector3(10, 0, 0);
        Vector3 other = new Vector3(0, 0, 0);

        Vector3 direction = vector.getDirectionTo(other);

        assertVector(direction, 1, 0, 0);
    }

    @Test
    void generateRandomRadial2DReturnsRequestedLengthWithZeroZ() {
        for (int i = 0; i < 100; i++) {
            Vector3 vector = Vector3.generateRandomRadial2D(7.5);

            assertEquals(7.5, vector.length(), DELTA);
            assertEquals(0, vector.z, DELTA);
        }
    }

    @Test
    void distanceCalculatesDistanceBetweenVectors() {
        Vector3 v1 = new Vector3(1.0, 2.0, 3.0);
        Vector3 v2 = new Vector3(4.0, 6.0, 3.0);

        assertEquals(5.0, v1.distance(v2), DELTA);
    }

    @Test
    void distanceCalculates2dDistanceToCoordinates() {
        Vector3 vector = new Vector3(1.0, 2.0, 3.0);

        assertEquals(Math.sqrt(2), vector.distance(2, 3), DELTA);
    }

    @Test
    void lengthCalculatesMagnitude() {
        Vector3 vector = new Vector3(3.0, 4.0, 0.0);

        assertEquals(5.0, vector.length(), DELTA);
    }

    @Test
    void setFromAngleUpdatesXAndYUsingMagnitude() {
        Vector3 vector = new Vector3();

        vector.setFromAngle(Math.PI, 1);

        assertVector(vector, 0.0, -1.0, 0.0);
    }

    @Test
    void setFromAnglePreservesZ() {
        Vector3 vector = new Vector3(0, 0, 9);

        vector.setFromAngle(Math.PI / 2, 2);

        assertVector(vector, 2, 0, 9);
    }

    @Test
    void getAngleReturnsAngleUsedBySetFromAngle() {
        Vector3 vector = new Vector3();
        double angle = Math.PI / 2;

        vector.setFromAngle(angle, 1);

        assertEquals(angle, vector.getAngle(), 0.001);
    }

    @Test
    void getAngleReturnsCardinalDirections() {
        assertEquals(0, new Vector3(0, 1, 0).getAngle(), DELTA);
        assertEquals(Math.PI / 2, new Vector3(1, 0, 0).getAngle(), DELTA);
        assertEquals(Math.PI, new Vector3(0, -1, 0).getAngle(), DELTA);
        assertEquals(Math.PI * 1.5, new Vector3(-1, 0, 0).getAngle(), DELTA);
    }
}
