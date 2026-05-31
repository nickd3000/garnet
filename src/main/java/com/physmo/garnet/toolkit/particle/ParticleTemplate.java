package com.physmo.garnet.toolkit.particle;


import com.physmo.garnet.ColorUtils;
import com.physmo.garnet.structure.Vector3;
import com.physmo.garnet.toolkit.color.ColorSupplier;
import com.physmo.garnet.toolkit.color.ColorSupplierLinear;
import com.physmo.garnet.toolkit.curve.Curve;
import com.physmo.garnet.toolkit.curve.CurveType;
import com.physmo.garnet.toolkit.curve.StandardCurve;

/**
 * Defines the initial properties used to configure a {@link Particle} when it is spawned.
 * <p>
 * A template is attached to an {@link Emitter} and applied each time a new particle is
 * emitted. Properties such as lifetime, speed, colour, and position jitter can be
 * customised before the emitter is added to a {@link ParticleManager}.
 */
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

    /**
     * Resets this template to default explosion-style settings: short-to-medium lifetime,
     * moderate speed range, and small position jitter.
     */
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

    /**
     * Sets the random position offset applied to each spawned particle.
     * A value of 0 means all particles start exactly at the emitter position.
     *
     * @param positionJitter the maximum random offset in world units
     */
    public void setPositionJitter(double positionJitter) {
        this.positionJitter = positionJitter;
    }

    /**
     * Sets the random lifetime range for spawned particles.
     *
     * @param min minimum lifetime in seconds
     * @param max maximum lifetime in seconds
     */
    public void setLifeTime(double min, double max) {
        lifeTime = new RangedValue(min, max);
    }

    /**
     * Sets the random speed range for spawned particles.
     *
     * @param min minimum speed in world units per second
     * @param max maximum speed in world units per second
     */
    public void setSpeed(double min, double max) {
        speed = new RangedValue(min, max);
    }

    /**
     * Sets the curve that controls how particle speed changes over its lifetime.
     *
     * @param speedCurve the speed curve to apply
     */
    public void setSpeedCurve(Curve speedCurve) {
        this.speedCurve = speedCurve;
    }

    /**
     * Sets the colour supplier used to determine each particle's colour over its lifetime.
     *
     * @param colorSupplier the colour supplier to use
     */
    public void setColorSupplier(ColorSupplier colorSupplier) {
        this.colorSupplier = colorSupplier;
    }

    /**
     * Initialises a pooled {@link Particle} with values drawn from this template.
     * Called by {@link Emitter#emit()} each time a particle is spawned.
     *
     * @param p          the particle to initialise
     * @param emitterPos the world position of the emitter
     */
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

    /**
     * Returns a copy of {@code v} with a random offset applied to x and y.
     *
     * @param v      the base vector
     * @param jitter the maximum random offset in each axis
     * @return a new vector with jitter applied
     */
    public Vector3 getVectorWithJitter(Vector3 v, double jitter) {
        Vector3 nv = new Vector3(v);
        if (jitter != 0) {
            nv.x += Math.random() * jitter;
            nv.y += Math.random() * jitter;
        }
        return nv;
    }

}
