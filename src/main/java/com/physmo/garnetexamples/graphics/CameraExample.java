package com.physmo.garnetexamples.graphics;

import com.physmo.garnet.Garnet;
import com.physmo.garnet.GarnetApp;
import com.physmo.garnet.graphics.Camera;
import com.physmo.garnet.graphics.Graphics;
import com.physmo.garnet.graphics.Texture;
import com.physmo.garnet.graphics.TileSheet;

// NOTE: On MacOS the following VM argument is required: -XstartOnFirstThread
public class CameraExample extends GarnetApp {
    int cameraId1 = 1;
    int cameraId2 = 2;

    TileSheet tileSheet;
    Texture texture;

    double scale = 1;
    double angle = 0;

    int RED = 0xff5555ff;
    int GREEN = 0x55ff55ff;

    public CameraExample(Garnet garnet, String name) {
        super(garnet, name);
    }

    public static void main(String[] args) {
        Garnet garnet = new Garnet(400, 400);
        GarnetApp app = new CameraExample(garnet, "");

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

        Camera camera1 = graphics.getCameraManager().getCamera(cameraId1);
        camera1.setWindowX(25);
        camera1.setWindowY(25);
        camera1.setWidth(150);
        camera1.setHeight(350);
        camera1.setClipActive(true);
        camera1.setDrawDebugInfo(true);

        Camera camera2 = graphics.getCameraManager().getCamera(cameraId2);
        camera2.setWindowX(225);
        camera2.setWindowY(25);
        camera2.setWidth(150);
        camera2.setHeight(350);
        camera2.setClipActive(true);

        camera2.setDrawDebugInfo(true);
    }

    @Override
    public void tick(double delta) {
        angle += delta * 60.0;
    }

    @Override
    public void draw(Graphics g) {
        g.setZoom(scale);

        double[] mpn = garnet.getInput().getMouse().getPositionNormalised();

        Camera camera1 = garnet.getGraphics().getCameraManager().getCamera(cameraId1);
        camera1.setX(mpn[0] * 150);
        camera1.setY(mpn[1] * 150);

        g.setActiveCamera(cameraId1);
        g.setZoom(1.5);
        g.setColor(RED);
        drawSomeThings(g);

        g.setActiveCamera(cameraId2);
        g.setZoom(mpn[0] * 5);
        g.setColor(GREEN);
        drawSomeThings(g);

    }

    public void drawSomeThings(Graphics g) {
        g.drawCircle(50, 50, 50, 50);
        g.filledCircle(50, 50 + 100, 50, 50);
        g.drawRect(0, 0, 100, 100);
        for (int i = 0; i < 8; i++) {
            g.drawImage(tileSheet.getSubImage(2, 2), i * 16, i * 16);
        }

        g.drawImage(tileSheet, 16 * 3, 16, 2, 2, angle);

        // Draw a sprite at the lower right corner of the camera's visible window.
        Camera activeCamera = g.getCameraManager().getActiveCamera();
        double[] visibleRect = activeCamera.getVisibleRect();
        g.drawImage(tileSheet.getSubImage(2, 2),
                visibleRect[0] + visibleRect[2] - 16, visibleRect[1] + visibleRect[3] - 16);
    }
}

