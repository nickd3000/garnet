package com.physmo.garnet.graphics;

import com.physmo.garnet.FileUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.lwjgl.opengl.GL20.GL_COMPILE_STATUS;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_LINK_STATUS;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL20.glAttachShader;
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
import static org.lwjgl.opengl.GL20.glUseProgram;

public class ShaderProgram {

    private final int programId;

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
        } catch (IOException e) {
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
        int vertId = compileShader(GL_VERTEX_SHADER, vertSrc, "vertex");
        int fragId = compileShader(GL_FRAGMENT_SHADER, fragSrc, "fragment");

        int programId = glCreateProgram();
        glAttachShader(programId, vertId);
        glAttachShader(programId, fragId);
        glLinkProgram(programId);

        if (glGetProgrami(programId, GL_LINK_STATUS) == 0) {
            String log = glGetProgramInfoLog(programId);
            glDeleteProgram(programId);
            glDeleteShader(vertId);
            glDeleteShader(fragId);
            throw new RuntimeException("Shader program link failed:\n" + log);
        }

        glDeleteShader(vertId);
        glDeleteShader(fragId);

        return new ShaderProgram(programId);
    }

    private static int compileShader(int type, String src, String name) {
        int shaderId = glCreateShader(type);
        glShaderSource(shaderId, src);
        glCompileShader(shaderId);

        if (glGetShaderi(shaderId, GL_COMPILE_STATUS) == 0) {
            String log = glGetShaderInfoLog(shaderId);
            glDeleteShader(shaderId);
            throw new RuntimeException("Shader compilation failed [" + name + "]:\n" + log);
        }

        return shaderId;
    }

    public void bind() {
        glUseProgram(programId);
    }

    public void unbind() {
        glUseProgram(0);
    }

    public int getProgramId() {
        return programId;
    }

    public int getUniformLocation(String name) {
        return glGetUniformLocation(programId, name);
    }

    public void delete() {
        glDeleteProgram(programId);
    }
}
