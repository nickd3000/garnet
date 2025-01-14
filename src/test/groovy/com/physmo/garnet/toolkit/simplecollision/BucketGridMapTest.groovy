package com.physmo.garnet.toolkit.simplecollision

import spock.lang.Specification

class BucketGridMapTest extends Specification {
    def "cdscs"() {
        given:
            BucketGridMap bucketGridMap = new BucketGridMap()

        when:
            List<Object> obj = new ArrayList<>()

            for (int i = 0; i < 5000; i++) {
                //println i
                bucketGridMap.put(i, obj)
            }

        then:
            bucketGridMap.size() == 5000

    }
}
