package com.physmo.garnet.renderer;

import com.physmo.garnet.drawablebatch.BlendMode;
import com.physmo.garnet.graphics.Viewport;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RenderRunBuilderTest {

    @Test
    void groupsOnlyAdjacentCompatibleContiguousCommands() {
        Viewport viewport = new Viewport(7, 320, 200);
        RenderStateKey normalTexture = RenderStateKey.of(10, null, BlendMode.NORMAL, viewport, BatchVertex.FLAG_TEXTURED);
        RenderStateKey additiveTexture = RenderStateKey.of(10, null, BlendMode.ADDITIVE, viewport, BatchVertex.FLAG_TEXTURED);

        List<RenderRun> runs = RenderRunBuilder.buildRuns(
                List.of(
                        new RenderCommand(0, 4, 0, 6),
                        new RenderCommand(4, 4, 6, 6),
                        new RenderCommand(8, 4, 12, 6),
                        new RenderCommand(12, 4, 30, 6)),
                List.of(normalTexture, normalTexture, additiveTexture, additiveTexture));

        assertEquals(3, runs.size());
        assertEquals(new RenderRun(normalTexture, 0, 12, 2), runs.get(0));
        assertEquals(new RenderRun(additiveTexture, 12, 6, 1), runs.get(1));
        assertEquals(new RenderRun(additiveTexture, 30, 6, 1), runs.get(2));
    }

    @Test
    void rejectsMismatchedCommandAndStateLists() {
        assertThrows(IllegalArgumentException.class, () -> RenderRunBuilder.buildRuns(
                List.of(new RenderCommand(0, 4, 0, 6)),
                List.of()));
    }
}
