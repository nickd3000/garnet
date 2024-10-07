package com.physmo.garnet.toolkit.particle;

public class RangedValue {
    double min;
    double max;

    public RangedValue(double min, double max) {
        this.min = min;
        this.max = max;
    }

    public double getValue() {
        double span = max - min;
        return min + (Math.random() * span);
    }
}
