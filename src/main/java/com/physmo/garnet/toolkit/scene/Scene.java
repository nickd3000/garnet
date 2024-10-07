package com.physmo.garnet.toolkit.scene;

import com.physmo.garnet.toolkit.Context;

/**
 * A scene can be thought of as a game state.
 * Each scene contains a Context that can contain GameObjects and other objects.
 * All game objects in the context get ticked and drawn.
 */
public abstract class Scene {

    private final String name;
    protected Context context = new Context();
    private boolean initCalled = false;

    public Scene(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void _init() {
        init();
        context.init();
        initCalled = true;
    }

    public abstract void init();

    public void _tick(double delta) {
        context.tick(delta);
        tick(delta);
    }

    public abstract void tick(double delta);

    public void _draw() {

        draw();
        context.draw();
    }

    public abstract void draw();

    public abstract void onMakeActive();

    public abstract void onMakeInactive();

    public boolean isInitCalled() {
        return initCalled;
    }
}
