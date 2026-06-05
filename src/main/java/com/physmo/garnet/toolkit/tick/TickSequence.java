package com.physmo.garnet.toolkit.tick;

import java.util.ArrayList;
import java.util.List;

/**
 * Fluent sequence of immediate callbacks and timed waits.
 */
public class TickSequence implements Tickable {

    private static final int MAX_STEPS_PER_TICK = 10_000;

    private final List<Step> steps = new ArrayList<>();
    private int currentStep;
    private double waitRemaining;
    private boolean running;
    private boolean complete;
    private boolean looping;

    /**
     * Append a wait step.
     *
     * @param duration duration in seconds. Must not be negative.
     * @return this sequence.
     */
    public TickSequence waitFor(double duration) {
        if (duration < 0.0) {
            throw new IllegalArgumentException("duration must not be negative");
        }
        steps.add(Step.wait(duration));
        return this;
    }

    /**
     * Append an immediate callback step.
     *
     * @param callback callback to run when this step is reached.
     * @return this sequence.
     */
    public TickSequence then(Runnable callback) {
        if (callback == null) {
            throw new IllegalArgumentException("callback must not be null");
        }
        steps.add(Step.callback(callback));
        return this;
    }

    /**
     * Mark this sequence as looping.
     *
     * @return this sequence.
     */
    public TickSequence loop() {
        validateLoopable();
        looping = true;
        complete = false;
        return this;
    }

    private void validateLoopable() {
        if (steps.isEmpty()) {
            throw new IllegalStateException("looping sequence must contain at least one step");
        }
        for (Step step : steps) {
            if (step.callback == null && step.duration > 0.0) {
                return;
            }
        }
        throw new IllegalStateException("looping sequence must contain at least one positive wait step");
    }

    /**
     * Start this sequence from the beginning.
     */
    public void start() {
        if (looping) {
            validateLoopable();
        }
        currentStep = 0;
        complete = false;
        running = !steps.isEmpty();
        prepareCurrentStep();
        if (steps.isEmpty()) {
            complete = true;
        }
    }

    private void prepareCurrentStep() {
        if (currentStep < steps.size() && steps.get(currentStep).callback == null) {
            waitRemaining = steps.get(currentStep).duration;
        } else {
            waitRemaining = 0.0;
        }
    }

    /**
     * Stop this sequence without clearing loop behavior.
     */
    public void stop() {
        running = false;
    }

    /**
     * Rewind to the first step without clearing loop behavior.
     */
    public void reset() {
        currentStep = 0;
        complete = false;
        running = false;
        prepareCurrentStep();
    }

    /**
     * @return true while this sequence is processing ticks.
     */
    public boolean isRunning() {
        return running;
    }

    /**
     * @return true after a non-looping sequence reaches the end.
     */
    public boolean isComplete() {
        return complete;
    }

    /**
     * @return true when this sequence is configured to loop.
     */
    public boolean isLooping() {
        return looping;
    }

    /**
     * Advance this sequence by elapsed time.
     *
     * @param delta elapsed time since the previous tick, in seconds.
     */
    @Override
    public void tick(double delta) {
        Timer.validateDelta(delta);
        if (!running) {
            return;
        }

        double remainingDelta = delta;
        int processedSteps = 0;
        while (running && currentStep < steps.size()) {
            if (++processedSteps > MAX_STEPS_PER_TICK) {
                throw new IllegalStateException("too many sequence steps processed in one tick");
            }

            Step step = steps.get(currentStep);
            if (step.callback != null) {
                step.callback.run();
                moveToNextStep();
                continue;
            }

            if (waitRemaining > remainingDelta) {
                waitRemaining -= remainingDelta;
                return;
            }

            remainingDelta -= waitRemaining;
            moveToNextStep();
        }
    }

    private void moveToNextStep() {
        currentStep++;
        if (currentStep >= steps.size()) {
            if (looping) {
                currentStep = 0;
                complete = false;
            } else {
                running = false;
                complete = true;
                return;
            }
        }
        prepareCurrentStep();
    }

    private static class Step {
        private final double duration;
        private final Runnable callback;

        private Step(double duration, Runnable callback) {
            this.duration = duration;
            this.callback = callback;
        }

        private static Step wait(double duration) {
            return new Step(duration, null);
        }

        private static Step callback(Runnable callback) {
            return new Step(0.0, callback);
        }
    }
}
