package com.physmo.garnet.spritebatch;


import com.physmo.garnet.Utils;

import static org.lwjgl.opengl.GL11.*;

// TODO: sprite 2d should not have to handle scaling sprites, eg making them all x4 size etc.
public class Sprite2D implements BatchElement {

    public static int FLAG_COLOR = 1;
    public static int FLAG_ANGLE = 2;

    private int FLAGS = 0;

    private float textureScale = 1; //1.0f/255f; // TODO: get texture size
    private float x, y, w, h, tx, ty, tw, th, angle, _w, _h;

    private float[] color = new float[4];

    public Sprite2D() {
    }

    public Sprite2D(int x, int y, int w, int h, int tx, int ty, int tw, int th) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.tx = tx * textureScale;
        this.ty = ty * textureScale;
        this.tw = tw * textureScale;
        this.th = th * textureScale;
        this._w = this.w / 2;
        this._h = this.h / 2;
    }

    public Sprite2D(int x, int y, int w, int h, int tx, int ty, int tw, int th, float angle) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.tx = tx * textureScale;
        this.ty = ty * textureScale;
        this.tw = tw * textureScale;
        this.th = th * textureScale;
        this.angle = angle;
        this._w = this.w / 2;
        this._h = this.h / 2;
    }

    public static Sprite2D build(int x, int y, int w, int h, int tx, int ty, int tw, int th) {
        Sprite2D spr = new Sprite2D(x, y, w, h, tx, ty, tw, th);
        return spr;
    }

    // Build without texture coords.
    public static Sprite2D build(int x, int y, int w, int h) {
        Sprite2D spr = new Sprite2D(x, y, w, h, 0, 0, 0, 0);
        return spr;
    }

    // Set texture coords based on grid id.
    public Sprite2D setTile(int tileX, int tileY, int tileSize) {
        //int tx, int ty, int tw, int th
        tx = tileX * tileSize;
        ty = tileY * tileSize;
        tw = tileSize;
        th = tileSize;
        return this;
    }

    public Sprite2D setDrawPosition(int x, int y, int w, int h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        return this;
    }


    // Angle is 0-360
    public Sprite2D addAngle(float angle) {
        setFlag(FLAG_ANGLE);
        this.angle = angle;
        return this;
    }

    private void setFlag(int flag) {
        FLAGS |= flag;
    }

    public Sprite2D addColor(int rgb) {
        return addColor(Utils.rgbToFloat(rgb));
    }

    public Sprite2D addColor(float[] c) {
        return addColor(c[0], c[1], c[2], c[3]);
    }

    public Sprite2D addColor(float r, float g, float b, float a) {
        color[0] = r;
        color[1] = g;
        color[2] = b;
        color[3] = a;
        //color.setValues(r, g, b, a);
        setFlag(FLAG_COLOR);
        return this;
    }

    public Sprite2D addColor(float r, float g, float b) {
        return addColor(r, g, b, 1.0f);
    }

    @Override
    public void render(float textureScale, float outputScale) {
        //if ((FLAGS & FLAG_COLOR) != 0) {
        glColor4f(color[0], color[1], color[2], color[3]);
        //} else {
        //    glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        //}

        if ((FLAGS & FLAG_ANGLE) != 0) {
            renderRotated(textureScale);
            return;
        }

        float txs = tx * textureScale;
        float tys = ty * textureScale;
        float tws = tw * textureScale;
        float ths = th * textureScale;

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

    private void renderRotated(float textureScale) {
        glPushMatrix();

        //glLoadIdentity();
        //glTranslatef(-_w,-_h,0);
        glTranslatef(x + _w, y + _h, 0);
        glRotatef(angle, 0f, 0f, 1.0f);
        //glTranslatef(_w,_h,0);

        float txs = tx * textureScale;
        float tys = ty * textureScale;
        float tws = tw * textureScale;
        float ths = th * textureScale;

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

}
