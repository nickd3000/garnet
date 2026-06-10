package com.physmo.garnet.drawablebatch;

import com.physmo.garnet.ColorUtils;
import com.physmo.garnet.graphics.Viewport;
import com.physmo.garnet.renderer.BatchMesh;
import com.physmo.garnet.renderer.BatchRenderPlan;
import com.physmo.garnet.renderer.BatchVertex;
import com.physmo.garnet.renderer.RenderCommand;
import com.physmo.garnet.renderer.TextureRegion;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DrawableBatchRenderPlanTest {

    @Test
    void buildRenderPlanSortsByDrawOrderBeforeAppendingGeometry() {
        Viewport viewport = new Viewport(1, 320, 200);
        DrawableBatch batch = new DrawableBatch();
        Sprite2D later = sprite(viewport, 100, 0, 10);
        Sprite2D earlier = sprite(viewport, 20, 0, 1);

        batch.add(later);
        batch.add(earlier);

        BatchRenderPlan plan = batch.buildRenderPlan();

        assertEquals(2, plan.commands().size());
        assertEquals(8, plan.mesh().vertexCount());
        assertEquals(12, plan.mesh().indexCount());
        assertEquals(1, plan.runs().size());
        assertEquals(2, plan.runs().get(0).commandCount());
        assertEquals(20, plan.mesh().vertices().get(0).x(), 0.0001f);
        assertEquals(100, plan.mesh().vertices().get(4).x(), 0.0001f);
    }

    private static Sprite2D sprite(Viewport viewport, int x, int y, int drawOrder) {
        Sprite2D sprite = new Sprite2D();
        sprite.setCoords(x, y, 10, 10, 0, 0, 10, 10);
        sprite.setTextureId(7);
        sprite.setTextureScale(0.1f, 0.1f);
        sprite.setCommonValues(viewport, drawOrder, ColorUtils.WHITE);
        return sprite;
    }

    @Test
    void buildRenderPlanSplitsRunsWhenMaterialFlagsChange() {
        Viewport viewport = new Viewport(1, 320, 200);
        DrawableBatch batch = new DrawableBatch();
        Sprite2D normal = sprite(viewport, 0, 0, 0);
        Sprite2D colorOverride = sprite(viewport, 20, 0, 1);
        colorOverride.setColorOverride(true);

        batch.add(normal);
        batch.add(colorOverride);

        BatchRenderPlan plan = batch.buildRenderPlan();

        assertEquals(2, plan.runs().size());
        assertEquals(BatchVertex.FLAG_TEXTURED, plan.states().get(0).materialFlags());
        assertEquals(BatchVertex.FLAG_TEXTURED | BatchVertex.FLAG_COLOR_OVERRIDE, plan.states().get(1).materialFlags());
    }

    @Test
    void atlasBackedSpritesOnSamePageCollapseToOneRenderRun() {
        Viewport viewport = new Viewport(1, 320, 200);
        DrawableBatch batch = new DrawableBatch();
        TextureRegion firstAtlasRegion = new TextureRegion(100, 1, 1, 16, 16, 256, 256, true);
        TextureRegion secondAtlasRegion = new TextureRegion(100, 18, 1, 16, 16, 256, 256, true);

        batch.add(sprite(viewport, 0, 0, 0, firstAtlasRegion));
        batch.add(sprite(viewport, 20, 0, 0, secondAtlasRegion));

        BatchRenderPlan plan = batch.buildRenderPlan();

        assertEquals(1, plan.runs().size());
        assertEquals(2, plan.runs().get(0).commandCount());
        assertEquals(100, plan.runs().get(0).stateKey().textureId());
    }

    private static Sprite2D sprite(Viewport viewport, int x, int y, int drawOrder, TextureRegion region) {
        Sprite2D sprite = new Sprite2D();
        sprite.setRegionCoords(x, y, region.width(), region.height(), region);
        sprite.setCommonValues(viewport, drawOrder, ColorUtils.WHITE);
        return sprite;
    }

    @Test
    void rawSpritesWithDifferentTextureIdsStayInSeparateRenderRuns() {
        Viewport viewport = new Viewport(1, 320, 200);
        DrawableBatch batch = new DrawableBatch();

        batch.add(sprite(viewport, 0, 0, 0, TextureRegion.raw(10, 16, 16)));
        batch.add(sprite(viewport, 20, 0, 0, TextureRegion.raw(11, 16, 16)));

        BatchRenderPlan plan = batch.buildRenderPlan();

        assertEquals(2, plan.runs().size());
        assertEquals(10, plan.runs().get(0).stateKey().textureId());
        assertEquals(11, plan.runs().get(1).stateKey().textureId());
    }

    @Test
    void shapeAndStrokeDrawablesAppendTriangleGeometry() {
        Viewport viewport = new Viewport(1, 320, 200);
        BatchMesh mesh = new BatchMesh();
        Shape2D shape = new Shape2D(new float[]{0, 0, 10, 0, 10, 10, 0, 10});
        Line2D line = new Line2D(0, 0, 10, 0);
        Circle2D filledCircle = new Circle2D(20, 20, 8, 8);
        EllipseStroke2D stroke = new EllipseStroke2D(40, 40, 10, 5, 3);

        shape.setCommonValues(viewport, 0, ColorUtils.WHITE);
        line.setCommonValues(viewport, 0, ColorUtils.WHITE);
        filledCircle.setFilled(true);
        filledCircle.setCommonValues(viewport, 0, ColorUtils.WHITE);
        stroke.setCommonValues(viewport, 0, ColorUtils.WHITE);

        RenderCommand shapeCommand = shape.appendToBatch(mesh);
        RenderCommand lineCommand = line.appendToBatch(mesh);
        RenderCommand circleCommand = filledCircle.appendToBatch(mesh);
        RenderCommand strokeCommand = stroke.appendToBatch(mesh);

        assertEquals(12, shapeCommand.indexCount());
        assertEquals(12, lineCommand.indexCount());
        assertEquals(18, circleCommand.indexCount());
        assertEquals(0, shape.getMaterialFlags());
        assertEquals(0, line.getMaterialFlags());
        assertEquals(0, filledCircle.getMaterialFlags());
        assertEquals(0, stroke.getMaterialFlags());
        assertEquals(strokeCommand.firstIndex() + strokeCommand.indexCount(), mesh.indexCount());
    }
}
