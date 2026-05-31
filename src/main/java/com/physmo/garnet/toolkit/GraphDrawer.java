package com.physmo.garnet.toolkit;

import com.physmo.garnet.graphics.Graphics;

/**
 * Utility class for drawing a simple line graph overlay.
 * Useful for visualising time-series data such as frame times or performance counters.
 */
public class GraphDrawer {

    /**
     * Draws a line graph of the supplied data array within the given screen rectangle.
     * Horizontal grid lines are drawn at integer intervals when {@code maxValue} is below 15.
     *
     * @param g         the graphics context
     * @param doubles   the data values to plot
     * @param x         the x-coordinate of the top-left corner of the graph area
     * @param y         the y-coordinate of the top-left corner of the graph area
     * @param width     the width of the graph area in pixels
     * @param height    the height of the graph area in pixels
     * @param maxValue  the value that maps to the top of the graph (full height)
     * @param numValues the number of values from {@code doubles} to plot
     */
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
