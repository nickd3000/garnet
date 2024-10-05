package com.physmo.garnet.toolkit.color;


import static com.physmo.garnet.toolkit.Utils.lerp;

/**
 * A color supplier used to blend two colors.
 */
public class ColorSupplierLinear implements ColorSupplier {
    float[] c1;
    float[] c2;

    public ColorSupplierLinear(int c1, int c2) {
        this.c1 = ColorUtils.rgbaToFloats(c1);
        this.c2 = ColorUtils.rgbaToFloats(c2);
    }

    @Override
    public int getColor(double t) {
        return ColorUtils.asRGBA(
                lerp(c1[0], c2[0], (float) t),
                lerp(c1[1], c2[1], (float) t),
                lerp(c1[2], c2[2], (float) t),
                lerp(c1[3], c2[3], (float) t));
    }

}
