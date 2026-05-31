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

    /**
     * Called once when the application starts, after the display and subsystems are ready.
     * Use this to load textures, set up shaders, and initialise game state.
     *
     * @param garnet the {@link Garnet} instance providing access to all subsystems
     */
    public abstract void init(Garnet garnet);

    /**
     * tick is called periodically and usually more frequently
     * that the draw method.
     *
     * @param delta number of seconds passed since last call to tick.
     */
    public abstract void tick(double delta);

    /**
     * Called once per rendered frame to draw the current game state.
     *
     * @param g the {@link Graphics} context used for all draw calls
     */
    public abstract void draw(Graphics g);


}
