package com.physmo.garnet.spritebatch;

import static org.lwjgl.opengl.GL11.*;

public class Sprite2D implements BatchElement {

    public static int FLAG_COLOR = 1;
    public static int FLAG_ANGLE = 2;

    private int FLAGS=0;

    private float scale = 1;
    private float textureScale = 1; //1.0f/255f; // TODO: get texture size
    private float x,y,w,h,tx,ty,tw,th,angle,_w,_h;
    private float r,g,b;

    public static Sprite2D build(int x, int y, int w, int h, int tx, int ty, int tw, int th) {
        Sprite2D spr = new Sprite2D(x,y,w,h,tx,ty,tw,th);
        return spr;
    }

    // Angle is 0-360
    public Sprite2D addAngle(float angle) {
        setFlag(FLAG_ANGLE);
        this.angle = angle;
        return this;
    }

    public Sprite2D addColor(double r, double g, double b) {
        this.r = (float)r;
        this.g = (float)g;
        this.b = (float)b;
        setFlag(FLAG_COLOR);
        return this;
    }

    private void setFlag(int flag) {
        FLAGS |= flag;
    }

    public Sprite2D(int x, int y, int w, int h, int tx, int ty, int tw, int th) {
        this.x=x*scale;
        this.y=y*scale;
        this.w=w*scale;
        this.h=h*scale;
        this.tx=tx*textureScale;
        this.ty=ty*textureScale;
        this.tw=tw*textureScale;
        this.th=th*textureScale;
        this._w = this.w/2;
        this._h = this.h/2;
    }

    public Sprite2D(int x, int y, int w, int h, int tx, int ty, int tw, int th, float angle) {
        this.x=x*scale;
        this.y=y*scale;
        this.w=w*scale;
        this.h=h*scale;
        this.tx=tx*textureScale;
        this.ty=ty*textureScale;
        this.tw=tw*textureScale;
        this.th=th*textureScale;
        this.angle = angle;
        this._w = this.w/2;
        this._h = this.h/2;
    }

    @Override
    public void render(float textureScale) {
        if ((FLAGS&FLAG_COLOR)!=0) {
            glColor3f(r,g,b);
        }

        // todo: reset to white if no colour flag?

        // todo: use angle flag.
        if ((FLAGS&FLAG_ANGLE)!=0) {
            renderRotated(textureScale);
            return;
        }

        float txs=tx*textureScale;
        float tys=ty*textureScale;
        float tws=tw*textureScale;
        float ths=th*textureScale;

        glBegin(GL_QUADS);
        {
            glTexCoord2f(txs, tys);
            glVertex2f(x, y);
            glTexCoord2f(txs+tws, tys);
            glVertex2f(x+w, y);
            glTexCoord2f(txs+tws, tys+ths);
            glVertex2f(x+w, y+h);
            glTexCoord2f(txs, tys+ths);
            glVertex2f(x, y+h);
        }
        glEnd();
    }

    private void renderRotated(float textureScale) {
        glPushMatrix();

        //glLoadIdentity();
        //glTranslatef(-_w,-_h,0);
        glTranslatef(x+_w,y+_h,0);
        glRotatef(angle,0f,0f,1.0f);
        //glTranslatef(_w,_h,0);


        glBegin(GL_QUADS);
        {
            glTexCoord2f(tx, ty);
            glVertex2f(-_w, -_h);
            glTexCoord2f(tx+tw, ty);
            glVertex2f(_w, -_h);
            glTexCoord2f(tx+tw, ty+th);
            glVertex2f(_w, _h);
            glTexCoord2f(tx, ty+th);
            glVertex2f(-_w, _h);

        }
        glEnd();
        glPopMatrix();
    }

}
