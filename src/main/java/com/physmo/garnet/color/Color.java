package com.physmo.garnet.color;

public class Color {
    public static Color BLACK, WHITE, RED, GREEN, BLUE, YELLOW;

    static {
        BLACK = new Color(0, 0, 0, 1);
        WHITE = new Color(1, 1, 1, 1);
        RED = new Color(1, 0, 0, 1);
        GREEN = new Color(0, 1, 0, 1);
        BLUE = new Color(0, 0, 1, 1);
        YELLOW = new Color(1, 1, 0, 1);
    }

    public float r, g, b, a;

    public Color() {
        setValues(1, 1, 1, 1);
    }

    public void setValues(float r, float g, float b, float a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }

    public Color(float r, float g, float b) {
        setValues(r, g, b, 1);
    }

    public Color(float r, float g, float b, float a) {
        setValues(r, g, b, a);
    }
}
