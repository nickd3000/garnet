package com.physmo.garnet.drawablebatch;

import com.physmo.garnet.renderer.BatchMesh;
import com.physmo.garnet.renderer.RenderCommand;
import com.physmo.garnet.renderer.ShapeGeometry;

public class EllipseStroke2D extends DrawableElement {

    private final float[] coords;

    public EllipseStroke2D(float x, float y, float radiusX, float radiusY, float thickness) {
        int segments = StrokeGeometry.calculateEllipseSegments(radiusX + thickness, radiusY + thickness);
        coords = StrokeGeometry.createEllipseRingStrip(x, y, radiusX, radiusY, thickness, segments);
    }

    @Override
    public RenderCommand appendToBatch(BatchMesh mesh) {
        if (coords.length == 0) return new RenderCommand(mesh.vertexCount(), 0, mesh.indexCount(), 0);
        return ShapeGeometry.appendTriangleStripAsTriangles(mesh, viewport, coords, color);
    }

    @Override
    public int getMaterialFlags() {
        return 0;
    }

    @Override
    int getTextureId() {
        return 0;
    }

    @Override
    public int getType() {
        return OTHER;
    }
}
