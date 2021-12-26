package com.physmo.garnet.entity;

import com.physmo.garnet.collision.CollisionPacket;

public abstract class Component {

    protected Entity parent;

    public void injectParent(Entity parent) {
        this.parent = parent;
    }

    abstract public void init();

    abstract public void tick(double delta);

    public void onCollisionStart(CollisionPacket collisionPacket) {
    };


}
