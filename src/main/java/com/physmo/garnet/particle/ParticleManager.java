package com.physmo.garnet.particle;

import com.physmo.garnet.spritebatch.SpriteBatch;

import java.util.ArrayList;
import java.util.List;

public class ParticleManager {

    List<Emitter> emitterList;
    List<Particle> particles;
    SpriteBatch spriteBatch;

    public ParticleManager(int numParticles) {
        emitterList = new ArrayList<>();
        particles = new ArrayList<>();

        for (int i = 0; i < numParticles; i++) {
            particles.add(new Particle());
        }
    }

    public void tick(double delta) {
        for (Emitter emitter : emitterList) {
            emitter.tick(delta);
        }

        List<Emitter> emitterList2 = new ArrayList<>();
        for (Emitter emitter : emitterList) {
            if (!emitter.remove) emitterList2.add(emitter);
        }
        emitterList = emitterList2;

        for (Particle particle : particles) {
            if (particle.active)
                particle.tick(delta);
        }

    }

    // TODO: we'll optimise this later.
    public Particle getFreeParticle() {
        for (Particle particle : particles) {
            if (!particle.active) return particle;
        }
        return null;
    }

    public void draw() {
        for (Particle particle : particles) {
            if (particle.active) particle.draw(spriteBatch);
        }
    }

    public void setSpriteBatch(SpriteBatch spriteBatch) {
        this.spriteBatch = spriteBatch;
    }

    public SpriteBatch getSpriteBatch() {
        return spriteBatch;
    }

    public void addEmitter(Emitter emitter) {
        emitter.setParticleManager(this);
        emitterList.add(emitter);
    }
}
