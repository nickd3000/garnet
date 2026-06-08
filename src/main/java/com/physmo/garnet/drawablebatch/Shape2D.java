package com.physmo.garnet.drawablebatch;

import com.physmo.garnet.renderer.BatchMesh;
import com.physmo.garnet.renderer.RenderCommand;
import com.physmo.garnet.renderer.ShapeGeometry;

public class Shape2D extends DrawableElement {

    float[] coords;
    float[] midPoint = new float[2];


    public Shape2D(float[] coords) {
        int length = coords.length;
        this.coords = new float[length];
        System.arraycopy(coords, 0, this.coords, 0, length);
        calculateMidPoint(coords);
    }

    private void calculateMidPoint(float[] coords) {
        midPoint[0] = 0;
        midPoint[1] = 0;
        int length = coords.length;
        for (int i = 0; i < length; i += 2) {
            midPoint[0] += coords[i];
            midPoint[1] += coords[i + 1];
        }
        midPoint[0] /= (length / 2.0f);
        midPoint[1] /= (length / 2.0f);
    }

    @Override
    public RenderCommand appendToBatch(BatchMesh mesh) {
        return ShapeGeometry.appendFilledConvexPolygon(mesh, viewport, coords, color);
    }

    @Override
    public int getMaterialFlags() {
        return 0;
    }

    @Override
    int getTextureId() {
        return 0;
    }

    @Override
    public int getType() {
        return SHAPE;
    }
}
