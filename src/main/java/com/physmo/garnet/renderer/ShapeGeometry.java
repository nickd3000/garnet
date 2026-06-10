package com.physmo.garnet.renderer;

import com.physmo.garnet.ColorUtils;
import com.physmo.garnet.graphics.Viewport;

/**
 * Builds triangle geometry for untextured filled shapes.
 */
public final class ShapeGeometry {
    private ShapeGeometry() {
    }

    public static RenderCommand appendFilledConvexPolygon(BatchMesh mesh, Viewport viewport, float[] coords, int rgba) {
        if (coords.length < 6 || coords.length % 2 != 0) {
            throw new IllegalArgumentException("coords must contain at least three x/y pairs");
        }

        int firstVertex = mesh.vertexCount();
        int firstIndex = mesh.indexCount();
        float[] color = ColorUtils.rgbToFloat(rgba);
        float centerX = 0;
        float centerY = 0;
        int pointCount = coords.length / 2;
        for (int i = 0; i < coords.length; i += 2) {
            centerX += coords[i];
            centerY += coords[i + 1];
        }
        centerX /= pointCount;
        centerY /= pointCount;

        int centerIndex = addShapeVertex(mesh, viewport, centerX, centerY, color);
        int[] perimeterIndices = new int[pointCount];
        for (int i = 0; i < pointCount; i++) {
            perimeterIndices[i] = addShapeVertex(mesh, viewport, coords[i * 2], coords[(i * 2) + 1], color);
        }
        for (int i = 0; i < pointCount; i++) {
            mesh.addTriangle(centerIndex, perimeterIndices[i], perimeterIndices[(i + 1) % pointCount]);
        }

        return new RenderCommand(firstVertex, pointCount + 1, firstIndex, pointCount * 3);
    }

    private static int addShapeVertex(BatchMesh mesh, Viewport viewport, float x, float y, float[] color) {
        return mesh.addVertex(new BatchVertex(
                ViewportTransform.screenX(viewport, x),
                ViewportTransform.screenY(viewport, y),
                0,
                0,
                color[0],
                color[1],
                color[2],
                color[3],
                0));
    }

    public static RenderCommand appendTriangleStripAsTriangles(BatchMesh mesh, Viewport viewport, float[] coords, int rgba) {
        if (coords.length < 6 || coords.length % 2 != 0) {
            throw new IllegalArgumentException("coords must contain at least three x/y pairs");
        }

        int firstVertex = mesh.vertexCount();
        int firstIndex = mesh.indexCount();
        float[] color = ColorUtils.rgbToFloat(rgba);
        int pointCount = coords.length / 2;
        for (int i = 0; i < pointCount; i++) {
            addShapeVertex(mesh, viewport, coords[i * 2], coords[(i * 2) + 1], color);
        }
        for (int i = 0; i < pointCount - 2; i++) {
            if ((i & 1) == 0) {
                mesh.addTriangle(firstVertex + i, firstVertex + i + 1, firstVertex + i + 2);
            } else {
                mesh.addTriangle(firstVertex + i + 1, firstVertex + i, firstVertex + i + 2);
            }
        }

        return new RenderCommand(firstVertex, pointCount, firstIndex, (pointCount - 2) * 3);
    }
}
