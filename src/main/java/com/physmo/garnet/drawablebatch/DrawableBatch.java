package com.physmo.garnet.drawablebatch;

import com.physmo.garnet.graphics.Graphics;
import com.physmo.garnet.structure.Array;

import java.util.Comparator;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glEnable;

public class DrawableBatch {

    final Array<DrawableElement> elements;

    public DrawableBatch() {
        elements = new Array<>(10);
    }

    public Array<DrawableElement> getElements() {
        return elements;
    }

    public void add(DrawableElement batchElement) {
        elements.add(batchElement);
    }

    public int size() {
        return elements.size();
    }

    public void render(Graphics graphics) {
        elements.sort(Comparator.comparingInt(DrawableElement::getDrawOrder));

        glEnable(GL_TEXTURE_2D);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        for (DrawableElement element : elements) {
            applyClipRectIfRequired(graphics, element);
            graphics.bindTexture(element.getTextureId());
            element.render(graphics);
        }

    }

    public void applyClipRectIfRequired(Graphics graphics, DrawableElement de) {
        graphics._activateClipRect(de.getViewport());
    }

    public void clear() {
        elements.clear();
    }


}
