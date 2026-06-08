package com.physmo.garnet.renderer;

import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AtlasManagerTest {

    @Test
    void allocatesRegionsWithConfiguredPadding() {
        AtlasManager atlasManager = new AtlasManager(new AtlasPolicy(64, 64, 1, 0), 1000);

        TextureRegion first = atlasManager.allocate(16, 16);
        TextureRegion second = atlasManager.allocate(8, 8);

        assertEquals(1000, first.textureId());
        assertEquals(1, first.x());
        assertEquals(1, first.y());
        assertEquals(18, second.x());
        assertEquals(1, second.y());
        assertTrue(first.atlasBacked());
        assertEquals(1, atlasManager.getPages().size());
    }

    @Test
    void startsNewShelfWhenCurrentRowWouldOverflow() {
        AtlasManager atlasManager = new AtlasManager(new AtlasPolicy(32, 64, 1, 0), 2000);

        TextureRegion first = atlasManager.allocate(20, 10);
        TextureRegion second = atlasManager.allocate(10, 8);

        assertEquals(1, first.x());
        assertEquals(1, first.y());
        assertEquals(1, second.x());
        assertEquals(12, second.y());
    }

    @Test
    void startsNewPageWhenAtlasPageIsFull() {
        AtlasManager atlasManager = new AtlasManager(new AtlasPolicy(32, 32, 1, 0), 3000);

        TextureRegion first = atlasManager.allocate(20, 20);
        TextureRegion second = atlasManager.allocate(20, 20);

        assertEquals(3000, first.textureId());
        assertEquals(3001, second.textureId());
        assertEquals(2, atlasManager.getPages().size());
    }

    @Test
    void rejectsRegionsLargerThanPageIncludingPadding() {
        AtlasManager atlasManager = new AtlasManager(new AtlasPolicy(16, 16, 1, 0), 4000);

        assertThrows(IllegalArgumentException.class, () -> atlasManager.allocate(15, 15));
    }

    @Test
    void rawRegionsAreExplicitlyNotAtlasBacked() {
        AtlasManager atlasManager = new AtlasManager(new AtlasPolicy(64, 64, 1, 0), 5000);

        TextureRegion raw = atlasManager.rawRegion(77, 128, 128);

        assertEquals(77, raw.textureId());
        assertEquals(128, raw.width());
        assertEquals(128, raw.height());
        assertFalse(raw.atlasBacked());
        assertEquals(0, atlasManager.getPages().size());
    }

    @Test
    void requestsPageTextureIdsFromSupplier() {
        int[] ids = {9000, 9001};
        int[] index = {0};
        AtlasManager atlasManager = new AtlasManager(new AtlasPolicy(16, 16, 1, 0), () -> ids[index[0]++]);

        TextureRegion first = atlasManager.allocate(10, 10);
        TextureRegion second = atlasManager.allocate(10, 10);

        assertEquals(9000, first.textureId());
        assertEquals(9001, second.textureId());
        assertEquals(2, index[0]);
        assertEquals(atlasManager.getPages().get(0), atlasManager.getPage(9000));
        assertEquals(atlasManager.getPages().get(1), atlasManager.getPage(9001));
    }

    @Test
    void writesRegionPixelsIntoOwningPage() {
        AtlasManager atlasManager = new AtlasManager(new AtlasPolicy(8, 8, 1, 0), 6000);
        TextureRegion region = atlasManager.allocate(2, 2);
        TextureAtlasPage page = atlasManager.getPages().get(0);

        page.writeRegionPixels(region, rgbaPixels(
                10, 20, 30, 40,
                50, 60, 70, 80,
                90, 100, 110, 120,
                130, 140, 150, 160
        ));

        ByteBuffer pixels = page.getRgbaPixelsCopy();
        assertPixel(pixels, 8, 1, 1, 10, 20, 30, 40);
        assertPixel(pixels, 8, 2, 1, 50, 60, 70, 80);
        assertPixel(pixels, 8, 1, 2, 90, 100, 110, 120);
        assertPixel(pixels, 8, 2, 2, 130, 140, 150, 160);
    }

    private static ByteBuffer rgbaPixels(int... bytes) {
        ByteBuffer buffer = ByteBuffer.allocateDirect(bytes.length);
        for (int value : bytes) {
            buffer.put((byte) value);
        }
        buffer.position(0);
        return buffer;
    }

    private static void assertPixel(ByteBuffer pixels, int width, int x, int y, int r, int g, int b, int a) {
        int index = ((y * width) + x) * 4;
        assertEquals(r, Byte.toUnsignedInt(pixels.get(index)));
        assertEquals(g, Byte.toUnsignedInt(pixels.get(index + 1)));
        assertEquals(b, Byte.toUnsignedInt(pixels.get(index + 2)));
        assertEquals(a, Byte.toUnsignedInt(pixels.get(index + 3)));
    }

    @Test
    void extrudesRegionEdgesIntoPadding() {
        AtlasManager atlasManager = new AtlasManager(new AtlasPolicy(8, 8, 1, 0), 7000);
        TextureRegion region = atlasManager.allocate(2, 2);
        TextureAtlasPage page = atlasManager.getPages().get(0);

        page.writeRegionPixels(region, rgbaPixels(
                1, 0, 0, 255,
                2, 0, 0, 255,
                3, 0, 0, 255,
                4, 0, 0, 255
        ));

        ByteBuffer pixels = page.getRgbaPixelsCopy();
        assertPixel(pixels, 8, 0, 0, 1, 0, 0, 255);
        assertPixel(pixels, 8, 1, 0, 1, 0, 0, 255);
        assertPixel(pixels, 8, 2, 0, 2, 0, 0, 255);
        assertPixel(pixels, 8, 3, 0, 2, 0, 0, 255);
        assertPixel(pixels, 8, 0, 3, 3, 0, 0, 255);
        assertPixel(pixels, 8, 3, 3, 4, 0, 0, 255);
    }
}
