package com.physmo.garnet.graphics;

import java.util.ArrayList;
import java.util.List;

/**
 * Animation represents frames of a sprite animation by defining sections of a sprite sheet for each frame.
 * The classes tick function should be called with the elapsed time since the last call passed in.
 */
public class Animation {
    Texture texture;
    double totalDuration = 0;
    double currentTime = 0;
    int currentFrame = 0;
    int numFrames = 0;
    int frameWidth;
    int frameHeight;
    TileSheet tileSheet;
    List<FrameInfo> frameList = new ArrayList<>();

    /**
     * Constructs an Animation object
     *
     * @param texture       The texture object to source the image data from.
     * @param frameWidth    The width of each animation frame sub-image
     * @param frameHeight   The height of each animation frame sub-image
     * @param totalDuration The total duration of the animation in seconds
     */
    public Animation(Texture texture, int frameWidth, int frameHeight, double totalDuration) {
        this.texture = texture;
        this.frameWidth = frameWidth;
        this.frameHeight = frameHeight;
        this.totalDuration = totalDuration;
        tileSheet = new TileSheet(texture, frameWidth, frameHeight);
    }

    /**
     * Add a frame definition to the animation based on the tilesheet coordiniates.
     *
     * @param x Column where the tile is located in the tile sheet.
     * @param y Row where the tile is located in the tile sheet.
     */
    public void addFrame(int x, int y) {
        frameList.add(new FrameInfo(x, y));
        numFrames = frameList.size();
    }

    /**
     * Add a list of frame definitions to the animation.
     * Coordinates of each frame are stored in a 2d int array, e.g.
     * {{x1,y1},{x2,y2},{x3,y3}}
     *
     * @param coords 2D array of integer coordinates representing animation frame tiles.
     */
    public void addFrames(int[][] coords) {
        for (int[] coord : coords) {
            frameList.add(new FrameInfo(coord[0], coord[1]));
        }
        numFrames = frameList.size();
    }

    /**
     * Process the animation's logic.
     * This method processes the animations current frame based on elapsed time.
     *
     * @param t Time elapsed in seconds since the last call to tick.
     */
    public void tick(double t) {
        currentTime += t;
        if (currentTime > totalDuration) currentTime -= totalDuration;
        currentFrame = calculateFrameFromTime(currentTime);
    }

    private int calculateFrameFromTime(double t) {
        int f = (int) ((t / totalDuration) * (double) numFrames);
        if (f < 0) return 0;
        if (f >= numFrames) return numFrames - 1;
        return f;
    }

    /**
     * The current frame of the animation as an index of the total number of frames.
     *
     * @return the current frame index
     */
    public int getCurrentFrameIndex() {
        return currentFrame;
    }

    /**
     * Returns the current frame as a SubImage object compatible with image drawing methods.
     *
     * @return A SubImage object representing the image area of the frame.
     */
    public void getImage(SubImage outSubImage) {
        FrameInfo frameInfo = frameList.get(currentFrame);
        tileSheet.getSubImage(frameInfo.col, frameInfo.row, outSubImage);
    }

    private class FrameInfo {
        public int row;
        public int col;

        public FrameInfo(int x, int y) {
            this.col = x;
            this.row = y;
        }
    }
}
