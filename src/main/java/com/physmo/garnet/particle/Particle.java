package com.physmo.garnet.particle;

import com.physmo.garnet.Vec3;
import com.physmo.garnet.color.Color;
import com.physmo.garnet.color.ColorSupplier;
import com.physmo.garnet.color.ColorSupplierLinear;
import com.physmo.garnet.curve.Curve;
import com.physmo.garnet.spritebatch.Sprite2D;
import com.physmo.garnet.spritebatch.SpriteBatch;


public class Particle {
    Vec3 position = new Vec3();
    Vec3 direction = new Vec3();
    double speed = 5;
    Curve speedCurve;
    Vec3 force = new Vec3();

    boolean active = false;

    double lifeTime;
    double age;
    ColorSupplier colorSupplier = new ColorSupplierLinear(Color.YELLOW, new Color(1, 0, 0, 0));

    public void tick(double delta) {
        double pAge = age / lifeTime;
        double _speed = speed * delta * speedCurve.value(pAge);

        position.x += direction.x * _speed;
        position.y += direction.y * _speed;
        position.z += direction.z * _speed;

//        direction.x += force.x * delta;
//        direction.y += force.y * delta;
//        direction.z += force.z * delta;

//        direction.x -= direction.x * friction * delta;
//        direction.y -= direction.y * friction * delta;
//        direction.z -= direction.z * friction * delta;

        age += delta;
        if (age > lifeTime) active = false;
    }

    public void draw(SpriteBatch sb) {
        Sprite2D spr = Sprite2D.build(
                (int) (position.x) - 8,
                (int) (position.y) - 8,
                16, 16, 16 * 3, 0, 16, 16);

        float pAge = (float) (age / lifeTime);

        spr.addColor(colorSupplier.getColor(pAge));

        sb.add(spr);
    }
}
