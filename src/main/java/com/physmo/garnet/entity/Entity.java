package com.physmo.garnet.entity;

import com.physmo.garnet.GPoint;
import com.physmo.garnet.spritebatch.SpriteBatch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Entity {
    public GPoint position;
    public GPoint velocity;

    List<Component> components;
    Map<String, Object> properties;
    EntityDrawer entityDrawer;

    public Entity() {
        position = new GPoint(0, 0);
        velocity = new GPoint(0, 0);
        components = new ArrayList<>();
        properties = new HashMap<>();
    }

    public void tick(double delta) {
        for (Component component : components) {
            component.tick(delta);
        }
    }

    public void draw(SpriteBatch sb) {
        entityDrawer.draw(sb);
    }

    public void addComponent(Component c) {
        components.add(c);
        c.injectParent(this);
    }

    public void addEntityDrawer(EntityDrawer ed) {
        this.entityDrawer = ed;
        ed.injectParent(this);
    }
}
