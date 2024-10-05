package com.physmo.garnet.toolkit;

/**
 * The Rect class represents a rectangle defined by its
 * top-left corner (x, y) and its width (w) and height (h).
 */
public class Rect {

    public double x;
    public double y;
    public double w;
    public double h;

    /**
     * Constructs a rectangle with default values for its top-left corner coordinates (x, y)
     * and its dimensions (width, height). The default values are all set to 0.
     */
    public Rect() {
        x = 0;
        y = 0;
        w = 0;
        h = 0;
    }

    /**
     * Constructs a rectangle defined by its top-left corner (x, y) and
     * its width (w) and height (h).
     *
     * @param x The x-coordinate of the top-left corner of the rectangle.
     * @param y The y-coordinate of the top-left corner of the rectangle.
     * @param w The width of the rectangle.
     * @param h The height of the rectangle.
     */
    public Rect(double x, double y, double w, double h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    /**
     * Sets the position and dimensions of the rectangle.
     *
     * @param x The x-coordinate of the top-left corner of the rectangle.
     * @param y The y-coordinate of the top-left corner of the rectangle.
     * @param w The width of the rectangle.
     * @param h The height of the rectangle.
     */
    public void set(double x, double y, double w, double h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    /**
     * Determines if this rectangle intersects with another rectangle.
     *
     * @param other The rectangle to check for intersection with.
     * @return true if this rectangle intersects with the other rectangle, false otherwise.
     */
    public boolean intersect(Rect other) {
        if (x + w < other.x) return false;
        if (y + h < other.y) return false;
        if (x > other.x + other.w) return false;
        return !(y > other.y + other.h);
    }

    // Return array containing overlap amounts for each side.
    // 0 == top, 1 = right etc

    /**
     * Calculates the overlap between this rectangle and another rectangle
     * and stores the results in the provided array.
     *
     * @param other   The other rectangle to check against.
     * @param overlap An array where the overlap values will be stored.
     *                The array should have a length of 4, with the following
     *                indexing: 0 - top, 1 - right, 2 - bottom, 3 - left.
     */
    public void overlap(Rect other, double[] overlap) {

        double hh = this.h / 2 + other.h / 2;
        double ww = this.w / 2 + other.w / 2;

        double dy = (other.y + (other.h / 2)) - (this.y + (this.h / 2));
        double dx = (other.x + (other.w / 2)) - (this.x + (this.w / 2));

        if (Math.abs(dx) >= ww) return;
        if (Math.abs(dy) >= hh) return;

        double up = 0, down = 0, left = 0, right = 0;

        if (this.y + (this.h / 2) > other.y + (other.h / 2)) {
            up = hh + dy;
        } else {
            down = hh - dy;
        }

        if (this.x + (this.w / 2) < other.x + (other.w / 2)) {
            right = ww - dx;
        } else {
            left = ww + dx;
        }

        overlap[0] = up;
        overlap[1] = right;
        overlap[2] = down;
        overlap[3] = left;

    }
}
