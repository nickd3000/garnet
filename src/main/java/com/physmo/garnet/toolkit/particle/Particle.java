package com.physmo.garnet.toolkit.particle;


import com.physmo.garnet.ColorUtils;
import com.physmo.garnet.structure.Vector3;
import com.physmo.garnet.toolkit.color.ColorSupplier;
import com.physmo.garnet.toolkit.color.ColorSupplierLinear;
import com.physmo.garnet.toolkit.curve.Curve;

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

    ParticleDrawer particleDrawer;

    public ParticleDrawer getParticleDrawer() {
        return particleDrawer;
    }

    public void setParticleDrawer(ParticleDrawer particleDrawer) {
        this.particleDrawer = particleDrawer;
    }

    public void tick(double delta) {
        double pAge = age / lifeTime;
        double _speed = speed * delta * speedCurve.value(pAge);

        position.x += direction.x * _speed;
        position.y += direction.y * _speed;
        position.z += direction.z * _speed;

        age += delta;
        if (age > lifeTime) active = false;
    }

    public double getTime() {
        return age / lifeTime;
    }
}
