package com.physmo.reference.graphics;

import com.physmo.garnet.Garnet;
import com.physmo.garnet.GarnetApp;
import com.physmo.garnet.graphics.Graphics;

/**
 * Example showing smooth movement / FPS
 */
public class MovementExample extends GarnetApp {

    double x1 = 0, x2 = 0, x3 = 0;

    public MovementExample(Garnet garnet, String name) {
        super(garnet, name);
    }

    public static void main(String[] args) {
        Garnet garnet = new Garnet(600, 400);
        GarnetApp app = new MovementExample(garnet, "");
        garnet.setApp(app);
        garnet.init();
        garnet.run();
    }

    @Override
    public void init(Garnet garnet) {
        garnet.getDebugDrawer().setVisible(true);
        garnet.getDebugDrawer().setDrawFps(true);
    }

    @Override
    public void tick(double delta) {
        x1 = (x1 + delta * 50) % 600;
        x2 = (x2 + delta * 50 * 2) % 600;
        x3 = (x3 + delta * 50 * 5) % 600;

    }

    @Override
    public void draw(Graphics g) {

        g.filledRect((float) (x1 + 50), 50, 20, 20);
        g.filledRect((float) (x2 + 50), 150, 20, 20);
        g.filledRect((float) (x3 + 50), 250, 20, 20);
        g.drawRect(1, 1, 600 - 2, 400 - 2);
    }
}
