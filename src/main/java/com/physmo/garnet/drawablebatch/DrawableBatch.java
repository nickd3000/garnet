package com.physmo.garnet.drawablebatch;

import com.physmo.garnet.graphics.Graphics;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static org.lwjgl.opengl.GL11.*;

public class DrawableBatch {
    final List<DrawableElement> elements;

    public DrawableBatch() {
        elements = new ArrayList<>();
    }

    public List<DrawableElement> getElements() {
        return elements;
    }

    public void add(DrawableElement batchElement) {
        elements.add(batchElement);
    }

    public int size() {
        return elements.size();
    }

    public void render(Graphics graphics) {
        List<DrawableElement> orderedList = elements.stream().sorted(Comparator.comparingInt(DrawableElement::getDrawOrder)).collect(Collectors.toList());

        glEnable(GL_TEXTURE_2D);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        for (DrawableElement de : orderedList) {
            //System.out.println("scale "+de.getScale());
            applyClipRectIfRequired(graphics, de);
            graphics.bindTexture(de.getTextureId());
            de.render(graphics);
        }
    }

    public void applyClipRectIfRequired(Graphics graphics, DrawableElement de) {

        graphics._activateClipRect(de.getCamera());

    }

    public void clear() {
        elements.clear();
    }


}
