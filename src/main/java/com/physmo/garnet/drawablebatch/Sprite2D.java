package com.physmo.garnet.drawablebatch;


import com.physmo.garnet.graphics.Graphics;

import static org.lwjgl.opengl.GL11.GL_MODULATE;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_REPLACE;
import static org.lwjgl.opengl.GL11.GL_TEXTURE;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_ENV;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_ENV_MODE;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor4fv;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glRotatef;
import static org.lwjgl.opengl.GL11.glScalef;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glTexEnvi;
import static org.lwjgl.opengl.GL11.glTranslatef;
import static org.lwjgl.opengl.GL11.glVertex2f;
import static org.lwjgl.opengl.GL13.GL_COMBINE;
import static org.lwjgl.opengl.GL13.GL_COMBINE_ALPHA;
import static org.lwjgl.opengl.GL13.GL_COMBINE_RGB;
import static org.lwjgl.opengl.GL13.GL_PRIMARY_COLOR;
import static org.lwjgl.opengl.GL13.GL_SOURCE0_ALPHA;
import static org.lwjgl.opengl.GL13.GL_SOURCE0_RGB;
import static org.lwjgl.opengl.GL13.GL_SOURCE1_ALPHA;

// TODO: sprite 2d should not have to handle scaling sprites, eg making them all x4 size etc.
public class Sprite2D extends DrawableElement {

    int textureId = 0;
    float textureScaleX;
    float textureScaleY;
    boolean rotated = false;
    private float x, y, w, h, tx, ty, tw, th, angle, _w, _h;

    public Sprite2D() {
        reset();
    }

    public void reset() {
        rotated = false;
        setColorOverride(false);
        clearShader();
    }

    public void setCoords(float[] vertexCoords, float[] texCoords) {
        rotated = false;
        this.x = vertexCoords[0];
        this.y = vertexCoords[1];
        this.w = vertexCoords[2] - vertexCoords[0];
        this.h = vertexCoords[7] - vertexCoords[1];
        this.tx = texCoords[0];
        this.ty = texCoords[1];
        this.tw = texCoords[2] - texCoords[0];
        this.th = texCoords[7] - texCoords[1];
        this._w = this.w / 2;
        this._h = this.h / 2;
    }

    public void setCoords(int x, int y, int w, int h, int tx, int ty, int tw, int th) {
        rotated = false;
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.tx = tx;
        this.ty = ty;
        this.tw = tw;
        this.th = th;
        this._w = this.w / 2;
        this._h = this.h / 2;
    }

    public void setCoords(int x, int y, int w, int h, int tx, int ty, int tw, int th, float angle) {
        rotated = (angle != 0);
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.tx = tx;
        this.ty = ty;
        this.tw = tw;
        this.th = th;
        this.angle = angle;
        this._w = this.w / 2;
        this._h = this.h / 2;
    }

    // Angle is 0-360
    public Sprite2D addAngle(float angle) {
        rotated = true;
        this.angle = angle;
        return this;
    }



    @Override
    public void render(Graphics graphics) {
        glEnable(GL_TEXTURE_2D);

        glColor4fv(colorFloats);
        if (rotated) {
            renderRotated(graphics, 1.0f);
            return;
        }
        pushViewportTransform(graphics);

        if (isColorOverride()) applyColorOverride();

        float txs = tx * textureScaleX;
        float tys = ty * textureScaleY;
        float tws = tw * textureScaleX;
        float ths = th * textureScaleY;

        glBegin(GL_QUADS);
        {
            glTexCoord2f(txs, tys);
            glVertex2f(x, y);
            glTexCoord2f(txs + tws, tys);
            glVertex2f(x + w, y);
            glTexCoord2f(txs + tws, tys + ths);
            glVertex2f(x + w, y + h);
            glTexCoord2f(txs, tys + ths);
            glVertex2f(x, y + h);
        }
        glEnd();

        if (isColorOverride()) removeColorOverride();

        popViewportTransform();
    }

    @Override
    public int getTextureId() {
        return textureId;
    }

    public void setTextureId(int textureId) {
        this.textureId = textureId;
    }

    @Override
    public int getType() {
        return SPRITE;
    }

    private void renderRotated(Graphics graphics, float textureScale) {
        glPushMatrix();

        double z = viewport.getZoom();

        float xo, yo;
        xo = (float) (viewport.getWindowX() - ((viewport.getX() - x) * z));
        yo = (float) (viewport.getWindowY() - ((viewport.getY() - y) * z));

        glTranslatef(xo, yo, 0);
        glScalef((float) z, (float) z, 1);
        glRotatef(angle, 0f, 0f, 1.0f);

        float txs = tx * textureScaleX;
        float tys = ty * textureScaleY;
        float tws = tw * textureScaleX;
        float ths = th * textureScaleY;

        if (isColorOverride()) applyColorOverride();

        glBegin(GL_QUADS);
        {
            glTexCoord2f(txs, tys);
            glVertex2f(-_w, -_h);
            glTexCoord2f(txs + tws, tys);
            glVertex2f(_w, -_h);
            glTexCoord2f(txs + tws, tys + ths);
            glVertex2f(_w, _h);
            glTexCoord2f(txs, tys + ths);
            glVertex2f(-_w, _h);

        }

        glEnd();
        if (isColorOverride()) removeColorOverride();
        glPopMatrix();
    }

    private static void applyColorOverride() {
        glTexEnvi(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, GL_COMBINE);
        glTexEnvi(GL_TEXTURE_ENV, GL_COMBINE_RGB, GL_REPLACE);
        glTexEnvi(GL_TEXTURE_ENV, GL_SOURCE0_RGB, GL_PRIMARY_COLOR);
        glTexEnvi(GL_TEXTURE_ENV, GL_COMBINE_ALPHA, GL_MODULATE);
        glTexEnvi(GL_TEXTURE_ENV, GL_SOURCE0_ALPHA, GL_TEXTURE);
        glTexEnvi(GL_TEXTURE_ENV, GL_SOURCE1_ALPHA, GL_PRIMARY_COLOR);
    }

    private static void removeColorOverride() {
        glTexEnvi(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, GL_MODULATE);
    }

    public void setTextureScale(float x, float y) {
        textureScaleX = x;
        textureScaleY = y;
    }

    @Override
    public String toString() {
        return "Sprite2D{" +
                "textureId=" + textureId +
                ", textureScaleX=" + textureScaleX +
                ", textureScaleY=" + textureScaleY +
                ", x=" + x +
                ", y=" + y +
                ", w=" + w +
                ", h=" + h +
                ", tx=" + tx +
                ", ty=" + ty +
                ", tw=" + tw +
                ", th=" + th +
                ", angle=" + angle +
                ", _w=" + _w +
                ", _h=" + _h +
                ", color=" + color +
                '}';
    }
}
