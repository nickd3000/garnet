package com.physmo.reference.input;

import com.physmo.garnet.ColorUtils;
import com.physmo.garnet.Garnet;
import com.physmo.garnet.GarnetApp;
import com.physmo.garnet.graphics.Graphics;
import com.physmo.garnet.graphics.TileSheet;
import com.physmo.garnet.input.Input;
import com.physmo.garnet.input.Mouse;

// NOTE: On MacOS the following VM argument is required: -XstartOnFirstThread
public class MouseExample extends GarnetApp {

    String imageFileName = "space.png";
    TileSheet tileSheet;
    Input input;
    boolean mousePressed = false;
    double scale = 3;

    public static void main(String[] args) {
        Garnet.launch(400, 400, MouseExample::new);
    }

    @Override
    public void init() {
        tileSheet = garnet.getGraphics().loadTileSheet(imageFileName, 16, 16);
        garnet.getGraphics().setBackgroundColor(ColorUtils.WINTER_BLACK);
        input = garnet.getInput();
        garnet.getDebugDrawer().setVisible(true);
        garnet.getDebugDrawer().setScale(1.5);
    }

    @Override
    public void tick(double delta) {
        mousePressed = input.getMouse().isButtonPressed(Mouse.BUTTON_LEFT);
    }

    @Override
    public void draw(Graphics g) {

        int[] mp;
        double[] mpn;

        mp = input.getMouse().getPosition();
        mpn = input.getMouse().getPositionNormalised();

        garnet.getDebugDrawer().setUserString("Mouse pos:        ", String.format("%d,%d", mp[0], mp[1]));
        garnet.getDebugDrawer().setUserString("Mouse normalised: ", String.format("%.2f,%.2f", mpn[0], mpn[1]));

        g.setZoom(scale);

        if (mousePressed) g.setColor(ColorUtils.SUNSET_RED);
        else g.setColor(com.physmo.garnet.ColorUtils.SUNSET_YELLOW);

        g.drawImage(tileSheet, mp[0] - 8, mp[1] - 8, 2, 2);

    }

}
