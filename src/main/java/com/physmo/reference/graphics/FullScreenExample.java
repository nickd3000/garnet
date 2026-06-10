package com.physmo.reference.graphics;

import com.physmo.garnet.Garnet;
import com.physmo.garnet.GarnetApp;
import com.physmo.garnet.graphics.Graphics;
import com.physmo.garnet.graphics.Texture;
import com.physmo.garnet.input.Mouse;

// NOTE: On MacOS the following VM argument is required: -XstartOnFirstThread
public class FullScreenExample extends GarnetApp {

    Texture texture;

    public static void main(String[] args) {
        Garnet.launch(800, 600, FullScreenExample::new);
    }

    @Override
    public void init() {
        texture = garnet.getGraphics().loadTexture("garnetCrystal.png");
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
