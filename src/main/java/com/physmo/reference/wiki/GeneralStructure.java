package com.physmo.reference.wiki;

import com.physmo.garnet.Garnet;
import com.physmo.garnet.GarnetApp;
import com.physmo.garnet.graphics.Graphics;

// NOTE: On MacOS the following VM argument is required: -XstartOnFirstThread
public class GeneralStructure extends GarnetApp {

    public GeneralStructure(Garnet garnet, String name) {
        super(garnet, name);
    }

    public static void main(String[] args) {
        Garnet garnet = new Garnet(400, 400);
        GarnetApp app = new GeneralStructure(garnet, "");
        garnet.setApp(app);
        garnet.init();
        garnet.run();
    }

    @Override
    public void init(Garnet garnet) {
        garnet.getDebugDrawer().setVisible(true);
        garnet.getDebugDrawer().setUserString("message:", "Hello!");
    }

    @Override
    public void tick(double delta) {
    }

    @Override
    public void draw(Graphics g) {
    }
}

