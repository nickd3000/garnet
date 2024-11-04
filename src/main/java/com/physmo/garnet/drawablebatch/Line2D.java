package com.physmo.garnet.drawablebatch;

import com.physmo.garnet.graphics.Graphics;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_LINES;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glColor4fv;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glVertex2f;

/**
 * The Line2D class represents a drawable 2D line that can be rendered on the screen.
 * It extends the DrawableElement class and provides implementation for rendering
 * a line between two points using OpenGL functions.
 */
public class Line2D extends DrawableElement {

    float[] coords = new float[4];

    public Line2D() {
    }

    public Line2D(float x1, float y1, float x2, float y2) {
        coords[0] = x1;
        coords[1] = y1;
        coords[2] = x2;
        coords[3] = y2;
    }

    public void set(float x1, float y1, float x2, float y2) {
        coords[0] = x1;
        coords[1] = y1;
        coords[2] = x2;
        coords[3] = y2;
    }

    @Override
    public void render(Graphics graphics) {
        glDisable(GL_TEXTURE_2D);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        glColor4fv(colorFloats);

        applyTranslation();

        glBegin(GL_LINES);
        glVertex2f(coords[0], coords[1]);
        glVertex2f(coords[2], coords[3]);
        glEnd();

        removeTranslation();
    }

    @Override
    public int getTextureId() {
        return 0;
    }

    @Override
    public boolean hasTexture() {
        return false;
    }

    @Override
    public int getType() {
        return LINE;
    }
}
