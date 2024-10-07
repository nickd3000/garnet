package com.physmo.garnet.toolkit.simplecollision;

import com.physmo.garnet.toolkit.GameObject;
import com.physmo.garnet.toolkit.Rect;

public interface Collidable {
    Rect collisionGetRegion();

    void collisionCallback(CollisionPacket collisionPacket);

    void proximityCallback(RelativeObject relativeObject);

    GameObject collisionGetGameObject();
}
