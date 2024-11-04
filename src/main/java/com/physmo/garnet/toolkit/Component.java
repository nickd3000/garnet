package com.physmo.garnet.toolkit;

import com.physmo.garnet.graphics.Graphics;

/**
 * Abstract class representing logic and behaviour that can be attached to a game object.
 */
public abstract class Component {
    protected GameObject parent;

    abstract public void init();

    abstract public void tick(double t);

    abstract public void draw(Graphics g);

    public void setParent(GameObject parent) {
        this.parent = parent;
    }
}
