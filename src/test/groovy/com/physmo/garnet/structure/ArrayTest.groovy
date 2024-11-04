package com.physmo.garnet.structure


import spock.lang.Specification

class ArrayTest extends Specification {

    def "Array capacity should double when full"() {
        given:
            Array<String> array = new Array<>(5);

        when:
            for (int i = 0; i < 6; i++) {
                array.add("A")
            }

        then:
            array.size == 6
            array.getCapacity() == 10;

    }
}
