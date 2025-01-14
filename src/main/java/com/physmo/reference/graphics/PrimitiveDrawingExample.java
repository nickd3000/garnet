package com.physmo.reference.graphics;

import com.physmo.garnet.ColorUtils;
import com.physmo.garnet.Garnet;
import com.physmo.garnet.GarnetApp;
import com.physmo.garnet.graphics.Graphics;
import com.physmo.garnet.input.Mouse;

// NOTE: On MacOS the following VM argument is required: -XstartOnFirstThread
public class PrimitiveDrawingExample extends GarnetApp {

    public PrimitiveDrawingExample(Garnet garnet, String name) {
        super(garnet, name);
    }

    public static void main(String[] args) {
        Garnet garnet = new Garnet(640, 480);
        GarnetApp app = new PrimitiveDrawingExample(garnet, "");

        garnet.setApp(app);

        garnet.init();
        garnet.run();
    }

    @Override
    public void init(Garnet garnet) {
        garnet.getDisplay().setWindowTitle("Primitive drawing example");
    }

    @Override
    public void tick(double delta) {

    }

    @Override
    public void draw(Graphics g) {

        g.setColor(com.physmo.garnet.ColorUtils.WHITE);

        double[] mpn = garnet.getInput().getMouse().getPositionNormalised();
        if (garnet.getInput().getMouse().isButtonPressed(Mouse.BUTTON_LEFT)) {
            g.setZoom(1 + (mpn[0] * 1.5));
        } else {
            g.setZoom(1);
        }

        g.drawLine(190, 20 + 50, 190 + 100, 20 + 50);

        g.setColor(com.physmo.garnet.ColorUtils.rgb(255, 0, 0, 255));
        g.drawRect(20, 20, 100, 100);

        g.setColor(com.physmo.garnet.ColorUtils.SUNSET_YELLOW);
        g.filledRect(20, 140, 100, 100);

        g.setColor(ColorUtils.SUNSET_YELLOW);
        g.drawCircle(70, 310, 50, 50);

        g.setColor(com.physmo.garnet.ColorUtils.SUNSET_BLUE);
        g.setColor(com.physmo.garnet.ColorUtils.asRGBA(1, 0.5f, 1f, 0.4f));
        g.filledCircle(190, 310, 50, 50);
        g.filledCircle(190 + 50, 310, 50, 50);

    }


}
