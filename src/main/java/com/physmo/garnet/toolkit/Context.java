package com.physmo.garnet.toolkit;

import com.physmo.garnet.graphics.Graphics;

import java.util.ArrayList;
import java.util.List;

/**
 * A Context is a container for any objects required for a game or scene.
 * Objects can be retrieved by class type or by tag.
 * GameObject derived objects are given special treatment and can be ticked and drawn as a batch.
 *
 * <p>
 * Newly added GameObjects will be initialised just before they are ticked for the first time.
 * GameObjects added during the tick cycle will be available on the next tick cycle.
 */
public class Context {

    private final List<Object> newObjects = new ArrayList<>();
    private final List<Object> uninitialisedObjects = new ArrayList<>();
    private List<Object> objects = new ArrayList<>();
    private boolean duringTick = false;
    boolean initialised = false;

    public Context() {
    }

    /**
     * Add an object to this context.  If the object is derived
     * from GameObject, the context will be automatically injected into it.
     *
     * @param object
     */
    public void add(Object object) {

        // Game objects have this context injected.
        if (object instanceof GameObject) {
            ((GameObject) object).injectContext(this);
        }

        if (duringTick) {
            newObjects.add(object);
        } else {
            objects.add(object);
        }

        uninitialisedObjects.add(object);
    }

    /**
     * Retrieve an object that matches the supplied class.
     *
     * @param clazz The class type matching the object to retrieve.
     */
    public <T> T getObjectByType(Class<T> clazz) {
        for (Object object : objects) {
            if (object.getClass() == clazz) return (T) object;
        }

        throw new RuntimeException("Context: object not found: " + clazz.getCanonicalName());

    }

    /**
     * Search all game objects in this context for a specific component.
     * NOTE: Only use this if you are sure there is only one game object with a given component.
     * (Or happy to get the first instance found)
     *
     * @param clazz class type of components to return
     * @param <T>
     * @return the component matching the passed in class type
     */
    public <T> T getComponent(Class<T> clazz) {
        for (Object object : objects) {
            if (!(object instanceof GameObject)) continue;
            T component = ((GameObject) object).getComponent(clazz);
            if (component != null) return component;
        }
        return null;
    }

    /**
     * Return a list of game objects that match the supplied tag
     *
     * @param tag The string based tag to search for.
     * @return List of game objects matching the supplied tag.
     */
    public List<GameObject> getObjectsByTag(String tag) {
        List<GameObject> list = new ArrayList<>();

        int tagId = StringIdBroker.INSTANCE.getId(tag);

        for (Object object : objects) {
            if (object instanceof GameObject) {
                if (((GameObject) object).getTags().contains(tagId)) {
                    list.add((GameObject) object);
                }
            }
        }
        return list;
    }


    private void addNewObjects() {
        if (newObjects.isEmpty()) return;
        objects.addAll(newObjects);
        newObjects.clear();
    }

    private void initialiseNewObjects() {
        if (uninitialisedObjects.isEmpty()) return;
        for (Object object : uninitialisedObjects) {
            if (object instanceof GameObject) {
                ((GameObject) object)._init();
            }
        }
        uninitialisedObjects.clear();
    }

    public void init() {
        initialised = true;
        addNewObjects();
        initialiseNewObjects();
    }

    private void processDestruction() {
        int destructionCount = 0;
        for (Object object : objects) {
            if (object instanceof GameObject) {
                if (((GameObject) object).isDestroy()) destructionCount++;
            }
        }
        if (destructionCount == 0) return;

        List<Object> keep = new ArrayList<>();

        for (Object object : objects) {
            if (object instanceof GameObject) {
                if (!((GameObject) object).isDestroy()) {
                    keep.add(object);
                } else {
                    System.out.println("removing object");
                    //System.out.println("skipping");
                }
            } else {
                keep.add(object);
            }
        }

        objects = keep;
    }

    /**
     * Tick every game object in this context by the supplied amount of time
     *
     * @param t Time in seconds sent to each objects tick method.
     */
    public void tick(double t) {
        if (!initialised) throw new RuntimeException("Context: not initialised");

        addNewObjects();
        initialiseNewObjects();
        processDestruction();

        duringTick = true;
        for (Object object : objects) {
            if (object instanceof GameObject) {
                if (!((GameObject) object).isActive()) continue;
                ((GameObject) object)._tick(t);
            }
        }
        duringTick = false;

    }

    /**
     * Draw every game object in this context.
     */
    public void draw(Graphics g) {
        if (!initialised) throw new RuntimeException("Context: not initialised");
        for (Object object : objects) {
            if (object instanceof GameObject) {
                ((GameObject) object)._draw(g);
            }
        }
    }

    /**
     * Erase all objects contained within this context.
     */
    public void reset() {
        objects.clear();
    }

    public int getObjectCount() {
        return objects.size();
    }

    /**
     * Retrieve the first GameObject found that matches the supplied tag.
     *
     * @param tag The string based tag to search for.
     * @return The first GameObject that matches the supplied tag.
     */
    public GameObject getObjectByTag(String tag) {
        List<GameObject> objectsByTag = getObjectsByTag(tag);
        return objectsByTag.get(0);
    }
}
