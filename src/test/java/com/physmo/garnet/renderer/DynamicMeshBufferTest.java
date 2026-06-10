package com.physmo.garnet.renderer;

import com.physmo.garnet.ColorUtils;
import com.physmo.garnet.graphics.Viewport;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class DynamicMeshBufferTest {

    @Test
    void vertexLayoutConstantsMatchBatchVertexFormat() {
        assertEquals(9, DynamicMeshBuffer.FLOATS_PER_VERTEX);
        assertEquals(36, DynamicMeshBuffer.VERTEX_STRIDE_BYTES);
        assertEquals(0, DynamicMeshBuffer.POSITION_OFFSET_BYTES);
        assertEquals(8, DynamicMeshBuffer.TEX_COORD_OFFSET_BYTES);
        assertEquals(16, DynamicMeshBuffer.COLOR_OFFSET_BYTES);
        assertEquals(32, DynamicMeshBuffer.MATERIAL_FLAGS_OFFSET_BYTES);
    }

    @Test
    void packsBatchMeshVerticesAndIndicesForUpload() {
        BatchMesh mesh = new BatchMesh();
        SpriteGeometry.appendQuad(
                mesh,
                new Viewport(1, 320, 200),
                10, 20, 4, 2,
                0.25f, 0.5f, 0.125f, 0.25f,
                ColorUtils.rgb(255, 128, 0, 64),
                true);

        float[] vertices = DynamicMeshBuffer.toFloatArray(mesh);
        int[] indices = DynamicMeshBuffer.toIntArray(mesh);

        assertEquals(36, vertices.length);
        assertArrayEquals(new int[]{0, 1, 2, 0, 2, 3}, indices);
        assertEquals(10, vertices[0], 0.0001f);
        assertEquals(20, vertices[1], 0.0001f);
        assertEquals(0.25f, vertices[2], 0.0001f);
        assertEquals(0.5f, vertices[3], 0.0001f);
        assertEquals(1.0f, vertices[4], 0.0001f);
        assertEquals(128 / 255f, vertices[5], 0.0001f);
        assertEquals(0.0f, vertices[6], 0.0001f);
        assertEquals(64 / 255f, vertices[7], 0.0001f);
        assertEquals(BatchVertex.FLAG_TEXTURED | BatchVertex.FLAG_COLOR_OVERRIDE, vertices[8], 0.0001f);
    }
}
