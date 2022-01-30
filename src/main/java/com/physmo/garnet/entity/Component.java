package com.physmo.garnet.entity;

import com.physmo.garnet.GameState;
import com.physmo.garnet.collision.CollisionPacket;

public abstract class Component {

    protected Entity parent;
    protected GameState parentState;

    public void injectParent(Entity parent) {
        this.parent = parent;
    }

    public void injectParentState(GameState parentState) {
        this.parentState = parentState;
    }

    abstract public void init();

    abstract public void tick(double delta);

    public void draw() {
    }

    ;

    public void onCollisionStart(CollisionPacket collisionPacket) {
    }

    // Convenience function.
    public <T extends Component> T getComponent(Class<T> type) {
        if (parentState != null) {
            return parentState.getComponent(type);
        }
        return null;
    }

}
