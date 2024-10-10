package com.physmo.garnetexamples.toolkit.support;

import com.physmo.garnet.toolkit.GameObject;

public class DummyGameObject extends GameObject {
    public DummyGameObject(String name) {
        super(name);
    }

    @Override
    public void init() {
        super.init();
        System.out.println("init called");
    }

    @Override
    public void tick(double t) {
        super.tick(t);
        System.out.println("tick called");
    }

    @Override
    public void draw() {
        super.draw();
        System.out.println("draw called");
    }
}
