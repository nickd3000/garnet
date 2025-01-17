package com.physmo.garnet.structure

import spock.lang.Specification

class Vector3Test extends Specification {

    def "Test Vector3 creation and initial values"() {
        when:
            Vector3 v = new Vector3()

        then:
            v.x == 0
            v.y == 0
            v.z == 0

        when:
            v = new Vector3(1.0, 2.0, 3.0)

        then:
            v.x == 1
            v.y == 2
            v.z == 3
    }

    def "Test setting Vector3 values"() {
        given: "A Vector3 instance"
            Vector3 v = new Vector3()

        when: "Values are set with the set method"
            v.set(4.0, 5.0, 6.0)

        then: "Individual position values are as expected"
            v.x == 4
            v.y == 5
            v.z == 6
    }

    def "Test setting Vector3 values from another vector"() {
        given: "Two Vector3 instances"
            Vector3 v = new Vector3()
            Vector3 v2 = new Vector3(1, 2, 3)

        when: "Values are set with the set method"
            v.set(v2)

        then: "Individual position values are as expected"
            v.x == 1
            v.y == 2
            v.z == 3
    }

    def "Test translate method"() {
        given: "Two Vector3 instances"
            Vector3 v1 = new Vector3(0, 0, 0)
            Vector3 v2 = new Vector3(1, 2, 3)

        when: "Values are set with the set method"
            v1.translate(v2)

        then: "Individual position values are as expected"
            v1.x == 1
            v1.y == 2
            v1.z == 3
    }


    def "Test addScaled method"() {
        given: "A Vector3 instance and another Vector3 to scale and add"
            Vector3 v1 = new Vector3(1, 1, 1)
            Vector3 v2 = new Vector3(2, 3, 4)

        when: "addScaled method is called with a scale factor"
            v1.addScaled(v2, 2.0)

        then: "The values of v1 are updated correctly based on the scaling and addition"
            v1.x == 5  // 1 + (2 * 2)
            v1.y == 7  // 1 + (3 * 2)
            v1.z == 9  // 1 + (4 * 2)
    }


    def "Test sub method"() {
        given: "Two Vector3 instances"
            Vector3 v1 = new Vector3(5, 5, 5)
            Vector3 v2 = new Vector3(2, 3, 4)

        when: "sub method is called"
            v1.sub(v2)

        then: "The values of v1 are updated correctly based on the subtraction"
            v1.x == 3  // 5 - 2
            v1.y == 2  // 5 - 3
            v1.z == 1  // 5 - 4
    }


    def "Test toString method"() {
        given: "A Vector3 instance with specific values"
            Vector3 v = new Vector3(1.0, 2.0, 3.0)

        when: "toString is called"
            def result = v.toString()

        then: "The output is formatted correctly with the vector values"
            result == "[x:1.00, y:2.00, z:3.00]"
    }


    def "Test normalize method"() {
        given: "A Vector3 instance with specific values"
            Vector3 v = new Vector3(3.0, 4.0, 0.0)

        when: "normalize method is called"
            v.normalise()

        then: "The vector is normalized to unit length"
            Math.abs(v.x - 0.6) < 0.0001 // 3/5
            Math.abs(v.y - 0.8) < 0.0001 // 4/5
            Math.abs(v.z - 0.0) < 0.0001
            Math.abs((v.x * v.x + v.y * v.y + v.z * v.z) - 1.0) < 0.0001 // Unit length
    }


    def "Test distance method between two vectors"() {
        given: "Two Vector3 instances with specific positions"
            Vector3 v1 = new Vector3(1.0, 2.0, 3.0)
            Vector3 v2 = new Vector3(4.0, 6.0, 3.0)

        when: "distance method is called"
            def result = v1.distance(v2)

        then: "The distance is calculated correctly"
            Math.abs(result - 5.0) < 0.0001 // sqrt((4-1)^2 + (6-2)^2 + (3-3)^2) = 5
    }

    def "Test 2d distance method"() {
        given: "Two Vector3 instances with specific positions"
            Vector3 v1 = new Vector3(1.0, 2.0, 3.0)

        when: "distanceSquared method is called"
            def result = v1.distance(2, 3)

        then: "The squared distance is calculated correctly"
            Math.abs(result - 1.414) < 0.001
    }


    def "Test length method"() {
        given: "A Vector3 instance with specific values"
            Vector3 v = new Vector3(3.0, 4.0, 0.0)

        when: "length method is called"
            def result = v.length()

        then: "The length is calculated correctly"
            Math.abs(result - 5.0) < 0.0001 // sqrt(3^2 + 4^2 + 0^2) = 5
    }


    def "Test setFromAngle method"() {
        given: "A Vector3 instance and an angle in degrees"
            Vector3 v = new Vector3()
            double angle = Math.PI // 180 degrees

        when: "setFromAngle is called"
            v.setFromAngle(angle, 1)

        then: "The vector is updated to correspond to the given angle on a unit circle"
            Math.abs(v.x - 0.0) < 0.0001 // cos(180 degrees) = 0
            Math.abs(v.y + 1.0) < 0.0001 // sin(180 degrees) = 1
            Math.abs(v.z - 0.0) < 0.0001 // assuming the Z component stays unchanged or is 0
    }

    def "Test getAngle method"() {
        given: "A Vector3 instance and an angle in degrees"
            Vector3 v = new Vector3()
            double angle = Math.PI / 2 // 180 degrees

        when: "setFromAngle is called"
            v.setFromAngle(angle, 1)

        and:
            double result = v.getAngle()
            println "x:" + v.x
            println "y:" + v.y
            println "a:" + result


        then: "The vector is updated to correspond to the given angle on a unit circle"
            Math.abs(result - angle) < 0.001 // cos(180 degrees) = 0

    }
}
