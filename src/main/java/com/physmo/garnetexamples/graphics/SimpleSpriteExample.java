package com.physmo.garnetexamples.graphics;

import com.physmo.garnet.ColorUtils;
import com.physmo.garnet.Garnet;
import com.physmo.garnet.GarnetApp;
import com.physmo.garnet.graphics.Graphics;
import com.physmo.garnet.graphics.Texture;

// NOTE: On MacOS the following VM argument is required: -XstartOnFirstThread
public class SimpleSpriteExample extends GarnetApp {

    Texture texture;

    public SimpleSpriteExample(Garnet garnet, String name) {
        super(garnet, name);
    }

    public static void main(String[] args) {
        Garnet garnet = new Garnet(600, 400);
        GarnetApp app = new SimpleSpriteExample(garnet, "");
        garnet.setApp(app);
        garnet.init();
        garnet.run();
    }

    @Override
    public void init(Garnet garnet) {
        // Load texture
        texture = Texture.loadTexture("garnetCrystal.png");

        // Add texture to graphics system.
        garnet.getGraphics().addTexture(texture);
        garnet.getGraphics().setBackgroundColor(ColorUtils.DARK_GREY);
    }

    @Override
    public void tick(double delta) {
    }

    @Override
    public void draw(Graphics g) {
        int[] mousePosition = garnet.getInput().getMouse().getPosition();
        g.drawImage(texture, mousePosition[0], mousePosition[1]);
    }
}

