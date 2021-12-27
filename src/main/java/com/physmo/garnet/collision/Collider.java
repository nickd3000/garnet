package com.physmo.garnet.collision;

public interface Collider {
    boolean testCollision(Collider other);
    void render();
}
