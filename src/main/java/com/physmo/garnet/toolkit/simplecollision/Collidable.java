package com.physmo.garnet.toolkit.simplecollision;

import com.physmo.garnet.structure.Rect;
import com.physmo.garnet.toolkit.GameObject;

public interface Collidable {
    Rect collisionGetRegion();

    void collisionCallback(CollisionPacket collisionPacket);

    void proximityCallback(RelativeObject relativeObject);

    GameObject collisionGetGameObject();
}
