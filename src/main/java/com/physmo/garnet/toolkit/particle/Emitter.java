package com.physmo.garnet.toolkit.particle;


import com.physmo.garnet.structure.Vector3;
import com.physmo.garnet.toolkit.curve.Curve;
import com.physmo.garnet.toolkit.curve.CurveType;
import com.physmo.garnet.toolkit.curve.StandardCurve;

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
