package com.physmo.garnet.renderer;

import java.util.Collections;
import java.util.List;

/**
 * CPU-side output from converting queued drawables into batchable geometry and render runs.
 */
public class BatchRenderPlan {
    private final BatchMesh mesh;
    private final List<RenderCommand> commands;
    private final List<RenderStateKey> states;
    private final List<RenderRun> runs;

    public BatchRenderPlan(BatchMesh mesh, List<RenderCommand> commands, List<RenderStateKey> states, List<RenderRun> runs) {
        this.mesh = mesh;
        this.commands = List.copyOf(commands);
        this.states = List.copyOf(states);
        this.runs = List.copyOf(runs);
    }

    public BatchMesh mesh() {
        return mesh;
    }

    public List<RenderCommand> commands() {
        return Collections.unmodifiableList(commands);
    }

    public List<RenderStateKey> states() {
        return Collections.unmodifiableList(states);
    }

    public List<RenderRun> runs() {
        return Collections.unmodifiableList(runs);
    }
}
