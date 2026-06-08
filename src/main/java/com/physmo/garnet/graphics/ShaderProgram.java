package com.physmo.garnet.graphics;

import com.physmo.garnet.FileUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL20.GL_COMPILE_STATUS;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_LINK_STATUS;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL20.glAttachShader;
import static org.lwjgl.opengl.GL20.glBindAttribLocation;
import static org.lwjgl.opengl.GL20.glCompileShader;
import static org.lwjgl.opengl.GL20.glCreateProgram;
import static org.lwjgl.opengl.GL20.glCreateShader;
import static org.lwjgl.opengl.GL20.glDeleteProgram;
import static org.lwjgl.opengl.GL20.glDeleteShader;
import static org.lwjgl.opengl.GL20.glGetProgramInfoLog;
import static org.lwjgl.opengl.GL20.glGetProgrami;
import static org.lwjgl.opengl.GL20.glGetShaderInfoLog;
import static org.lwjgl.opengl.GL20.glGetShaderi;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glLinkProgram;
import static org.lwjgl.opengl.GL20.glShaderSource;
import static org.lwjgl.opengl.GL20.glUniform1f;
import static org.lwjgl.opengl.GL20.glUniform1i;
import static org.lwjgl.opengl.GL20.glUniform2f;
import static org.lwjgl.opengl.GL20.glUniform2i;
import static org.lwjgl.opengl.GL20.glUniform3f;
import static org.lwjgl.opengl.GL20.glUniform4f;
import static org.lwjgl.opengl.GL20.glUseProgram;

/**
 * Represents a compiled and linked OpenGL shader program.
 * <p>
 * Use {@link #fromFiles} or {@link #fromSource} to create an instance, then call
 * {@link #bind()} before issuing draw calls or setting uniforms, and {@link #unbind()}
 * afterwards to clear the active program.
 */
public class ShaderProgram {

    private final Map<String, Integer> uniformLocations = new HashMap<>();
    private static final Map<String, Integer> STANDARD_BATCH_ATTRIBUTE_LOCATIONS = Map.of(
            "a_position", 0,
            "a_texCoord", 1,
            "a_color", 2,
            "a_materialFlags", 3
    );
    private int programId;
    private boolean deleted = false;

    private ShaderProgram(int programId) {
        this.programId = programId;
    }

    /**
     * Loads, compiles, and links a shader program from classpath resource files.
     *
     * @param vertPath classpath path to the vertex shader source file (e.g. "shaders/passthrough.vert")
     * @param fragPath classpath path to the fragment shader source file (e.g. "shaders/greyscale.frag")
     * @return a compiled and linked ShaderProgram
     */
    public static ShaderProgram fromFiles(String vertPath, String fragPath) {
        String vertSrc = readResource(vertPath);
        String fragSrc = readResource(fragPath);
        return fromSource(vertSrc, fragSrc);
    }

