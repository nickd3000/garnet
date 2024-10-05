package com.physmo.garnet.toolkit;

/**
 * Abstract class representing logic and behaviour that can be attached to a game object.
 */
public abstract class Component {
    protected GameObject parent;

    abstract public void init();

    abstract public void tick(double t);

    abstract public void draw();

    public void setParent(GameObject parent) {
        this.parent = parent;
    }
}
