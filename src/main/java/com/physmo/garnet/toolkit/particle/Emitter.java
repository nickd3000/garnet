package com.physmo.garnet.toolkit.particle;


import com.physmo.garnet.structure.Vector3;
import com.physmo.garnet.toolkit.curve.Curve;
import com.physmo.garnet.toolkit.curve.CurveType;
import com.physmo.garnet.toolkit.curve.StandardCurve;

/**
 * Spawns particles from a {@link ParticleTemplate} over a fixed duration.
 * <p>
 * The emission rate follows an {@link com.physmo.garnet.toolkit.curve.Curve} so it can ramp
 * up or down over the emitter's lifetime. Once the duration expires the emitter marks itself
 * for removal by the owning {@link ParticleManager}.
 */
public class Emitter {
    ParticleManager particleManager;
    Vector3 position;

    double duration;
    double age;
    boolean remove = false;

    ParticleTemplate particleTemplate;

    double emitPerSecond = 1500;
    Curve emissionRateCurve = new StandardCurve(CurveType.LINE_DOWN);

    public Emitter(Vector3 position, double duration, ParticleTemplate particleTemplate) {
        this.particleTemplate = particleTemplate;
        this.position = new Vector3(position);
        this.duration = duration;
        this.emissionRateCurve = new StandardCurve(CurveType.LINE_DOWN);
        age = 0;
    }

    public Emitter(Vector3 position, double duration) {
        new Emitter(position, duration, new ParticleTemplate());
    }

    /**
     * Injects the owning {@link ParticleManager} so the emitter can request free particles.
     * Called automatically by {@link ParticleManager#addEmitter}.
     *
     * @param particleManager the manager that owns this emitter
     */
    public void setParticleManager(ParticleManager particleManager) {
        this.particleManager = particleManager;
    }

    /**
     * Advances the emitter by one frame: ages it, calculates how many particles to emit
     * this tick, and marks the emitter for removal when its duration is exceeded.
     *
     * @param delta seconds elapsed since the last tick
     */
    public void tick(double delta) {
        age += delta;
        double pAge = age / duration;
        double chance = emitPerSecond * delta * emissionRateCurve.value(pAge);

        while (chance > 1) {
            chance -= 1;
            emit();
        }

        if (Math.random() < chance) {
            emit();
        }

        if (age > duration) remove = true;
    }

    /**
     * Requests a free particle from the manager and initialises it using the
     * current {@link ParticleTemplate} and this emitter's position.
     */
    public void emit() {
        if (particleTemplate != null) {
            Particle p = particleManager.getFreeParticle();
            if (p != null) {
                particleTemplate.initParticle(p, position);
            }
        }
    }

    /**
     * Replaces the particle template used by this emitter.
     *
     * @param particleTemplate the new template
     */
    public void addParticleTemplate(ParticleTemplate particleTemplate) {
        this.particleTemplate = particleTemplate;
    }

    /**
     * Sets the peak emission rate in particles per second.
     * The actual rate each frame is scaled by the emission-rate curve.
     *
     * @param emitPerSecond the desired peak emission rate
     */
    public void setEmitPerSecond(double emitPerSecond) {
        this.emitPerSecond = emitPerSecond;
    }
}
