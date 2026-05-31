package com.physmo.garnet.toolkit.scene;

import com.physmo.garnet.graphics.Graphics;
import com.physmo.garnet.toolkit.Context;

/**
 * A scene can be thought of as a game state.
 * Each scene contains a Context that can contain GameObjects and other objects.
 * All game objects in the context get ticked and drawn by the SceneManager when the scene
 * is active.
 */
public abstract class Scene {

    private final String name;
    protected Context context = new Context();
    private boolean isInitialized = false;

    public Scene(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    /**
     * Internal tick: ticks the scene's context then calls {@link #tick(double)}.
     * Called by the framework; do not call directly.
     *
     * @param delta seconds elapsed since the last tick
     */
    public void _tick(double delta) {
        if (!isInitialized) _init();
        context.tick(delta);
        tick(delta);
    }

    public abstract void init();

    /**
     * Internal initialisation: calls {@link #init()} then initialises the scene's context.
     * Called by the framework; do not call directly.
     */
    public void _init() {
        init();
        context.init();
        isInitialized = true;
    }

    public abstract void tick(double delta);

    /**
     * Internal draw: calls {@link #draw(Graphics)} then draws the scene's context.
     * Called by the framework; do not call directly.
     *
     * @param g the graphics context
     */
    public void _draw(Graphics g) {

        draw(g);
        context.draw(g);
    }

    public abstract void draw(Graphics g);

    /**
     * Called by the framework when this scene becomes the active scene.
     */
    public abstract void onMakeActive();

    /** Called by the framework when this scene is deactivated (another scene takes over). */
    public abstract void onMakeInactive();

    /**
     * Returns whether this scene has been initialised.
     *
     * @return {@code true} if {@link #_init()} has been called
     */
    public boolean isInitialized() {
        return isInitialized;
    }
}
