package com.physmo.reference.graphics;

import com.physmo.garnet.Garnet;
import com.physmo.garnet.GarnetApp;
import com.physmo.garnet.graphics.Graphics;
import com.physmo.garnet.graphics.SubImage;
import com.physmo.garnet.graphics.Texture;
import com.physmo.garnet.graphics.TileSheet;
import com.physmo.garnet.graphics.Viewport;

// NOTE: On MacOS the following VM argument is required: -XstartOnFirstThread
public class ViewportExample extends GarnetApp {
    int viewportId1 = 1;
    int viewportId2 = 2;

    TileSheet tileSheet;
    Texture texture;

    double scale = 1;
    double angle = 0;

    int RED = 0xff5555ff;
    int GREEN = 0x55ff55ff;

    public ViewportExample(Garnet garnet, String name) {
        super(garnet, name);
    }

    public static void main(String[] args) {
        Garnet garnet = new Garnet(400, 400);
        GarnetApp app = new ViewportExample(garnet, "");

        garnet.setApp(app);

        garnet.init();
        garnet.run();
    }

    @Override
    public void init(Garnet garnet) {
        texture = Texture.loadTexture("space.png");
        tileSheet = new TileSheet(texture, 16, 16);
        Graphics graphics = garnet.getGraphics();
        graphics.addTexture(texture);

        garnet.getDebugDrawer().setColor(0xff00ffff);
        garnet.getDebugDrawer().setVisible(true);
        garnet.getDebugDrawer().setDrawMouseCoords(true);

        Viewport vp1 = graphics.getViewportManager().getViewport(viewportId1);
        vp1.setWindowX(25);
        vp1.setWindowY(25);
        vp1.setWidth(150);
        vp1.setHeight(350);
        vp1.setClipActive(true);
        vp1.setDrawDebugInfo(true);

        Viewport vp2 = graphics.getViewportManager().getViewport(viewportId2);
        vp2.setWindowX(225);
        vp2.setWindowY(25);
        vp2.setWidth(150);
        vp2.setHeight(350);
        vp2.setClipActive(true);

        vp2.setDrawDebugInfo(true);
    }

    @Override
    public void tick(double delta) {
        angle += delta * 60.0;

        //garnet.getGraphics().getViewportManager().getViewport(viewportId1).recalculate();
    }

    @Override
    public void draw(Graphics g) {
        g.setZoom(scale);

        double[] mpn = garnet.getInput().getMouse().getPositionNormalised();

        Viewport vp1 = garnet.getGraphics().getViewportManager().getViewport(viewportId1);
        vp1.setX(mpn[0] * 150);
        vp1.setY(mpn[1] * 150);

        g.setActiveViewport(viewportId1);
        g.setZoom(1.5);
        g.setColor(RED);
        drawSomeThings(g);

        g.setActiveViewport(viewportId2);
        g.setZoom(mpn[0] * 5);
        g.setColor(GREEN);
        drawSomeThings(g);

    }

    SubImage subImage = new SubImage();

    public void drawSomeThings(Graphics g) {
        g.drawCircle(50, 50, 50, 50);
        g.filledCircle(50, 50 + 100, 50, 50);
        g.drawRect(0, 0, 100, 100);
        tileSheet.getSubImage(2, 2, subImage);

        for (int i = 0; i < 8; i++) {
            g.drawImage(subImage, i * 16, i * 16);
        }

        g.drawImage(tileSheet, 16 * 3, 16, 2, 2, angle);

        // Draw a sprite at the lower right corner of the viewport's visible window.
        Viewport activeViewport = g.getViewportManager().getActiveViewport();
        double[] visibleRect = activeViewport.getVisibleRect();
        g.drawImage(subImage,
                visibleRect[0] + visibleRect[2] - 16, visibleRect[1] + visibleRect[3] - 16);
    }
}

