package com.physmo.garnet.clock;

/**
 * The GameClock class is responsible for tracking the frame count and logic updates per second.
 * It provides methods to log logic ticks and frames, and calculates the logic updates per second (LPS)
 * and frames per second (FPS).
 */
public class GameClock {

    private static final long ONE_SECOND_IN_NANOS = 1_000_000_000L;
    public static int TIMER_RENDER = 0;
    public static int TIMER_LOGIC_AND_RENDER = 1;
    public static int TIMER_DEBUG = 2;
    int frameCount = 0;
    int logicCount = 0;
    long fpsCheckpointTime = System.nanoTime();
    long logicCheckpointTime = System.nanoTime();
    double fps = 0;
    double lps = 0;
    long[] logicTicksPerFrame = new long[256];
    TimeIntervalTracker[] timers = new TimeIntervalTracker[10];
    int logicTicksPerFrameIndex = 0;


    public GameClock() {
        for (int i = 0; i < timers.length; i++) {
            timers[i] = new TimeIntervalTracker();
        }
    }

    public TimeIntervalTracker getTimer(int index) {
        return timers[index];
    }


    /**
     * Returns the current frames per second (FPS) value.
     *
     * @return the current FPS value
     */
    public double getFps() {
        return fps;
    }

    /**
     * Retrieves the current logic updates per second (LPS) value.
     *
     * @return the current LPS value
     */
    public double getLps() {
        return lps;
    }


    /**
     * Logs a logic update tick and updates the logic updates per second (LPS) count.
     * <p>
     * This method should be called whenever a logic update occurs. It increments
     * the logic update counter and checks if a second has passed since the last LPS
     * checkpoint. If so, it updates the LPS value, resets the logic update counter,
     * and sets a new checkpoint time.
     */
    public void logLogicTick() {
        long curTime = System.nanoTime();
        logicCount++;

        long timeDelta = curTime - logicCheckpointTime;

        if (timeDelta > ONE_SECOND_IN_NANOS) {
            lps = logicCount;
            logicCount = 0;
            logicCheckpointTime = curTime;
        }

        logicTicksPerFrame[logicTicksPerFrameIndex]++;
    }

    public void logFrame() {
        long curTime = System.nanoTime();
        frameCount++;

        long timeDelta = curTime - fpsCheckpointTime;

        if (timeDelta > ONE_SECOND_IN_NANOS) {
            fps = frameCount;
            frameCount = 0;
            fpsCheckpointTime = curTime;
        }

    }

}
