package com.physmo.garnet;

import com.physmo.garnet.collision.CollisionSystem;
import com.physmo.garnet.entity.Component;
import com.physmo.garnet.entity.Entity;
import com.physmo.garnet.entity.EntityGroup;
import com.physmo.garnet.particle.ParticleManager;

import java.util.List;

public abstract class GameState {

    public Garnet garnet;
    String name;
    CollisionSystem collisionSystem;
    EntityGroup entityGroup;
    ParticleManager particleManager;
    boolean debugCollision = false;
    public GameState(Garnet garnet, String name) {
        this.garnet = garnet;
        this.name = name;
    }

    public ParticleManager getParticleManager() {
        return particleManager;
    }

    public String getName() {
        return name;
    }

    public void _init(Garnet garnet) {
        this.garnet = garnet;
        collisionSystem = new CollisionSystem();
        entityGroup = new EntityGroup();
        particleManager = new ParticleManager(1000);
        init(garnet);
    }

    public abstract void init(Garnet garnet);

    public void _tick(double delta) {
        if (this.garnet == null) {
            System.out.println("ERROR, init not called on game state");
        }

        collisionSystem.tick(entityGroup);
        entityGroup.tickAll(delta);
        particleManager.tick(delta);
        tick(delta);
    }

    public abstract void tick(double delta);

    public void _draw() {
        entityGroup.drawAll();
        if (debugCollision) collisionSystem.debugRender(entityGroup);
        draw();
        particleManager.draw();
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
                if (component.getClass() == type) return (T) component;
            }
        }
        return null;
    }

}
