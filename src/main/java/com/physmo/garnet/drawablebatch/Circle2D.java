package com.physmo.garnet.drawablebatch;

import com.physmo.garnet.renderer.BatchMesh;
import com.physmo.garnet.renderer.RenderCommand;
import com.physmo.garnet.renderer.ShapeGeometry;

public class Circle2D extends DrawableElement {

    float x;
    float y;
    float width;
    float height;
    double detail = 1.5;
    int numSegments;
    boolean filled = false;
    private final float[] coords;

    public Circle2D(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        numSegments = (int) (Math.max(width, height) / 2);
        numSegments = (int) (numSegments * detail);
        if (numSegments < 5) numSegments = 5;
        coords = new float[numSegments * 2];
    }

    @Override
    public RenderCommand appendToBatch(BatchMesh mesh) {
        generatePoints();
        if (filled) {
            return ShapeGeometry.appendFilledConvexPolygon(mesh, viewport, coords, color);
        }

        float[] ringStrip = StrokeGeometry.createEllipseRingStrip(x, y, width, height, 1.0f, numSegments);
        if (ringStrip.length == 0) return new RenderCommand(mesh.vertexCount(), 0, mesh.indexCount(), 0);
        return ShapeGeometry.appendTriangleStripAsTriangles(mesh, viewport, ringStrip, color);
    }

    @Override
    public int getMaterialFlags() {
        return 0;
    }

    public float[] generatePoints() {
        int coordIndex = 0;
        float a = (float) (Math.PI / numSegments) * 2;
        float xx, yy;

        for (int i = 0; i < numSegments; i++) {
            xx = (float) (Math.sin(a * i) * width);
            yy = (float) (Math.cos(a * i) * height);
            coords[coordIndex++] = x + xx;
            coords[coordIndex++] = y + yy;
        }
        return coords;
    }

    @Override
    public int getTextureId() {
        return 0;
    }

    @Override
    public int getType() {
        return CIRCLE;
    }

    public void setFilled(boolean val) {
        filled = val;
    }
}
