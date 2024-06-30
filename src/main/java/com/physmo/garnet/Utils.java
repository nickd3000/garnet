package com.physmo.garnet;

import java.io.File;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Utils {

    public static InputStream getFileFromResourceAsStream(String fileName) {

        // The class loader that loaded the class
        ClassLoader classLoader = Utils.class.getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(fileName);

        // the stream holding the file content
        if (inputStream == null) {
            throw new IllegalArgumentException("file not found! " + fileName);
        } else {
            return inputStream;
        }

    }

    public static String getPathForResource(Object me, String resourceName) {

        URL resource = me.getClass().getClassLoader().getResource(resourceName);

        if (resource == null) {
            throw new RuntimeException("File not found: [" + resourceName + "]");
        }

        File file = null;
        try {
            Path path = Paths.get(resource.toURI());
            file = path.toFile();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        if (file != null)
            return file.getAbsolutePath();
        else
            return null;
    }

    /**
     * Convert an array of 4 floats to an RGBA integer value.
     *
     * @param f Array containing 4 float components of the color: [R,G,B,A]
     * @return An RGBA encoded integer color.
     */
    public static int floatToRgb(float[] f) {
        int rgb = 0;

        rgb += ((int) (clampUnit(f[0]) * 255f)) << 24;
        rgb += ((int) (clampUnit(f[1]) * 255f)) << 16;
        rgb += ((int) (clampUnit(f[2]) * 255f)) << 8;
        rgb += ((int) (clampUnit(f[3]) * 255f));

        return rgb;
    }

    /**
     * Convert 4 float color components to an RGBA encoded integer color value.
     * The range of each component is 0.0 - 1.0
     * @param r Red component
     * @param g Green component
     * @param b Blue component
     * @param a Alpha component
     * @return An RGBA encoded integer color.
     */
    public static int floatToRgb(float r, float g, float b, float a) {
        int rgb = 0;

        rgb += ((int) (clampUnit(r) * 255f)) << 24;
        rgb += ((int) (clampUnit(g) * 255f)) << 16;
        rgb += ((int) (clampUnit(b) * 255f)) << 8;
        rgb += ((int) (clampUnit(a) * 255f));

        return rgb;
    }

    /**
     * Convert 4 integer color components to an RGBA encoded integer color value.
     * The range of each component is 0 - 255
     * @param r Red component
     * @param g Green component
     * @param b Blue component
     * @param a Alpha component
     * @return An RGBA encoded integer color.
     */
    public static int rgb(int r, int g, int b, int a) {
        int rgb = 0;

        rgb += (r & 0xff) << 24;
        rgb += (g & 0xff) << 16;
        rgb += (b & 0xff) << 8;
        rgb += (a & 0xff);

        return rgb;
    }

    public static float[] rgbToFloat(int rgba) {
        float[] f = new float[4];
        rgbToFloat(rgba, f);
        return f;
    }

    public static void rgbToFloat(int rgba, float[] outFloats) {
        outFloats[0] = ((rgba >> 24) & 0xff) / 255f;
        outFloats[1] = ((rgba >> 16) & 0xff) / 255f;
        outFloats[2] = ((rgba >> 8) & 0xff) / 255f;
        outFloats[3] = ((rgba) & 0xff) / 255f;
    }

    public static double lerp(double v1, double v2, double pos) {
        double span = v2 - v1;
        return (v1 + span * pos);
    }

//    public String getPathToResource(String resourceName) {
//        URL resource = Utils.class.getResource(resourceName);
//        String path;
//        try {
//            File file = Paths.get(resource.toURI()).toFile();
//            path = file.getAbsolutePath();
//
//        } catch (URISyntaxException e) {
//            throw new RuntimeException(e);
//        }
//        return path;
//    }

    public static float clampUnit(float v) {
        if (v < 0) return 0;
        if (v > 1) return 1;
        return v;
    }

    public static double clampUnit(double v) {
        if (v < 0) return 0;
        if (v > 1) return 1;
        return v;
    }

    public static double remapRange(double value, double inMin, double inMax, double outMin, double outMax) {
        if (outMax - outMin == 0) return 0;
        value = (value - inMin) / ((inMax - inMin) / (outMax - outMin));
        return value + outMin;
    }

    public static float remapRange(float value, float inMin, float inMax, float outMin, float outMax) {
        if (outMax - outMin == 0) return 0;
        value = (value - inMin) / ((inMax - inMin) / (outMax - outMin));
        return value + outMin;
    }
}
