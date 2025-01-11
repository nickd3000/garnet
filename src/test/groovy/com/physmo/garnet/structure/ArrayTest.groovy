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
            array.size() == 6
            array.getCapacity() == 10;

    }

    def "Clear clears the Array"() {
        given:
            Array<String> array = new Array<>(5);

        when:
            array.add("A")
            array.add("B")
            array.add("C")
            array.add("D")
            array.clear()

        then:
            array.size == 0

    }

    def "Array items can be added and retrieved"() {
        given:
            Array<String> array = new Array<>(5);

        when:
            array.add("A")
            array.add("B")
            array.add("C")


        then:
            array.get(0) == "A"
            array.get(1) == "B"
            array.get(2) == "C"

    }

    def "Array iterator works as expected"() {

        given:
            Array<String> array = new Array<>(5);

        and:
            array.add("A")
            array.add("B")
            array.add("C")

        when:
            int count = 0
            String[] strings = new String[5]
            for (String str : array) {
                strings[count++] = str
            }

        then:
            strings[0] == "A"
            strings[1] == "B"
            strings[2] == "C"

    }

}
