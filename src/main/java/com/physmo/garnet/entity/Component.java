package com.physmo.garnet.entity;

public abstract class Component {

    protected Entity parent;

    public void injectParent(Entity parent) {
        this.parent = parent;
    }

    abstract public void tick(double delta);
}
