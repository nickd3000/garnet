package com.physmo.garnet.graphics;

import java.util.ArrayList;
import java.util.List;

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

    public Animation(Texture texture, int frameWidth, int frameHeight, double totalDuration) {
        this.texture = texture;
        this.frameWidth = frameWidth;
        this.frameHeight = frameHeight;
        this.totalDuration = totalDuration;
        tileSheet = new TileSheet(texture, frameWidth, frameHeight);
    }

    public void addFrame(int x, int y) {
        frameList.add(new FrameInfo(x, y));
        numFrames = frameList.size();
    }

    public void addFrames(int[][] coords) {
        for (int[] coord : coords) {
            frameList.add(new FrameInfo(coord[0], coord[1]));
        }
        numFrames = frameList.size();
    }

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

    public int getCurrentFrameIndex() {
        return currentFrame;
    }

    public SubImage getImage() {
        FrameInfo frameInfo = frameList.get(currentFrame);
        return tileSheet.getSubImage(frameInfo.col, frameInfo.row);
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
