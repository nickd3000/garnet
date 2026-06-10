package com.physmo.garnet.renderer;

/**
 * Vertex/index range emitted for one captured drawable element.
 */
public record RenderCommand(
        int firstVertex,
        int vertexCount,
        int firstIndex,
        int indexCount
) {
}
