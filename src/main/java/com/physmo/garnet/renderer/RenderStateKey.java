package com.physmo.garnet.renderer;

import com.physmo.garnet.drawablebatch.BlendMode;
import com.physmo.garnet.graphics.ShaderProgram;
import com.physmo.garnet.graphics.Viewport;

/**
 * Immutable render state that must match for adjacent commands to share a draw run.
 */
public record RenderStateKey(
        int textureId,
        ShaderProgram shader,
        BlendMode blendMode,
        int viewportId,
        boolean clipActive,
        int clipRectHash,
        int materialFlags
) {
    public static RenderStateKey of(
            int textureId,
            ShaderProgram shader,
            BlendMode blendMode,
            Viewport viewport,
            int materialFlags
    ) {
        return new RenderStateKey(
                textureId,
                shader,
                blendMode,
                viewport.getId(),
                viewport.isClipActive(),
                viewport.getClipRectHash(),
                materialFlags);
    }
}
