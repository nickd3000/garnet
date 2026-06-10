package com.physmo.garnet;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FileUtilsTest {

    @Test
    void getFileFromResourceAsStreamReturnsReadableStreamForExistingResource() throws IOException {
        try (InputStream stream = FileUtils.getFileFromResourceAsStream("defaultfont.fnt")) {
            assertNotNull(stream);
            assertTrue(stream.read() >= 0);
        }
    }

    @Test
    void getFileFromResourceAsStreamFallsBackToExampleResources() throws IOException {
        try (InputStream stream = FileUtils.getFileFromResourceAsStream("garnetCrystal.png")) {
            assertNotNull(stream);
            assertTrue(stream.read() >= 0);
        }
    }

    @Test
    void getFileFromResourceAsStreamThrowsForMissingResourceAndIncludesName() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> FileUtils.getFileFromResourceAsStream("missing-resource.fnt")
        );

        assertTrue(exception.getMessage().contains("missing-resource.fnt"));
    }

    @Test
    void getPathForResourceReturnsAbsolutePathForExistingResource() {
        String path = FileUtils.getPathForResource(this, "defaultfont.fnt");

        assertNotNull(path);
        assertTrue(new File(path).isAbsolute());
        assertTrue(new File(path).isFile());
    }

    @Test
    void getPathForResourceThrowsForMissingResourceAndIncludesName() {
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> FileUtils.getPathForResource(this, "missing-resource.fnt")
        );

        assertTrue(exception.getMessage().contains("missing-resource.fnt"));
    }
}
