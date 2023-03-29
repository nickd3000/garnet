package com.physmo.garnet.spritebatch;

import com.physmo.garnet.graphics.Graphics;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glEnable;

public class DrawableBatch {
    final List<DrawableElement> elements;

    public DrawableBatch() {
        elements = new ArrayList<>();
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
            applyClipRectIfRequired(graphics, de);
            graphics.bindTexture(de.getTextureId());
            de.render(graphics);
        }
    }

    public void applyClipRectIfRequired(Graphics graphics, DrawableElement de) {

        graphics._activateClipRect(de.getClipRect());

    }

    public void clear() {
        elements.clear();
    }


}
