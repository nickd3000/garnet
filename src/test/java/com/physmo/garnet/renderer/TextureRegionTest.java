package com.physmo.garnet.renderer;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TextureRegionTest {

    @Test
    void calculatesNormalizedUvsFromPixelRegion() {
        TextureRegion region = new TextureRegion(42, 16, 32, 8, 16, 64, 128, true);

        assertEquals(0.25f, region.u0(), 0.0001f);
        assertEquals(0.25f, region.v0(), 0.0001f);
        assertEquals(0.375f, region.u1(), 0.0001f);
        assertEquals(0.375f, region.v1(), 0.0001f);
        assertEquals(0.125f, region.uWidth(), 0.0001f);
        assertEquals(0.125f, region.vHeight(), 0.0001f);
        assertTrue(region.atlasBacked());
    }

    @Test
    void createsRawFullTextureRegion() {
        TextureRegion raw = TextureRegion.raw(7, 32, 16);

        assertEquals(7, raw.textureId());
        assertEquals(0, raw.x());
        assertEquals(0, raw.y());
        assertEquals(32, raw.width());
        assertEquals(16, raw.height());
        assertEquals(1.0f, raw.u1(), 0.0001f);
        assertEquals(1.0f, raw.v1(), 0.0001f);
        assertFalse(raw.atlasBacked());
    }

    @Test
    void mapsTileSheetStyleSubRegionsInsideParentRegion() {
        TextureRegion sheet = new TextureRegion(50, 8, 8, 64, 32, 128, 64, true);

        TextureRegion tile = sheet.subRegion(16, 16, 16, 16);

        assertEquals(50, tile.textureId());
        assertEquals(24, tile.x());
        assertEquals(24, tile.y());
        assertEquals(16, tile.width());
        assertEquals(16, tile.height());
        assertEquals(24 / 128f, tile.u0(), 0.0001f);
        assertEquals(40 / 128f, tile.u1(), 0.0001f);
        assertTrue(tile.atlasBacked());
    }

    @Test
    void rejectsSubRegionOutsideParent() {
        TextureRegion sheet = new TextureRegion(50, 0, 0, 32, 32, 64, 64, true);

        assertThrows(IllegalArgumentException.class, () -> sheet.subRegion(24, 0, 16, 16));
    }
}
