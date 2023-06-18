package com.physmo.garnet.drawablebatch;


import com.physmo.garnet.graphics.Graphics;

import static org.lwjgl.opengl.GL11.*;

// TODO: sprite 2d should not have to handle scaling sprites, eg making them all x4 size etc.
public class Sprite2D extends DrawableElement {

    int textureId = 0;
    float textureScaleX;
    float textureScaleY;
    boolean rotated = false;
    private float x, y, w, h, tx, ty, tw, th, angle, _w, _h;

    static int creationCount = 0;

    public Sprite2D() {
        creationCount++;
        System.out.println("Sprite2D construction count: " + creationCount);
        reset();
    }

    public void reset() {
        rotated = false;
    }

    public void setValues(float[] vertexCoords, float[] texCoords) {
        float[] v = vertexCoords;
        float[] t = texCoords;
        rotated = false;
        this.x = v[0];
        this.y = v[1];
        this.w = v[2] - v[0];
        this.h = v[7] - v[1];
        this.tx = t[0];
        this.ty = t[1];
        this.tw = t[2] - t[0];
        this.th = t[7] - t[1];
        this._w = this.w / 2;
        this._h = this.h / 2;
    }

    public void setValues(int x, int y, int w, int h, int tx, int ty, int tw, int th) {
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

    public void setValues(int x, int y, int w, int h, int tx, int ty, int tw, int th, float angle) {
        rotated = false;
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

    @Override
    public void render(Graphics graphics) {
        glEnable(GL_TEXTURE_2D);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        glColor4fv(colorFloats);



        float outputScale = 1;
        float txs = tx * textureScaleX;
        float tys = ty * textureScaleY;
        float tws = tw * textureScaleX;
        float ths = th * textureScaleY;

        glBegin(GL_QUADS);
        {
            glTexCoord2f(txs, tys);
            glVertex2f(x * outputScale, y * outputScale);
            glTexCoord2f(txs + tws, tys);
            glVertex2f(x * outputScale + w * outputScale, y * outputScale);
            glTexCoord2f(txs + tws, tys + ths);
            glVertex2f(x * outputScale + w * outputScale, y * outputScale + h * outputScale);
            glTexCoord2f(txs, tys + ths);
            glVertex2f(x * outputScale, y * outputScale + h * outputScale);
        }
        glEnd();

    }

    @Override
    public int getTextureId() {
        return textureId;
    }

    public void setTextureId(int textureId) {
        this.textureId = textureId;
    }

    @Override
    public boolean hasTexture() {
        return true;
    }

    @Override
    public int getType() {
        return SPRITE;
    }

//    public void applyTranslation() {
//        glPushMatrix();
//        float xo = (float) (camera.getWindowX() - (camera.getX() * scale));
//        float yo = (float) (camera.getWindowY() - (camera.getY() * scale));
//
//        glTranslatef(xo, yo, 0);
//        glScalef((float) scale, (float) scale, 1);
//    }

    private void renderRotated(float textureScale) {
        glPushMatrix();

        glTranslatef(x + _w, y + _h, 0);

        glRotatef(angle, 0f, 0f, 1.0f);

        double z = camera.getZoom();

        // new
        float xo = (float) (camera.getWindowX() - (camera.getX() * z));
        float yo = (float) (camera.getWindowY() - (camera.getY() * z));

        glTranslatef(xo, yo, 0);
        glScalef((float) z, (float) z, 1);
        // new

        float txs = tx * textureScaleX;
        float tys = ty * textureScaleY;
        float tws = tw * textureScaleX;
        float ths = th * textureScaleY;

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
        glPopMatrix();
    }

    public void setTextureScale(float x, float y) {
        textureScaleX = x;
        textureScaleY = y;
    }
}
