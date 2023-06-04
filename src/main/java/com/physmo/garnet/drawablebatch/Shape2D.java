package com.physmo.garnet.drawablebatch;

import com.physmo.garnet.graphics.Graphics;

import static org.lwjgl.opengl.GL11.*;

public class Shape2D extends DrawableElement {

    float[] coords;
    float[] midPoint = new float[2];


    public Shape2D(float[] coords) {
        int length = coords.length;
        this.coords = new float[length];
        System.arraycopy(coords, 0, this.coords, 0, length);
        calculateMidPoint(coords);
    }

    private void calculateMidPoint(float[] coords) {
        midPoint[0] = 0;
        midPoint[1] = 0;
        int length = coords.length;
        for (int i = 0; i < length; i += 2) {
            midPoint[0] += coords[i];
            midPoint[1] += coords[i + 1];
        }
        midPoint[0] /= (length / 2.0f);
        midPoint[1] /= (length / 2.0f);
    }

    @Override
    void render(Graphics graphics) {
        glDisable(GL_TEXTURE_2D);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        glColor4fv(colorFloats);

        //glBegin(GL_LINES);
        glBegin(GL_TRIANGLE_FAN);
        // Add mid-point
        glVertex2f(midPoint[0], midPoint[1]);
        //
        for (int i = 0; i < coords.length; i += 2) {
            glVertex2f(coords[i], coords[i + 1]);
        }
        glVertex2f(coords[0], coords[1]);
        glEnd();
    }

    @Override
    int getTextureId() {
        return 0;
    }

    @Override
    boolean hasTexture() {
        return false;
    }

    @Override
    int getType() {
        return SHAPE;
    }
}
