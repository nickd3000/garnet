package com.physmo.garnetexamples;

import com.physmo.garnet.Garnet;
import com.physmo.garnet.GarnetApp;
import com.physmo.garnet.graphics.Graphics;

public class HelloWorld extends GarnetApp {

    double time, offset;

    public HelloWorld(Garnet garnet, String name) {
        super(garnet, name);
    }

    public static void main(String[] args) {
        Garnet garnet = new Garnet(400, 400);
        GarnetApp app = new HelloWorld(garnet, "");

        garnet.setApp(app);

        garnet.init();
        garnet.run();
    }

    @Override
    public void init(Garnet garnet) {
        System.out.println("Hello, World");
    }

    @Override
    public void tick(double delta) {
        time += delta;
        offset = Math.sin(time * 2) * 20;
    }

    @Override
    public void draw(Graphics g) {
        g.drawCircle((float) (200 + offset), 200, 100, 100);
        g.drawCircle((float) (200 - offset), 200, 110, 150);
    }
}
