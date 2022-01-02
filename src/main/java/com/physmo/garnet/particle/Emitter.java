package com.physmo.garnet.particle;

import com.physmo.garnet.Vec3;

public class Emitter {
    ParticleManager particleManager;
    Vec3 position;

    double duration;
    double age;
    boolean remove = false;

    ParticleTemplate particleTemplate;

    double emitPerSecond = 100;

    public Emitter(Vec3 position, double duration) {
        particleTemplate = new ParticleTemplate();
        this.position = new Vec3(position);
        this.duration = duration;
        age = 0;
    }

    public void setParticleManager(ParticleManager particleManager) {
        this.particleManager = particleManager;
    }

    public void tick(double delta) {
        age += delta;

        double chance = emitPerSecond * delta;
        if (Math.random() < chance) {
            emit();
        }

        if (age > duration) remove = true;
    }

    public void emit() {
        if (particleTemplate != null) {
            Particle p = particleManager.getFreeParticle();
            if (p != null) {
                particleTemplate.initParticle(p, position);
            }
        }
    }

    public void addParticleTemplate(ParticleTemplate particleTemplate) {
        this.particleTemplate = particleTemplate;
    }
}
