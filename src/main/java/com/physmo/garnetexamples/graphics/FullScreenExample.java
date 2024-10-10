package com.physmo.garnetexamples.graphics;

import com.physmo.garnet.Garnet;
import com.physmo.garnet.GarnetApp;
import com.physmo.garnet.graphics.Graphics;
import com.physmo.garnet.graphics.Texture;
import com.physmo.garnet.input.Mouse;

// NOTE: On MacOS the following VM argument is required: -XstartOnFirstThread
public class FullScreenExample extends GarnetApp {

    Texture texture;

    public FullScreenExample(Garnet garnet, String name) {
        super(garnet, name);
    }

    public static void main(String[] args) {
        Garnet garnet = new Garnet(800, 600);
        GarnetApp app = new FullScreenExample(garnet, "");
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
    }

    @Override
    public void tick(double delta) {
        if (garnet.getInput().getMouse().isButtonPressed(Mouse.BUTTON_LEFT)) {
            garnet.getDisplay().setFullScreen(true);
        } else if (garnet.getInput().getMouse().isButtonPressed(Mouse.BUTTON_RIGHT)) {
            garnet.getDisplay().setFullScreen(false);
        }
    }

    @Override
    public void draw(Graphics g) {
        int[] mousePosition = garnet.getInput().getMouse().getPosition();
        g.drawImage(texture, mousePosition[0], mousePosition[1]);
    }
}

