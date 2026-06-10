package com.physmo.garnet;

import com.physmo.garnet.graphics.Graphics;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

class GarnetAppTest {

    @Test
    void getNameReturnsProvidedApplicationName() {
        GarnetApp testApp = new GarnetApp("testApp") {
            @Override
            public void tick(double delta) {
            }

            @Override
            public void draw(Graphics g) {
            }
        };

        assertEquals("testApp", testApp.getName());
    }

    @Test
    void getNameDefaultsToEmptyString() {
        GarnetApp testApp = new GarnetApp() {
            @Override
            public void tick(double delta) {
            }

            @Override
            public void draw(Graphics g) {
            }
        };

        assertEquals("", testApp.getName());
    }

    @Test
    void legacyInitDelegatesToNoArgumentInit() {
        class TestApp extends GarnetApp {
            boolean initCalled;

            @Override
            public void init() {
                initCalled = true;
            }

            @Override
            public void tick(double delta) {
            }

            @Override
            public void draw(Graphics g) {
            }
        }

        TestApp testApp = new TestApp();

        testApp.init(null);

        assertEquals(true, testApp.initCalled);
    }

    @Test
    void setAppAttachesGarnetToApp() {
        Garnet garnet = new Garnet(100, 100);
        GarnetApp testApp = new GarnetApp() {
            @Override
            public void tick(double delta) {
            }

            @Override
            public void draw(Graphics g) {
            }
        };

        garnet.setApp(testApp);

        assertSame(garnet, testApp.getGarnet());
    }
}
