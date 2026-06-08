package com.physmo.garnet.drawablebatch;

import com.physmo.garnet.graphics.Graphics;
import com.physmo.garnet.renderer.BatchMesh;
import com.physmo.garnet.renderer.BatchRenderPlan;
import com.physmo.garnet.renderer.BatchRenderStats;
import com.physmo.garnet.renderer.BatchRenderer;
import com.physmo.garnet.renderer.RenderCommand;
import com.physmo.garnet.renderer.RenderRun;
import com.physmo.garnet.renderer.RenderRunBuilder;
import com.physmo.garnet.renderer.RenderStateKey;
import com.physmo.garnet.structure.Array;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glEnable;

/**
 * Collects drawable elements and flushes them in draw-order.
 * <p>
 * The batch also owns the transient OpenGL state needed for each element, such
 * as blend mode, shader binding, texture binding, and clipping.
 */
public class DrawableBatch {

    final Array<DrawableElement> elements;
    // Sorting is deferred until render so multiple additions only pay one sort.
    private boolean dirty = true;
    private final BatchRenderer batchRenderer;

    public DrawableBatch() {
        elements = new Array<>(10);
        batchRenderer = new BatchRenderer();
    }

    /**
     * Returns the backing element list.
     * <p>
     * Mutating this list directly can bypass the dirty flag, so callers that add
     * elements should normally use {@link #add(DrawableElement)}.
     *
     * @return mutable backing collection of drawable elements
     */
    public Array<DrawableElement> getElements() {
        return elements;
    }

    /**
     * Adds an element to the batch and marks draw-order sorting as stale.
     *
     * @param batchElement element to render during the next batch flush
     */
    public void add(DrawableElement batchElement) {
        elements.add(batchElement);
        dirty = true;
    }

    public int size() {
        return elements.size();
    }

    /**
     * Renders all queued elements in ascending draw-order.
     * <p>
     * The render pass tracks the currently active blend mode and shader to avoid
     * unnecessary OpenGL state changes for adjacent elements with the same state.
     *
     * @param graphics active graphics context
     */
    public void render(Graphics graphics) {
        sortIfDirty();

        glEnable(GL_TEXTURE_2D);
        glEnable(GL_BLEND);

        batchRenderer.render(graphics, buildRenderPlan());
    }

    private void sortIfDirty() {
        if (dirty) {
            elements.sort(Comparator.comparingInt(DrawableElement::getDrawOrder));
            dirty = false;
        }
    }

    /**
     * Builds CPU-side batch geometry and render runs without issuing OpenGL calls.
     * This is the migration seam used before the dynamic GL buffer path is wired in.
     */
    public BatchRenderPlan buildRenderPlan() {
        sortIfDirty();

        BatchMesh mesh = new BatchMesh();
        List<RenderCommand> commands = new ArrayList<>(elements.size());
        List<RenderStateKey> states = new ArrayList<>(elements.size());

        for (DrawableElement element : elements) {
            commands.add(element.appendToBatch(mesh));
            states.add(element.createRenderStateKey());
        }

        List<RenderRun> runs = RenderRunBuilder.buildRuns(commands, states);
        return new BatchRenderPlan(mesh, commands, states, runs);
    }

    /**
     * Returns a snapshot of the most recent buffered render counters.
     */
    public BatchRenderStats getRenderStats() {
        return batchRenderer.getStats().snapshot();
    }

    /**
     * Applies the element viewport's clipping rectangle, if one is configured.
     *
     * @param graphics active graphics context
     * @param de       element whose viewport should drive clipping
     */
    public void applyClipRectIfRequired(Graphics graphics, DrawableElement de) {
        graphics._activateClipRect(de.getViewport());
    }

    /**
     * Removes all queued elements and marks the sorted order as stale.
     */
    public void clear() {
        elements.clear();
        dirty = true;
    }


}
