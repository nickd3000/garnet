package com.physmo.garnet.entity;

import com.physmo.garnet.GameState;
import com.physmo.garnet.Garnet;
import com.physmo.garnet.Vec3;
import com.physmo.garnet.collision.Collider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Entity {

    public Vec3 position;
    public Vec3 velocity;
    public Garnet garnet;
    GameState gameState;
    String name;
    boolean visible;
    boolean active;
    boolean paused; // Rendered but no logic is processed.
    Set<String> tags;
    List<Collider> colliders;
    List<Component> components;
    Map<String, Object> properties;

    public Entity(String name, GameState gameState) {
        this.garnet = gameState.garnet;
        this.gameState = gameState;
        this.name = name;
        position = new Vec3();
        velocity = new Vec3();
        components = new ArrayList<>();
        properties = new HashMap<>();
        tags = new HashSet<>();
        colliders = new ArrayList<>();
        paused = false;
    }

    public boolean isPaused() {
        return paused;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    public void tick(double delta) {
        if (paused) return;

        for (Component component : components) {
            component.tick(delta);
        }
    }

    public void draw() {
        //if (renderComponent != null) renderComponent.draw();
        for (Component component : components) {
            component.draw();
        }
    }

    public void addComponent(Component c) {
        components.add(c);
        c.injectParent(this);
        c.injectParentState(gameState);
        c.init();
    }

//    public void addEntityDrawer(RenderComponent renderComponent) {
//        this.renderComponent = renderComponent;
//        this.renderComponent.injectParent(this);
//        renderComponent.init();
//    }

    public void addTag(String tag) {
        this.tags.add(tag);
    }

    public boolean getActive() {
        return active;
    }

    public void setActive(boolean b) {
        active = b;
    }

    public boolean getVisible() {
        return visible;
    }

    public void setVisible(boolean b) {
        visible = b;
    }

    public GameState getGameState() {
        return gameState;
    }

    public List<Collider> getColliders() {
        return colliders;
    }

    public void addCollider(Collider collider) {
        colliders.add(collider);
    }

    public boolean hasTag(String tagName) {
        return tags.contains(tagName);
    }

    public <T extends Component> T getComponent(Class<T> type) {

        for (Component component : getComponents()) {
            if (component.getClass() == type) return (T) component;
        }

        return null;
    }

    public List<Component> getComponents() {
        return components;
    }
}
