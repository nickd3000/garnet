package com.physmo.garnet.renderer;

import com.physmo.garnet.FileUtils;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertTrue;

class BatchShaderResourcesTest {

    @Test
    void defaultBatchShadersDeclareExpectedAttributesAndUniforms() throws IOException {
        String vertex = readResource(BatchShaderResources.VERTEX_SHADER);
        String fragment = readResource(BatchShaderResources.FRAGMENT_SHADER);

        assertTrue(vertex.contains("attribute vec2 " + BatchShaderResources.ATTR_POSITION));
        assertTrue(vertex.contains("attribute vec2 " + BatchShaderResources.ATTR_TEX_COORD));
        assertTrue(vertex.contains("attribute vec4 " + BatchShaderResources.ATTR_COLOR));
        assertTrue(vertex.contains("attribute float " + BatchShaderResources.ATTR_MATERIAL_FLAGS));
        assertTrue(vertex.contains("uniform vec2 " + BatchShaderResources.UNIFORM_SCREEN_SIZE));
        assertTrue(fragment.contains("uniform sampler2D " + BatchShaderResources.UNIFORM_TEXTURE));
        assertTrue(fragment.contains("colorOverride"));
    }

    private static String readResource(String path) throws IOException {
        try (InputStream inputStream = FileUtils.getFileFromResourceAsStream(path)) {
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        }
    }

    @Test
    void passthroughShaderAdaptsBatchAttributesForLegacyFragments() throws IOException {
        String vertex = readResource("shaders/passthrough.vert");

        assertTrue(vertex.contains("attribute vec2 " + BatchShaderResources.ATTR_POSITION));
        assertTrue(vertex.contains("attribute vec2 " + BatchShaderResources.ATTR_TEX_COORD));
        assertTrue(vertex.contains("attribute vec4 " + BatchShaderResources.ATTR_COLOR));
        assertTrue(vertex.contains("uniform vec2 " + BatchShaderResources.UNIFORM_SCREEN_SIZE));
        assertTrue(vertex.contains("gl_TexCoord[0]"));
        assertTrue(vertex.contains("gl_FrontColor"));
    }
}
