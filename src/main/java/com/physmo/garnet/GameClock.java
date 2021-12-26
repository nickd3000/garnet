package com.physmo.garnet;

public class GameClock {
    long initTime = System.nanoTime();
    int frameCount = 0;
    int logicCount = 0;
    long fpsCheckpointTime = System.nanoTime();
    long logicCheckpointTime = System.nanoTime();
    double fps = 0;
    double lps = 0;

    public void tick() {
        long l = System.nanoTime();
    }

    public void logLogicTick() {
        long curTime = System.nanoTime();
        logicCount++;

        long timeDelta = curTime - logicCheckpointTime;

        if (timeDelta > 1000000000) {
            lps = logicCount;
            logicCount = 0;
            logicCheckpointTime = curTime;
            //System.out.println("LPS " + lps);
        }
    }

    public void logFrame() {
        long curTime = System.nanoTime();
        frameCount++;

        long timeDelta = curTime - fpsCheckpointTime;

        if (timeDelta > 1000000000) {
            fps = frameCount;
            frameCount = 0;
            fpsCheckpointTime = curTime;
            System.out.println("FPS " + fps);
        }

    }

}
