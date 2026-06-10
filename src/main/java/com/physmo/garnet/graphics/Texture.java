package com.physmo.garnet.graphics;

/*
 * The MIT License (MIT)
 *
 * Copyright © 2014-2017, Heiko Brumme
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */


import com.physmo.garnet.FileUtils;
import org.lwjgl.system.MemoryStack;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.GL_LINEAR;
import static org.lwjgl.opengl.GL11.GL_NEAREST;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_RGBA8;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_S;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_T;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glDeleteTextures;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import static org.lwjgl.opengl.GL13.GL_CLAMP_TO_BORDER;
import static org.lwjgl.stb.STBImage.stbi_failure_reason;
import static org.lwjgl.stb.STBImage.stbi_info_from_memory;
import static org.lwjgl.stb.STBImage.stbi_load_from_memory;
import static org.lwjgl.stb.STBImage.stbi_set_flip_vertically_on_load;

/**
 * This class represents a texture.
 *
 * @author Heiko Brumme
 */
public class Texture {

    private final int id;
    private int width;
    private int height;
    private ByteBuffer rgbaPixelData;
    private TextureAtlasMode atlasMode = TextureAtlasMode.DEFAULT;
    public static int defaultFilterMode = GL_NEAREST; // Default filter mode is pixelated, not smooth.

    public Texture() {
        id = glGenTextures();
    }

    /**
     * Loads a texture from a classpath resource path.
     *
     * @param path the classpath-relative path to the image file (e.g. {@code "garnetCrystal.png"})
     * @return the loaded {@link Texture}
     */
    public static Texture loadTexture(String path) {
        InputStream inputStream = FileUtils.getFileFromResourceAsStream(path);
        return loadTexture(inputStream);
    }

