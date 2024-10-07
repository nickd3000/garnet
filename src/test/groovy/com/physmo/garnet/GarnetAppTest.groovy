package com.physmo.garnet

import com.physmo.garnet.graphics.Graphics
import spock.lang.Specification

class GarnetAppTest extends Specification {
    def "Given a minimal implementation of GarnetApp"() {
        given:
            GarnetApp testApp = new GarnetApp(null, "testApp") {
                @Override
                void init(Garnet garnet) {

                }

                @Override
                void tick(double delta) {

                }

                @Override
                void draw(Graphics g) {

                }
            }

        expect: "getName returns the given name"
            testApp.getName() == "testApp"


    }
}
