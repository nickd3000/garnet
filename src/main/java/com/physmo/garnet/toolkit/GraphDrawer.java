package com.physmo.garnet.toolkit;

import com.physmo.garnet.graphics.Graphics;

public class GraphDrawer {

    public static void drawGraph(Graphics g, double[] doubles, int x, int y, int width, int height, double maxValue, int numValues) {

        int storedCol = g.getColor();

        g.setColor(0x333333ff);
        if (maxValue < 15) {
            int span = (int) (height / maxValue);
            for (int i = 0; i < maxValue; i++) {
                int yy = y + height - (i * span);
                g.drawLine(x, yy, x + width, yy);
            }
        }

        g.setColor(storedCol);
        int px = 0;
        int py = 0;
        for (int i = 0; i < Math.min(doubles.length, numValues); i++) {
            double scaledValue = (doubles[i] / maxValue) * (double) height;
            int xx = x + i;
            int yy = y + (int) (height - scaledValue);
            if (i > 0) g.drawLine(px, py, xx, yy);
            px = xx;
            py = yy;
        }

        g.setColor(0x444444ff);
        g.drawRect(x, y, width, height);

    }

}
