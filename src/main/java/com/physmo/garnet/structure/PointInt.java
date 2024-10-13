package com.physmo.garnet.structure;

/**
 * Represents a point in 3D space with integer coordinates.
 */
public class PointInt {

    public int x;
    public int y;
    public int z;

    public PointInt() {
        x = 0;
        y = 0;
        z = 0;
    }

    public PointInt(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public PointInt(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public PointInt(PointInt p) {
        this.x = p.x;
        this.y = p.y;
        this.z = p.z;
    }

    public String toString() {
        return "[" + x + "," + y + "," + z + "]";
    }
}
