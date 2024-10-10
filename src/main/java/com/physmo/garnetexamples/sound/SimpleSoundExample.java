package com.physmo.garnetexamples.sound;

import com.physmo.garnet.ColorUtils;
import com.physmo.garnet.FileUtils;
import com.physmo.garnet.Garnet;
import com.physmo.garnet.GarnetApp;
import com.physmo.garnet.graphics.Graphics;
import com.physmo.garnet.input.Mouse;
import com.physmo.garnet.text.RegularFont;

// NOTE: On MacOS the following VM argument is required: -XstartOnFirstThread
public class SimpleSoundExample extends GarnetApp {

    RegularFont regularFont;
    int soundA;
    int soundB;

    public SimpleSoundExample(Garnet garnet, String name) {
        super(garnet, name);
    }

    public static void main(String[] args) {
        Garnet garnet = new Garnet(400, 400);
        GarnetApp app = new SimpleSoundExample(garnet, "");

        garnet.setApp(app);

        garnet.init();
        garnet.run();
    }


    @Override
    public void init(Garnet garnet) {
        regularFont = new RegularFont("regularfonts/12x12Font.png", 12, 12);

        soundA = garnet.getSound().loadSound(FileUtils.getPathForResource(this, "sounds/laserShoot-3.wav"));
        soundB = garnet.getSound().loadSound(FileUtils.getPathForResource(this, "sounds/laserShoot.wav"));

        garnet.getSound().playSound(soundA);
        garnet.getSound().playSound(soundB);
    }

    @Override
    public void tick(double delta) {

        if (garnet.getInput().getMouse().isButtonFirstPress(Mouse.BUTTON_LEFT)) {
            garnet.getSound().playSound(soundA);
        }
        if (garnet.getInput().getMouse().isButtonFirstPress(Mouse.BUTTON_RIGHT)) {
            garnet.getSound().playSound(soundB);
        }
    }

    @Override
    public void draw(Graphics g) {

        g.setColor(com.physmo.garnet.ColorUtils.GREEN);
        g.setZoom(2);

        g.setColor(ColorUtils.SUNSET_BLUE);

        regularFont.setHorizontalPad(-4);

        regularFont.drawText(g, "Left / Right mouse", 20, 20);
        regularFont.drawText(g, "button to play", 20, 35);
        regularFont.drawText(g, "sounds", 20, 50);

    }

    public float clamp(float v) {
        if (v < 0) return 0;
        if (v > 1) return 1;
        return v;
    }

}
