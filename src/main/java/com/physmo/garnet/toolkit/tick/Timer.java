package com.physmo.garnet.toolkit.tick;

/**
 * Stateful countdown timer driven only by {@link #tick(double)}.
 *
 * <p>Timers do not run callbacks. Use them when gameplay code wants to query
 * elapsed time, remaining time, progress, or completion explicitly.</p>
 */
public class Timer implements Tickable {

    private double duration;
    private double elapsed;
    private boolean running;
    private boolean complete;

    /**
     * Create a stopped timer with zero duration.
     */
    public Timer() {
        this(0.0);
    }

    /**
     * Create a stopped timer with a reusable duration.
     *
     * @param duration duration in seconds. Must not be negative.
     */
    public Timer(double duration) {
        validateDuration(duration);
        this.duration = duration;
    }

    private static void validateDuration(double duration) {
        if (duration < 0.0) {
            throw new IllegalArgumentException("duration must not be negative");
        }
    }

    /**
     * Restart this timer using its current duration.
     */
    public void restart() {
        start(duration);
    }

    /**
     * Start this timer from zero elapsed time.
     *
     * @param duration duration in seconds. Must not be negative.
     */
    public void start(double duration) {
        validateDuration(duration);
        this.duration = duration;
        elapsed = 0.0;
        complete = duration == 0.0;
        running = duration > 0.0;
    }

    /**
     * Stop this timer without changing elapsed time or completion state.
     */
    public void stop() {
        running = false;
    }

    /**
     * Stop this timer and clear elapsed/progress.
     */
    public void reset() {
        elapsed = 0.0;
        running = false;
        complete = false;
    }

    /**
     * @return true while this timer is accumulating elapsed time.
     */
    public boolean isRunning() {
        return running;
    }

    /**
     * @return true after the timer has reached its duration.
     */
    public boolean isComplete() {
        return complete;
    }

    /**
     * @return elapsed time in seconds, clamped to the duration.
     */
    public double getElapsed() {
        return elapsed;
    }

    /**
     * @return remaining time in seconds, never negative.
     */
    public double getRemaining() {
        return Math.max(0.0, duration - elapsed);
    }

    /**
     * @return configured duration in seconds.
     */
    public double getDuration() {
        return duration;
    }

    /**
     * @return completion progress clamped to {@code 0.0..1.0}.
     */
    public double getProgress() {
        if (duration == 0.0) {
            return complete ? 1.0 : 0.0;
        }
        return Math.min(1.0, Math.max(0.0, elapsed / duration));
    }

    /**
     * Advance the timer if it is running.
     *
     * @param delta elapsed time since the previous tick, in seconds.
     */
    @Override
    public void tick(double delta) {
        validateDelta(delta);
        if (!running) {
            return;
        }

        elapsed += delta;
        if (elapsed >= duration) {
            elapsed = duration;
            running = false;
            complete = true;
        }
    }

    static void validateDelta(double delta) {
        if (delta < 0.0) {
            throw new IllegalArgumentException("delta must not be negative");
        }
    }
}
