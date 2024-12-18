package com.physmo.garnet.toolkit;

import com.physmo.garnet.graphics.Graphics;

import java.util.List;

/**
 * Abstract class representing logic and behaviour that can be attached to a game object.
 */
public abstract class Component {
    protected GameObject parent;

    abstract public void init();

    abstract public void tick(double t);

    abstract public void draw(Graphics g);

    public void setParent(GameObject parent) {
        this.parent = parent;
    }


    // Parent Context pass-though methods:

    /**
     * Retrieves an object of the specified type from the parent context.
     * This method is a pass-through to the parent context's {@code getObjectByType} method.
     *
     * @param <T>   The type of the object to retrieve.
     * @param clazz The {@code Class} object representing the type of the object to retrieve.
     * @return An object of the specified type, if found in the parent context.
     * @throws RuntimeException If no object of the specified type is found.
     */
    public <T> T getObjectByTypeFromParentContext(Class<T> clazz) {
        return parent.getContext().getObjectByType(clazz);
    }

    /**
     * Searches the parent context for a specific component of the specified type.
     * This method allows retrieving components associated with game objects
     * within the current context.
     *
     * @param <T>   The type of the component to retrieve.
     * @param clazz The {@code Class} object representing the type of the component to retrieve.
     * @return The component of the specified type, or {@code null} if no matching component is found.
     */
    public <T> T getComponentFromParentContext(Class<T> clazz) {
        return parent.getContext().getComponent(clazz);
    }

    /**
     * Retrieves a list of game objects that are associated with the specified tag.
     * This method delegates the call to the parent context's {@code getObjectsByTag} method.
     *
     * @param tag The string-based tag used to filter game objects.
     * @return A list of {@code GameObject} instances that match the specified tag.
     */
    public List<GameObject> getObjectsByTagFromParentContext(String tag) {
        return parent.getContext().getObjectsByTag(tag);
    }

    /**
     * Retrieves the first {@code GameObject} with the specified tag from the parent context.
     * This method delegates the request to the parent context's {@code getObjectByTag} method.
     *
     * @param tag The string-based tag used to identify the {@code GameObject}.
     * @return The first {@code GameObject} that matches the provided tag,
     * or {@code null} if no matching object is found.
     */
    public GameObject getObjectByTagFromParentContext(String tag) {
        return parent.getContext().getObjectByTag(tag);
    }

    private void checkParentContextAvailable() {
        if (parent == null) {
            throw new RuntimeException("Component: parent context not available (Parent is null)");
        }
        if (parent.getContext() == null) {
            throw new RuntimeException("Component: parent context not available (Context is null)");
        }
    }
}
