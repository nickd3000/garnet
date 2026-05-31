package com.physmo.garnet.toolkit.particle;


import com.physmo.garnet.ColorUtils;
import com.physmo.garnet.structure.Vector3;
import com.physmo.garnet.toolkit.color.ColorSupplier;
import com.physmo.garnet.toolkit.color.ColorSupplierLinear;
import com.physmo.garnet.toolkit.curve.Curve;

/**
 * A single particle instance managed by a {@link ParticleManager}.
 * <p>
 * Particles are pooled and reused. When {@link #active} is {@code true} the particle is
 * alive and will be ticked and drawn each frame. Fields are populated by a
 * {@link ParticleTemplate} when the particle is spawned.
 */
public class Particle {
    public Vector3 position = new Vector3();
    public double lifeTime;
    public double age;
    public ColorSupplier colorSupplier = new ColorSupplierLinear(new int[]{ColorUtils.YELLOW, ColorUtils.asRGBA(1, 0, 0, 0)});
    Vector3 direction = new Vector3();
    double speed = 5;
    Curve speedCurve;
    Vector3 force = new Vector3();
    boolean active = false;

    Vector3 gravityDirection;
    double gravityForce;

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

    ParticleDrawer particleDrawer;

    public ParticleDrawer getParticleDrawer() {
        return particleDrawer;
    }

    public void setParticleDrawer(ParticleDrawer particleDrawer) {
        this.particleDrawer = particleDrawer;
    }

    /**
     * Advances this particle by one frame: moves it along its direction vector,
     * ages it, and deactivates it when its lifetime is exceeded.
     *
     * @param delta seconds elapsed since the last tick
     */
    public void tick(double delta) {
        double pAge = age / lifeTime;
        double _speed = speed * delta * speedCurve.value(pAge);

        position.x += direction.x * _speed;
        position.y += direction.y * _speed;
        position.z += direction.z * _speed;

        age += delta;
        if (age > lifeTime) active = false;
    }

    /**
     * Returns the normalised age of this particle in the range [0, 1].
     * 0 = just spawned, 1 = end of lifetime.
     *
     * @return normalised age
     */
    public double getTime() {
        return age / lifeTime;
    }
}
