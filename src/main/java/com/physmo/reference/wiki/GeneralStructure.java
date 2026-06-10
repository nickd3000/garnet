package com.physmo.reference.wiki;

import com.physmo.garnet.Garnet;
import com.physmo.garnet.GarnetApp;
import com.physmo.garnet.graphics.Graphics;

// NOTE: On MacOS the following VM argument is required: -XstartOnFirstThread
public class GeneralStructure extends GarnetApp {

    public static void main(String[] args) {
        Garnet.launch(400, 400, GeneralStructure::new);
    }

    @Override
    public void init() {
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

