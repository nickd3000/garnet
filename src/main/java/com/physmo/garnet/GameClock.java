package com.physmo.garnet;

public class GameClock {

    int frameCount = 0;
    int logicCount = 0;
    long fpsCheckpointTime = System.nanoTime();
    long logicCheckpointTime = System.nanoTime();
    double fps = 0;
    double lps = 0;

    public double getFps() {
        return fps;
    }

    public double getLps() {
        return lps;
    }

    public void logLogicTick() {
        long curTime = System.nanoTime();
        logicCount++;

        long timeDelta = curTime - logicCheckpointTime;

        if (timeDelta > 1000000000) {
            lps = logicCount;
            logicCount = 0;
            logicCheckpointTime = curTime;
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
        }

    }

}
