package com.physmo.garnet.graphics;

import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TexturePixelDataTest {

    @Test
    void copiesRgbaPixelsFromBufferStartWithoutMutatingSourcePosition() {
        ByteBuffer source = ByteBuffer.allocateDirect(16);
        for (int i = 0; i < 16; i++) {
            source.put((byte) (i + 1));
        }
        source.position(7);

        ByteBuffer copy = Texture.copyRgbaPixels(source, 2, 2);

        assertEquals(7, source.position());
        assertEquals(0, copy.position());
        for (int i = 0; i < 16; i++) {
            assertEquals(i + 1, Byte.toUnsignedInt(copy.get(i)));
        }
    }

    @Test
    void rejectsTooSmallRgbaPixelBuffers() {
        ByteBuffer source = ByteBuffer.allocateDirect(15);

        assertThrows(IllegalArgumentException.class, () -> Texture.copyRgbaPixels(source, 2, 2));
    }
}
