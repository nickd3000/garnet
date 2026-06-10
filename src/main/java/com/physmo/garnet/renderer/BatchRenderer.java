package com.physmo.garnet.renderer;

import com.physmo.garnet.graphics.Graphics;
import com.physmo.garnet.graphics.ShaderProgram;
import com.physmo.garnet.graphics.Viewport;
import org.lwjgl.BufferUtils;

import java.nio.ByteBuffer;
import java.util.LinkedHashMap;
import java.util.Map;

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
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;

/**
 * Renders a CPU batch plan through the dynamic mesh buffer path.
 */
public class BatchRenderer {
    private final DynamicMeshBuffer meshBuffer;
    private final BatchRenderStats stats;
    private ShaderProgram defaultShader;
    private int whiteTextureId;
    private int currentTextureId;

    public BatchRenderer() {
        this(new DynamicMeshBuffer(), new BatchRenderStats());
    }

    BatchRenderer(DynamicMeshBuffer meshBuffer, BatchRenderStats stats) {
        this.meshBuffer = meshBuffer;
        this.stats = stats;
    }

    public void render(Graphics graphics, BatchRenderPlan plan) {
        stats.reset();
        stats.setQueuedElements(plan.commands().size());
        stats.setRenderRuns(plan.runs().size());
        currentTextureId = 0;
        if (plan.mesh().indexCount() == 0) return;

        meshBuffer.upload(plan.mesh());
        stats.incrementBufferUploads();

        ShaderProgram currentShader = null;
        for (RenderRun run : plan.runs()) {
            RenderStateKey stateKey = run.stateKey();
            ShaderProgram shader = selectShader(stateKey);
            if (shader != currentShader) {
                if (currentShader != null) currentShader.unbind();
                shader.bind();
                setCommonUniforms(graphics, shader);
                currentShader = shader;
            }

            graphics.applyBlendMode(stateKey.blendMode());
            applyClip(graphics, stateKey);
            bindTextureIfNeeded(graphics, stateKey.textureId());
            meshBuffer.draw(run);
            stats.incrementDrawCalls();
        }

        if (currentShader != null) currentShader.unbind();
        graphics.resetBlendMode();
    }

    private ShaderProgram selectShader(RenderStateKey stateKey) {
        if (stateKey.shader() != null) return stateKey.shader();
        return getDefaultShader();
    }

    private void setCommonUniforms(Graphics graphics, ShaderProgram shader) {
        int[] canvasSize = graphics.getCanvasSize();
        shader.setUniform2f(BatchShaderResources.UNIFORM_SCREEN_SIZE, canvasSize[0], canvasSize[1]);
        shader.setUniform1i(BatchShaderResources.UNIFORM_TEXTURE, 0);
        // Existing example shaders use the legacy sampler name "texture".
        // Keep it in sync with the new default "u_texture" sampler.
        shader.setUniform1i("texture", 0);
    }

    private void applyClip(Graphics graphics, RenderStateKey stateKey) {
        Viewport viewport = graphics.getViewportManager().getViewport(stateKey.viewportId());
        graphics._activateClipRect(viewport);
    }

    private void bindTextureIfNeeded(Graphics graphics, int textureId) {
        int textureToBind = textureId == 0 ? getWhiteTextureId() : textureId;
        if (textureToBind == currentTextureId) return;

        if (textureId == 0) {
            glBindTexture(GL_TEXTURE_2D, textureToBind);
            // Shapes bind the renderer-owned white texture directly. Graphics
            // tracks its own texture cache, so invalidate it before sprites bind
            // through Graphics again.
            graphics.invalidateTextureBindingCache();
        } else {
            graphics.bindTexture(textureId);
        }
        currentTextureId = textureToBind;
        stats.incrementTextureBinds();
    }

    private ShaderProgram getDefaultShader() {
        if (defaultShader == null) {
            defaultShader = ShaderProgram.fromSource(
                    BatchShaderResources.readVertexShader(),
                    BatchShaderResources.readFragmentShader(),
                    defaultAttributeBindings());
        }
        return defaultShader;
    }

    private int getWhiteTextureId() {
        if (whiteTextureId != 0) return whiteTextureId;

        whiteTextureId = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, whiteTextureId);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);

        ByteBuffer pixel = BufferUtils.createByteBuffer(4);
        pixel.put((byte) 0xff);
        pixel.put((byte) 0xff);
        pixel.put((byte) 0xff);
        pixel.put((byte) 0xff);
        pixel.flip();
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, 1, 1, 0, GL_RGBA, GL_UNSIGNED_BYTE, pixel);
        return whiteTextureId;
    }

    private static Map<String, Integer> defaultAttributeBindings() {
        Map<String, Integer> bindings = new LinkedHashMap<>();
        bindings.put(BatchShaderResources.ATTR_POSITION, DynamicMeshBuffer.POSITION_ATTRIBUTE_INDEX);
        bindings.put(BatchShaderResources.ATTR_TEX_COORD, DynamicMeshBuffer.TEX_COORD_ATTRIBUTE_INDEX);
        bindings.put(BatchShaderResources.ATTR_COLOR, DynamicMeshBuffer.COLOR_ATTRIBUTE_INDEX);
        bindings.put(BatchShaderResources.ATTR_MATERIAL_FLAGS, DynamicMeshBuffer.MATERIAL_FLAGS_ATTRIBUTE_INDEX);
        return bindings;
    }

    public BatchRenderStats getStats() {
        return stats;
    }

    public void dispose() {
        meshBuffer.dispose();
        if (defaultShader != null) {
            defaultShader.delete();
            defaultShader = null;
        }
        if (whiteTextureId != 0) {
            glDeleteTextures(whiteTextureId);
            whiteTextureId = 0;
        }
    }
}
