package com.physmo.garnet.clock;

public class Timer {
    private final double[] times;
    private long startTime = 0;
    private int timeIndex;

    public Timer() {
        times = new double[256];
        timeIndex = 0;
    }

    public double[] getTimes() {
        return times;
    }

    public void start() {
        startTime = System.nanoTime();
    }

    public void stop() {
        long elapsedTime = System.nanoTime() - startTime;
        times[timeIndex] = elapsedTime / 1_000_000_000.0;
        timeIndex = (timeIndex + 1) % times.length;
    }
}
