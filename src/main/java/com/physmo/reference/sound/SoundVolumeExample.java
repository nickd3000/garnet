package com.physmo.reference.sound;

import com.physmo.garnet.ColorUtils;
import com.physmo.garnet.FileUtils;
import com.physmo.garnet.Garnet;
import com.physmo.garnet.GarnetApp;
import com.physmo.garnet.graphics.Graphics;
import com.physmo.garnet.text.BitmapFont;
import com.physmo.garnet.text.ParagraphDrawer;

import java.io.IOException;

// NOTE: On MacOS the following VM argument is required: -XstartOnFirstThread
public class SoundVolumeExample extends GarnetApp {

    BitmapFont bitmapFont;
    int soundA;
    float time = 0;
    float timeBetweenSounds = 0.5f;
    float volume;
    float masterVolume;


    public SoundVolumeExample(Garnet garnet, String name) {
        super(garnet, name);
    }

    public static void main(String[] args) {
        Garnet garnet = new Garnet(400, 400);
        GarnetApp app = new SoundVolumeExample(garnet, "");

        garnet.setApp(app);

        garnet.init();
        garnet.run();
    }

    @Override
    public void init(Garnet garnet) {
        String imagePath = "bitmapfonts/ptmono16_0.png";
        String definitionPath = "bitmapfonts/ptmono16.fnt";

        try {
            bitmapFont = new BitmapFont(imagePath, definitionPath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        soundA = garnet.getSound().loadSound(FileUtils.getPathForResource(this, "sounds/laserShoot-3.wav"));
    }

    @Override
    public void tick(double delta) {

        double[] mousePosition = garnet.getInput().getMouse().getPositionNormalised();
        volume = (float) mousePosition[0];
        masterVolume = (float) mousePosition[1];

        garnet.getSound().setMasterVolume(masterVolume);

        time += delta;

        if (time > timeBetweenSounds) {
            time = 0;
            garnet.getSound().playSound(soundA, volume, 0);
        }

    }

    @Override
    public void draw(Graphics g) {

        g.setZoom(1);

        g.setColor(ColorUtils.SUNSET_GREEN);

        String text = "Move mouse around to alter the master volume and sample volume. \n \n ";
        text += "Mouse X - sample volume: " + volume + " \n ";
        text += "Mouse Y - master volume: " + masterVolume + " \n ";

        ParagraphDrawer paragraphDrawer = new ParagraphDrawer(bitmapFont);
        paragraphDrawer.setPadY(5);
        paragraphDrawer.drawParagraph(g, text, 400, 400, 10, 10);

    }

    public float clamp(float v) {
        if (v < 0) return 0;
        if (v > 1) return 1;
        return v;
    }

}
