package com.physmo.garnet.particle;

import com.physmo.garnet.Vec3;

public class Particle {
    Vec3 position;
    Vec3 velocity;
    Vec3 force;
    double friction;

    boolean active=false;

    double lifeTime;
    double age;

    public void tick(double delta) {
        position.x+=velocity.x*delta;
        position.y+=velocity.y*delta;
        position.z+=velocity.z*delta;

        velocity.x+=force.x*delta;
        velocity.y+=force.y*delta;
        velocity.z+=force.z*delta;

        velocity.x -= velocity.x*friction*delta;
        velocity.y -= velocity.y*friction*delta;
        velocity.z -= velocity.z*friction*delta;
    }
}
