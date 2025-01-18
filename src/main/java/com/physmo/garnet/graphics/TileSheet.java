package com.physmo.garnet.graphics;

/**
 * The TileSheet class represents a collection of tiles extracted from a
 * single texture. It divides the texture into smaller rectangles (tiles)
 * based on the specified tile width and height.
 */
public class TileSheet {
    private final int tilesWide;
    private final int tilesHigh;
    Texture texture;
    int tileWidth;
    int tileHeight;
    SubImage[][] subImages;

    public TileSheet(Texture texture, int tileWidth, int tileHeight) {
        this.texture = texture;
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
        this.tilesWide = texture.getWidth() / tileWidth;
        this.tilesHigh = texture.getHeight() / tileHeight;

        setUpSubImages(texture, tileWidth, tileHeight);
    }

    private void setUpSubImages(Texture texture, int tileWidth, int tileHeight) {
        subImages = new SubImage[tilesWide][tilesHigh];
        for (int x = 0; x < tilesWide; x++) {
            for (int y = 0; y < tilesHigh; y++) {
                subImages[x][y] = new SubImage(texture, x * tileWidth, y * tileHeight, tileWidth, tileHeight);
            }
        }
    }

    public Texture getTexture() {
        return texture;
    }

    public int getTileWidth() {
        return tileWidth;
    }

    public int getTileHeight() {
        return tileHeight;
    }

    public int[] getTileCoordsFromIndex(int index) {
        int y = index / tilesHigh;
        int x = index % tilesWide;
        return new int[]{x, y};
    }

    /**
     * Retrieves the {@link SubImage} located at the specified column and row
     * within the tile sheet.
     *
     * @param column the column index of the desired sub-image (0-based).
     * @param row the row index of the desired sub-image (0-based).
     * @return the {@link SubImage} located at the specified column and row.
     *         Returns null if the specified indices are out of bounds.
     */
    public SubImage getSubImage(int column, int row) {
        return subImages[column][row];
    }

    /**
     * Retrieves a {@link SubImage} from the tile sheet based on a 1D index.
     * The index is translated into 2D coordinates within the tile sheet, using
     * the tile sheet's width and height.
     *
     * @param index the 1D index of the desired tile in the sheet.
     *              Uses a 0-based index and converts it to 2D coordinates for lookup.
     * @return the {@link SubImage} corresponding to the specified index.
     * Returns null if the index is out of bounds.
     */
    public SubImage getSubImage(int index) {
        return subImages[index % tilesWide][index / tilesHigh];
    }

    /**
     * Calculates the index of a tile in a 1D array representation of the tile map
     * based on its 2D coordinates within the tile sheet.
     *
     * @param x the x-coordinate (column) of the tile.
     * @param y the y-coordinate (row) of the tile.
     * @return the index of the tile in the 1D array representation.
     */
    public int getTileIndexFromCoords(int x, int y) {
        return x + (y * tilesWide);
    }
}
