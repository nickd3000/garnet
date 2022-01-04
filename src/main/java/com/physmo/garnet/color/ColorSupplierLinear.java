package com.physmo.garnet.color;

public class ColorSupplierLinear implements ColorSupplier {
    Color c1;
    Color c2;

    public ColorSupplierLinear(Color c1, Color c2) {
        this.c1 = c1;
        this.c2 = c2;
    }

    @Override
    public Color getColor(double t) {
        return new Color(lerp(c1.r, c2.r, (float) t),
                lerp(c1.g, c2.g, (float) t),
                lerp(c1.b, c2.b, (float) t),
                lerp(c1.a, c2.a, (float) t));
    }

    public static float lerp(float v1, float v2, float pos) {
        float span = v2 - v1;
        return (v1 + span * pos);
    }
}
