package com.physmo.garnet.drawablebatch;

import com.physmo.garnet.graphics.Graphics;
import com.physmo.garnet.graphics.ShaderProgram;
import com.physmo.garnet.structure.Array;

import java.util.Comparator;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_DST_COLOR;
import static org.lwjgl.opengl.GL11.GL_ONE;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_ZERO;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glColorMask;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL14.GL_FUNC_ADD;
import static org.lwjgl.opengl.GL14.GL_FUNC_REVERSE_SUBTRACT;
import static org.lwjgl.opengl.GL14.glBlendEquation;

/**
 * Collects drawable elements and flushes them in draw-order.
 * <p>
 * The batch also owns the transient OpenGL state needed for each element, such
 * as blend mode, shader binding, texture binding, and clipping.
 */
public class DrawableBatch {

    final Array<DrawableElement> elements;
    // Sorting is deferred until render so multiple additions only pay one sort.
    private boolean dirty = true;

    public DrawableBatch() {
        elements = new Array<>(10);
    }

    /**
     * Returns the backing element list.
     * <p>
     * Mutating this list directly can bypass the dirty flag, so callers that add
     * elements should normally use {@link #add(DrawableElement)}.
     *
     * @return mutable backing collection of drawable elements
     */
    public Array<DrawableElement> getElements() {
        return elements;
    }

    /**
     * Adds an element to the batch and marks draw-order sorting as stale.
     *
     * @param batchElement element to render during the next batch flush
     */
    public void add(DrawableElement batchElement) {
        elements.add(batchElement);
        dirty = true;
    }

    public int size() {
        return elements.size();
    }

    /**
     * Renders all queued elements in ascending draw-order.
     * <p>
     * The render pass tracks the currently active blend mode and shader to avoid
     * unnecessary OpenGL state changes for adjacent elements with the same state.
     *
     * @param graphics active graphics context
     */
    public void render(Graphics graphics) {
        if (dirty) {
            elements.sort(Comparator.comparingInt(DrawableElement::getDrawOrder));
            dirty = false;
        }

        glEnable(GL_TEXTURE_2D);
        glEnable(GL_BLEND);

        BlendMode currentMode = null;
        ShaderProgram currentShader = null;
        for (DrawableElement element : elements) {
            BlendMode mode = element.getBlendMode();
            if (mode != currentMode) {
                applyBlendMode(mode);
                currentMode = mode;
            }
            ShaderProgram shader = element.getShader();
            if (shader != currentShader) {
                // Only switch shaders when needed; shader binds are global GL state changes.
                if (currentShader != null) currentShader.unbind();
                if (shader != null) shader.bind();
                currentShader = shader;
            }
            applyClipRectIfRequired(graphics, element);
            graphics.bindTexture(element.getTextureId());
            element.render(graphics);
        }

        if (currentShader != null) currentShader.unbind();
        // Leave subsequent drawing with the default blend state.
        applyBlendMode(BlendMode.NORMAL);
    }

    /**
     * Applies the OpenGL blend/color-mask state for a batch element.
     * <p>
     * Blend and colour-mask settings are global GL state, so special modes must
     * reset any state they alter before later elements are drawn.
     *
     * @param mode blend mode to apply
     */
    private void applyBlendMode(BlendMode mode) {
        switch (mode) {
            case NORMAL:
                glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
                break;
            case ADDITIVE:
                glBlendFunc(GL_SRC_ALPHA, GL_ONE);
                break;
            case SUBTRACTIVE:
                glBlendEquation(GL_FUNC_REVERSE_SUBTRACT);
                glBlendFunc(GL_SRC_ALPHA, GL_ONE);
                break;
            case MULTIPLY:
                glBlendFunc(GL_DST_COLOR, GL_ZERO);
                break;
            case MATTE:
                glColorMask(false, false, false, true);
                glBlendFunc(GL_ONE, GL_ZERO);
                break;
        }
        if (mode != BlendMode.MATTE) {
            glColorMask(true, true, true, true);
        }
        if (mode != BlendMode.SUBTRACTIVE) {
            glBlendEquation(GL_FUNC_ADD);
        }
    }

    /**
     * Applies the element viewport's clipping rectangle, if one is configured.
     *
     * @param graphics active graphics context
     * @param de       element whose viewport should drive clipping
     */
    public void applyClipRectIfRequired(Graphics graphics, DrawableElement de) {
        graphics._activateClipRect(de.getViewport());
    }

    /**
     * Removes all queued elements and marks the sorted order as stale.
     */
    public void clear() {
        elements.clear();
        dirty = true;
    }


}
