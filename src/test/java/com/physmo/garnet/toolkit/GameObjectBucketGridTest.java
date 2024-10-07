package com.physmo.garnet.toolkit;

import com.physmo.garnet.toolkit.simplecollision.GameObjectBucketGrid;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

public class GameObjectBucketGridTest {

    GameObjectBucketGrid gameObjectBucketGrid = new GameObjectBucketGrid(10, 10);

    @Test
    public void testRangeOfPoints() {
        testEncodeDecode(-10, -50);
        testEncodeDecode(0, 0);
        testEncodeDecode(1, 1);
        testEncodeDecode(1, 5);
    }

    public void testEncodeDecode(int cellX, int cellY) {
        int encodedValue = gameObjectBucketGrid.encoder(cellX, cellY);
        Integer[] decodedPosition = gameObjectBucketGrid.decoder(encodedValue);
        Assertions.assertEquals(cellX, decodedPosition[0]);
        Assertions.assertEquals(cellY, decodedPosition[1]);
    }


}
