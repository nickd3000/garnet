package com.physmo.garnet;

public abstract class GarnetApp {

    public Garnet garnet;
    String name;

    public GarnetApp(Garnet garnet, String name) {
        this.garnet = garnet;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public abstract void init(Garnet garnet);

    public abstract void tick(double delta);

    public abstract void draw();


}
