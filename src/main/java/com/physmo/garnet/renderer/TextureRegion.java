package com.physmo.garnet.renderer;

/**
 * Describes a drawable texture rectangle, either atlas-backed or raw.
 */
public record TextureRegion(
        int textureId,
        int x,
        int y,
        int width,
        int height,
        int textureWidth,
        int textureHeight,
        boolean atlasBacked
) {
    public TextureRegion {
        if (width <= 0 || height <= 0) throw new IllegalArgumentException("Region dimensions must be positive");
        if (textureWidth <= 0 || textureHeight <= 0)
            throw new IllegalArgumentException("Texture dimensions must be positive");
    }

    public static TextureRegion raw(int textureId, int width, int height) {
        return new TextureRegion(textureId, 0, 0, width, height, width, height, false);
    }

    public float u0() {
        return x / (float) textureWidth;
    }

    public float v0() {
        return y / (float) textureHeight;
    }

    public float u1() {
        return (x + width) / (float) textureWidth;
    }

    public float v1() {
        return (y + height) / (float) textureHeight;
    }

    public float uWidth() {
        return width / (float) textureWidth;
    }

    public float vHeight() {
        return height / (float) textureHeight;
    }

    public TextureRegion subRegion(int subX, int subY, int subWidth, int subHeight) {
        if (subX < 0 || subY < 0 || subWidth <= 0 || subHeight <= 0) {
            throw new IllegalArgumentException("Sub-region dimensions must be positive and inside the parent region");
        }
        if (subX + subWidth > width || subY + subHeight > height) {
            throw new IllegalArgumentException("Sub-region exceeds parent region");
        }
        return new TextureRegion(textureId, x + subX, y + subY, subWidth, subHeight, textureWidth, textureHeight, atlasBacked);
    }
}
