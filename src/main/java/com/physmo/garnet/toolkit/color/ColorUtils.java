package com.physmo.garnet.toolkit.color;


public class ColorUtils {

    public static int BLACK, WHITE, RED, ORANGE, GREEN, BLUE, YELLOW;
    public static int INDIGO, VIOLET;
    public static int LIGHT_GREY, GREY, DARK_GREY;
    public static int SUNSET_BLUE, SUNSET_GREEN, SUNSET_YELLOW, SUNSET_ORANGE, SUNSET_RED;

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
