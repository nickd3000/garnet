package com.physmo.garnet.toolkit.simplecollision;

import com.physmo.garnet.structure.Rect;
import com.physmo.garnet.toolkit.GameObject;

public interface Collidable {
    Rect collisionGetRegion();

    void collisionCallback(CollisionPacket collisionPacket);

    void proximityCallback(RelativeObject relativeObject);

    GameObject collisionGetGameObject();

    int getCollisionGroup();

    /**
     * Sets the collision group identifier for the object implementing the Collidable interface.
     * The collision group is used to categorize objects for collision detection or filtering.
     *
     * @param collisionGroup an integer representing the collision group for this object
     */
    void setCollisionGroup(int collisionGroup);
}
