package com.physmo.garnet;

import com.physmo.garnet.collision.CollisionSystem;
import com.physmo.garnet.entity.Component;
import com.physmo.garnet.entity.Entity;
import com.physmo.garnet.entity.EntityGroup;

import java.util.List;

public abstract class GameState {

    CollisionSystem collisionSystem;

    String name;

    EntityGroup entityGroup = new EntityGroup();

    boolean debugCollision = false;

    public void _init(Garnet garnet) {
        collisionSystem = new CollisionSystem();
        init(garnet);
    }

    public abstract void init(Garnet garnet);

    public void _tick(double delta) {
        collisionSystem.tick(entityGroup);
        entityGroup.tickAll(delta);
        tick(delta);
    }

    public abstract void tick(double delta);

    public void _draw() {
        entityGroup.drawAll();
        if (debugCollision) collisionSystem.debugRender(entityGroup);
        draw();
    }

    public abstract void draw();

    public void addEntity(Entity entity) {
        entityGroup.add(entity);
    }

    public EntityGroup getEntityGroup() {
        return entityGroup;
    }

    public List<Entity> getEntitiesByTag(String tag) {
        return entityGroup.getByTag(tag);
    }

    public Entity getEntityByTag(String tag) {
        for (Entity entity : entityGroup.getByTag(tag)) {
            return entity;
        }
        return null;
    }

    public <T extends Component> T getComponent(Class<T> type) {
        for (Entity entity : entityGroup.getAll()) {
            for (Component component : entity.getComponents()) {
                if (component.getClass()==type) return (T) component;
            }
        }
        return null;
    }

}
