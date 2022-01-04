package com.physmo.garnet.color;

public class ColorSupplierSolid implements ColorSupplier {
    Color color;

    public ColorSupplierSolid(Color color) {
        this.color = color;
    }

    @Override
    public Color getColor(double t) {
        return color;
    }
}
