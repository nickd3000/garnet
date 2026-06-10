package com.physmo.garnet.toolkit;

import com.physmo.garnet.graphics.Graphics;
import com.physmo.garnet.structure.PointInt;
import com.physmo.garnet.structure.Vector3;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * The GameObject class represents a generic entity in a game that contains
 * various components and properties. It provides foundational behavior for
 * game objects such as managing components, transformation, velocity,
 * visibility, activity status, and tagging functionality.
 */
public class GameObject implements MessageListener {

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

    /**
     * Creates a named game object for fluent construction.
     *
     * @param name the object name
     * @return a new game object
     */
    public static GameObject named(String name) {
        return new GameObject(name);
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
     * Returns the integer-based position of this game object.
     *
     * @return the position as a {@link PointInt}
     */
    public PointInt getPosition() {
        return position;
    }

    /**
     * Sets the integer-based position of this game object.
     *
     * @param x the x-coordinate
     * @param y the y-coordinate
     */
    public void setPosition(int x, int y) {
        position.x = x;
        position.y = y;
    }

    /**
     * Sets the transform position, leaving z at 0.
     *
     * @param x the x-coordinate
     * @param y the y-coordinate
     * @return this game object, for chaining
     */
    public GameObject at(double x, double y) {
        return at(x, y, 0);
    }

    /**
     * Sets the transform position.
     *
     * @param x the x-coordinate
     * @param y the y-coordinate
     * @param z the z-coordinate
     * @return this game object, for chaining
     */
    public GameObject at(double x, double y, double z) {
        transform.set(x, y, z);
        return this;
    }


    /**
     * Injects the owning {@link Context} into this game object so it can access
     * sibling objects and components at runtime.
     *
     * @param context the context this object belongs to
     */
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
     * Adds a component to this object as part of a fluent construction chain.
     *
     * @param component the component to add
     * @return this game object, for chaining
     */
    public GameObject with(Component component) {
        return addComponent(component);
    }

    /**
     * Adds this object to a context as the terminal step of a fluent construction chain.
     *
     * @param context the context to add this object to
     * @return this game object, for assignment or further use
     */
    public GameObject inContext(Context context) {
        Objects.requireNonNull(context, "context must not be null").add(this);
        return this;
    }


    /**
     * Internal initialisation: calls {@link #init()} then initialises all attached components.
     * Called by the framework; do not call directly.
     */
    public void _init() {
        this.init();
        for (Component c : components) {
            c.init();
        }
    }

    public void init() {
    }


    /**
     * Internal tick: calls {@link #tick(double)} then ticks all attached components.
     * Called by the framework; do not call directly.
     *
     * @param t seconds elapsed since the last tick
     */
    public void _tick(double t) {
        this.tick(t);

        for (Component component : components) {
            component.tick(t);
        }
    }

    public void tick(double t) {
    }

    /**
     * Internal draw: calls {@link #draw(Graphics)} then draws all attached components.
     * Called by the framework; do not call directly.
     *
     * @param g the graphics context
     */
    protected void _draw(Graphics g) {
        this.draw(g);
        for (Component component : components) {
            component.draw(g);
        }
    }

    public void draw(Graphics g) {
    }

    /**
     * Sends a message to this game object and all of its attached components.
     *
     * @param name The name of the message.
     */
    public void sendMessage(String name) {
        sendMessage(name, null);
    }

    /**
     * Sends a message to this game object and all of its attached components.
     *
     * @param name The name of the message.
     * @param data Optional data associated with the message.
     */
    public void sendMessage(String name, Object data) {
        onMessage(name, data);
        List<Component> componentsCopy = new ArrayList<>(components);
        for (Component component : componentsCopy) {
            component.onMessage(name, data);
        }
    }

    /**
     * Called when a message is sent to this game object.
     * Subclasses can override this method to handle specific messages.
     *
     * @param name The name of the message.
     * @param data Optional data associated with the message.
     */
    @Override
    public void onMessage(String name, Object data) {
        // Default: do nothing
    }

    /**
     * Broadcasts a message to all game objects in the current context.
     *
     * @param name The name of the message.
     */
    public void broadcastMessage(String name) {
        broadcastMessage(name, null);
    }

    /**
     * Broadcasts a message to all game objects in the current context.
     *
     * @param name The name of the message.
     * @param data Optional data associated with the message.
     */
    public void broadcastMessage(String name, Object data) {
        if (context != null) {
            context.broadcastMessage(name, data);
        }
    }

    /**
     * Sets the visibility of this game object.
     *
     * @param b {@code true} to make the object visible, {@code false} to hide it
     * @return this game object, for chaining
     */
    public GameObject setVisible(boolean b) {
        visible = b;
        return this;
    }

    /**
     * Adds a string tag to this object as part of a fluent construction chain.
     *
     * @param tag the tag string to add
     * @return this game object, for chaining
     */
    public GameObject tagged(String tag) {
        return addTag(tag);
    }

    /**
     * Adds a string tag to this game object. Tags can be used to query groups of objects
     * from a {@link Context}.
     *
     * @param tag the tag string to add
     * @return this game object, for chaining
     */
    public GameObject addTag(String tag) {
        tags.add(StringIdBroker.INSTANCE.getId(tag));
        return this;
    }

    public Set<Integer> getTags() {
        return tags;
    }

    /**
     * Returns whether this game object has the given string tag.
     *
     * @param tag the tag string to check
     * @return {@code true} if the tag is present
     */
    public boolean hasTag(String tag) {
        return hasTag(StringIdBroker.INSTANCE.getId(tag));
    }

    public boolean hasTag(int tagId) {
        return tags.contains(tagId);
    }

    /**
     * Returns whether this game object is currently active.
     * Inactive objects are skipped during tick and draw.
     *
     * @return {@code true} if active
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Sets the active state of this game object.
     * Inactive objects are skipped during tick and draw.
     *
     * @param b {@code true} to activate, {@code false} to deactivate
     * @return this game object, for chaining
     */
    public GameObject setActive(boolean b) {
        active = b;
        return this;
    }
}
