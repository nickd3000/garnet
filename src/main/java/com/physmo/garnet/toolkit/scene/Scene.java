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

    public void _init() {
        init();
        context.init();
        isInitialized = true;
    }

    public abstract void init();

    public void _tick(double delta) {
        if (!isInitialized) _init();
        context.tick(delta);
        tick(delta);
    }

    public abstract void tick(double delta);

    public void _draw(Graphics g) {

        draw(g);
        context.draw(g);
    }

    public abstract void draw(Graphics g);

    public abstract void onMakeActive();

    public abstract void onMakeInactive();

    public boolean isInitialized() {
        return isInitialized;
    }
}
