package com.physmo.reference.graphics;

import com.physmo.garnet.Garnet;
import com.physmo.garnet.GarnetApp;
import com.physmo.garnet.graphics.Graphics;
import com.physmo.garnet.graphics.Texture;
import com.physmo.garnet.graphics.TileSheet;

// NOTE: On MacOS the following VM argument is required: -XstartOnFirstThread
public class TileSheetExample extends GarnetApp {

    TileSheet tileSheet;
    Texture texture;
    double xPos = 0;
    double scale = 4;

    int RED = 0xff5555ff;
    int GREEN = 0x55ff55ff;
    int BLUE = 0x5555ffff;

    public TileSheetExample(Garnet garnet, String name) {
        super(garnet, name);
    }

    public static void main(String[] args) {
        Garnet garnet = new Garnet(400, 400);
        GarnetApp app = new TileSheetExample(garnet, "");

        garnet.setApp(app);

        garnet.init();
        garnet.run();
    }

    @Override
    public void init(Garnet garnet) {
        texture = Texture.loadTexture("space.png");
        tileSheet = new TileSheet(texture, 16, 16);
        Graphics graphics = garnet.getGraphics();
        graphics.addTexture(texture);

        garnet.getDebugDrawer().setColor(0xaa4477ff);
        garnet.getDebugDrawer().setVisible(true);
        garnet.getDebugDrawer().setDrawMouseCoords(true);
        garnet.getDebugDrawer().setScale(3);
    }

    @Override
    public void tick(double delta) {
        xPos += delta * 50;
        if (xPos > 80) xPos = -16;
    }

    @Override
    public void draw(Graphics g) {
        g.setZoom(scale);

        // Retrieve the current mouse position.
        int[] mp = garnet.getInput().getMouse().getPositionScaled(scale);


        // Draw sprite by specifying the row and column where the sub image is.
        g.setColor(RED);
        g.drawImage(tileSheet, (int) xPos, 5, 2, 2);

        // Draw sprite using the getSubImage helper function.
        g.setColor(GREEN);
        g.drawImage(tileSheet.getSubImage(2, 2), (int) xPos + 20, 5);

        g.setColor(BLUE);
        g.drawImage(tileSheet, mp[0], mp[1], 2, 2);
    }
}

