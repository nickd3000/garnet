package com.physmo.garnet.structure;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PointIntTest {

    @Test
    void constructorsInitializeCoordinates() {
        PointInt p1 = new PointInt();
        PointInt p2 = new PointInt(1, 2);
        PointInt p3 = new PointInt(1, 2, 3);
        PointInt p4 = new PointInt(p3);

        assertEquals(0, p1.x);
        assertEquals(0, p1.y);
        assertEquals(0, p1.z);
        assertEquals("[0,0,0]", p1.toString());

        assertEquals(1, p2.x);
        assertEquals(2, p2.y);
        assertEquals(0, p2.z);

        assertEquals(1, p3.x);
        assertEquals(2, p3.y);
        assertEquals(3, p3.z);

        assertEquals(p3.x, p4.x);
        assertEquals(p3.y, p4.y);
        assertEquals(p3.z, p4.z);
    }
}
