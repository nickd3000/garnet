package com.physmo.garnet;

import com.physmo.garnet.graphics.Graphics;


/**
 * GarnetApp serves as an abstract base class for applications built on the Garnet framework.
 * It includes properties such as the {@link Garnet} instance and the application's name.
 * Subclasses must implement the {@link #tick(double)} and {@link #draw(Graphics)} methods.
 * New applications should override {@link #init()} for startup work.
 */
public abstract class GarnetApp {

    public Garnet garnet;
    private String name;

    public GarnetApp() {
        this("");
    }

    public GarnetApp(String name) {
        this.name = name;
    }

    /**
     * Creates an app with an already-attached Garnet instance.
     *
     * @deprecated Prefer {@link #GarnetApp()} or {@link #GarnetApp(String)}. Garnet now attaches itself before app init.
     */
    @Deprecated
    public GarnetApp(Garnet garnet, String name) {
        this.garnet = garnet;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Garnet getGarnet() {
        return garnet;
    }

    void attach(Garnet garnet) {
        this.garnet = garnet;
    }

    /**
     * Called once when the application starts, after the display and subsystems are ready.
     * Use this to load textures, set up shaders, and initialise game state.
     *
     * @param garnet the {@link Garnet} instance providing access to all subsystems
     * @deprecated Override {@link #init()} instead. The app can access the attached Garnet instance via {@link #garnet} or {@link #getGarnet()}.
     */
    @Deprecated
    public void init(Garnet garnet) {
        init();
    }

    /**
     * Called once when the application starts, after the display and subsystems are ready.
     * Use this to load textures, set up shaders, and initialise game state.
     */
    public void init() {
    }

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
