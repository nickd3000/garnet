package com.physmo.garnet.renderer;

import java.util.ArrayList;
import java.util.List;

/**
 * Groups adjacent commands when their render state and index ranges are compatible.
 */
public final class RenderRunBuilder {
    private RenderRunBuilder() {
    }

    public static List<RenderRun> buildRuns(List<RenderCommand> commands, List<RenderStateKey> states) {
        if (commands.size() != states.size()) {
            throw new IllegalArgumentException("commands and states must have the same size");
        }

        List<RenderRun> runs = new ArrayList<>();
        RenderStateKey currentState = null;
        int firstIndex = 0;
        int indexCount = 0;
        int commandCount = 0;

        for (int i = 0; i < commands.size(); i++) {
            RenderCommand command = commands.get(i);
            RenderStateKey state = states.get(i);
            boolean contiguous = commandCount == 0 || command.firstIndex() == firstIndex + indexCount;
            if (commandCount == 0 || (state.equals(currentState) && contiguous)) {
                if (commandCount == 0) {
                    currentState = state;
                    firstIndex = command.firstIndex();
                }
                indexCount += command.indexCount();
                commandCount++;
            } else {
                runs.add(new RenderRun(currentState, firstIndex, indexCount, commandCount));
                currentState = state;
                firstIndex = command.firstIndex();
                indexCount = command.indexCount();
                commandCount = 1;
            }
        }

        if (commandCount > 0) {
            runs.add(new RenderRun(currentState, firstIndex, indexCount, commandCount));
        }
        return runs;
    }
}
