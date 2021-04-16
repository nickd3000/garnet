package com.physmo.garnet;

public interface GameContainer {
    void init(Garnet garnet);

    // TODO: we should be passing tick times here
    void tick(double delta);

    void draw();
}
