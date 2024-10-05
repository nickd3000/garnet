package com.physmo.garnet;

import spock.lang.Specification

class TestUtils extends Specification {

    def "Test floatToRgb"(float[] floatArray) {
        expect:
            Utils.floatToRgb(floatArray) == expectedRgb
        where:
            floatArray   || expectedRgb
            [0, 0, 0, 0] || (int) 0x00000000
            [1, 0, 0, 0] || (int) 0xFF000000
            [1, 1, 1, 1] || (int) 0xFFFFFFFF
            [2, 0, 0, 0] || (int) 0xFF000000

    }
}
