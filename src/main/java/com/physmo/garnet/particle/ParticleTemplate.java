package com.physmo.garnet.particle;

import com.physmo.garnet.Vec3;
import com.physmo.garnet.curve.CurveType;
import com.physmo.garnet.curve.StandardCurve;

public class ParticleTemplate {
    Vec3 position;
    double positionJitter;
    Vec3 velocity;
    double velocityJitter;
    Vec3 force;
    //double friction;

    RangedValue lifeTime;
    RangedValue speed;

    public ParticleTemplate() {
        positionJitter = 2.1;
        position = new Vec3();
        velocity = new Vec3();
        velocityJitter = 0.1;
        force = new Vec3();
        //friction=0.9;
        lifeTime = new RangedValue(0.2, 3);
        speed = new RangedValue(10, 50);
    }

    public void initParticle(Particle p, Vec3 emitterPos) {
        p.active = true;
        p.position = getVectorWithJitter(emitterPos, positionJitter);
        p.lifeTime = lifeTime.getValue();

        p.direction = Vec3.generateRandomRadial2D(Math.random());

        p.age = 0;
        p.speed = speed.getValue();
        p.speedCurve = new StandardCurve(CurveType.LINE_DOWN);
    }

    public Vec3 getVectorWithJitter(Vec3 v, double jitter) {
        Vec3 nv = new Vec3(v);
        if (jitter != 0) {
            nv.x += Math.random() * jitter;
            nv.y += Math.random() * jitter;
        }
        return nv;
    }

}
