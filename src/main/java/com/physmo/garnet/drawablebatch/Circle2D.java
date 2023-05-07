package com.physmo.garnet.drawablebatch;

import com.physmo.garnet.Utils;
import com.physmo.garnet.graphics.Graphics;

import static org.lwjgl.opengl.GL11.*;

public class Circle2D implements DrawableElement {

    float x;
    float y;
    float width;
    float height;

    int drawOrder;
    int color;

    int numSegments;

    public Circle2D(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        numSegments = (int) (Math.max(width, height) / 2);
        if (numSegments < 5) numSegments = 5;
    }

    public void addColor(int rgb) {
        this.color = rgb;
    }

    @Override
    public void render(Graphics graphics) {
        glDisable(GL_TEXTURE_2D);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        glColor4fv(Utils.rgbToFloat(color));
        glBegin(GL_LINE_LOOP);

        float[] coords = generatePoints();
        for (int i = 0; i < coords.length / 2; i++) {
            glVertex2f(coords[i * 2], coords[(i * 2) + 1]);
        }

        glEnd();
    }

    public float[] generatePoints() {
        float[] coords = new float[(numSegments) * 2];
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
    public int getDrawOrder() {
        return drawOrder;
    }

    @Override
    public void setDrawOrder(int drawOrder) {
        this.drawOrder = drawOrder;

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
    public int getClipRect() {
        return 0;
    }

    @Override
    public int getType() {
        return CIRCLE;
    }

}
