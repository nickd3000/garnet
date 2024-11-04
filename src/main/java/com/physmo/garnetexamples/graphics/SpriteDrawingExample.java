package com.physmo.garnetexamples.graphics;

import com.physmo.garnet.ColorUtils;
import com.physmo.garnet.Garnet;
import com.physmo.garnet.GarnetApp;
import com.physmo.garnet.graphics.Graphics;
import com.physmo.garnet.graphics.Texture;
import com.physmo.garnet.graphics.TileSheet;

// NOTE: On MacOS the following VM argument is required: -XstartOnFirstThread
public class SpriteDrawingExample extends GarnetApp {

    private static final String fileName1 = "space.PNG";
    private static final String fileName2 = "prototypeArt.PNG";

    float time = 0;
    int FOREGROUND_LAYER = 2;
    int BACKGROUND_LAYER = 1;

    TileSheet tileSheet1;
    TileSheet tileSheet2;

    private Texture texture1;
    private Texture texture2;


    public SpriteDrawingExample(Garnet garnet, String name) {
        super(garnet, name);
    }

    public static void main(String[] args) {
        Garnet garnet = new Garnet(640, 480);
        GarnetApp app = new SpriteDrawingExample(garnet, "");

        garnet.setApp(app);

        garnet.init();
        garnet.run();
    }

    @Override
    public void init(Garnet garnet) {

        texture1 = Texture.loadTexture(fileName1);
        tileSheet1 = new TileSheet(texture1, 16, 16);
        garnet.getGraphics().addTexture(texture1);

        texture2 = Texture.loadTexture(fileName2);
        tileSheet2 = new TileSheet(texture2, 16, 16);
        garnet.getGraphics().addTexture(texture2);

        System.out.println("adding keyboard callback from game container");

        garnet.addKeyboardCallback((key, scancode, action, mods) -> {
            System.out.println("keyboard handler" + scancode + "  " + action);
        });

    }

    @Override
    public void tick(double delta) {
        time += delta;
    }

    @Override
    public void draw(Graphics g) {
        drawTestSpriteBuilder();
    }

    private void drawTestSpriteBuilder() {

        int bounceOffset = (int) (Math.sin(time * 7) * 4);

        Graphics graphics = garnet.getGraphics();
        graphics.setZoom(3);
        graphics.setColor(com.physmo.garnet.ColorUtils.WHITE);

        // Draw unscaled sprites using sprite sheet

        graphics.setDrawOrder(1);
        graphics.drawImage(tileSheet1, 0, 10, 0, 0);
        graphics.drawImage(tileSheet1, 64, 10, 1, 0);
        graphics.drawImage(tileSheet2, 128, 10, 1, 0);

        // Draw scaled sprites using sprite sheet

        graphics.drawImage(tileSheet1, 0, 30, 0, 0);
        graphics.drawImage(tileSheet1, 64, 30, 1, 0);
        graphics.drawImage(tileSheet2, 128, 30, 1, 0);

        // Draw scaled and tinted sprites using sprite sheet

        graphics.setColor(com.physmo.garnet.ColorUtils.RED);
        graphics.drawImage(tileSheet1, 0, 60, 1, 0);
        graphics.setColor(com.physmo.garnet.ColorUtils.GREEN);
        graphics.drawImage(tileSheet1, 64, 60, 1, 0);
        graphics.setColor(com.physmo.garnet.ColorUtils.YELLOW);
        graphics.drawImage(tileSheet1, 128, 60, 1, 0);

        // Using draw order

        graphics.setColor(com.physmo.garnet.ColorUtils.WHITE);
        graphics.setDrawOrder(BACKGROUND_LAYER);
        graphics.drawImage(tileSheet2, 0, 70, 5, 2);
        graphics.setDrawOrder(FOREGROUND_LAYER);
        graphics.drawImage(tileSheet2, 4, 70 + 4 + bounceOffset, 0, 3);

        graphics.setDrawOrder(FOREGROUND_LAYER);
        graphics.drawImage(tileSheet2, 30, 70, 5, 2);
        graphics.setDrawOrder(BACKGROUND_LAYER);
        graphics.drawImage(tileSheet2, 30 + 4, 70 + 4 + bounceOffset, 0, 3);

        graphics.drawLine(0, 0, 100, 100);

        graphics.setColor(com.physmo.garnet.ColorUtils.GREEN);
        graphics.drawRect(30, 30, 30, 30);

        graphics.setColor(ColorUtils.SUNSET_BLUE);
        graphics.filledRect(70, 30, 30, 30);

    }

    private void drawNew() {

    }


}
