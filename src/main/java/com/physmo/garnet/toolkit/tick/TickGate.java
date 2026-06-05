package com.physmo.garnet.toolkit.tick;

/**
 * Throttles logic by returning true only after a fixed interval has elapsed.
 *
 * <p>This class intentionally does not implement {@link Tickable}: calling
 * {@link #allow(double)} both advances and queries the gate.</p>
 */
public class TickGate {

    private final double interval;
    private double accumulated;

    /**
     * Create a gate that opens once per interval.
     *
     * @param interval interval in seconds. Must be greater than zero.
     */
    public TickGate(double interval) {
        if (interval <= 0.0) {
            throw new IllegalArgumentException("interval must be greater than zero");
        }
        this.interval = interval;
    }

    /**
     * Advance the gate and report whether this call is allowed.
     *
     * <p>Large deltas allow once and carry surplus time forward.</p>
     *
     * @param delta elapsed time since the previous call, in seconds.
     * @return true when the accumulated time reaches the interval.
     */
    public boolean allow(double delta) {
        Timer.validateDelta(delta);
        accumulated += delta;
        if (accumulated < interval) {
            return false;
        }
        accumulated -= interval;
        return true;
    }

    /**
     * Clear accumulated time.
     */
    public void reset() {
        accumulated = 0.0;
    }

    /**
     * @return interval in seconds.
     */
    public double getInterval() {
        return interval;
    }

    /**
     * @return time until the next allowed call, in seconds.
     */
    public double getTimeUntilNextAllow() {
        return Math.max(0.0, interval - accumulated);
    }
}
