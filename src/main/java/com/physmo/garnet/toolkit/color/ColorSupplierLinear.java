package com.physmo.garnet.toolkit.color;


import com.physmo.garnet.ColorUtils;

import static com.physmo.garnet.Utils.lerp;

/**
 * A color supplier used to blend multiple colors.
 */
public class ColorSupplierLinear implements ColorSupplier {
    float[][] cols;
    int numColors;

    public ColorSupplierLinear(int[] colors) {
        numColors = colors.length;
        cols = new float[numColors][4];
        for (int i = 0; i < numColors; i++) {
            cols[i] = ColorUtils.rgbaToFloats(colors[i]);
        }
    }


    @Override
    public int getColor(double t) {
        double span = (double) 1 / (numColors - 1);

        t = t % 1.0;
        int i1 = (int) (t / span);
        if (i1 < 0) i1 = 0;
        int i2 = i1 + 1;
        if (i2 >= numColors) i2 -= numColors;

        double u = (t % span) / span;

        return ColorUtils.asRGBA(
                lerp(cols[i1][0], cols[i2][0], (float) u),
                lerp(cols[i1][1], cols[i2][1], (float) u),
                lerp(cols[i1][2], cols[i2][2], (float) u),
                lerp(cols[i1][3], cols[i2][3], (float) u));
    }

}
