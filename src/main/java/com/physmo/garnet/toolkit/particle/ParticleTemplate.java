package com.physmo.garnet.toolkit.particle;


import com.physmo.garnet.ColorUtils;
import com.physmo.garnet.structure.Vector3;
import com.physmo.garnet.toolkit.color.ColorSupplier;
import com.physmo.garnet.toolkit.color.ColorSupplierLinear;
import com.physmo.garnet.toolkit.curve.Curve;
import com.physmo.garnet.toolkit.curve.CurveType;
import com.physmo.garnet.toolkit.curve.StandardCurve;

public class ParticleTemplate {
    Vector3 position;
    double positionJitter;
    Vector3 velocity;
    double velocityJitter;
    Vector3 force;

    Vector3 gravityDirection;
    double gravityForce;
    RangedValue lifeTime;
    RangedValue speed;
    Curve speedCurve;
    ColorSupplier colorSupplier;
    ParticleDrawer particleDrawer;
    public ParticleTemplate() {
        positionJitter = 2.1;
        position = new Vector3();
        velocity = new Vector3();
        gravityDirection = new Vector3();
        velocityJitter = 0.1;
        force = new Vector3();
        gravityForce = 0.0;
        //friction=0.9;
        lifeTime = new RangedValue(0.2, 3);
        speed = new RangedValue(10, 50);
        speedCurve = new StandardCurve(CurveType.LINE_DOWN);
        colorSupplier = new ColorSupplierLinear(new int[]{ColorUtils.YELLOW, ColorUtils.asRGBA(1, 0, 0, 0)});
    }

    public Vector3 getGravityDirection() {
        return gravityDirection;
    }

    public void setGravityDirection(Vector3 gravityDirection) {
        this.gravityDirection = gravityDirection;
    }

    public double getGravityForce() {
        return gravityForce;
    }

    public void setGravityForce(double gravityForce) {
        this.gravityForce = gravityForce;
    }

    public ParticleDrawer getParticleDrawer() {
        return particleDrawer;
    }

    public void setParticleDrawer(ParticleDrawer particleDrawer) {
        this.particleDrawer = particleDrawer;
    }

    public void initExplosion() {
        positionJitter = 2.1;
        position = new Vector3();
        velocity = new Vector3();
        velocityJitter = 0.1;
        force = new Vector3();
        //friction=0.9;
        lifeTime = new RangedValue(0.2, 3);
        speed = new RangedValue(10, 50);
    }

    public void setPositionJitter(double positionJitter) {
        this.positionJitter = positionJitter;
    }

    public void setLifeTime(double min, double max) {
        lifeTime = new RangedValue(min, max);
    }

    public void setSpeed(double min, double max) {
        speed = new RangedValue(min, max);
    }

    public void setSpeedCurve(Curve speedCurve) {
        this.speedCurve = speedCurve;
    }

    public void setColorSupplier(ColorSupplier colorSupplier) {
        this.colorSupplier = colorSupplier;
    }

    public void initParticle(Particle p, Vector3 emitterPos) {
        p.active = true;
        p.position = getVectorWithJitter(emitterPos, positionJitter);
        p.lifeTime = lifeTime.getValue();

        p.direction = Vector3.generateRandomRadial2D(Math.random());

        p.age = 0;
        p.speed = speed.getValue();
        p.speedCurve = speedCurve;
        p.colorSupplier = colorSupplier;
        p.particleDrawer = particleDrawer;

    }

    public Vector3 getVectorWithJitter(Vector3 v, double jitter) {
        Vector3 nv = new Vector3(v);
        if (jitter != 0) {
            nv.x += Math.random() * jitter;
            nv.y += Math.random() * jitter;
        }
        return nv;
    }

}
