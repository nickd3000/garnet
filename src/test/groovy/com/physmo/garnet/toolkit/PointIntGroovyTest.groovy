package com.physmo.garnet.toolkit


import spock.lang.Specification

class PointIntGroovyTest extends Specification {

    def "PointInt creation should work as expected"() {

        given:
            PointInt p1 = new PointInt()
            PointInt p2 = new PointInt(1, 2)
            PointInt p3 = new PointInt(1, 2, 3)
            PointInt p4 = new PointInt(p3)

        when:
            def string = p1.toString()

        then:
            p1.x == 0
            p1.y == 0
            p1.z == 0
            p2.x == 1
            p2.y == 2
            p3.x == p4.x
            p3.y == p4.y
            p3.z == p4.z

    }
}