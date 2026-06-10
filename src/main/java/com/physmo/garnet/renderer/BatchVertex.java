package com.physmo.garnet.renderer;

/**
 * CPU-side vertex used by the future buffered renderer.
 * <p>
 * Coordinates are already transformed to screen space. Texture coordinates are
 * normalized UVs. Color channels are normalized floats in the range 0..1.
 */
public record BatchVertex(
        float x,
        float y,
        float u,
        float v,
        float r,
        float g,
        float b,
        float a,
        int materialFlags
) {
    public static final int FLAG_TEXTURED = 1;
    public static final int FLAG_COLOR_OVERRIDE = 1 << 1;
}
