package com.physmo.garnet.toolkit.tick;

import java.util.function.DoubleConsumer;

/**
 * One-shot countdown event driven by the game tick.
 *
 * <p>Once started, the event counts down by the elapsed tick time. While active,
 * it can run a per-tick callback; when the countdown expires, it can run a
 * completion callback and then becomes inactive.</p>
 */
public class TimedEvent implements Tickable {

    boolean active;
    double time;
    Runnable onEndRunnable;
    DoubleConsumer tickRunnable;

    /**
     * Create an inactive event that must be ticked manually or added to a
     * {@link TickPool} separately.
     */
    public TimedEvent() {
    }

    /**
     * Advance the countdown and dispatch callbacks if the event is active.
     *
     * <p>The per-tick callback receives the elapsed tick time. If this tick
     * causes the timer to expire, only the completion callback runs.</p>
     *
     * @param t elapsed time since the previous tick, in seconds.
     */
    @Override
    public void tick(double t) {
        if (!active) return;

        time -= t;
        if (time < 0) {
            active = false;
            if (onEndRunnable != null) onEndRunnable.run();
        } else {
            if (tickRunnable != null) tickRunnable.accept(t);
        }
    }

    /**
     * @return true while the countdown is running.
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Start a countdown with no callbacks.
     *
     * @param time duration to count down, in seconds.
     */
    public void start(double time) {
        active = true;
        this.time = time;
        this.tickRunnable = null;
        this.onEndRunnable = null;
    }

    /**
     * Start a countdown and run a callback on each active tick.
     *
     * @param time duration to count down, in seconds.
     * @param r    callback that receives each tick's elapsed time.
     */
    public void startAndWhileRunning(double time, DoubleConsumer r) {
        active = true;
        this.time = time;
        this.tickRunnable = r;
        this.onEndRunnable = null;
    }

    /**
     * Start a countdown and run a callback when it expires.
     *
     * @param time duration to count down, in seconds.
     * @param r    callback to run once when the event completes.
     */
    public void startAndOnEnd(double time, Runnable r) {
        active = true;
        this.time = time;
        this.tickRunnable = null;
        this.onEndRunnable = r;
    }

    /**
     * Start a countdown with both per-tick and completion callbacks.
     *
     * @param time duration to count down, in seconds.
     * @param r    callback that receives each tick's elapsed time while active.
     * @param r2   callback to run once when the event completes.
     */
    public void startAndWhileRunningAndOnEnd(double time, DoubleConsumer r, Runnable r2) {
        active = true;
        this.time = time;
        this.tickRunnable = r;
        this.onEndRunnable = r2;
    }
}
