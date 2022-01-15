package com.physmo.garnet;

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


import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

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
import static org.lwjgl.stb.STBImage.stbi_load;
import static org.lwjgl.stb.STBImage.stbi_set_flip_vertically_on_load;

//import static org.lwjgl.opengl.GL13.GL_CLAMP_TO_BORDER;

/**
 * This class represents a texture.
 *
 * @author Heiko Brumme
 */
public class Texture {

    private final int id;
    private int width;
    private int height;


    public Texture() {
        id = glGenTextures();
    }

    /**
     * Load texture from file.
     *
     * @param path File path of the texture
     * @return Texture from specified file
     */
    public static Texture loadTexture(String path) {
        ByteBuffer image;
        int width, height;
        try (MemoryStack stack = MemoryStack.stackPush()) {
            // Prepare image buffers
            IntBuffer w = stack.mallocInt(1);
            IntBuffer h = stack.mallocInt(1);
            IntBuffer comp = stack.mallocInt(1);

            // Load image.
            stbi_set_flip_vertically_on_load(false);
            image = stbi_load(path, w, h, comp, 4);
            if (image == null) {
                System.out.println("image was null");
                throw new RuntimeException("Failed to load a texture file!"
                        + System.lineSeparator() + stbi_failure_reason() + " " + path);
            }

            // Get width and height of image.
            width = w.get();
            height = h.get();
        }

        return createTexture(width, height, image);
    }

//    public static Texture loadTexture(BufferedImage bufferedImage) {
//        ByteBuffer byteBuffer = bufferedImageToByteBuffer(bufferedImage);
//        return createTexture(bufferedImage.getWidth(), bufferedImage.getHeight(), byteBuffer);
//    }

//    public static ByteBuffer bufferedImageToByteBuffer(BufferedImage bufferedImage) {
//        Raster raster = bufferedImage.getRaster();
//        DataBufferByte dataBufferByte = (DataBufferByte) raster.getDataBuffer();
//        byte[] data = dataBufferByte.getData();
//
//        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(data.length);
//        byteBuffer.order(ByteOrder.nativeOrder());
//        byteBuffer.put(data, 0, data.length);
//        byteBuffer.flip();
//
//        return byteBuffer;
//    }

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
        texture.setParameter(GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        texture.setParameter(GL_TEXTURE_MAG_FILTER, GL_NEAREST);

        texture.uploadData(GL_RGBA8, width, height, GL_RGBA, data);

        return texture;
    }

    public void bind() {
        glBindTexture(GL_TEXTURE_2D, id);
    }

    public void setParameter(int name, int value) {
        glTexParameteri(GL_TEXTURE_2D, name, value);
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
    }

    public void setWidth(int width) {
        if (width > 0) {
            this.width = width;
        }
    }

    public void setHeight(int height) {
        if (height > 0) {
            this.height = height;
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

    public void delete() {
        glDeleteTextures(id);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

}
