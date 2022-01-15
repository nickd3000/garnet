package com.physmo.garnet.particle;

import com.physmo.garnet.Vec3;
import com.physmo.garnet.curve.Curve;
import com.physmo.garnet.curve.CurveType;
import com.physmo.garnet.curve.StandardCurve;

public class Emitter {
    ParticleManager particleManager;
    Vec3 position;

    double duration;
    double age;
    boolean remove = false;

    ParticleTemplate particleTemplate;

    double emitPerSecond = 1500;
    Curve emissionRateCurve;

    public Emitter(Vec3 position, double duration, ParticleTemplate particleTemplate) {
        this.particleTemplate = particleTemplate;
        this.position = new Vec3(position);
        this.duration = duration;
        this.emissionRateCurve = new StandardCurve(CurveType.LINE_DOWN);
        age = 0;
    }

    public Emitter(Vec3 position, double duration) {
        new Emitter(position, duration, new ParticleTemplate());
    }

    public void setParticleManager(ParticleManager particleManager) {
        this.particleManager = particleManager;
    }

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

    public void setEmitPerSecond(double emitPerSecond) {
        this.emitPerSecond = emitPerSecond;
    }
}
