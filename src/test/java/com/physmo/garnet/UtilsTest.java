package com.physmo.garnet;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UtilsTest {

    @ParameterizedTest
    @CsvSource({
            "0.0, 1.0, 0.5, 0.5",
            "0.0, 10.0, 0.0, 0.0",
            "0.0, 10.0, 1.0, 10.0"
    })
    void lerpInterpolatesFloatValues(float start, float end, float position, float expected) {
        assertEquals(expected, Utils.lerp(start, end, position));
    }

    @ParameterizedTest
    @CsvSource({
            "0.0, 1.0, 0.5, 0.5",
            "0.0, 10.0, 0.0, 0.0",
            "0.0, 10.0, 1.0, 10.0"
    })
    void lerpInterpolatesDoubleValues(double start, double end, double position, double expected) {
        assertEquals(expected, Utils.lerp(start, end, position));
    }

    @ParameterizedTest
    @CsvSource({
            "5.0, 0.0, 10.0, 0.0, 100.0, 50.0",
            "0.0, 0.0, 10.0, 0.0, 100.0, 0.0",
            "10.0, 0.0, 10.0, 0.0, 100.0, 100.0",
            "5.0, 0.0, 10.0, 0.0, 0.0, 0.0"
    })
    void remapRangeMapsFloatValues(
            float value,
            float inMin,
            float inMax,
            float outMin,
            float outMax,
            float expected
    ) {
        assertEquals(expected, Utils.remapRange(value, inMin, inMax, outMin, outMax));
    }

    @ParameterizedTest
    @CsvSource({
            "5.0, 0.0, 10.0, 0.0, 100.0, 50.0",
            "0.0, 0.0, 10.0, 0.0, 100.0, 0.0",
            "10.0, 0.0, 10.0, 0.0, 100.0, 100.0",
            "5.0, 0.0, 10.0, 0.0, 0.0, 0.0"
    })
    void remapRangeMapsDoubleValues(
            double value,
            double inMin,
            double inMax,
            double outMin,
            double outMax,
            double expected
    ) {
        assertEquals(expected, Utils.remapRange(value, inMin, inMax, outMin, outMax));
    }
}
