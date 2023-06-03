package com.physmo.garnet;

import com.physmo.garnet.graphics.Graphics;

/**
 * Base component to derive garnet applications from
 */
public abstract class GarnetApp {

    public Garnet garnet;
    String name;

    public GarnetApp(Garnet garnet, String name) {
        this.garnet = garnet;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public abstract void init(Garnet garnet);

    /**
     * tick is called periodically and usually more frequently
     * that the draw method.
     *
     * @param delta
     */
    public abstract void tick(double delta);

    public abstract void draw(Graphics g);


}
