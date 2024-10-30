package com.physmo.garnet.toolkit.simplecollision;

import com.physmo.garnet.graphics.Graphics;
import com.physmo.garnet.structure.Rect;
import com.physmo.garnet.toolkit.Component;
import com.physmo.garnet.toolkit.GameObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A helper component that encapsulates a collidable and offers a place to add callback methods
 * when certain collision events occur.
 * The object keeps track of collisions and can report when a collision starts and ends.
 */
public class ColliderComponent extends Component implements Collidable {

    int ox, oy, width, height;
    Rect collisionRegion = new Rect();
    CollisionCallback callbackEnter = null;
    CollisionCallback callbackLeave = null;
    CollisionCallback callbackContinue = null;
    ProximityCallback callbackProximity = null;

    List<GameObject> newCollisions = new ArrayList<>();
    List<GameObject> existingCollisions = new ArrayList<>();

    public ColliderComponent() {
        setCallbackEnter(a -> {
        });
        setCallbackLeave(a -> {
        });
        setCallbackContinue(a -> {
        });
    }

    /**
     * Add a callback that is triggered when a collision with another object starts.
     *
     * @param callback
     */
    public void setCallbackEnter(CollisionCallback callback) {
        this.callbackEnter = callback;

    }

    /**
     * Add a callback that is triggered when a collision with another object ends.
     *
     * @param callback
     */
    public void setCallbackLeave(CollisionCallback callback) {
        this.callbackLeave = callback;

    }

    /**
     * Add a callback that is triggered every tick that the collision continues.
     *
     * @param callback
     */
    public void setCallbackContinue(CollisionCallback callback) {
        this.callbackContinue = callback;
    }

    /**
     * Add a callback that is triggered when in proximity to another collidable.
     *
     * @param callback
     */
    public void setCallbackProximity(ProximityCallback callback) {
        this.callbackProximity = callback;
    }

    @Override
    public void init() {
        // TODO: we need to be able to set these
        ox = -6;
        oy = -6;
        width = 12;
        height = 12;
    }

    public void setCollisionRegion(int offsetX, int offsetY, int width, int height) {
        this.ox = offsetX;
        this.oy = offsetY;
        this.width = width;
        this.height = height;
    }

    @Override
    public void tick(double t) {
        List<GameObject> keepList = new ArrayList<>();

        // First handle collisions leaving
        for (GameObject other : existingCollisions) {
            if (!newCollisions.contains(other)) {
                callbackLeave.go(other);
            } else {
                keepList.add(other);
            }
        }
        existingCollisions.clear();
        existingCollisions.addAll(keepList);
        keepList.clear();


        for (GameObject other : newCollisions) {
            if (existingCollisions.contains(other)) {
                // call existing handler
                callbackContinue.go(other);
            } else {
                // call new handler
                callbackEnter.go(other);
                keepList.add(other);
            }
        }
        newCollisions.clear();
        existingCollisions.addAll(keepList);
    }

    @Override
    public void draw(Graphics g) {

    }

    @Override
    public Rect collisionGetRegion() {
        collisionRegion.set(parent.getTransform().x + ox, parent.getTransform().y + oy, width, height);
        return collisionRegion;
    }

    // Notified when the collision system detects an intersection
    // this class should handle whether this is new, maintaining or leaving collision
    // and call user supplied handlers
    @Override
    public void collisionCallback(CollisionPacket collisionPacket) {
        // TODO: debug this - out of memory
        newCollisions.add(collisionPacket.targetEntity.collisionGetGameObject());
    }

    @Override
    public void proximityCallback(RelativeObject relativeObject) {
        if (callbackProximity == null) return;

        callbackProximity.go(relativeObject);
    }

    @Override
    public GameObject collisionGetGameObject() {
        return parent;
    }

    // TODO: list all resources
    public String report() {
        return "";
    }

}
