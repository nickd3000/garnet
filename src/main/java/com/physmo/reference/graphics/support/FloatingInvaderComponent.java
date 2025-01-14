package com.physmo.reference.graphics.support;

import com.physmo.garnet.ColorUtils;
import com.physmo.garnet.graphics.Graphics;
import com.physmo.garnet.graphics.TileSheet;
import com.physmo.garnet.structure.Vector3;
import com.physmo.garnet.toolkit.Component;

public class FloatingInvaderComponent extends Component {
    Vector3 velocity;

    int width;
    int height;
    TileSheet tileSheet;
    int color;

    public FloatingInvaderComponent(int width, int height) {
        this.width = width;
        this.height = height;

        color = ColorUtils.asRGBA((float) Math.random(), (float) Math.random(), (float) Math.random(), 0.5f);
    }

    @Override
    public void init() {
        tileSheet = parent.getContext().getObjectByType(TileSheet.class);
        velocity = new Vector3();
        velocity.set(Math.random() - 0.5, Math.random() - 0.5, 0);
        parent.getTransform().set(Math.random() * 100, Math.random() * 100, 0);
    }

    @Override
    public void tick(double t) {

        Vector3 transform = parent.getTransform();
        transform.translate(velocity);

        // Screen wrapping
        if (transform.x > width) transform.x -= width;
        if (transform.x < 0) transform.x += width;
        if (transform.y > height) transform.y -= height;
        if (transform.y < 0) transform.y += height;
    }


    @Override
    public void draw(Graphics g) {
        if (g == null) return;
        Vector3 transform = parent.getTransform();
        g.setColor(color);
        g.drawImage(tileSheet, (int) transform.x, (int) transform.y, 2, 2);
    }
}
