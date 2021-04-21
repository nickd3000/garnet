package com.physmo.garnet;

public class GPoint {
    public double x, y;

    public GPoint() {
        x = 0;
        y = 0;
    }

    public GPoint(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public GPoint(GPoint other) {
        this.x = other.x;
        this.y = other.y;
    }

    public GPoint(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public double distance(GPoint other) {
        // todo
        return 0;
    }

    public void set(double x, double y) {
        this.x = x;
        this.y = y;
    }
}
