package com.physmo.garnet.toolkit.tick;

/**
 * Common contract for objects that update once per game loop tick.
 */
public interface Tickable {

    /**
     * Advance this object by the elapsed frame time.
     *
     * @param t elapsed time since the previous tick, in seconds.
     */
    void tick(double t);

    /**
     * Add this tickable to a pool and return it for fluent initialization.
     *
     * <p>The generic return keeps concrete assignments ergonomic, for example
     * {@code Timer timer = new Timer().addToPool(pool);}. The cast is safe when
     * callers assign the result to the object's own concrete type.</p>
     *
     * @param tickPool pool that should update this tickable on subsequent ticks.
     * @return this tickable.
     */
    @SuppressWarnings("unchecked")
    default <T extends Tickable> T addToPool(TickPool tickPool) {
        if (tickPool == null) {
            throw new IllegalArgumentException("tickPool must not be null");
        }
        tickPool.add(this);
        return (T) this;
    }
}
