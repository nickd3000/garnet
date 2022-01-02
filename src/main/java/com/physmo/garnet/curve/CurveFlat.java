package com.physmo.garnet.curve;

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
