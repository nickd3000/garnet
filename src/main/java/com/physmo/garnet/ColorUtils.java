package com.physmo.garnet;

public class ColorUtils {
    public static int BLACK;
    public static int WHITE;
    public static int RED;
    public static int ORANGE;
    public static int GREEN;
    public static int BLUE;
    public static int YELLOW;
    public static int INDIGO;
    public static int VIOLET;
    public static int LIGHT_GREY;
    public static int GREY;
    public static int DARK_GREY;
    public static int SUNSET_BLUE;
    public static int SUNSET_GREEN;
    public static int SUNSET_YELLOW;
    public static int SUNSET_ORANGE;
    public static int SUNSET_RED;
    public static int WINTER_BLACK;
    public static int WINTER_WHITE;
    public static int WINTER_RED;
    public static int WINTER_ORANGE;
    public static int WINTER_YELLOW;
    public static int WINTER_GREEN;
    public static int WINTER_BLUE;
    public static int WINTER_INDIGO;

    // https://htmlcolorcodes.com/color-picker/
    static {
        BLACK = 0x000000FF;
        WHITE = 0xFFFFFFFF;
        //
        RED = 0xF4370FFF;
        ORANGE = 0xEE7415FF;
        YELLOW = 0xECE924FF;
        GREEN = 0x2FE036FF;
        BLUE = 0x2F8CE0FF;
        INDIGO = 0xA02FE0FF;
        VIOLET = 0xE02F92FF;
        //
        LIGHT_GREY = 0xC3C3C3FF;
        GREY = 0x808080FF;
        DARK_GREY = 0x626262FF;
        //

        //
        SUNSET_BLUE = 0x264653ff;
        SUNSET_GREEN = 0x2A9D8Fff;
        SUNSET_YELLOW = 0xE9C46Aff;
        SUNSET_ORANGE = 0xF4A261ff;
        SUNSET_RED = 0xE76F51ff;

        //
        WINTER_BLACK = 0x0f0f34ff;
        WINTER_WHITE = 0xc0d6fbff;
        WINTER_RED = 0xff6450ff;
        WINTER_ORANGE = 0xffa643ff;
        WINTER_YELLOW = 0xfff76cff;
        WINTER_GREEN = 0x35b663ff;
        WINTER_BLUE = 0x3363b3ff;
        WINTER_INDIGO = 0x863b9dff;

    }

    /**
     * Convert an array of 4 floats to an RGBA integer value.
     *
     * @param f Array containing 4 float components of the color: [R,G,B,A]
     * @return An RGBA encoded integer color.
     */
    public static int floatToRgb(float[] f) {
        int rgb = 0;

        rgb += ((int) (Utils.clampUnit(f[0]) * 255f)) << 24;
        rgb += ((int) (Utils.clampUnit(f[1]) * 255f)) << 16;
        rgb += ((int) (Utils.clampUnit(f[2]) * 255f)) << 8;
        rgb += ((int) (Utils.clampUnit(f[3]) * 255f));

        return rgb;
    }

    /**
     * Convert 4 float color components to an RGBA encoded integer color value.
     * The range of each component is 0.0 - 1.0
     *
     * @param r Red component
     * @param g Green component
     * @param b Blue component
     * @param a Alpha component
     * @return An RGBA encoded integer color.
     */
    public static int floatToRgb(float r, float g, float b, float a) {
        int rgb = 0;

        rgb += ((int) (Utils.clampUnit(r) * 255f)) << 24;
        rgb += ((int) (Utils.clampUnit(g) * 255f)) << 16;
        rgb += ((int) (Utils.clampUnit(b) * 255f)) << 8;
        rgb += ((int) (Utils.clampUnit(a) * 255f));

        return rgb;
    }

    /**
     * Convert 4 integer color components to an RGBA encoded integer color value.
     * The range of each component is 0 - 255
     *
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

    public static float[] rgbaToFloats(int rgba) {
        float[] f = new float[4];
        f[0] = ((rgba >> 24) & 0xff) / 255f;
        f[1] = ((rgba >> 16) & 0xff) / 255f;
        f[2] = ((rgba >> 8) & 0xff) / 255f;
        f[3] = ((rgba) & 0xff) / 255f;
        return f;
    }

    public static int asRGBA(float r, float g, float b, float a) {
        int rgb = 0;

        rgb += ((int) (clampFloat(r) * 255f)) << 24;
        rgb += ((int) (clampFloat(g) * 255f)) << 16;
        rgb += ((int) (clampFloat(b) * 255f)) << 8;
        rgb += ((int) (clampFloat(a) * 255f));

        return rgb;
    }

    private static float clampFloat(float f) {
        if (f < 0) return 0;
        if (f > 1) return 1;
        return f;
    }
}
