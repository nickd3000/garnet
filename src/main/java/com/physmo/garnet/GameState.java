package com.physmo.garnet;

import com.physmo.garnet.entity.Entity;
import com.physmo.garnet.entity.EntityGroup;

import java.util.List;

public abstract class GameState {

    String name;

    EntityGroup entityGroup = new EntityGroup();

    public abstract void init(Garnet garnet);

    public void _tick(double delta) {
        entityGroup.tickAll(delta);
        tick(delta);
    }

    public abstract void tick(double delta);

    public void _draw() {
        entityGroup.drawAll();
        draw();
    }

    public abstract void draw();

    public void addEntity(Entity entity) {
        entityGroup.add(entity);
    }

    public EntityGroup getEntityGroup() {
        return entityGroup;
    }

    public List<Entity> getEntityByTag(String tag) {
        return entityGroup.getByTag(tag);
    }
}
