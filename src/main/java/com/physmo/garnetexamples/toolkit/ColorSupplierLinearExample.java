package com.physmo.garnetexamples.toolkit;

import com.physmo.garnet.ColorUtils;
import com.physmo.garnet.Garnet;
import com.physmo.garnet.GarnetApp;
import com.physmo.garnet.graphics.Graphics;
import com.physmo.garnet.toolkit.color.ColorSupplierLinear;

public class ColorSupplierLinearExample extends GarnetApp {

    ColorSupplierLinear colorSupplierLinear_a;
    ColorSupplierLinear colorSupplierLinear_b;

    double timer = 0;

    public ColorSupplierLinearExample(Garnet garnet, String name) {
        super(garnet, name);
    }

    public static void main(String[] args) {
        Garnet garnet = new Garnet(640, 480);
        GarnetApp app = new ColorSupplierLinearExample(garnet, "");

        garnet.setApp(app);

        garnet.init();
        garnet.run();
    }

    @Override
    public void init(Garnet garnet) {
        garnet.getDisplay().setWindowTitle("ColorSupplierLinearExample example");

        // create an array of color RGB values
        int[] colorArray_a = new int[]{ColorUtils.SUNSET_GREEN, ColorUtils.SUNSET_ORANGE, ColorUtils.BLUE, ColorUtils.SUNSET_GREEN};
        // Create a ColorSupplierLinear object  from the color list.
        colorSupplierLinear_a = new ColorSupplierLinear(colorArray_a);

        int[] colorArray_b = new int[]{ColorUtils.WINTER_BLACK, ColorUtils.SUNSET_RED};
        colorSupplierLinear_b = new ColorSupplierLinear(colorArray_b);
    }

    @Override
    public void tick(double delta) {
        timer += delta;
    }

    @Override
    public void draw(Graphics g) {

        int numRects = 80;
        double span = (double) garnet.getDisplay().getWindowWidth() / numRects;
        int col;

        // Draw a gradient made up of rectangles.
        for (int i = 0; i < numRects; i++) {
            col = colorSupplierLinear_a.getColor(i / (double) numRects);
            g.setColor(col);
            g.filledRect((float) (span * i), (float) 0, (float) span, 100);

            col = colorSupplierLinear_b.getColor(i / (double) numRects);
            g.setColor(col);
            g.filledRect((float) (span * i), (float) 200, (float) span, 100);
        }

        // Draw flashing squares.
        col = colorSupplierLinear_a.getColor(timer);
        g.setColor(col);
        g.filledRect(50, 50 + 75, 50, 50);

        col = colorSupplierLinear_b.getColor(timer);
        g.setColor(col);
        g.filledRect(50 + 75, 50 + 75, 50, 50);

        col = colorSupplierLinear_b.getColor(timer / 2);
        g.setColor(col);
        g.filledRect(50 + 150, 50 + 75, 50, 50);

    }


}
