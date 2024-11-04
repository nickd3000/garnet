package com.physmo.garnet;


public class Utils {

    /**
     * Clamps the given value to the range [0, 1].
     *
     * @param v the value to be clamped
     * @return the clamped value within the range 0 to 1
     */
    public static float clampUnit(float v) {
        if (v < 0) return 0;
        if (v > 1) return 1;
        return v;
    }

    /**
     * Clamps the given value to the range [0, 1].
     *
     * @param v the value to be clamped
     * @return the clamped value within the range 0 to 1
     */
    public static double clampUnit(double v) {
        if (v < 0) return 0;
        if (v > 1) return 1;
        return v;
    }


    /**
     * Linearly interpolates between two float values.
     *
     * @param v1  the start value
     * @param v2  the end value
     * @param pos the interpolation position between 0.0 and 1.0
     * @return the interpolated value
     */
    public static float lerp(float v1, float v2, float pos) {
        float span = v2 - v1;
        return (v1 + span * pos);
    }

    /**
     * Linearly interpolates between two double values.
     *
     * @param v1  the start value
     * @param v2  the end value
     * @param pos the interpolation position between 0.0 and 1.0
     * @return the interpolated value
     */
    public static double lerp(double v1, double v2, double pos) {
        double span = v2 - v1;
        return (v1 + span * pos);
    }

    /**
     * Remaps a given value from one range to another range.
     *
     * @param value  the value to be remapped from the input range to the output range
     * @param inMin  the minimum value of the input range
     * @param inMax  the maximum value of the input range
     * @param outMin the minimum value of the output range
     * @param outMax the maximum value of the output range
     * @return the remapped value in the output range
     */
    public static double remapRange(double value, double inMin, double inMax, double outMin, double outMax) {
        if (outMax - outMin == 0) return 0;
        value = (value - inMin) / ((inMax - inMin) / (outMax - outMin));
        return value + outMin;
    }

    /**
     * Remaps a given value from one range to another range.
     *
     * @param value the value to be remapped from the input range to the output range
     * @param inMin the minimum value of the input range
     * @param inMax the maximum value of the input range
     * @param outMin the minimum value of the output range
     * @param outMax the maximum value of the output range
     * @return the remapped value in the output range
     */
    public static float remapRange(float value, float inMin, float inMax, float outMin, float outMax) {
        if (outMax - outMin == 0) return 0;
        value = (value - inMin) / ((inMax - inMin) / (outMax - outMin));
        return value + outMin;
    }


    /**
     * Convert a numeric value to a brief string, retaining only 2 digits after the decimal place.
     *
     * @return the formatted string with 2 decimal places
     */
    public static String numberToString(double val) {
        return String.format("%.2f", val);
    }
}
