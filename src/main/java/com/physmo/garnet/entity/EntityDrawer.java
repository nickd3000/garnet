package com.physmo.garnet.entity;

import com.physmo.garnet.spritebatch.SpriteBatch;

public interface EntityDrawer {
    void injectParent(Entity parent);

    void draw(SpriteBatch spriteBatch);
}
