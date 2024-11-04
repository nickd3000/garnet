package com.physmo.garnetexamples.collision;

import com.physmo.garnet.ColorUtils;
import com.physmo.garnet.graphics.Graphics;
import com.physmo.garnet.graphics.TileSheet;
import com.physmo.garnet.structure.Vector3;
import com.physmo.garnet.toolkit.Component;
import com.physmo.garnet.toolkit.simplecollision.ColliderComponent;
import com.physmo.garnet.toolkit.simplecollision.RelativeObject;

import java.util.ArrayList;
import java.util.List;

public class ComponentCollidingSprite extends Component {
    TileSheet tileSheet;

    int mode = 0;

    List<RelativeObject> closeObjects = new ArrayList<>();

    @Override
    public void init() {
        tileSheet = parent.getContext().getObjectByType(TileSheet.class);

        ColliderComponent component = parent.getComponent(ColliderComponent.class);
        component.setCallbackEnter(target -> mode = 2);
        component.setCallbackContinue(target -> {
            if (target.hasTag("testobject")) {
                mode = 1;
            }
        });
        component.setCallbackLeave(target -> mode = 3);
        component.setCallbackProximity(relativeObject -> {
            closeObjects.add(relativeObject); // Just store for now and process the event in the tick function.
        });
    }

    @Override
    public void tick(double t) {

        double minDist = 20;
        double pushForce = 80;
        for (RelativeObject closeObject : closeObjects) {

            if (closeObject.distance > minDist) continue;
            Vector3 transform = parent.getTransform();
            double dx = closeObject.dx / closeObject.distance;
            double dy = closeObject.dy / closeObject.distance;
            transform.x += dx * t * pushForce;
            transform.y += dy * t * pushForce;
        }
        closeObjects.clear();

    }

    @Override
    public void draw(Graphics g) {
        //graphics.setScale(3);
        if (mode == 0) g.setColor(com.physmo.garnet.ColorUtils.SUNSET_GREEN);
        if (mode == 1) g.setColor(com.physmo.garnet.ColorUtils.SUNSET_ORANGE);
        if (mode == 2) g.setColor(ColorUtils.SUNSET_RED);
        if (mode == 3) g.setColor(com.physmo.garnet.ColorUtils.SUNSET_BLUE);
        g.drawImage(tileSheet, (int) parent.getTransform().x, (int) parent.getTransform().y, 2, 2);
    }
}
