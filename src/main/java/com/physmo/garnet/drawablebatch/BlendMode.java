package com.physmo.garnet.drawablebatch;

/**
 * Blend modes that can be applied to individual DrawableElements.
 */
public enum BlendMode {
    /**
     * Standard alpha transparency (default).
     */
    NORMAL,
    /**
     * Additive — colours are added; bright areas accumulate (fire, glow, particles).
     */
    ADDITIVE,
    /**
     * Subtractive — colours are subtracted; darkens the destination (shadows, ink).
     */
    SUBTRACTIVE,
    /**
     * Multiply — destination is multiplied by source; useful for tinting/darkening.
     */
    MULTIPLY,
    /**
     * Matte/mask — writes only to the alpha channel, not colour (stencil-like effects).
     */
    MATTE,
}
