package com.physmo.reference.graphics;

import com.physmo.garnet.Garnet;
import com.physmo.garnet.GarnetApp;
import com.physmo.garnet.graphics.Graphics;
import com.physmo.garnet.graphics.Texture;

// NOTE: on MacOS we need to add a vm argument: -XstartOnFirstThread
public class SimpleSpriteExample extends GarnetApp {

    Texture texture;

    public static void main(String[] args) {
        Garnet.launch(600, 400, SimpleSpriteExample::new);
    }

    @Override
    public void init() {
        texture = Texture.loadTexture("garnetCrystal.png");
        garnet.getGraphics().addTexture(texture);
    }

    @Override
    public void tick(double delta) {
    }

    @Override
    public void draw(Graphics g) {
        int[] mousePosition = garnet.getInput().getMousePosition();
        g.drawImage(texture, mousePosition[0], mousePosition[1]);
    }
}
