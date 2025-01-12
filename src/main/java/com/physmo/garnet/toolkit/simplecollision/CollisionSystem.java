package com.physmo.garnet.toolkit.simplecollision;


import com.physmo.garnet.graphics.Graphics;
import com.physmo.garnet.structure.Array;
import com.physmo.garnet.structure.Rect;
import com.physmo.garnet.structure.Vector3;
import com.physmo.garnet.toolkit.Component;
import com.physmo.garnet.toolkit.GameObject;
import com.physmo.garnet.toolkit.StringIdBroker;

import java.util.ArrayList;
import java.util.List;

public class CollisionSystem extends GameObject {

    List<Collidable> collidables = new ArrayList<>();
    List<Collidable> collidablesPendingRemoval = new ArrayList<>();
    CollisionDrawingCallback collisionDrawingCallback = null;
    int testsPerFrame = 0;
    GameObjectBucketGrid bucketGrid;

    public CollisionSystem(String name) {
        super(name);
    }

    public void setCollisionDrawingCallback(CollisionDrawingCallback collisionDrawingCallback) {
        this.collisionDrawingCallback = collisionDrawingCallback;
    }

    @Override
    public void init() {

    }

    @Override
    public void tick(double t) {
        testsPerFrame = 0;
        removePendingCollidables();


//        List<CollisionPacket> collisions = calculateCollisions();
        List<CollisionPacket> collisions = calculateCollisions2();

        // Notify collider of collision.
        for (CollisionPacket collision : collisions) {
            collision.sourceEntity.collisionCallback(collision);
        }

    }


    private void removePendingCollidables() {
        if (collidablesPendingRemoval.isEmpty()) return;

        collidables.removeIf(collidable -> collidablesPendingRemoval.contains(collidable));

        collidablesPendingRemoval.clear();
    }

    private List<CollisionPacket> calculateCollisions2() {
        List<CollisionPacket> collisions = new ArrayList<>();
        bucketGrid = new GameObjectBucketGrid(32, 32);
        for (Collidable collidable : getListOfActiveCollidables()) {
            bucketGrid.addObject(collidable, (int) collidable.collisionGetRegion().x, (int) collidable.collisionGetRegion().y);
        }


        List<Integer[]> listOfActiveCells = bucketGrid.getListOfActiveCells();
        Array<Object> surroundingObjects = new Array<>(10);
        for (Integer[] cellCoords : listOfActiveCells) {

            List<Object> cellObjects = bucketGrid.getCellObjects(cellCoords[0], cellCoords[1]);
            bucketGrid.getSurroundingObjects(cellCoords[0], cellCoords[1], 1, surroundingObjects);

            for (Object cellObject : cellObjects) {
                for (Object surroundingObject : surroundingObjects) {
                    if (cellObject == surroundingObject) continue;

                    boolean collided = testCollision((Collidable) cellObject, (Collidable) surroundingObject);
                    if (collided) collisions.add(
                            new CollisionPacket((Collidable) cellObject, (Collidable) surroundingObject));
                }

            }

        }


        return collisions;
    }

    @Override
    public void draw(Graphics g) {

        if (collisionDrawingCallback == null) return;
        List<Collidable> activeCollidables = getListOfActiveCollidables();
        for (Collidable collidable : activeCollidables) {
            collisionDrawingCallback.draw(collidable);
        }

    }

    /**
     * Retrieves a list of all active collidable objects currently present in the collision system.
     * A collidable object is considered active if its associated game object is active.
     *
     * @return A list of active Collidable objects.
     */
    public List<Collidable> getListOfActiveCollidables() {
        List<Collidable> activeCollidables = new ArrayList<>();
        for (Collidable collidable : collidables) {
            if (!collidable.collisionGetGameObject().isActive()) continue;
            activeCollidables.add(collidable);
        }
        return activeCollidables;
    }

    public int getTestsPerFrame() {
        return testsPerFrame;
    }

    // TODO: don't create a list here, pass it in.
    public List<Collidable> getListOfCollidablesWithTag(String tag) {
        int tagId = StringIdBroker.INSTANCE.getId(tag);

        List<Collidable> collidableList = new ArrayList<>();
        for (Collidable collidable : collidables) {
            if (!collidable.collisionGetGameObject().hasTag(tagId)) continue;
            collidableList.add(collidable);
        }
        return collidableList;
    }

    /**
     * Calculates all collisions between active collidable objects in the system.
     * The method retrieves the list of active collidables, identifies pairs of objects
     * that are colliding, and stores these interactions as {@code CollisionPacket} instances.
     *
     * @return A list of {@code CollisionPacket} objects representing detected collisions
     * between pairs of collidable objects.
     */
    private List<CollisionPacket> calculateCollisions() {
        List<CollisionPacket> collisions = new ArrayList<>();
        List<Collidable> activeCollidables = getListOfActiveCollidables();

        for (Collidable c1 : activeCollidables) {
            for (Collidable c2 : activeCollidables) {
                if (c1 == c2) continue;
                boolean collided = testCollision(c1, c2);
                if (collided) collisions.add(new CollisionPacket(c1, c2));
            }
        }

        return collisions;
    }

