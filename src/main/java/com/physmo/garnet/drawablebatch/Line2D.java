package com.physmo.garnet.drawablebatch;

import com.physmo.garnet.renderer.BatchMesh;
import com.physmo.garnet.renderer.RenderCommand;
import com.physmo.garnet.renderer.ShapeGeometry;

/**
 * The Line2D class represents a drawable 2D line that can be rendered on the screen.
 * It extends the DrawableElement class and provides implementation for rendering
 * a line between two points using OpenGL functions.
 */
public class Line2D extends DrawableElement {

    float[] coords = new float[4];

    public Line2D() {
    }

    public Line2D(float x1, float y1, float x2, float y2) {
        coords[0] = x1;
        coords[1] = y1;
        coords[2] = x2;
        coords[3] = y2;
    }

    public void reset() {
        resetCommonState();
    }

    public void set(float x1, float y1, float x2, float y2) {
        coords[0] = x1;
        coords[1] = y1;
        coords[2] = x2;
        coords[3] = y2;
    }

    @Override
    public RenderCommand appendToBatch(BatchMesh mesh) {
        float[] lineQuad = StrokeGeometry.createLineQuad(coords[0], coords[1], coords[2], coords[3], 1.0f);
        if (lineQuad.length == 0) return new RenderCommand(mesh.vertexCount(), 0, mesh.indexCount(), 0);
        return ShapeGeometry.appendFilledConvexPolygon(mesh, viewport, lineQuad, color);
    }

    @Override
    public int getMaterialFlags() {
        return 0;
    }

    @Override
    public int getTextureId() {
        return 0;
    }

    @Override
    public int getType() {
        return LINE;
    }
}
