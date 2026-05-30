package com.physmo.garnet.graphics;

import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.glDeleteTextures;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glOrtho;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.opengl.GL30.GL_COLOR_ATTACHMENT0;
import static org.lwjgl.opengl.GL30.GL_FRAMEBUFFER;
import static org.lwjgl.opengl.GL30.GL_FRAMEBUFFER_COMPLETE;
import static org.lwjgl.opengl.GL30.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL30.glBindFramebuffer;
import static org.lwjgl.opengl.GL30.glCheckFramebufferStatus;
import static org.lwjgl.opengl.GL30.glDeleteFramebuffers;
import static org.lwjgl.opengl.GL30.glFramebufferTexture2D;
import static org.lwjgl.opengl.GL30.glGenFramebuffers;

/**
 * A render-to-texture target backed by an OpenGL FBO.
 * <p>
 * Usage:
 * <pre>
 *   RenderTexture rt = new RenderTexture(width, height);
 *   rt.bind();          // redirect rendering into the FBO
 *   // ... draw calls ...
 *   rt.unbind();        // restore default framebuffer
 *   Texture tex = rt.getTexture();   // use as a normal texture
 * </pre>
 */
public class RenderTexture {

    private final int fboId;
    private final Texture texture;
    private final int width;
    private final int height;

    public RenderTexture(int width, int height) {
        this.width = width;
        this.height = height;

        texture = Texture.createEmpty(width, height);

        fboId = glGenFramebuffers();
        glBindFramebuffer(GL_FRAMEBUFFER, fboId);
        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, texture.getId(), 0);

        int status = glCheckFramebufferStatus(GL_FRAMEBUFFER);
        if (status != GL_FRAMEBUFFER_COMPLETE) {
            throw new RuntimeException("Framebuffer is not complete, status: " + status);
        }

        glBindFramebuffer(GL_FRAMEBUFFER, 0);
    }

    /**
     * Redirect subsequent GL draw calls into this FBO.
     */
    public void bind() {
        glBindFramebuffer(GL_FRAMEBUFFER, fboId);
        // Set viewport and projection to exactly match the FBO texture size
        glViewport(0, 0, width, height);
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(0.0f, width, height, 0.0f, 0.0f, 1.0f);
    }

    /**
     * Restore the default (window) framebuffer and re-apply the window viewport.
     * The {@code display} is used to restore the correct scaled viewport.
     */
    public void unbind(com.physmo.garnet.Display display) {
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
        display.placeGlViewport();
    }

    /**
     * Restore the default (window) framebuffer (no viewport restore).
     */
    public void unbind() {
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
    }

    /**
     * The colour-attachment texture, ready to be drawn with {@code g.drawImage(...)}.
     */
    public Texture getTexture() {
        return texture;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void delete() {
        glDeleteFramebuffers(fboId);
        glDeleteTextures(texture.getId());
    }
}
