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
    // TODO: we could have multiple lists based on collision group
    List<Collidable> collidables = new ArrayList<>();
    List<Collidable> collidablesPendingRemoval = new ArrayList<>();
    CollisionDrawingCallback collisionDrawingCallback = null;
    int testsPerFrame = 0;
    GameObjectBucketGrid bucketGrid;
    Array<Object> surroundingObjectsTemp = new Array<>(100);
    Array<CollisionPacket> collisions = new Array<>(100);
    int[] collisionGroupMatrix = new int[0xff];

    public CollisionSystem(String name) {
        super(name);
        setCollisionGroupMatrix(0, 0, true);
    }

    public void setCollisionDrawingCallback(CollisionDrawingCallback collisionDrawingCallback) {
        this.collisionDrawingCallback = collisionDrawingCallback;
    }

    @Override
    public void init() {

    }

    /**
     * Configures the collision detection behavior between two specified collision groups.
     * This method determines whether objects in the provided groups are allowed to interact
     * during collision detection.
     *
     * @param group1     The first collision group identifier (0-15).
     * @param group2     The second collision group identifier (0-15).
     * @param canCollide A boolean indicating whether objects in the two groups can collide;
     *                   {@code true} enables collisions, {@code false} disables collisions.
     */
    public void setCollisionGroupMatrix(int group1, int group2, boolean canCollide) {
        int index = ((group1 & 0b1111) << 4) | group2 & 0b1111;
        collisionGroupMatrix[index] = canCollide ?1:0;
    }

    @Override
    public void tick(double t) {
        testsPerFrame = 0;
        removePendingCollidables();

        collisions.clear();
        calculateCollisions2(collisions);

        // Notify collider of collision.
        for (CollisionPacket collision : collisions) {
            collision.sourceEntity.collisionCallback(collision);
        }

    }

    /**
     * Removes all pending collidable objects from the collision system.
     * <p>
     * This method processes the collection of collidables marked for removal
     * (`collidablesPendingRemoval`) and eliminates any corresponding objects
     * from the main `collidables` list. After all pending removals are processed,
     * the temporary storage used for tracking objects to be removed is cleared.
     * <p>
     * The purpose of this method is to ensure the collision system's data structure
     * remains synchronized by removing collidables that are no longer active or
     * required for collision detection.
     * <p>
     * Implementation Notes:
     * - If the `collidablesPendingRemoval` list is empty, the method exits early,
     * avoiding unnecessary processing.
     * - The `removeIf` method is used to efficiently filter `collidables` based
     * on membership in the `collidablesPendingRemoval` list.
     * - After processing, `collidablesPendingRemoval` is cleared to prepare for
     * the next update cycle.
     */
    private void removePendingCollidables() {
        if (collidablesPendingRemoval.isEmpty()) return;

        collidables.removeIf(collidable -> collidablesPendingRemoval.contains(collidable));

        collidablesPendingRemoval.clear();
    }

    private void calculateCollisions2(Array<CollisionPacket> outCollisions) {

        bucketGrid = new GameObjectBucketGrid(32, 32);
        for (Collidable collidable : getListOfActiveCollidables()) {
            bucketGrid.addObject(collidable, (int) collidable.collisionGetRegion().x, (int) collidable.collisionGetRegion().y);
        }

        int[] listOfActiveCellsEncoded = bucketGrid.getListOfActiveCellsEncoded();
        int[] cellCoords = new int[2];

        for (int cellCoordsEncoded : listOfActiveCellsEncoded) {
            bucketGrid.decoder(cellCoordsEncoded, cellCoords);
            List<Object> cellObjects = bucketGrid.getCellObjects(cellCoords[0], cellCoords[1]);
            surroundingObjectsTemp.clear();
            bucketGrid.getSurroundingObjects(cellCoords[0], cellCoords[1], 1, surroundingObjectsTemp);

            for (Object cellObject : cellObjects) {
                for (Object surroundingObject : surroundingObjectsTemp) {
                    if (cellObject == surroundingObject) continue;
                    if (!canGroupsCollide((Collidable) cellObject, (Collidable) surroundingObject)) continue;

                    boolean collided = testCollision((Collidable) cellObject, (Collidable) surroundingObject);
                    if (collided) outCollisions.add(
                            new CollisionPacket((Collidable) cellObject, (Collidable) surroundingObject));
                }

            }

        }

    }

    private boolean canGroupsCollide(Collidable collidable1, Collidable collidable2) {
        int index = ((collidable1.getCollisionGroup() & 0b1111) << 4) | collidable2.getCollisionGroup() & 0b1111;
        return collisionGroupMatrix[index] == 1;
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

    /**
     * Retrieves the number of collision tests performed per frame.
     *
     * @return The number of collision tests conducted during a single frame.
     */
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
     * Populates a list of objects that are nearest to a given point within a specified radius,
     * filtered by collision group. The method identifies all nearby objects that match the collision group
     * and calculates their distance, direction, and relative properties before adding them to the output list.
     *
     * @param collisionGroup The collision group to filter objects by. Only objects belonging to this group are considered.
     * @param x The x-coordinate of the origin point to search from.
     * @param y The y-coordinate of the origin point to search from.
     * @param withinRadius The radius within which to search for objects.
     * @param outNearObjects A list of RelativeObject instances that will be populated with nearby objects and their relative properties.
     */
    public void getNearestObjects(int collisionGroup, int x, int y, double withinRadius, Array<RelativeObject> outNearObjects) {

        int[] cellCoords = bucketGrid.getCellCoordsForPoint(x, y);

        int tileRadius = (int) ((withinRadius / bucketGrid.getCellWidth()) + 1);

        surroundingObjectsTemp.clear();
        bucketGrid.getSurroundingObjects(cellCoords[0], cellCoords[1], tileRadius, surroundingObjectsTemp);
        List<Object> filteredObjects = new ArrayList<>();

        for (Object surroundingObject : surroundingObjectsTemp) {
            if (((Collidable) surroundingObject).getCollisionGroup() == collisionGroup)
                filteredObjects.add(surroundingObject);
        }

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
                outNearObjects.add(relativeObject);

            }
        }

    }

    public boolean testCollision(Collidable c1, Collidable c2) {
        testsPerFrame++;
        Rect rect1 = c1.collisionGetRegion();
        Rect rect2 = c2.collisionGetRegion();
        return rect1.intersect(rect2);
    }

    /**
     * Retrieves the total number of collidable objects currently present in the collision system.
     *
     * @return The number of collidable objects in the system.
     */
    public int getSize() {
        return collidables.size();
    }

    /**
     * Check for objects that are close to each other and call their
     * processing functions
     */
    public int processCloseObjects(int collisionGroup, double distanceThreshold) {

        GameObjectBucketGrid coBucketGrid = new GameObjectBucketGrid(32, 32);
        for (Collidable collidable : getListOfActiveCollidables()) {
            if (collidable.getCollisionGroup() != collisionGroup) continue;

            coBucketGrid.addObject(collidable, (int) collidable.collisionGetRegion().x, (int) collidable.collisionGetRegion().y);
        }

        int count = 0;
        List<RelativeObject> nearObjects = new ArrayList<>();

        int[] listOfActiveCellsEncoded = coBucketGrid.getListOfActiveCellsEncoded();
        int[] cellCoords = new int[2];

        for (int cellCoordsEncoded : listOfActiveCellsEncoded) {
            coBucketGrid.decoder(cellCoordsEncoded, cellCoords);
            List<Object> cellObjects = coBucketGrid.getCellObjects(cellCoords[0], cellCoords[1]);
            surroundingObjectsTemp.clear();
            coBucketGrid.getSurroundingObjects(cellCoords[0], cellCoords[1], 1, surroundingObjectsTemp);


            for (Object cellObject : cellObjects) {
                GameObject gameObject1 = ((Collidable) cellObject).collisionGetGameObject();

                for (Object surroundingObject : surroundingObjectsTemp) {
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

    /**
     * Marks a specific collidable object for removal from the collision system.
     * The collidable object will be added to a pending removal list and will
     * be removed from the system during the next update cycle.
     *
     * @param collidable The collidable object to mark for removal. This parameter
     *                   cannot be null. If a null value is provided, a runtime
     *                   exception will be thrown.
     */
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
