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

    public static int floatToRgb(float[] f) {
        int rgb = 0;

        rgb += ((int) (clampFloat(f[0]) * 255f)) << 24;
        rgb += ((int) (clampFloat(f[1]) * 255f)) << 16;
        rgb += ((int) (clampFloat(f[2]) * 255f)) << 8;
        rgb += ((int) (clampFloat(f[3]) * 255f));

        return rgb;
    }

    public static float clampFloat(float f) {
        if (f < 0) return 0;
        if (f > 1) return 1;
        return f;
    }

    public static int floatToRgb(float r, float g, float b, float a) {
        int rgb = 0;

        rgb += ((int) (clampFloat(r) * 255f)) << 24;
        rgb += ((int) (clampFloat(g) * 255f)) << 16;
        rgb += ((int) (clampFloat(b) * 255f)) << 8;
        rgb += ((int) (clampFloat(a) * 255f));

        return rgb;
    }

    /**
     * Create int RGB value from int components
     *
     * @param r
     * @param g
     * @param b
     * @param a
     * @return
     */
    public static int rgb(int r, int g, int b, int a) {
        int rgb = 0;

        rgb += (r & 0xff) << 24;
        rgb += (g & 0xff) << 16;
        rgb += (b & 0xff) << 8;
        rgb += (a & 0xff);

        return rgb;
    }

    public static float[] rgbToFloat(int rgb) {
        float[] f = new float[4];
        f[0] = ((rgb >> 24) & 0xff) / 255f;
        f[1] = ((rgb >> 16) & 0xff) / 255f;
        f[2] = ((rgb >> 8) & 0xff) / 255f;
        f[3] = ((rgb) & 0xff) / 255f;
        return f;
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
}
