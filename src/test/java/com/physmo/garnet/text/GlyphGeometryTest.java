package com.physmo.garnet.text;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GlyphGeometryTest {

    @Test
    void parseLineReadsBmFontCharacterGeometry() {
        GlyphGeometry glyphGeometry = new GlyphGeometry();

        glyphGeometry.parseLine("char id=32   x=66    y=43    width=6     height=1     xoffset=-2    yoffset=9     xadvance=2     page=0  chnl=15");

        assertEquals(32, glyphGeometry.id);
        assertEquals(66, glyphGeometry.x);
        assertEquals(43, glyphGeometry.y);
        assertEquals(6, glyphGeometry.width);
        assertEquals(1, glyphGeometry.height);
        assertEquals(-2, glyphGeometry.xoffset);
        assertEquals(9, glyphGeometry.yoffset);
        assertEquals(2, glyphGeometry.xadvance);
        assertEquals(0, glyphGeometry.page);
        assertEquals(15, glyphGeometry.channel);
    }
}
