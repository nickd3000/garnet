package com.physmo.garnet.toolkit;

import com.physmo.garnet.graphics.Graphics;
import com.physmo.garnet.structure.PointInt;
import com.physmo.garnet.structure.Vector3;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * The GameObject class represents a generic entity in a game that contains
 * various components and properties. It provides foundational behavior for
 * game objects such as managing components, transformation, velocity,
 * visibility, activity status, and tagging functionality.
 */
public class GameObject {

    protected final List<Component> components = new ArrayList<>();
    private final PointInt position = new PointInt(0, 0, 0);
    protected Vector3 transform = new Vector3(0, 0, 0);
    protected Vector3 velocity = new Vector3(0, 0, 0);
    protected Context context;
    String name;
    Set<Integer> tags = new HashSet<>();
    boolean active = true;
    boolean visible = true;
    boolean destroy = false;
    public GameObject(String name) {
        this.name = name;
    }


    public String getName() {
        return name;
    }

    public boolean isDestroy() {
        return destroy;
    }

    /**
     * Request this object is destroyed by the context update loop.
     */
    public void destroy() {
        this.destroy = true;
    }

    public Context getContext() {
        return context;
    }

    public Vector3 getTransform() {
        return transform;
    }

    public void setTransform(Vector3 transform) {
        this.transform = transform;
    }

    public Vector3 getVelocity() {
        return velocity;
    }

    public void setVelocity(Vector3 velocity) {
        this.velocity = velocity;
    }


    /**
     * Returns a list of components associated with this game object.
     *
     * @return a list of components.
     */
    public List<Component> getComponents() {
        return components;
    }

    /**
     * Retrieves a component of the specified class type from the list of components.
     *
     * @param <T>   the type of the component to be retrieved
     * @param clazz the class object representing the type of the component
     * @return the component instance if found; otherwise, null
     */
    public <T> T getComponent(Class<T> clazz) {
        for (Object component : components) {
            if (component.getClass() == clazz) return (T) component;
        }
        return null;
    }

    /**
     * Get the integer based position for this game object.
     *
     * @return
     */
    public PointInt getPosition() {
        return position;
    }

    public void setPosition(int x, int y) {
        position.x = x;
        position.y = y;
    }


    public void injectContext(Context context) {
        this.context = context;
    }

    /**
     * Adds a component to the game object and sets the component's parent as this game object.
     *
     * @param component the component to be added to this game object
     * @return the game object to which the component has been added
     */
    public GameObject addComponent(Component component) {
        component.setParent(this);
        components.add(component);
        return this;
    }

    /**
     * Retrieves a component of the specified class type from the list of components.
     *
     * @param <T>   the type of the component to be retrieved
     * @param clazz the class object representing the type of the component
     * @return the component instance if found; otherwise, null
     */
    public <T> T getComponentByType(Class<T> clazz) {
        for (Object object : components) {
            if (object.getClass() == clazz) return (T) object;
        }
        return null;
    }

    public void _init() {
        this.init();
        for (Component c : components) {
            c.init();
        }
    }

    public void init() {
    }


    public void _tick(double t) {
        this.tick(t);

        for (Component component : components) {
            component.tick(t);
        }
    }

    public void tick(double t) {
    }

    protected void _draw(Graphics g) {
        this.draw(g);
        for (Component component : components) {
            component.draw(g);
        }
    }

    public void draw(Graphics g) {
    }

    public GameObject setVisible(boolean b) {
        visible = b;
        return this;
    }

    public GameObject addTag(String tag) {
        tags.add(StringIdBroker.INSTANCE.getId(tag));
        return this;
    }

    public Set<Integer> getTags() {
        return tags;
    }

    public boolean hasTag(String tag) {
        return hasTag(StringIdBroker.INSTANCE.getId(tag));
    }

    public boolean hasTag(int tagId) {
        return tags.contains(tagId);
    }

    public boolean isActive() {
        return active;
    }

    public GameObject setActive(boolean b) {
        active = b;
        return this;
    }
}
