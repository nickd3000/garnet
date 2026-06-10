package com.physmo.reference.graphics;

import com.physmo.garnet.ColorUtils;
import com.physmo.garnet.Garnet;
import com.physmo.garnet.GarnetApp;
import com.physmo.garnet.graphics.Graphics;
import com.physmo.garnet.graphics.TileSheet;
import com.physmo.garnet.toolkit.Context;
import com.physmo.garnet.toolkit.GameObject;
import com.physmo.reference.graphics.support.FloatingInvaderComponent;

// NOTE: On MacOS the following VM argument is required: -XstartOnFirstThread
public class StressTest extends GarnetApp {

    private static final int WIDTH = 640;
    private static final int HEIGHT = 480;

    String imageFileName = "space.png";
    TileSheet tileSheet;
    int numSprites = 25000 / 2;
    Context context;

    public static void main(String[] args) {
        Garnet.launch(WIDTH, HEIGHT, StressTest::new);
    }

    @Override
    public void init() {
        // Create a context to hold game objects
        context = new Context();

        // Load the texture
        tileSheet = garnet.getGraphics().loadTileSheet(imageFileName, 16, 16);

        // Add the tileSheet and graphics object to the context so the sprite entities can access them.
        context.add(tileSheet);
        context.add(garnet.getGraphics());

        // Create a number of entities and add them to the context.
        for (int i = 0; i < numSprites; i++) {
            GameObject.named("")
                    .with(new FloatingInvaderComponent(WIDTH, HEIGHT))
                    .inContext(context);
        }

        // Configure the debug text.
        garnet.getDebugDrawer().setScale(2);
        garnet.getDebugDrawer().setUserString("Sprite count:", String.valueOf(numSprites));
        garnet.getDebugDrawer().setDrawFps(true);
        garnet.getDebugDrawer().setVisible(true);

        context.init();
    }

    @Override
    public void tick(double delta) {
        // Tick all game objects in the context
        context.tick(delta);
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(ColorUtils.GREEN);
        g.setZoom(1);

        // Draw all game objects in the context
        context.draw(g);

        g.setColor(0x00000070);
        g.filledRect(0, 0, WIDTH, 80);
    }

}
