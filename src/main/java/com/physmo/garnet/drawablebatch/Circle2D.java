package com.physmo.garnet.drawablebatch;

import com.physmo.garnet.graphics.Graphics;

import static org.lwjgl.opengl.GL11.GL_LINE_LOOP;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TRIANGLE_FAN;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor4fv;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glVertex2f;

public class Circle2D extends DrawableElement {

    float x;
    float y;
    float width;
    float height;
    double detail = 1.5;
    int numSegments;
    boolean filled = false;
    private final float[] coords;

    public Circle2D(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        numSegments = (int) (Math.max(width, height) / 2);
        numSegments = (int) (numSegments * detail);
        if (numSegments < 5) numSegments = 5;
        coords = new float[numSegments * 2];
    }

    @Override
    public void render(Graphics graphics) {
        glDisable(GL_TEXTURE_2D);

        glColor4fv(colorFloats);
        pushViewportTransform();

        generatePoints();

        if (!filled) {
            glBegin(GL_LINE_LOOP);
            for (int i = 0; i < coords.length / 2; i++) {
                glVertex2f(coords[i * 2], coords[(i * 2) + 1]);
            }
            glEnd();
        } else {
            glBegin(GL_TRIANGLE_FAN);
            // Add mid-point
            glVertex2f(x, y);
            for (int i = 0; i < coords.length / 2; i++) {
                glVertex2f(coords[i * 2], coords[(i * 2) + 1]);
            }
            glVertex2f(coords[0], coords[1]);
            glEnd();
        }

        popViewportTransform();

    }

    public float[] generatePoints() {
        int coordIndex = 0;
        float a = (float) (Math.PI / numSegments) * 2;
        float xx, yy;

        for (int i = 0; i < numSegments; i++) {
            xx = (float) (Math.sin(a * i) * width);
            yy = (float) (Math.cos(a * i) * height);
            coords[coordIndex++] = x + xx;
            coords[coordIndex++] = y + yy;
        }
        return coords;
    }

    @Override
    public int getTextureId() {
        return 0;
    }

    @Override
    public int getType() {
        return CIRCLE;
    }

    public void setFilled(boolean val) {
        filled = val;
    }
}
