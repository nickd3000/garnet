package com.physmo.garnet;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ColorUtilsTest {

    static Stream<Object[]> floatToRgbCases() {
        return Stream.of(
                new Object[]{new float[]{0, 0, 0, 0}, 0x00000000},
                new Object[]{new float[]{1, 0, 0, 0}, 0xFF000000},
                new Object[]{new float[]{1, 1, 1, 1}, 0xFFFFFFFF},
                new Object[]{new float[]{2, 0, 0, 0}, 0xFF000000}
        );
    }

    static Stream<Object[]> rgbaRoundTripCases() {
        return Stream.of(
                new Object[]{0x00000000, new float[]{0, 0, 0, 0}},
                new Object[]{0xFFFFFFFF, new float[]{1, 1, 1, 1}},
                new Object[]{0x197FFFFC, new float[]{25 / 255f, 127 / 255f, 255 / 255f, 252 / 255f}}
        );
    }

    @ParameterizedTest
    @MethodSource("floatToRgbCases")
    void floatToRgbConvertsFloatArrayToPackedRgba(float[] rgba, int expectedRgb) {
        assertEquals(expectedRgb, ColorUtils.floatToRgb(rgba));
    }

    @ParameterizedTest
    @MethodSource("rgbaRoundTripCases")
    void rgbToFloatConvertsPackedRgbaToFloatComponents(int rgba, float[] expected) {
        assertArrayEquals(expected, ColorUtils.rgbToFloat(rgba), 1.0f / 255.0f);
        assertEquals(rgba, ColorUtils.floatToRgb(ColorUtils.rgbToFloat(rgba)));
    }
}
