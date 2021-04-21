package com.physmo.garnet.entity;

import com.physmo.garnet.spritebatch.SpriteBatch;

import java.util.ArrayList;
import java.util.List;

public class EntityGroup {
    List<Entity> entityList;

    public EntityGroup() {
        entityList = new ArrayList<>();
    }

    public void tickAll(double delta) {
        for (Entity entity : entityList) {
            entity.tick(delta);
        }
    }

    public void drawAll(SpriteBatch sb) {
        entityList.forEach((entity) -> {
            entity.draw(sb);
        });
    }

    public void add(Entity e) {
        entityList.add(e);
    }
}
