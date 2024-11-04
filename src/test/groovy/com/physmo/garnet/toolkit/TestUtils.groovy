package com.physmo.garnet.toolkit

import spock.lang.Specification

class TestUtils extends Specification {

    def "lerp for floats"() {
        expect:
            com.physmo.garnet.Utils.lerp(0.0f, 1.0f, 0.5f) == 0.5f
            com.physmo.garnet.Utils.lerp(0.0f, 10.0f, 0.0f) == 0.0f
            com.physmo.garnet.Utils.lerp(0.0f, 10.0f, 1.0f) == 10.0f
    }

    def "lerp for doubles"() {
        expect:
            com.physmo.garnet.Utils.lerp(0.0, 1.0, 0.5) == 0.5
            com.physmo.garnet.Utils.lerp(0.0, 10.0, 0.0) == 0.0
            com.physmo.garnet.Utils.lerp(0.0, 10.0, 1.0) == 10.0
    }

    def "remapRange for floats"() {
        expect:
            com.physmo.garnet.Utils.remapRange(5.0f, 0.0f, 10.0f, 0.0f, 100.0f) == 50.0f
            com.physmo.garnet.Utils.remapRange(0.0f, 0.0f, 10.0f, 0.0f, 100.0f) == 0.0f
            com.physmo.garnet.Utils.remapRange(10.0f, 0.0f, 10.0f, 0.0f, 100.0f) == 100.0f
            com.physmo.garnet.Utils.remapRange(5.0f, 0.0f, 10.0f, 0.0f, 0.0f) == 0.0f
    }

    def "remapRange for doubles"() {
        expect:
            com.physmo.garnet.Utils.remapRange(5.0, 0.0, 10.0, 0.0, 100.0) == 50.0
            com.physmo.garnet.Utils.remapRange(0.0, 0.0, 10.0, 0.0, 100.0) == 0.0
            com.physmo.garnet.Utils.remapRange(10.0, 0.0, 10.0, 0.0, 100.0) == 100.0
            com.physmo.garnet.Utils.remapRange(5.0, 0.0, 10.0, 0.0, 0.0) == 0.0
    }

}
