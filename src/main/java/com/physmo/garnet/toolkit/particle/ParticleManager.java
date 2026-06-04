package com.physmo.garnet.toolkit.particle;


import com.physmo.garnet.graphics.Graphics;
import com.physmo.garnet.toolkit.GameObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages a fixed-size pool of {@link Particle} objects and a list of active {@link Emitter}s.
 * <p>
 * Add emitters via {@link #addEmitter}; the manager ticks them each frame, spawning particles
 * from the pool as needed. Rendering is delegated to a {@link ParticleDrawer} set via
 * {@link #setParticleDrawer}, or to per-particle drawers if configured on individual particles.
 */
public class ParticleManager extends GameObject {

    List<Emitter> emitterList;
    List<Particle> particles;
    ParticleDrawer particleDrawer;

    public ParticleManager(int numParticles) {
        super("particle manager");

        emitterList = new ArrayList<>();
        particles = new ArrayList<>();

        for (int i = 0; i < numParticles; i++) {
            particles.add(new Particle());
        }
    }

    /**
     * Sets the default {@link ParticleDrawer} used to render particles that do not have
     * their own per-particle drawer.
     *
     * @param particleDrawer the drawer to use
     */
    public void setParticleDrawer(ParticleDrawer particleDrawer) {
        this.particleDrawer = particleDrawer;
    }

    @Override
    public void tick(double delta) {
        for (Emitter emitter : emitterList) {
            emitter.tick(delta);
        }

        emitterList.removeIf(e -> e.remove);

        for (Particle particle : particles) {
            if (particle.active)
                particle.tick(delta);
        }

    }


    /**
     * Returns the first inactive particle from the pool, or {@code null} if the pool is exhausted.
     *
     * @return a free {@link Particle}, or {@code null} if none are available
     */
    // TODO: we'll optimise this later.
    public Particle getFreeParticle() {
        for (Particle particle : particles) {
            if (!particle.active) return particle;
        }
        return null;
    }

    @Override
    public void draw(Graphics g) {
        for (Particle particle : particles) {
            if (!particle.active) continue;
            if (particle.particleDrawer != null) {
                particle.particleDrawer.draw(particle);
            } else {
                particleDrawer.draw(particle);
            }
        }
    }

//    public SpriteBatch getSpriteBatch() {
//        return spriteBatch;
//    }

//    public void setSpriteBatch(SpriteBatch spriteBatch) {
//        this.spriteBatch = spriteBatch;
//    }

    /**
     * Adds an {@link Emitter} to this manager. The emitter will be ticked each frame and
     * automatically removed when its duration expires.
     *
     * @param emitter the emitter to add
     */
    public void addEmitter(Emitter emitter) {
        emitter.setParticleManager(this);
        emitterList.add(emitter);
    }
}
