package com.physmo.garnet.toolkit;

import com.physmo.garnet.graphics.Texture;

import java.awt.Color;
import java.nio.ByteBuffer;

/**
 * The `InlineTexture` class provides methods to create textures from string data.
 * It uses specified tokens and a color palette to map characters in the string to colors.
 */
public class InlineTexture {


    static String defaultTokens = " .oX";
    static Color[] defaultPalette = getDefaultColors();

    /**
     * Creates a {@link Texture} from the provided string data, width, and height using
     * default tokens and color palette.
     *
     * @param data   The string data representing the texture.
     * @param width  The width of the texture.
     * @param height The height of the texture.
     * @return A new {@link Texture} instance created from the provided data.
     * @throws RuntimeException if the data length is smaller than width * height.
     */
    public static Texture create(String data, int width, int height) {
        return create(data, width, height, defaultTokens, defaultPalette);
    }

    /**
     * Creates a {@link Texture} from provided string data, width, height, tokens, and color palette.
     *
     * @param data    The string data representing the texture.
     * @param width   The width of the texture.
     * @param height  The height of the texture.
     * @param tokens  The string containing token characters for color mapping.
     * @param palette The array of colors used to map characters to colors.
     * @return A new {@link Texture} instance created from the provided data.
     * @throws RuntimeException if the data length is smaller than width * height.
     */
    public static Texture create(String data, int width, int height, String tokens, Color[] palette) {

        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(width * height * 4);

        if (data.length() < width * height) {
            throw new RuntimeException("InlineTexture data too small. Data size:" + data.length() + " expected:" + width * height);
        }

        for (int i = 0; i < data.length(); i++) {
            putColorInByteBuffer(data.charAt(i), tokens, palette, byteBuffer);
        }

        byteBuffer.position(0);

        return Texture.createTexture(width, height, byteBuffer);
    }

    /**
     * Maps a character token to a color from a specified palette and places the color's RGBA values
     * into a ByteBuffer.
     *
     * @param token        a character to be mapped to a color in the color palette.
     * @param tokenString  a string containing all possible token characters for color mapping.
     * @param colorPalette an array of Color objects used to map tokens to colors.
     * @param buffer       the ByteBuffer where the color's RGBA values will be placed.
     */
    public static void putColorInByteBuffer(char token, String tokenString, Color[] colorPalette, ByteBuffer buffer) {
        int tokenIndex = tokenString.indexOf(token);
        if (tokenIndex == -1) tokenIndex = 0;
        tokenIndex = tokenIndex % colorPalette.length;
        Color color = colorPalette[tokenIndex];
        buffer.put((byte) color.getRed());
        buffer.put((byte) color.getGreen());
        buffer.put((byte) color.getBlue());
        buffer.put((byte) color.getAlpha());
    }

    /**
     * Provides the default array of colors used in the palette.
     *
     * @return An array of Color objects representing the default colors.
     */
    public static Color[] getDefaultColors() {
        Color[] palette = new Color[4];
        palette[0] = new Color(23, 37, 56);
        palette[1] = new Color(55, 60, 80);
        palette[2] = new Color(99, 106, 128);
        palette[3] = new Color(190, 198, 204);
        return palette;
    }
}
