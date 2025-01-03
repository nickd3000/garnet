package com.physmo.garnet.drawablebatch;


import com.physmo.garnet.graphics.Graphics;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glColor4fv;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glRotatef;
import static org.lwjgl.opengl.GL11.glScalef;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glTranslatef;
import static org.lwjgl.opengl.GL11.glVertex2f;

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
        reset();
    }

    public void reset() {
        rotated = false;
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
    public void render(Graphics graphics) {
        glEnable(GL_TEXTURE_2D);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        glColor4fv(colorFloats);
        if (rotated) {
            renderRotated(1.0f);
            return;
        }
        applyTranslation();


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

        removeTranslation();
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


    private void renderRotated(float textureScale) {
        glPushMatrix();

        double z = viewport.getZoom();

        float xo = (float) (viewport.getWindowX() - ((viewport.getX() - x) * z));
        float yo = (float) (viewport.getWindowY() - ((viewport.getY() - y) * z));

        glTranslatef(xo, yo, 0);
        glScalef((float) z, (float) z, 1);
        glRotatef(angle, 0f, 0f, 1.0f);

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
