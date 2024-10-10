package com.physmo.garnetexamples.graphics;

import com.physmo.garnet.Garnet;
import com.physmo.garnet.GarnetApp;
import com.physmo.garnet.graphics.Graphics;
import com.physmo.garnet.graphics.Texture;

// NOTE: On MacOS the following VM argument is required: -XstartOnFirstThread
public class ClipWindowExample extends GarnetApp {

    private static final String fileName = "wood.png";

    Texture texture;
    double time = 0;

    public ClipWindowExample(Garnet garnet, String name) {
        super(garnet, name);
    }

    public static void main(String[] args) {
        Garnet garnet = new Garnet(400, 400);
        GarnetApp app = new ClipWindowExample(garnet, "");

        garnet.setApp(app);

        garnet.init();
        garnet.run();
    }

    @Override
    public void init(Garnet garnet) {

        texture = Texture.loadTexture(fileName);

        Graphics graphics = garnet.getGraphics();

        graphics.addTexture(texture);

        //graphics.addClipRect(1, 50, 50, 300, 300);
    }

    @Override
    public void tick(double delta) {
        time += delta;
    }

    @Override
    public void draw(Graphics g) {
        drawTestSpriteBuilder(g);
    }

    private void drawTestSpriteBuilder(Graphics graphics) {

        int offset1 = (int) (Math.sin(time * 3) * 50) - 50;
        int offset2 = (int) (Math.cos(time * 4) * 50) - 50;

        // Draw unscaled sprites using sprite sheet

        graphics.setZoom(2);
        graphics.setDrawOrder(1);

//        graphics.disableClipRect();
//        graphics.setColor(ColorUtils.WHITE);
//        graphics.drawImage(texture, offset1, 0);
//        graphics.setActiveClipRect(1);
//        graphics.setColor(ColorUtils.GREEN);
//        graphics.drawImage(texture, 0, offset2);
//        graphics.disableClipRect();

    }

}
