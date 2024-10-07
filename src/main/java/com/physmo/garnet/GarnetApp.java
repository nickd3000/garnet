package com.physmo.garnet;

import com.physmo.garnet.graphics.Graphics;


/**
 * GarnetApp serves as an abstract base class for applications built on the Garnet framework.
 * It includes properties such as the {@link Garnet} instance and the application's name.
 * Subclasses must implement the {@link #init(Garnet)}, {@link #tick(double)}, and {@link #draw(Graphics)} methods.
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
     * @param delta number of seconds passed since last call to tick.
     */
    public abstract void tick(double delta);

    public abstract void draw(Graphics g);


}
