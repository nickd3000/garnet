package com.physmo.garnet.entity;

public abstract class RenderComponent {

    protected Entity parent;

    public void injectParent(Entity parent) {
        this.parent = parent;
    }

    abstract public void draw();
}
