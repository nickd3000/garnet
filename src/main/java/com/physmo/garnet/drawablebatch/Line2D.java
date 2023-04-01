package com.physmo.garnet.drawablebatch;

import com.physmo.garnet.Utils;
import com.physmo.garnet.graphics.Graphics;

import static org.lwjgl.opengl.GL11.*;

public class Line2D implements DrawableElement {

    int drawOrder;
    float[] coords = new float[4];
    int color;

    public Line2D(float x1, float y1, float x2, float y2) {
        coords[0] = x1;
        coords[1] = y1;
        coords[2] = x2;
        coords[3] = y2;
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
        glBegin(GL_LINES);
        glVertex2f(coords[0], coords[1]);
        glVertex2f(coords[2], coords[3]);
        glEnd();
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
}
