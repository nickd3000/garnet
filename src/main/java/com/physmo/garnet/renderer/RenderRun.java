package com.physmo.garnet.renderer;

/**
 * Contiguous compatible command range that can be emitted as one GL draw call.
 */
public record RenderRun(
        RenderStateKey stateKey,
        int firstIndex,
        int indexCount,
        int commandCount
) {
}
