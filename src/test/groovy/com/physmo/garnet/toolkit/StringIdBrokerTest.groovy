package com.physmo.garnet.toolkit

import spock.lang.Specification

class StringIdBrokerTest extends Specification {

    def "getId should throw IllegalArgumentException for null or empty key"() {
        when: "StringIdBroker's getId is called with a null or empty key"
            StringIdBroker.getInstance().getId(key)

        then: "IllegalArgumentException is thrown"
            thrown(IllegalArgumentException)

        where:
            key << [null, ""]
    }

    def "getId returns the same ID for the same key on subsequent calls"() {
        given: "A key string"
            String key = "sampleKey"

        when: "We request an ID for that key twice"
            int id1 = StringIdBroker.getInstance().getId(key)
            int id2 = StringIdBroker.getInstance().getId(key)

        then: "The returned IDs should be the same"
            id1 == id2
    }

    def "getId returns different IDs for different keys"() {
        given: "Two different key strings"
            String key1 = "sampleKey1"
            String key2 = "sampleKey2"

        when: "We request an ID for each key"
            int id1 = StringIdBroker.getInstance().getId(key1)
            int id2 = StringIdBroker.getInstance().getId(key2)

        then: "The returned IDs should be different"
            id1 != id2
    }
}