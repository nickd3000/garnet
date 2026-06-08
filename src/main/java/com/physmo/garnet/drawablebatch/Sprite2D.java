package com.physmo.garnet.drawablebatch;

import com.physmo.garnet.renderer.BatchMesh;
import com.physmo.garnet.renderer.BatchVertex;
import com.physmo.garnet.renderer.RenderCommand;
import com.physmo.garnet.renderer.SpriteGeometry;
import com.physmo.garnet.renderer.TextureRegion;

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
        resetCommonState();
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

    public void setRegionCoords(float x, float y, float w, float h, TextureRegion region) {
        rotated = false;
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.tx = region.u0();
        this.ty = region.v0();
        this.tw = region.uWidth();
        this.th = region.vHeight();
        this.textureScaleX = 1.0f;
        this.textureScaleY = 1.0f;
        this.textureId = region.textureId();
        this._w = this.w / 2;
        this._h = this.h / 2;
    }

    public void setRegionCoords(float[] vertexCoords, float[] texCoords, TextureRegion region) {
        rotated = false;
        this.x = vertexCoords[0];
        this.y = vertexCoords[1];
        this.w = vertexCoords[2] - vertexCoords[0];
        this.h = vertexCoords[7] - vertexCoords[1];
        this.tx = (region.x() + texCoords[0]) / (float) region.textureWidth();
        this.ty = (region.y() + texCoords[1]) / (float) region.textureHeight();
        this.tw = (texCoords[2] - texCoords[0]) / (float) region.textureWidth();
        this.th = (texCoords[7] - texCoords[1]) / (float) region.textureHeight();
        this.textureScaleX = 1.0f;
        this.textureScaleY = 1.0f;
        this.textureId = region.textureId();
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
    public RenderCommand appendToBatch(BatchMesh mesh) {
        float txs = tx * textureScaleX;
        float tys = ty * textureScaleY;
        float tws = tw * textureScaleX;
        float ths = th * textureScaleY;

        if (rotated) {
            return SpriteGeometry.appendRotatedQuad(
                    mesh, viewport,
                    x, y, w, h,
                    txs, tys, tws, ths,
                    angle,
                    color,
                    isColorOverride());
        }

        return SpriteGeometry.appendQuad(
                mesh, viewport,
                x, y, w, h,
                txs, tys, tws, ths,
                color,
                isColorOverride());
    }

    @Override
    public int getMaterialFlags() {
        return BatchVertex.FLAG_TEXTURED | (isColorOverride() ? BatchVertex.FLAG_COLOR_OVERRIDE : 0);
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
