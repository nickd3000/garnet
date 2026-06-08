package com.physmo.garnet.graphics;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TextureAtlasModeTest {

    @Test
    void exposesDefaultAndRawAtlasModes() {
        assertEquals(TextureAtlasMode.DEFAULT, TextureAtlasMode.valueOf("DEFAULT"));
        assertEquals(TextureAtlasMode.RAW, TextureAtlasMode.valueOf("RAW"));
    }
}
