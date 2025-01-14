package com.physmo.garnet.toolkit.simplecollision;

import com.physmo.garnet.structure.Array;

import java.util.ArrayList;
import java.util.List;

public class GameObjectBucketGrid {
    //Map<Integer, List<Object>> objects = new HashMap<>();
    BucketGridMap objects = new BucketGridMap();

    int cellWidth;
    int cellHeight;
    int maxObjectsPerCell = 10;
    int gridSize = 1024; // Grid goes from -gridSize to +gridSize

    public GameObjectBucketGrid(int cellWidth, int cellHeight) {
        this.cellWidth = cellWidth;
        this.cellHeight = cellHeight;
    }

    public int getCellWidth() {
        return cellWidth;
    }

    public int getCellHeight() {
        return cellHeight;
    }

    public int getSize() {
        return objects.size();
    }

    public void clear() {
        objects.clear();
    }

    public int[] getCellCoordsForPoint(int x, int y) {
        return new int[]{x / cellWidth, y / cellHeight};
    }

    /**
     * Adds an object to the appropriate cell in the grid based on its coordinates.
     * If the cell does not exist, it is created. Limits the number of objects per cell
     * to the specified maximum.
     *
     * @param o the object to be added
     * @param x the x-coordinate where the object should be added
     * @param y the y-coordinate where the object should be added
     */
    public void addObject(Object o, int x, int y) {
        int num = encoder(x / cellWidth, y / cellHeight);

        if (!objects.containsKey(num)) {
            objects.put(num, new ArrayList<>());
        }

        if (objects.get(num).size() > maxObjectsPerCell) return;

        objects.get(num).add(o);
    }

    public int encoder(int x, int y) {
        if (x < -gridSize) x = -gridSize;
        if (y < -gridSize) y = -gridSize;
        if (x > gridSize) x = gridSize;
        if (y > gridSize) y = gridSize;

        x += gridSize;
        y += gridSize;

        return ((x & 0b1111_1111_1111) << 12) + (y & 0b1111_1111_1111);
    }

    public void decoder(int v, int[] coords) {
        int x = (v >> 12) & 0b1111_1111_1111;
        int y = (v) & 0b1111_1111_1111;
        coords[0] = x - gridSize;
        coords[1] = y - gridSize;
    }

    /**
     * Retrieves a list of active cells from the grid. Each cell is represented
     * as an array of integers where the first element is the x-coordinate
     * and the second element is the y-coordinate of the cell.
     *
     * @return a list of active cells, where each cell is represented as an Integer array [x, y]
     */
    public List<int[]> getListOfActiveCells() {
        List<int[]> activeCells = new ArrayList<>();
        for (Integer integer : objects.keySet()) {
            activeCells.add(decoder(integer));
        }
        return activeCells;
    }

    public int[] decoder(int v) {
        int x = (v >> 12) & 0b1111_1111_1111;
        int y = (v) & 0b1111_1111_1111;
        return new int[]{x - gridSize, y - gridSize};
    }

    // Faster version
    public int[] getListOfActiveCellsEncoded() {
        return objects.keySet();
    }

    /**
     * Return a list of objects contained in the cell referred to by the supplied coordinate.
     *
     * @param cellX
     * @param cellY
     * @return
     */
    public List<Object> getCellObjects(int cellX, int cellY) {
        int index = encoder(cellX, cellY);
        if (objects.containsKey(index)) {
            return objects.get(index);
        }

        return new ArrayList<>();
    }


    /**
     * Retrieves all objects within a specified radius of a given cell coordinate
     * and appends them to the provided output array.
     *
     * @param cellX                 the x-coordinate of the central cell
     * @param cellY                 the y-coordinate of the central cell
     * @param tileRadius            the radius (in cells) surrounding the central cell to include
     * @param outSurroundingObjects the array to which the surrounding objects will be added
     */
    public void getSurroundingObjects(int cellX, int cellY, int tileRadius, Array<Object> outSurroundingObjects) {
        int cx = cellX;
        int cy = cellY;
        int index;
        for (int y = cy - tileRadius; y <= cy + tileRadius; y++) {
            for (int x = cx - tileRadius; x <= cx + tileRadius; x++) {
                index = encoder(x, y);
                if (objects.containsKey(index)) {
                    outSurroundingObjects.addAll(objects.get(index));
                }
            }
        }

    }
}