    /**
     * Load texture from file.
     *
     * @param inputStream Input stream for the data
     * @return Texture from specified file
     */
    public static Texture loadTexture(InputStream inputStream) {

        byte[] bytes;
        try {
            bytes = inputStream.readAllBytes();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(bytes.length);
        byteBuffer.order(ByteOrder.nativeOrder());
        byteBuffer.put(bytes);
        byteBuffer.position(0); // Causes crash if not present!

        ByteBuffer imageBuffer;
        int width, height;
        try (MemoryStack stack = MemoryStack.stackPush()) {
            // Prepare image buffers
            IntBuffer w = stack.mallocInt(1);
            IntBuffer h = stack.mallocInt(1);
            IntBuffer comp = stack.mallocInt(1);

            if (!stbi_info_from_memory(byteBuffer, w, h, comp)) {
                throw new RuntimeException("Failed to read image information: " + stbi_failure_reason());
            } else {
                // Commented this out because it was reporting "Corrupt JPEG" when loading a png file
                //System.out.println("OK with reason: " + stbi_failure_reason());
            }

            // Load image.
            stbi_set_flip_vertically_on_load(false);
            imageBuffer = stbi_load_from_memory(byteBuffer, w, h, comp, 4);
            if (imageBuffer == null) {
                System.out.println("image was null");
                throw new RuntimeException("Failed to load a texture file!"
                        + System.lineSeparator() + stbi_failure_reason() + " " + inputStream);
            }

            // Get width and height of image.
            width = w.get();
            height = h.get();
        }

        return createTexture(width, height, imageBuffer);
    }


    /**
     * Creates an empty (uninitialized pixel data) RGBA texture suitable for use as an FBO colour attachment.
     *
     * @param width  Width of the texture
     * @param height Height of the texture
     * @return empty Texture
     */
    public static Texture createEmpty(int width, int height) {
        Texture texture = new Texture();
        texture.setAtlasMode(TextureAtlasMode.RAW);
        texture.setWidth(width);
        texture.setHeight(height);
        texture.bind();
        texture.setParameter(GL_TEXTURE_WRAP_S, GL_CLAMP_TO_BORDER);
        texture.setParameter(GL_TEXTURE_WRAP_T, GL_CLAMP_TO_BORDER);
        texture.setParameter(GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        texture.setParameter(GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, (java.nio.ByteBuffer) null);
        glBindTexture(GL_TEXTURE_2D, 0);
        return texture;
    }

    /**
     * Creates a texture with specified width, height and data.
     *
     * @param width  Width of the texture
     * @param height Height of the texture
     * @param data   Picture Data in RGBA format
     * @return Texture from the specified data
     */
    public static Texture createTexture(int width, int height, ByteBuffer data) {

        Texture texture = new Texture();
        texture.setWidth(width);
        texture.setHeight(height);

        texture.bind();

        texture.setParameter(GL_TEXTURE_WRAP_S, GL_CLAMP_TO_BORDER);
        texture.setParameter(GL_TEXTURE_WRAP_T, GL_CLAMP_TO_BORDER);


        texture.setParameter(GL_TEXTURE_MIN_FILTER, defaultFilterMode);
        texture.setParameter(GL_TEXTURE_MAG_FILTER, defaultFilterMode);

        texture.uploadData(GL_RGBA8, width, height, GL_RGBA, data);

        return texture;
    }

    /**
     * Sets the texture filter mode.
     *
     * @param val {@code true} for linear (smooth) filtering, {@code false} for nearest-neighbour (pixelated)
     */
    public void setFilter(boolean val) {
        int filterMode = !val ? GL_NEAREST : GL_LINEAR;
        setParameter(GL_TEXTURE_MIN_FILTER, filterMode);
        setParameter(GL_TEXTURE_MAG_FILTER, filterMode);
    }

    /**
     * Sets a texture parameter on the currently bound {@code GL_TEXTURE_2D} target.
     *
     * @param name  the parameter name (e.g. {@code GL_TEXTURE_MIN_FILTER})
     * @param value the parameter value
     */
    public void setParameter(int name, int value) {
        glTexParameteri(GL_TEXTURE_2D, name, value);
    }

    /**
     * Binds this texture as the current {@code GL_TEXTURE_2D} target.
     */
    public void bind() {
        glBindTexture(GL_TEXTURE_2D, id);
    }

    /**
     * Uploads image data with specified internal format, width, height and
     * image format.
     *
     * @param internalFormat Internal format of the image data
     * @param width          Width of the image
     * @param height         Height of the image
     * @param format         Format of the image data
     * @param data           Pixel data of the image
     */
    public void uploadData(int internalFormat, int width, int height, int format, ByteBuffer data) {
        glTexImage2D(GL_TEXTURE_2D, 0, internalFormat, width, height, 0, format, GL_UNSIGNED_BYTE, data);
        setWidth(width);
        setHeight(height);
        if (internalFormat == GL_RGBA8 && format == GL_RGBA && data != null) {
            // Keep a CPU copy so Graphics can later pack DEFAULT textures into
            // atlas pages. Dynamic/raw textures upload without retained pixels.
            rgbaPixelData = copyRgbaPixels(data, width, height);
        } else {
            rgbaPixelData = null;
        }
    }

    /**
     * Uploads image data with specified width and height.
     *
     * @param width     Width of the image
     * @param height    Height of the image
     * @param pixelData Pixel data of the image
     */
    public void uploadData(int width, int height, ByteBuffer pixelData) {
        uploadData(GL_RGBA8, width, height, GL_RGBA, pixelData);
    }

    /**
     * Frees the GPU memory associated with this texture.
     * The texture must not be used after this call.
     */
    public void delete() {
        glDeleteTextures(id);
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        if (width > 0) {
            this.width = width;
        }
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        if (height > 0) {
            this.height = height;
        }
    }

    /**
     * Returns the OpenGL texture object ID.
     *
     * @return the GL texture ID
     */
    public int getId() {
        return id;
    }

    static ByteBuffer copyRgbaPixels(ByteBuffer data, int width, int height) {
        int byteCount = width * height * 4;
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("Texture dimensions must be positive");
        }
        if (data.capacity() < byteCount) {
            throw new IllegalArgumentException("RGBA pixel buffer is smaller than width * height * 4");
        }

        ByteBuffer source = data.duplicate();
        source.position(0);
        source.limit(byteCount);

        ByteBuffer copy = ByteBuffer.allocateDirect(byteCount);
        copy.put(source);
        copy.position(0);
        return copy;
    }

    public boolean hasRgbaPixelData() {
        return rgbaPixelData != null;
    }

    /**
     * Returns a copy of this texture's retained RGBA8 pixels, or {@code null} for raw/dynamic textures.
     *
     * @return copied RGBA8 pixel data positioned at zero
     */
    public ByteBuffer getRgbaPixelDataCopy() {
        if (rgbaPixelData == null) return null;
        ByteBuffer copy = ByteBuffer.allocateDirect(rgbaPixelData.capacity());
        ByteBuffer source = rgbaPixelData.duplicate();
        source.position(0);
        copy.put(source);
        copy.position(0);
        return copy;
    }

    public TextureAtlasMode getAtlasMode() {
        return atlasMode;
    }

    public Texture setAtlasMode(TextureAtlasMode atlasMode) {
        if (atlasMode == null) throw new IllegalArgumentException("atlasMode must not be null");
        this.atlasMode = atlasMode;
        return this;
    }

    public boolean isAtlasRaw() {
        return atlasMode == TextureAtlasMode.RAW;
    }
}
