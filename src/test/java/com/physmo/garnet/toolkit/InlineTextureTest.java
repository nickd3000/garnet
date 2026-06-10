package com.physmo.garnet.toolkit;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class InlineTextureTest {

    @Test
    void normalizeDataPadsMultilineRowsToTextureWidth() {
        String data = """
                  XXXX
                 X    X
                X o  o X
                X      X
                X o  o X
                X  oo  X
                 X    X
                  XXXX
                """;

        assertEquals(
                "  XXXX  "
                        + " X    X "
                        + "X o  o X"
                        + "X      X"
                        + "X o  o X"
                        + "X  oo  X"
                        + " X    X "
                        + "  XXXX  ",
                InlineTexture.normalizeData(data, 8, 8)
        );
    }

    @Test
    void normalizeDataLeavesSingleLineDataUnchanged() {
        String data = "  XXXX  " + " X    X ";

        assertEquals(data, InlineTexture.normalizeData(data, 8, 2));
    }

    @Test
    void normalizeDataRejectsRowsThatAreTooWide() {
        String data = """
                123456789
                """;

        assertThrows(RuntimeException.class, () -> InlineTexture.normalizeData(data, 8, 1));
    }
}
