package com.physmo.garnet.entity;

import com.physmo.garnet.GameState;
import com.physmo.garnet.Vec3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Entity {

    public Vec3 position;
    public Vec3 velocity;
    GameState gameState;
    String name;
    boolean visible;
    boolean active;
    Set<String> tags;

    List<Component> components;
    Map<String, Object> properties;
    RenderComponent renderComponent;

    public Entity(String name, GameState gameState) {

        this.gameState = gameState;
        this.name = name;
        position = new Vec3();
        velocity = new Vec3();
        components = new ArrayList<>();
        properties = new HashMap<>();
        tags = new HashSet<>();
    }

    public void tick(double delta) {
        for (Component component : components) {
            component.tick(delta);
        }
    }

    public void draw() {
        if (renderComponent != null) renderComponent.draw();
    }

    public void addComponent(Component c) {
        components.add(c);
        c.injectParent(this);
    }

    public void addEntityDrawer(RenderComponent renderComponent) {
        this.renderComponent = renderComponent;
        this.renderComponent.injectParent(this);
    }

    public void addTag(String tag) {
        this.tags.add(tag);
    }

    public void setActive(boolean b) {
        active = b;
    }

    public GameState getGameState() {
        return gameState;
    }

    public boolean getActive() {
        return active;
    }
}
