package com.physmo.garnet.toolkit

import spock.lang.Specification

class Vector3Test extends Specification {

    def "default constructor initializes properties to zero"() {
        setup:
            def vector3 = new Vector3()

        expect:
            vector3.x == 0
            vector3.y == 0
            vector3.z == 0
    }

    def "Test set variants"() {
        given:
            def v1 = new Vector3(1, 1, 1)
            def v2 = new Vector3(2, 2, 2)
            def v3 = new Vector3(3, 3, 3)
        when:
            v1.set(v2)
            v2.set(3, 3, 3)
            v3.set(v3)

        then:
            checkVector3(v1, 2, 2, 2)
            checkVector3(v2, 3, 3, 3)
            checkVector3(v3, 3, 3, 3)

    }

    def "Test translation variants"() {
        given:
            def v1 = new Vector3(1, 2, 3)
            def v2 = new Vector3(3, 2, -1)
            def v3 = new Vector3(3, 3, 3)
            def v4 = new Vector3(3, 3, 3)
        when:
            v1.translate(v2)
            v3.addScaled(v4, 0.5)

        then:
            checkVector3(v1, 4, 4, 2)
            checkVector3(v3, 4.5, 4.5, 4.5)

    }

    def "Test scale"() {
        given:
            def v1 = new Vector3(1, 2, 3)
            def v2 = new Vector3(3, 2, -1)

        when:
            v1.scale(1)
            v2.scale(2)

        then:
            checkVector3(v1, 1, 2, 3)
            checkVector3(v2, 6, 4, -2)

    }

    boolean checkVector3(Vector3 v, double x, double y, double z) {
        if (v.x != x) return false
        if (v.y != y) return false
        if (v.z != z) return false
        return true
    }
}
