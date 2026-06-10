package com.physmo.garnet.drawablebatch;

import com.physmo.garnet.renderer.BatchMesh;
import com.physmo.garnet.renderer.RenderCommand;

/**
 * Internal migration seam for drawables that can emit CPU-side batch geometry.
 */
interface BatchRenderable {
    RenderCommand appendToBatch(BatchMesh mesh);

    int getMaterialFlags();
}
