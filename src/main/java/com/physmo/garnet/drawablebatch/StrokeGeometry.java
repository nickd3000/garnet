
package com.physmo.garnet.drawablebatch;

public final class StrokeGeometry {

    private StrokeGeometry() {
    }

    public static float[] createLineQuad(float x1, float y1, float x2, float y2, float thickness) {
        float dx = x2 - x1;
        float dy = y2 - y1;
        float length = (float) Math.sqrt((dx * dx) + (dy * dy));
        if (length == 0 || thickness <= 0) return new float[0];

        float halfThickness = thickness / 2.0f;
        float nx = (-dy / length) * halfThickness;
        float ny = (dx / length) * halfThickness;

        return new float[]{
                x1 + nx, y1 + ny,
                x2 + nx, y2 + ny,
                x2 - nx, y2 - ny,
                x1 - nx, y1 - ny
        };
    }

    public static float[] createEllipseRingStrip(float x, float y, float radiusX, float radiusY, float thickness, int segments) {
        if (thickness <= 0 || segments < 3) return new float[0];

        float outerX = Math.max(0, radiusX + (thickness / 2.0f));
        float outerY = Math.max(0, radiusY + (thickness / 2.0f));
        float innerX = Math.max(0, radiusX - (thickness / 2.0f));
        float innerY = Math.max(0, radiusY - (thickness / 2.0f));
        float[] coords = new float[(segments + 1) * 4];

        int coordIndex = 0;
        float angleStep = (float) ((Math.PI * 2.0) / segments);
        for (int i = 0; i <= segments; i++) {
            float angle = angleStep * i;
            float sin = (float) Math.sin(angle);
            float cos = (float) Math.cos(angle);

            coords[coordIndex++] = x + (sin * outerX);
            coords[coordIndex++] = y + (cos * outerY);
            coords[coordIndex++] = x + (sin * innerX);
            coords[coordIndex++] = y + (cos * innerY);
        }

        return coords;
    }

    public static int calculateEllipseSegments(float radiusX, float radiusY) {
        int numSegments = (int) (Math.max(radiusX, radiusY) / 2);
        numSegments = (int) (numSegments * 1.5);
        if (numSegments < 5) numSegments = 5;
        return numSegments;
    }
}
