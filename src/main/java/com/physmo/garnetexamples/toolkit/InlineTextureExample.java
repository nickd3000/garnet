package com.physmo.garnetexamples.toolkit;

import com.physmo.garnet.Garnet;
import com.physmo.garnet.GarnetApp;
import com.physmo.garnet.graphics.Graphics;
import com.physmo.garnet.graphics.Texture;
import com.physmo.garnet.toolkit.InlineTexture;

public class InlineTextureExample extends GarnetApp {

    Texture texture;

    public InlineTextureExample(Garnet garnet, String name) {
        super(garnet, name);
    }

    public static void main(String[] args) {
        Garnet garnet = new Garnet(300, 300);
        GarnetApp app = new InlineTextureExample(garnet, "");
        garnet.setApp(app);
        garnet.init();
        garnet.run();
    }

    @Override
    public void init(Garnet garnet) {
        String data = "  XXXX  " +
                " X    X " +
                "X o  o X" +
                "X      X" +
                "X o  o X" +
                "X  oo  X" +
                " X    X " +
                "  XXXX  ";

        // Load texture
        texture = InlineTexture.create(data, 8, 8);

        // Add texture to graphics system.
        garnet.getGraphics().addTexture(texture);
    }

    @Override
    public void tick(double delta) {
    }

    @Override
    public void draw(Graphics g) {
        g.setZoom(5);
        g.drawImage(texture, 5, 5);
    }
}
