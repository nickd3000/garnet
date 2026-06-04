package com.physmo.garnet;

import com.physmo.garnet.graphics.Graphics;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GarnetAppTest {

    @Test
    void getNameReturnsProvidedApplicationName() {
        GarnetApp testApp = new GarnetApp(null, "testApp") {
            @Override
            public void init(Garnet garnet) {
            }

            @Override
            public void tick(double delta) {
            }

            @Override
            public void draw(Graphics g) {
            }
        };

        assertEquals("testApp", testApp.getName());
    }
}
