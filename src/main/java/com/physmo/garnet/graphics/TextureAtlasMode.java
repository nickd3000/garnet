package com.physmo.garnet.graphics;

/**
 * Controls whether a texture should be atlas-managed or kept as its own raw GL texture.
 */
public enum TextureAtlasMode {
    /**
     * Normal image assets are eligible for atlas placement when atlas upload is enabled.
     */
    DEFAULT,
    /**
     * Keep this texture as a standalone GL texture. Use for render targets, dynamic textures, and explicit opt-out.
     */
    RAW
}
