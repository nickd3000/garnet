package com.physmo.garnet.renderer;

import com.physmo.garnet.ColorUtils;
import com.physmo.garnet.graphics.Viewport;

/**
 * Builds sprite quads using the same coordinate semantics as Sprite2D.
 */
public final class SpriteGeometry {
    private SpriteGeometry() {
    }

    public static RenderCommand appendQuad(
            BatchMesh mesh,
            Viewport viewport,
            float x,
            float y,
            float width,
            float height,
            float u,
            float v,
            float uvWidth,
            float uvHeight,
            int rgba,
            boolean colorOverride
    ) {
        int firstVertex = mesh.vertexCount();
        int firstIndex = mesh.indexCount();
        int flags = BatchVertex.FLAG_TEXTURED | (colorOverride ? BatchVertex.FLAG_COLOR_OVERRIDE : 0);
        float[] color = ColorUtils.rgbToFloat(rgba);

        int topLeft = addVertex(mesh, viewport, x, y, u, v, color, flags);
        int topRight = addVertex(mesh, viewport, x + width, y, u + uvWidth, v, color, flags);
        int bottomRight = addVertex(mesh, viewport, x + width, y + height, u + uvWidth, v + uvHeight, color, flags);
        int bottomLeft = addVertex(mesh, viewport, x, y + height, u, v + uvHeight, color, flags);
        mesh.addQuadIndices(topLeft, topRight, bottomRight, bottomLeft);

        return new RenderCommand(firstVertex, 4, firstIndex, 6);
    }

    private static int addVertex(BatchMesh mesh, Viewport viewport, float x, float y, float u, float v, float[] color, int flags) {
        return mesh.addVertex(new BatchVertex(
                ViewportTransform.screenX(viewport, x),
                ViewportTransform.screenY(viewport, y),
                u,
                v,
                color[0],
                color[1],
                color[2],
                color[3],
                flags));
    }

    public static RenderCommand appendRotatedQuad(
            BatchMesh mesh,
            Viewport viewport,
            float centerX,
            float centerY,
            float width,
            float height,
            float u,
            float v,
            float uvWidth,
            float uvHeight,
            float angleDegrees,
            int rgba,
            boolean colorOverride
    ) {
        int firstVertex = mesh.vertexCount();
        int firstIndex = mesh.indexCount();
        int flags = BatchVertex.FLAG_TEXTURED | (colorOverride ? BatchVertex.FLAG_COLOR_OVERRIDE : 0);
        float[] color = ColorUtils.rgbToFloat(rgba);
        float halfWidth = width / 2.0f;
        float halfHeight = height / 2.0f;
        double radians = Math.toRadians(angleDegrees);
        float sin = (float) Math.sin(radians);
        float cos = (float) Math.cos(radians);

        int topLeft = addRotatedVertex(mesh, viewport, centerX, centerY, -halfWidth, -halfHeight, cos, sin, u, v, color, flags);
        int topRight = addRotatedVertex(mesh, viewport, centerX, centerY, halfWidth, -halfHeight, cos, sin, u + uvWidth, v, color, flags);
        int bottomRight = addRotatedVertex(mesh, viewport, centerX, centerY, halfWidth, halfHeight, cos, sin, u + uvWidth, v + uvHeight, color, flags);
        int bottomLeft = addRotatedVertex(mesh, viewport, centerX, centerY, -halfWidth, halfHeight, cos, sin, u, v + uvHeight, color, flags);
        mesh.addQuadIndices(topLeft, topRight, bottomRight, bottomLeft);

        return new RenderCommand(firstVertex, 4, firstIndex, 6);
    }

    private static int addRotatedVertex(
            BatchMesh mesh,
            Viewport viewport,
            float centerX,
            float centerY,
            float localX,
            float localY,
            float cos,
            float sin,
            float u,
            float v,
            float[] color,
            int flags
    ) {
        float worldX = centerX + (localX * cos) - (localY * sin);
        float worldY = centerY + (localX * sin) + (localY * cos);
        return addVertex(mesh, viewport, worldX, worldY, u, v, color, flags);
    }
}
