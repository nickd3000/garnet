package com.physmo.garnet.tilegrid;

public class TileGridData {
    int width, height;
    int[] tileIds;
    Object[] tileData;

    public TileGridData(int width, int height) {
        this.width = width;
        this.height = height;
        initArrays();
    }

    private void initArrays() {
        int arraySize = width * height;
        tileIds = new int[arraySize];
        tileData = new Object[arraySize];
    }

    public int getTileId(int x, int y) {
        if (x < 0 || y < 0 || x >= width || y >= height) return -1;
        return tileIds[getIndex(x, y)];
    }

    public Object getTileData(int x, int y) {
        if (x < 0 || y < 0 || x >= width || y >= height) return null;
        return tileData[getIndex(x, y)];
    }

    private int getIndex(int x, int y) {
        return x + (width * y);
    }

    public void setTileId(int x, int y, int val) {
        tileIds[getIndex(x, y)] = val;
    }

    public void setTileData(int x, int y, Object data) {
        tileData[getIndex(x, y)] = data;
    }
}