    private static String readResource(String path) {
        try (InputStream is = FileUtils.getFileFromResourceAsStream(path)) {
            return new String(is.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException | RuntimeException e) {
            throw new RuntimeException("Failed to read shader file: " + path, e);
        }
    }

    /**
     * Compiles and links a shader program from inline GLSL source strings.
     *
     * @param vertSrc GLSL vertex shader source
     * @param fragSrc GLSL fragment shader source
     * @return a compiled and linked ShaderProgram
     */
    public static ShaderProgram fromSource(String vertSrc, String fragSrc) {
        return fromSource(vertSrc, fragSrc, STANDARD_BATCH_ATTRIBUTE_LOCATIONS);
    }

    /**
     * Compiles and links a shader program from source strings with explicit attribute locations.
     *
     * @param vertSrc            GLSL vertex shader source
     * @param fragSrc            GLSL fragment shader source
     * @param attributeLocations map of attribute name to location, or {@code null}
     * @return a compiled and linked ShaderProgram
     */
    public static ShaderProgram fromSource(String vertSrc, String fragSrc, Map<String, Integer> attributeLocations) {
        int vertId = 0;
        int fragId = 0;
        int programId = 0;

        try {
            vertId = compileShader(GL_VERTEX_SHADER, vertSrc, "vertex");
            fragId = compileShader(GL_FRAGMENT_SHADER, fragSrc, "fragment");

            programId = glCreateProgram();
            if (programId == 0) {
                throw new RuntimeException("Shader program creation failed");
            }

            glAttachShader(programId, vertId);
            glAttachShader(programId, fragId);
            if (attributeLocations != null) {
                for (Map.Entry<String, Integer> entry : attributeLocations.entrySet()) {
                    glBindAttribLocation(programId, entry.getValue(), entry.getKey());
                }
            }
            glLinkProgram(programId);

            if (glGetProgrami(programId, GL_LINK_STATUS) == 0) {
                String log = glGetProgramInfoLog(programId);
                throw new RuntimeException("Shader program link failed:\n" + log);
            }

            glDeleteShader(vertId);
            glDeleteShader(fragId);

            return new ShaderProgram(programId);
        } catch (RuntimeException e) {
            if (programId != 0) glDeleteProgram(programId);
            if (vertId != 0) glDeleteShader(vertId);
            if (fragId != 0) glDeleteShader(fragId);
            throw e;
        }
    }

    private static int compileShader(int type, String src, String name) {
        int shaderId = glCreateShader(type);
        if (shaderId == 0) {
            throw new RuntimeException("Shader creation failed [" + name + "]");
        }

        glShaderSource(shaderId, src);
        glCompileShader(shaderId);

        if (glGetShaderi(shaderId, GL_COMPILE_STATUS) == 0) {
            String log = glGetShaderInfoLog(shaderId);
            glDeleteShader(shaderId);
            throw new RuntimeException("Shader compilation failed [" + name + "]:\n" + log);
        }

        return shaderId;
    }

    /**
     * Activates this shader program for subsequent GL draw calls and uniform updates.
     */
    public void bind() {
        ensureNotDeleted();
        glUseProgram(programId);
    }

    /**
     * Deactivates this shader program, restoring the fixed-function pipeline.
     */
    public void unbind() {
        glUseProgram(0);
    }

    private void ensureNotDeleted() {
        if (deleted) {
            throw new IllegalStateException("ShaderProgram has been deleted");
        }
    }

    /**
     * Returns the OpenGL program object ID for this shader program.
     * Use this when calling {@code glGetUniformLocation} or other raw GL uniform APIs.
     *
     * @return the GL program ID
     */
    public int getProgramId() {
        ensureNotDeleted();
        return programId;
    }

    /**
     * Returns the location of the named uniform variable within this program.
     * Returns -1 if the name does not correspond to an active uniform.
     *
     * @param name the GLSL uniform variable name
     * @return the uniform location, or -1 if not found
     */
    public int getUniformLocation(String name) {
        ensureNotDeleted();
        return getCachedUniformLocation(name);
    }

    private int getCachedUniformLocation(String name) {
        ensureNotDeleted();
        Integer cachedLocation = uniformLocations.get(name);
        if (cachedLocation != null) return cachedLocation;

        int location = glGetUniformLocation(programId, name);
        uniformLocations.put(name, location);
        return location;
    }

    /**
     * Sets a float uniform value.
     *
     * @param name the GLSL uniform variable name
     * @param v0   the uniform value
     */
    public void setUniform1f(String name, float v0) {
        int location = getCachedUniformLocation(name);
        if (location == -1) return;
        glUniform1f(location, v0);
    }

    /**
     * Sets a vec2 uniform value.
     *
     * @param name the GLSL uniform variable name
     * @param v0   the first component
     * @param v1   the second component
     */
    public void setUniform2f(String name, float v0, float v1) {
        int location = getCachedUniformLocation(name);
        if (location == -1) return;
        glUniform2f(location, v0, v1);
    }

    /**
     * Sets a vec3 uniform value.
     *
     * @param name the GLSL uniform variable name
     * @param v0   the first component
     * @param v1   the second component
     * @param v2   the third component
     */
    public void setUniform3f(String name, float v0, float v1, float v2) {
        int location = getCachedUniformLocation(name);
        if (location == -1) return;
        glUniform3f(location, v0, v1, v2);
    }

    /**
     * Sets a vec4 uniform value.
     *
     * @param name the GLSL uniform variable name
     * @param v0   the first component
     * @param v1   the second component
     * @param v2   the third component
     * @param v3   the fourth component
     */
    public void setUniform4f(String name, float v0, float v1, float v2, float v3) {
        int location = getCachedUniformLocation(name);
        if (location == -1) return;
        glUniform4f(location, v0, v1, v2, v3);
    }

    /**
     * Sets an int uniform value.
     *
     * @param name the GLSL uniform variable name
     * @param v0   the uniform value
     */
    public void setUniform1i(String name, int v0) {
        int location = getCachedUniformLocation(name);
        if (location == -1) return;
        glUniform1i(location, v0);
    }

    /**
     * Sets an ivec2 uniform value.
     *
     * @param name the GLSL uniform variable name
     * @param v0   the first component
     * @param v1   the second component
     */
    public void setUniform2i(String name, int v0, int v1) {
        int location = getCachedUniformLocation(name);
        if (location == -1) return;
        glUniform2i(location, v0, v1);
    }

    /**
     * Frees the GPU resources associated with this shader program.
     * The program must not be used after this call.
     */
    public void delete() {
        if (deleted) return;
        glDeleteProgram(programId);
        programId = 0;
        uniformLocations.clear();
        deleted = true;
    }
}
