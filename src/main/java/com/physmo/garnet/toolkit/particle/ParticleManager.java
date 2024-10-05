package com.physmo.garnet.toolkit.particle;


import com.physmo.garnet.toolkit.GameObject;

import java.util.ArrayList;
import java.util.List;

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

    public void setParticleDrawer(ParticleDrawer particleDrawer) {
        this.particleDrawer = particleDrawer;
    }

    @Override
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

    @Override
    public void draw() {
        for (Particle particle : particles) {
            if (particle.active) particleDrawer.draw(particle);
        }
    }

//    public SpriteBatch getSpriteBatch() {
//        return spriteBatch;
//    }

//    public void setSpriteBatch(SpriteBatch spriteBatch) {
//        this.spriteBatch = spriteBatch;
//    }

    public void addEmitter(Emitter emitter) {
        emitter.setParticleManager(this);
        emitterList.add(emitter);
    }
}
