package com.physmo.garnet.drawablebatch;

import com.physmo.garnet.graphics.Graphics;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TRIANGLE_STRIP;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor4fv;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glVertex2f;

public class EllipseStroke2D extends DrawableElement {

    private final float[] coords;

    public EllipseStroke2D(float x, float y, float radiusX, float radiusY, float thickness) {
        int segments = StrokeGeometry.calculateEllipseSegments(radiusX + thickness, radiusY + thickness);
        coords = StrokeGeometry.createEllipseRingStrip(x, y, radiusX, radiusY, thickness, segments);
    }

    @Override
    void render(Graphics graphics) {
        if (coords.length == 0) return;

        glDisable(GL_TEXTURE_2D);
        glColor4fv(colorFloats);
        pushViewportTransform(graphics);

        glBegin(GL_TRIANGLE_STRIP);
        for (int i = 0; i < coords.length; i += 2) {
            glVertex2f(coords[i], coords[i + 1]);
        }
        glEnd();

        popViewportTransform();
    }

    @Override
    int getTextureId() {
        return 0;
    }

    @Override
    public int getType() {
        return OTHER;
    }
}
