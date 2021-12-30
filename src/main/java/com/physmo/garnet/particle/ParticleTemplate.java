package com.physmo.garnet.particle;

import com.physmo.garnet.Vec3;

public class ParticleTemplate {
    Vec3 position;
    double positionJitter;
    Vec3 velocity;
    double velocityJitter;
    Vec3 force;
    double friction;

    double lifeTime;

    public void initParticle(Particle p) {
        p.position=getVectorWithJitter(position, positionJitter);
    }

    public Vec3 getVectorWithJitter(Vec3 v, double jitter) {
        Vec3 nv = new Vec3(v);
        if (jitter!=0) {
            nv.x+=Math.random()*jitter;
            nv.y+=Math.random()*jitter;
        }
        return nv;
    }

}
