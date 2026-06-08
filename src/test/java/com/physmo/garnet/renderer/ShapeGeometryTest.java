package com.physmo.garnet.renderer;

import com.physmo.garnet.ColorUtils;
import com.physmo.garnet.graphics.Viewport;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ShapeGeometryTest {

    @Test
    void triangulatesFilledRectangleAsCenterFan() {
        BatchMesh mesh = new BatchMesh();
        Viewport viewport = new Viewport(1, 320, 200);

        RenderCommand command = ShapeGeometry.appendFilledConvexPolygon(mesh, viewport, new float[]{
                0, 0,
                10, 0,
                10, 20,
                0, 20
        }, ColorUtils.RED);

        assertEquals(new RenderCommand(0, 5, 0, 12), command);
        assertEquals(5, mesh.vertexCount());
        assertEquals(List.of(0, 1, 2, 0, 2, 3, 0, 3, 4, 0, 4, 1), mesh.indices());
        assertEquals(5, mesh.vertices().get(0).x(), 0.0001f);
        assertEquals(10, mesh.vertices().get(0).y(), 0.0001f);
        assertEquals(0, mesh.vertices().get(0).materialFlags());
    }

    @Test
    void convertsTriangleStripToIndependentTrianglesWithWindingCompensation() {
        BatchMesh mesh = new BatchMesh();
        Viewport viewport = new Viewport(1, 320, 200);

        RenderCommand command = ShapeGeometry.appendTriangleStripAsTriangles(mesh, viewport, new float[]{
                0, 0,
                10, 0,
                0, 10,
                10, 10
        }, ColorUtils.GREEN);

        assertEquals(new RenderCommand(0, 4, 0, 6), command);
        assertEquals(List.of(0, 1, 2, 2, 1, 3), mesh.indices());
    }
}
