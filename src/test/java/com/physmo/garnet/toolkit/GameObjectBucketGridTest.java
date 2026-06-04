package com.physmo.garnet.toolkit;

import com.physmo.garnet.toolkit.simplecollision.GameObjectBucketGrid;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GameObjectBucketGridTest {

    @Test
    void encoderDecoderRoundTripsSamplePoints() {
        GameObjectBucketGrid gameObjectBucketGrid = new GameObjectBucketGrid(10, 10);

        assertEncodeDecode(gameObjectBucketGrid, -10, -50);
        assertEncodeDecode(gameObjectBucketGrid, 0, 0);
        assertEncodeDecode(gameObjectBucketGrid, 1, 1);
        assertEncodeDecode(gameObjectBucketGrid, 1, 5);
    }

    private static void assertEncodeDecode(GameObjectBucketGrid gameObjectBucketGrid, int cellX, int cellY) {
        int encodedValue = gameObjectBucketGrid.encoder(cellX, cellY);
        int[] decodedPosition = gameObjectBucketGrid.decoder(encodedValue);
        assertEquals(cellX, decodedPosition[0]);
        assertEquals(cellY, decodedPosition[1]);
    }

    @Test
    void encoderDecoderRoundTripsRangeOfCellCoordinates() {
        GameObjectBucketGrid gameObjectBucketGrid = new GameObjectBucketGrid(32, 32);

        for (int y = -500; y < 500; y++) {
            for (int x = -500; x < 500; x++) {
                assertEncodeDecode(gameObjectBucketGrid, x, y);
            }
        }
    }
}
