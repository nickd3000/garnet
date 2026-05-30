package com.physmo.reference.graphics;

import com.physmo.garnet.Garnet;
import com.physmo.garnet.GarnetApp;
import com.physmo.garnet.graphics.Graphics;
import com.physmo.garnet.graphics.Viewport;

// NOTE: On MacOS the following VM argument is required: -XstartOnFirstThread
public class SimpleViewportExample extends GarnetApp {
    int viewportId1 = 1;
    int viewportId2 = 2;

    public SimpleViewportExample(Garnet garnet, String name) {
        super(garnet, name);
    }

    public static void main(String[] args) {
        Garnet garnet = new Garnet(600, 300);
        garnet.setInternalBufferMode(true); // Test with internal buffer mode
        GarnetApp app = new SimpleViewportExample(garnet, "Simple Viewport Example");
        garnet.setApp(app);
        garnet.init();
        garnet.run();
    }

    @Override
    public void init(Garnet garnet) {
        Graphics graphics = garnet.getGraphics();

        // Left Viewport
        Viewport vp1 = graphics.getViewportManager().getViewport(viewportId1);
        vp1.setWindowX(10);
        vp1.setWindowY(10);
        vp1.setWidth(280);
        vp1.setHeight(280);
        vp1.setClipActive(true);

        // Right Viewport
        Viewport vp2 = graphics.getViewportManager().getViewport(viewportId2);
        vp2.setWindowX(310);
        vp2.setWindowY(10);
        vp2.setWidth(280);
        vp2.setHeight(280);
        vp2.setClipActive(true);
    }

    @Override
    public void tick(double delta) {
    }

    @Override
    public void draw(Graphics g) {
        // Draw in left viewport
        g.setActiveViewport(viewportId1);
        g.setColor(0xff0000ff); // Red
        g.drawRect(0, 0, 280, 280);
        g.filledCircle(140, 140, 100, 100);

        // Draw in right viewport
        g.setActiveViewport(viewportId2);
        g.setColor(0x00ff00ff); // Green
        g.drawRect(0, 0, 280, 280);
        g.filledRect(40, 40, 200, 200);

        // Draw a circle that should be clipped by viewport 2
        g.setColor(0xffffffff); // White
        g.filledCircle(-50, 140, 100, 100);
    }
}
