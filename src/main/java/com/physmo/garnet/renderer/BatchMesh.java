package com.physmo.garnet.renderer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Collects vertices and indices before upload to a dynamic GL mesh buffer.
 */
public class BatchMesh {
    private final List<BatchVertex> vertices = new ArrayList<>();
    private final List<Integer> indices = new ArrayList<>();

    public int addVertex(BatchVertex vertex) {
        vertices.add(vertex);
        return vertices.size() - 1;
    }

    public void addQuadIndices(int topLeft, int topRight, int bottomRight, int bottomLeft) {
        addTriangle(topLeft, topRight, bottomRight);
        addTriangle(topLeft, bottomRight, bottomLeft);
    }

    public void addTriangle(int a, int b, int c) {
        indices.add(a);
        indices.add(b);
        indices.add(c);
    }

    public int vertexCount() {
        return vertices.size();
    }

    public int indexCount() {
        return indices.size();
    }

    public List<BatchVertex> vertices() {
        return Collections.unmodifiableList(vertices);
    }

    public List<Integer> indices() {
        return Collections.unmodifiableList(indices);
    }

    public void clear() {
        vertices.clear();
        indices.clear();
    }
}
