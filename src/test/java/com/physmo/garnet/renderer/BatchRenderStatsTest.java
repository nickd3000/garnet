package com.physmo.garnet.renderer;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BatchRenderStatsTest {

    @Test
    void snapshotCapturesCurrentCountersWithoutSharingMutation() {
        BatchRenderStats stats = new BatchRenderStats();
        stats.setQueuedElements(4);
        stats.setRenderRuns(2);
        stats.incrementBufferUploads();
        stats.incrementDrawCalls();
        stats.incrementTextureBinds();

        BatchRenderStats snapshot = stats.snapshot();
        stats.reset();

        assertEquals(4, snapshot.getQueuedElements());
        assertEquals(2, snapshot.getRenderRuns());
        assertEquals(1, snapshot.getBufferUploads());
        assertEquals(1, snapshot.getDrawCalls());
        assertEquals(1, snapshot.getTextureBinds());
        assertEquals(0, stats.getQueuedElements());
    }

    @Test
    void toStringIncludesCounterNames() {
        BatchRenderStats stats = new BatchRenderStats();
        stats.setQueuedElements(1);

        String output = stats.toString();

        assertTrue(output.contains("queuedElements=1"));
        assertTrue(output.contains("renderRuns="));
        assertTrue(output.contains("drawCalls="));
    }
}
