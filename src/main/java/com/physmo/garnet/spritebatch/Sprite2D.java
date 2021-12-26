package com.physmo.garnet.spritebatch;

import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glRotatef;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glTranslatef;
import static org.lwjgl.opengl.GL11.glVertex2f;

// TODO: sprite 2d should not have to handle scaling sprites, eg making them all x4 size etc.
public class Sprite2D implements BatchElement {

    public static int FLAG_COLOR = 1;
    public static int FLAG_ANGLE = 2;

    private int FLAGS = 0;

    private float textureScale = 1; //1.0f/255f; // TODO: get texture size
    private float x, y, w, h, tx, ty, tw, th, angle, _w, _h;
    private float r, g, b;

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

    public Sprite2D setDrawPoisition(int x, int y, int w, int h) {
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

    public Sprite2D addColor(double r, double g, double b) {
        this.r = (float) r;
        this.g = (float) g;
        this.b = (float) b;
        setFlag(FLAG_COLOR);
        return this;
    }

    @Override
    public void render(float textureScale, float outputScale) {
        if ((FLAGS & FLAG_COLOR) != 0) {
            glColor3f(r, g, b);
        } else {
            glColor3f(1.0f, 1.0f, 1.0f);
        }

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
            glVertex2f(x*outputScale, y*outputScale);
            glTexCoord2f(txs + tws, tys);
            glVertex2f(x*outputScale + w*outputScale, y*outputScale);
            glTexCoord2f(txs + tws, tys + ths);
            glVertex2f(x*outputScale + w*outputScale, y*outputScale + h*outputScale);
            glTexCoord2f(txs, tys + ths);
            glVertex2f(x*outputScale, y*outputScale + h*outputScale);
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
