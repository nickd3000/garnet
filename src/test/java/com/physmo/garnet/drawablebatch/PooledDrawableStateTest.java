package com.physmo.garnet.drawablebatch;

import com.physmo.garnet.graphics.ObjectPool;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

class PooledDrawableStateTest {

    @Test
    void spriteResetClearsInheritedRenderStateBeforePoolReuse() {
        ObjectPool<Sprite2D> pool = new ObjectPool<>(Sprite2D.class, Sprite2D::new);
        Sprite2D firstUse = pool.getFreeObject();

        firstUse.setBlendMode(BlendMode.ADDITIVE);
        firstUse.setColorOverride(true);
        pool.releaseObject(firstUse);

        Sprite2D secondUse = pool.getFreeObject();
        secondUse.reset();

        assertSame(firstUse, secondUse);
        assertSame(BlendMode.NORMAL, secondUse.getBlendMode());
        assertFalse(secondUse.isColorOverride());
        assertNull(secondUse.getShader());
    }

    @Test
    void lineResetClearsInheritedRenderStateBeforePoolReuse() {
        ObjectPool<Line2D> pool = new ObjectPool<>(Line2D.class, Line2D::new);
        Line2D firstUse = pool.getFreeObject();

        firstUse.setBlendMode(BlendMode.MULTIPLY);
        firstUse.setColorOverride(true);
        pool.releaseObject(firstUse);

        Line2D secondUse = pool.getFreeObject();
        secondUse.reset();

        assertSame(firstUse, secondUse);
        assertSame(BlendMode.NORMAL, secondUse.getBlendMode());
        assertFalse(secondUse.isColorOverride());
        assertNull(secondUse.getShader());
    }
}
