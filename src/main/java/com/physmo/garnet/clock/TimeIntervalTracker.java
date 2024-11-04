package com.physmo.garnet.clock;

/**
 * The TimeIntervalTracker class is designed to measure and store time intervals.
 * It uses an internal array to keep track of the elapsed times between calls to
 * its start and stop methods. These times are stored in seconds.
 */
public class TimeIntervalTracker {
    private final double[] times;
    private long startTime = 0;
    private int timeIndex;

    public TimeIntervalTracker() {
        times = new double[256];
        timeIndex = 0;
    }

    /**
     * Retrieves the array of recorded time intervals.
     *
     * @return an array of double values representing the elapsed times in seconds.
     */
    public double[] getTimes() {
        return times;
    }

    /**
     * Starts the timer by recording the current system time in nanoseconds.
     * This method sets the `startTime` variable to the current value of
     * {@link System#nanoTime()}, which can be used later to measure the
     * elapsed time.
     */
    public void start() {
        startTime = System.nanoTime();
    }

    /**
     * Stops the timer by calculating the elapsed time since the start method was called.
     * The elapsed time is converted from nanoseconds to seconds and stored in the `times` array.
     * It also updates the index for the next time interval measurement.
     */
    public void stop() {
        long elapsedTime = System.nanoTime() - startTime;
        times[timeIndex] = elapsedTime / 1_000_000_000.0;
        timeIndex = (timeIndex + 1) % times.length;
    }
}
