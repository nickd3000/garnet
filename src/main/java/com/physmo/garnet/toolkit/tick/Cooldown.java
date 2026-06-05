package com.physmo.garnet.toolkit.tick;

/**
 * Rate-limits actions by tracking time until the next use is allowed.
 */
public class Cooldown implements Tickable {

    private final double duration;
    private double remaining;

    /**
     * Create a ready cooldown.
     *
     * @param duration cooldown duration in seconds. Must not be negative.
     */
    public Cooldown(double duration) {
        if (duration < 0.0) {
            throw new IllegalArgumentException("duration must not be negative");
        }
        this.duration = duration;
    }

    /**
     * Use the action if ready.
     *
     * @return true if the cooldown was ready and has now been restarted.
     */
    public boolean tryUse() {
        if (!isReady()) {
            return false;
        }
        use();
        return true;
    }

    /**
     * @return true when the action can be used.
     */
    public boolean isReady() {
        return remaining == 0.0;
    }

    /**
     * Restart the cooldown without checking readiness.
     */
    public void use() {
        remaining = duration;
    }

    /**
     * Make the cooldown ready immediately.
     */
    public void reset() {
        remaining = 0.0;
    }

    /**
     * @return remaining cooldown time in seconds, never negative.
     */
    public double getRemaining() {
        return remaining;
    }

    /**
     * @return progress toward readiness, clamped to {@code 0.0..1.0}.
     */
    public double getProgress() {
        if (duration == 0.0) {
            return 1.0;
        }
        return Math.min(1.0, Math.max(0.0, 1.0 - (remaining / duration)));
    }

    /**
     * Reduce the remaining cooldown time.
     *
     * @param delta elapsed time since the previous tick, in seconds.
     */
    @Override
    public void tick(double delta) {
        Timer.validateDelta(delta);
        remaining = Math.max(0.0, remaining - delta);
    }
}