    /**
     * Search system for objects that are close to a supplied coordinate.
     *
     * @param x
     * @param y
     * @param withinRadius
     * @return
     */
    public List<RelativeObject> getNearestObjects(String tag, int x, int y, double withinRadius) {
        int tagId = StringIdBroker.INSTANCE.getId(tag);
        int cellWidth = bucketGrid.getCellWidth();

        int[] cellCoords = bucketGrid.getCellCoordsForPoint(x, y);

        int tileRadius = (int) ((withinRadius / bucketGrid.getCellWidth()) + 1);

        Array<Object> surroundingObjects = new Array<>(50);
        bucketGrid.getSurroundingObjects(cellCoords[0], cellCoords[1], tileRadius, surroundingObjects);
        List<Object> filteredObjects = new ArrayList<>();

        for (Object surroundingObject : surroundingObjects) {
            if (((Collidable) surroundingObject).collisionGetGameObject().hasTag(tagId))
                filteredObjects.add(surroundingObject);
        }


        List<RelativeObject> nearObjects = new ArrayList<>();

        for (Object surroundingObject : filteredObjects) {
            Vector3 transform1 = ((Collidable) surroundingObject).collisionGetGameObject().getTransform();
            double distance = transform1.distance(x, y);
            if (distance < withinRadius) {
                RelativeObject relativeObject = new RelativeObject();
                relativeObject.originX = x;
                relativeObject.originY = y;
                relativeObject.distance = distance;
                relativeObject.dx = (transform1.x - x) / distance;
                relativeObject.dy = (transform1.y - y) / distance;
                relativeObject.otherObject = (Collidable) surroundingObject;
                nearObjects.add(relativeObject);

            }
        }
        return nearObjects;

    }

    public boolean testCollision(Collidable c1, Collidable c2) {
        testsPerFrame++;
        Rect rect1 = c1.collisionGetRegion();
        Rect rect2 = c2.collisionGetRegion();
        return rect1.intersect(rect2);
    }

    public int getSize() {
        return collidables.size();
    }

    /**
     * Check for objects that are close to each other and call their
     * processing functions
     */
    public int processCloseObjects(String tag, double distanceThreshold) {
        int tagId = StringIdBroker.INSTANCE.getId(tag);

        GameObjectBucketGrid coBucketGrid = new GameObjectBucketGrid(32, 32);
        for (Collidable collidable : getListOfActiveCollidables()) {
            if (!collidable.collisionGetGameObject().hasTag(tagId)) continue;

            coBucketGrid.addObject(collidable, (int) collidable.collisionGetRegion().x, (int) collidable.collisionGetRegion().y);
        }

        int count = 0;
        List<RelativeObject> nearObjects = new ArrayList<>();
        List<Integer[]> listOfActiveCells = coBucketGrid.getListOfActiveCells();
        Array<Object> surroundingObjects = new Array<>(50);

        for (Integer[] cellCoords : listOfActiveCells) {

            List<Object> cellObjects = coBucketGrid.getCellObjects(cellCoords[0], cellCoords[1]);
            coBucketGrid.getSurroundingObjects(cellCoords[0], cellCoords[1], 1, surroundingObjects);


            for (Object cellObject : cellObjects) {
                GameObject gameObject1 = ((Collidable) cellObject).collisionGetGameObject();

                for (Object surroundingObject : surroundingObjects) {
                    GameObject gameObject2 = ((Collidable) surroundingObject).collisionGetGameObject();
                    if (gameObject1 == gameObject2) continue;
                    count++;
                    double dx = gameObject1.getTransform().x - gameObject2.getTransform().x;
                    double dy = gameObject1.getTransform().y - gameObject2.getTransform().y;
                    double distance = Math.sqrt((dx * dx) + (dy * dy));

                    if (distance < distanceThreshold) {
                        RelativeObject relativeObject = new RelativeObject();
                        relativeObject.thisObject = (Collidable) cellObject;
                        relativeObject.otherObject = (Collidable) surroundingObject;
                        relativeObject.distance = distance;
                        relativeObject.dx = dx;
                        relativeObject.dy = dy;
                        nearObjects.add(relativeObject);

                    }
                }

            }
        }

        for (RelativeObject nearObject : nearObjects) {
            nearObject.thisObject.proximityCallback(nearObject);
        }

        nearObjects.clear(); // Not clearing this results in a memory leak?

        return count;
    }

    /**
     * Removes all collider components from the specified GameObject.
     * A collider component is identified as an instance of a class
     * implementing the {@code Collidable} interface. This method iterates
     * through all components of the GameObject and removes the ones
     * that are collidable from the associated collision system.
     *
     * @param gameObject The GameObject from which collider components will be removed.
     */
    public void removeColliderFromGameObject(GameObject gameObject) {
        for (Component component : gameObject.getComponents()) {
            if (component instanceof Collidable c) {
                removeCollidable(c);
            }
        }
    }

    public void removeCollidable(Collidable collidable) {
        if (collidable == null) throw new RuntimeException("collidable is null");
        collidablesPendingRemoval.add(collidable);
    }

    /**
     * Adds a new {@code ColliderComponent} to the given {@code GameObject}.
     * The method creates a new ColliderComponent, adds it to the specified GameObject,
     * and registers the component with the collision system.
     *
     * @param gameObject The GameObject to which the ColliderComponent will be added.
     * @return The newly created and added ColliderComponent.
     */
    public ColliderComponent addNewColliderToGameObject(GameObject gameObject) {
        ColliderComponent colliderComponent = new ColliderComponent();
        gameObject.addComponent(colliderComponent);
        addCollidable(colliderComponent);
        return colliderComponent;
    }

    /**
     * Adds a collidable object to the collision system.
     * NOTE: Collidable must be removed when parent object is destroyed.
     *
     * @param collidable The collidable object to add.
     */
    public void addCollidable(Collidable collidable) {
        collidables.add(collidable);
    }

}
