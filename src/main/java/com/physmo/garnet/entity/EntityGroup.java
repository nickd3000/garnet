package com.physmo.garnet.entity;

import java.util.ArrayList;
import java.util.List;

// EntityGroup defines a collection of entities that can be ticked and drawn with a single method call.
// TODO: entity deletion
public class EntityGroup {
    List<Entity> entityList;

    public EntityGroup() {
        entityList = new ArrayList<>();
    }

    public void tickAll(double delta) {
        entityList.forEach((entity) -> entity.tick(delta));
    }

    public void drawAll() {
        entityList.forEach((entity) -> entity.draw());
    }

    public void add(Entity e) {
        entityList.add(e);
    }

    public Entity get(int index) {
        return entityList.get(index);
    }

    public Entity getByName(String name) {
        for (Entity entity : entityList) {
            if (entity.name.equalsIgnoreCase(name)) {
                return entity;
            }
        }
        return null;
    }

    public List<Entity> getByTag(String tag) {
        List<Entity> list = new ArrayList<>();
        for (Entity entity : entityList) {
            if (entity.tags.contains(tag)) {
                list.add(entity);
            }
        }
        return list;
    }
}