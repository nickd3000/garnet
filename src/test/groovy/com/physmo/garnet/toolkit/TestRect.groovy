package com.physmo.garnet.toolkit

import com.physmo.garnet.structure.Rect
import spock.lang.Specification


class TestRect extends Specification {

    def "default constructor initializes properties to zero"() {
        setup:
            def rect = new Rect()

        expect:
            rect.x == 0
            rect.y == 0
            rect.w == 0
            rect.h == 0
    }

    def "parameterized constructor initializes properties"() {
        setup:
            def rect = new Rect(1, 2, 3, 4)

        expect:
            rect.x == 1
            rect.y == 2
            rect.w == 3
            rect.h == 4
    }

    def "set method updates properties"() {
        setup:
            def rect = new Rect()

        when:
            rect.set(5, 6, 7, 8)

        then:
            rect.x == 5
            rect.y == 6
            rect.w == 7
            rect.h == 8
    }

    def "intersect detects rectangles overlap"() {
        setup:
            def rect1 = new Rect(0, 0, 10, 10)
            def rect2 = new Rect(5, 5, 10, 10)
            def rect3 = new Rect(20, 20, 5, 5)

        expect:
            rect1.intersect(rect2)  // Overlapping
            !rect1.intersect(rect3) // Not overlapping
            !rect2.intersect(rect3) // Not overlapping
    }

    def "test overlap"() {
        expect:
            def rect1 = new Rect(0, 0, 10, 10)
            def rect2 = new Rect(5, 5, 10, 10)
            double[] overlap = new double[4]
            rect1.overlap(rect2, overlap)
            1 == 1
            print overlap
    }

    def "test overlap 2"() {
        expect:
            def rect1 = new Rect(0, 0, 10, 10)
            def rect2 = new Rect(10, 0, 10, 10)
            double[] overlap = new double[4]
            rect1.overlap(rect2, overlap)
            1 == 1
            print overlap
    }

    def "test overlap 3"() {
        expect:
            def rect1 = new Rect(0, 0.1, 10, 10)
            def rect2 = new Rect(0, 10, 10, 10)
            double[] overlap = new double[4]
            rect1.overlap(rect2, overlap)
            1 == 1
            print overlap
    }
}
