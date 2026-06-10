package com.physmo.reference.toolkit;

import com.physmo.garnet.ColorUtils;
import com.physmo.garnet.Garnet;
import com.physmo.garnet.GarnetApp;
import com.physmo.garnet.graphics.Graphics;
import com.physmo.garnet.structure.Vector3;

public class VectorAngleExample extends GarnetApp {

    Vector3 screenMidPoint = new Vector3(200, 200, 0);
    double angleToMouse = 0;
    Vector3 vectorToMouse;

    public static void main(String[] args) {
        Garnet.launch(400, 400, VectorAngleExample::new);
    }

    @Override
    public void init() {
        garnet.getDebugDrawer().setVisible(true);
        garnet.getDisplay().setWindowScale(1, true);
    }

    @Override
    public void tick(double delta) {
        int[] mousePosition = garnet.getInput().getMouse().getPosition();

        vectorToMouse = new Vector3(mousePosition[0] - screenMidPoint.x, mousePosition[1] - screenMidPoint.y, 0);

        angleToMouse = vectorToMouse.getAngle();

        garnet.getDebugDrawer().setUserString("angle to mouse:", String.format("%.2f", angleToMouse));
    }

    @Override
    public void draw(Graphics g) {
        float lineThickness = 3;

        // Draw the vector to the actual mouse position in yellow.
        g.setColor(ColorUtils.SUNSET_YELLOW);
        if (vectorToMouse != null) {
            g.drawLine(200.0f, 200.0f, 200 + (float) vectorToMouse.x, 200 + (float) vectorToMouse.y, lineThickness);
        }

        // Draw the same vector computed from the angle in pink
        g.setColor(ColorUtils.VIOLET);
        Vector3 vtm = new Vector3(0, 0, 0);
        vtm.setFromAngle(angleToMouse, 50);
        g.drawLine(200.0f, 200.0f, 200 + (float) vtm.x, 200 + (float) vtm.y, lineThickness);

    }
}
