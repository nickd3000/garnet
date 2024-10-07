package com.physmo.garnet.toolkit.simplecollision;

import com.physmo.garnet.toolkit.GameObject;

@FunctionalInterface
public interface CollisionCallback {
    void go(GameObject target);
}
