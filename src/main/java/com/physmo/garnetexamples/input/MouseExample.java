package com.physmo.garnetexamples.input;

import com.physmo.garnet.ColorUtils;
import com.physmo.garnet.FileUtils;
import com.physmo.garnet.Garnet;
import com.physmo.garnet.GarnetApp;
import com.physmo.garnet.graphics.Graphics;
import com.physmo.garnet.graphics.Texture;
import com.physmo.garnet.graphics.TileSheet;
import com.physmo.garnet.input.Input;
import com.physmo.garnet.input.Mouse;

import java.io.InputStream;

// NOTE: On MacOS the following VM argument is required: -XstartOnFirstThread
public class MouseExample extends GarnetApp {

    String imageFileName = "space.png";
    TileSheet tileSheet;
    Texture texture;
    Input input;
    boolean mousePressed = false;
    double scale = 3;

    public MouseExample(Garnet garnet, String name) {
        super(garnet, name);
    }

    public static void main(String[] args) {
        Garnet garnet = new Garnet(400, 400);
        GarnetApp app = new MouseExample(garnet, "");

        garnet.setApp(app);

        garnet.init();
        garnet.run();
    }


    @Override
    public void init(Garnet garnet) {
        InputStream inputStream = FileUtils.getFileFromResourceAsStream(imageFileName);
        texture = Texture.loadTexture(inputStream);
        tileSheet = new TileSheet(texture, 16, 16);
        garnet.getGraphics().addTexture(texture);
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

        int[] mp, mps;
        double[] mpn;

        mps = input.getMouse().getPositionScaled(scale);
        mp = input.getMouse().getPosition();
        mpn = input.getMouse().getPositionNormalised();

        garnet.getDebugDrawer().setUserString("Mouse pos:        ", String.format("%d,%d", mp[0], mp[1]));
        garnet.getDebugDrawer().setUserString("Mouse pos scaled: ", String.format("%d,%d", mps[0], mps[1]));
        garnet.getDebugDrawer().setUserString("Mouse normalised: ", String.format("%.2f,%.2f", mpn[0], mpn[1]));

        g.setZoom(scale);

        if (mousePressed) g.setColor(ColorUtils.SUNSET_RED);
        else g.setColor(com.physmo.garnet.ColorUtils.SUNSET_YELLOW);

        g.drawImage(tileSheet, mps[0] - 8, mps[1] - 8, 2, 2);

    }

}
