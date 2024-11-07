package com.physmo.garnet.toolkit.simplecollision

import spock.lang.Specification

class GameObjectBucketGridTest extends Specification {

    def "Bucket Grid encoder works for a range of values."() {
        int errors = 0

        given:
            GameObjectBucketGrid gameObjectBucketGrid = new GameObjectBucketGrid(32, 32)

        when:
            for (int y = -500; y < 500; y++) {
                for (int x = -500; x < 500; x++) {
                    int encoded = gameObjectBucketGrid.encoder(x, y)
                    int[] decoded = gameObjectBucketGrid.decoder(encoded)
                    if (decoded[0] != x || decoded[1] != y) {
                        errors++;
                        println("x1:" + x + " x2:" + decoded[0] + " y1:" + y + "  y2:" + decoded[0])
                    }
                }
            }

        then:
            errors == 0
    }


}
