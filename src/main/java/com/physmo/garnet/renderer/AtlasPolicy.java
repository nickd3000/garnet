package com.physmo.garnet.renderer;

/**
 * Immutable settings for append-only atlas page allocation.
 */
public record AtlasPolicy(
        int pageWidth,
        int pageHeight,
        int padding,
        int filterMode
) {
    public AtlasPolicy {
        if (pageWidth <= 0 || pageHeight <= 0)
            throw new IllegalArgumentException("Atlas page dimensions must be positive");
        if (padding < 0) throw new IllegalArgumentException("Atlas padding must not be negative");
    }
}
