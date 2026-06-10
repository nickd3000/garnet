package com.physmo.garnet.renderer;

import com.physmo.garnet.ColorUtils;
import com.physmo.garnet.graphics.Viewport;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SpriteGeometryTest {

    @Test
    void appendsSpriteQuadWithViewportTransformAndUvCoordinates() {
        Viewport viewport = new Viewport(1, 320, 200)
                .setWindowX(10)
                .setWindowY(20)
                .setZoom(2.0);
        viewport.setScrollX(3);
        viewport.setScrollY(4);
        BatchMesh mesh = new BatchMesh();

        RenderCommand command = SpriteGeometry.appendQuad(
                mesh, viewport,
                5, 7, 16, 8,
                0.25f, 0.5f, 0.125f, 0.25f,
                ColorUtils.rgb(128, 64, 32, 255), false);

        assertEquals(new RenderCommand(0, 4, 0, 6), command);
        assertEquals(4, mesh.vertexCount());
        assertEquals(6, mesh.indexCount());
        assertEquals(List.of(0, 1, 2, 0, 2, 3), mesh.indices());

        BatchVertex topLeft = mesh.vertices().get(0);
        assertEquals(14, topLeft.x(), 0.0001f);
        assertEquals(26, topLeft.y(), 0.0001f);
        assertEquals(0.25f, topLeft.u(), 0.0001f);
        assertEquals(0.5f, topLeft.v(), 0.0001f);
        assertEquals(128 / 255f, topLeft.r(), 0.0001f);
        assertEquals(64 / 255f, topLeft.g(), 0.0001f);
        assertEquals(32 / 255f, topLeft.b(), 0.0001f);
        assertEquals(1.0f, topLeft.a(), 0.0001f);
        assertEquals(BatchVertex.FLAG_TEXTURED, topLeft.materialFlags());

        BatchVertex bottomRight = mesh.vertices().get(2);
        assertEquals(46, bottomRight.x(), 0.0001f);
        assertEquals(42, bottomRight.y(), 0.0001f);
        assertEquals(0.375f, bottomRight.u(), 0.0001f);
        assertEquals(0.75f, bottomRight.v(), 0.0001f);
    }

    @Test
    void appendsRotatedSpriteQuadAroundSpriteCenter() {
        Viewport viewport = new Viewport(1, 320, 200);
        BatchMesh mesh = new BatchMesh();

        SpriteGeometry.appendRotatedQuad(
                mesh, viewport,
                10, 20, 4, 2,
                0, 0, 1, 1,
                90,
                ColorUtils.WHITE,
                true);

        BatchVertex topLeft = mesh.vertices().get(0);
        BatchVertex topRight = mesh.vertices().get(1);
        BatchVertex bottomRight = mesh.vertices().get(2);
        BatchVertex bottomLeft = mesh.vertices().get(3);

        assertEquals(11, topLeft.x(), 0.0001f);
        assertEquals(18, topLeft.y(), 0.0001f);
        assertEquals(11, topRight.x(), 0.0001f);
        assertEquals(22, topRight.y(), 0.0001f);
        assertEquals(9, bottomRight.x(), 0.0001f);
        assertEquals(22, bottomRight.y(), 0.0001f);
        assertEquals(9, bottomLeft.x(), 0.0001f);
        assertEquals(18, bottomLeft.y(), 0.0001f);
        assertEquals(BatchVertex.FLAG_TEXTURED | BatchVertex.FLAG_COLOR_OVERRIDE, topLeft.materialFlags());
    }
}
