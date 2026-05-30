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

public class DrawableBatch {

    final Array<DrawableElement> elements;
    private boolean dirty = true;

    public DrawableBatch() {
        elements = new Array<>(10);
    }

    public Array<DrawableElement> getElements() {
        return elements;
    }

    public void add(DrawableElement batchElement) {
        elements.add(batchElement);
        dirty = true;
    }

    public int size() {
        return elements.size();
    }

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
                if (currentShader != null) currentShader.unbind();
                if (shader != null) shader.bind();
                currentShader = shader;
            }
            applyClipRectIfRequired(graphics, element);
            graphics.bindTexture(element.getTextureId());
            element.render(graphics);
        }

        if (currentShader != null) currentShader.unbind();
        applyBlendMode(BlendMode.NORMAL);
    }

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

    public void applyClipRectIfRequired(Graphics graphics, DrawableElement de) {
        graphics._activateClipRect(de.getViewport());
    }

    public void clear() {
        elements.clear();
        dirty = true;
    }


}
