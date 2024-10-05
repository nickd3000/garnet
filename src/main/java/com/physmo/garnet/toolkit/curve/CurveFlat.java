package com.physmo.garnet.toolkit.curve;

public class CurveFlat implements Curve {
    double value;

    public CurveFlat(double val) {
        this.value = val;
    }

    @Override
    public double value(double x) {
        return value;
    }
}
