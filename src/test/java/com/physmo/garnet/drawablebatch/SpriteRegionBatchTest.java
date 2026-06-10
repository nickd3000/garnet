package com.physmo.garnet.drawablebatch;

import com.physmo.garnet.ColorUtils;
import com.physmo.garnet.graphics.Viewport;
import com.physmo.garnet.renderer.BatchMesh;
import com.physmo.garnet.renderer.BatchVertex;
import com.physmo.garnet.renderer.TextureRegion;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SpriteRegionBatchTest {

    @Test
    void appendsRegionBasedSpriteWithNormalizedUvs() {
        Sprite2D sprite = new Sprite2D();
        TextureRegion region = new TextureRegion(12, 32, 16, 8, 8, 128, 64, true);
        sprite.setRegionCoords(10, 20, 8, 8, region);
        sprite.setCommonValues(new Viewport(1, 320, 200), 0, ColorUtils.WHITE);
        BatchMesh mesh = new BatchMesh();

        sprite.appendToBatch(mesh);

        BatchVertex topLeft = mesh.vertices().get(0);
        BatchVertex bottomRight = mesh.vertices().get(2);
        assertEquals(12, sprite.getTextureId());
        assertEquals(32 / 128f, topLeft.u(), 0.0001f);
        assertEquals(16 / 64f, topLeft.v(), 0.0001f);
        assertEquals(40 / 128f, bottomRight.u(), 0.0001f);
        assertEquals(24 / 64f, bottomRight.v(), 0.0001f);
    }

    @Test
    void appendsRotatedRegionBasedSpriteWithNormalizedUvs() {
        Sprite2D sprite = new Sprite2D();
        TextureRegion region = new TextureRegion(12, 32, 16, 8, 8, 128, 64, true);
        sprite.setRegionCoords(10, 20, 8, 8, region);
        sprite.addAngle(90);
        sprite.setCommonValues(new Viewport(1, 320, 200), 0, ColorUtils.WHITE);
        BatchMesh mesh = new BatchMesh();

        sprite.appendToBatch(mesh);

        BatchVertex topLeft = mesh.vertices().get(0);
        BatchVertex bottomRight = mesh.vertices().get(2);
        assertEquals(32 / 128f, topLeft.u(), 0.0001f);
        assertEquals(16 / 64f, topLeft.v(), 0.0001f);
        assertEquals(40 / 128f, bottomRight.u(), 0.0001f);
        assertEquals(24 / 64f, bottomRight.v(), 0.0001f);
    }

    @Test
    void remapsExplicitSourcePixelCoordinatesThroughRegion() {
        Sprite2D sprite = new Sprite2D();
        TextureRegion region = new TextureRegion(12, 32, 16, 64, 32, 128, 64, true);
        float[] vertices = new float[]{10, 20, 18, 20, 18, 28, 10, 28};
        float[] sourcePixels = new float[]{16, 8, 24, 8, 24, 16, 16, 16};
        sprite.setRegionCoords(vertices, sourcePixels, region);
        sprite.setCommonValues(new Viewport(1, 320, 200), 0, ColorUtils.WHITE);
        BatchMesh mesh = new BatchMesh();

        sprite.appendToBatch(mesh);

        BatchVertex topLeft = mesh.vertices().get(0);
        BatchVertex bottomRight = mesh.vertices().get(2);
        assertEquals((32 + 16) / 128f, topLeft.u(), 0.0001f);
        assertEquals((16 + 8) / 64f, topLeft.v(), 0.0001f);
        assertEquals((32 + 24) / 128f, bottomRight.u(), 0.0001f);
        assertEquals((16 + 16) / 64f, bottomRight.v(), 0.0001f);
    }

    @Test
    void supportsExplicitVerticallyFlippedRawTextureCoordinates() {
        Sprite2D sprite = new Sprite2D();
        TextureRegion region = TextureRegion.raw(21, 640, 480);
        float[] vertices = new float[]{0, 0, 640, 0, 640, 480, 0, 480};
        float[] sourcePixels = new float[]{0, 480, 640, 480, 640, 0, 0, 0};
        sprite.setRegionCoords(vertices, sourcePixels, region);
        sprite.setCommonValues(new Viewport(1, 640, 480), 0, ColorUtils.WHITE);
        BatchMesh mesh = new BatchMesh();

        sprite.appendToBatch(mesh);

        assertEquals(0.0f, mesh.vertices().get(0).u(), 0.0001f);
        assertEquals(1.0f, mesh.vertices().get(0).v(), 0.0001f);
        assertEquals(1.0f, mesh.vertices().get(2).u(), 0.0001f);
        assertEquals(0.0f, mesh.vertices().get(2).v(), 0.0001f);
    }
}
