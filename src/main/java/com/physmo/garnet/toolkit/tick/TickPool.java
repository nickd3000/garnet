package com.physmo.garnet.toolkit.tick;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Groups multiple {@link Tickable} instances behind a single {@link Tickable}.
 *
 * <p>Tickables are updated in insertion order. The pool is intentionally small
 * and does not guard against the list being modified while a tick is in
 * progress.</p>
 */
public class TickPool implements Tickable {

    List<Tickable> tickables = new ArrayList<>();

    /**
     * Create a pool with an optional initial set of tickables.
     *
     * @param tickables tickables to add immediately, in update order.
     */
    public TickPool(Tickable... tickables) {
        add(tickables);
    }

    /**
     * Add multiple tickables to the end of the update list.
     *
     * @param tickables objects to update on subsequent ticks.
     */
    public void add(Tickable... tickables) {
        this.tickables.addAll(Arrays.asList(tickables));
    }

    /**
     * Add one tickable to the end of the update list.
     *
     * @param tickable object to update on subsequent ticks.
     */
    public void add(Tickable tickable) {
        tickables.add(tickable);
    }

    /**
     * Remove the first matching tickable from the pool.
     *
     * @param tickable object to stop updating.
     */
    public void remove(Tickable tickable) {
        tickables.remove(tickable);
    }

    /**
     * Remove all tickables from the pool.
     */
    public void clear() {
        tickables.clear();
    }

    /**
     * Update every pooled tickable in insertion order.
     *
     * @param t elapsed time since the previous tick, in seconds.
     */
    @Override
    public void tick(double t) {
        for (Tickable tickable : tickables) {
            tickable.tick(t);
        }
    }
}
