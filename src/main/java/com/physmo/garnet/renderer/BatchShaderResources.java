package com.physmo.garnet.renderer;

import com.physmo.garnet.FileUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * Resource paths and public shader names used by the default buffered batch shader.
 */
public final class BatchShaderResources {
    public static final String VERTEX_SHADER = "shaders/batch.vert";
    public static final String FRAGMENT_SHADER = "shaders/batch.frag";

    public static final String ATTR_POSITION = "a_position";
    public static final String ATTR_TEX_COORD = "a_texCoord";
    public static final String ATTR_COLOR = "a_color";
    public static final String ATTR_MATERIAL_FLAGS = "a_materialFlags";

    public static final String UNIFORM_SCREEN_SIZE = "u_screenSize";
    public static final String UNIFORM_TEXTURE = "u_texture";

    private BatchShaderResources() {
    }

    public static String readVertexShader() {
        return readResource(VERTEX_SHADER);
    }

    private static String readResource(String path) {
        try (InputStream inputStream = FileUtils.getFileFromResourceAsStream(path)) {
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException | RuntimeException e) {
            throw new RuntimeException("Failed to read shader file: " + path, e);
        }
    }

    public static String readFragmentShader() {
        return readResource(FRAGMENT_SHADER);
    }
}
