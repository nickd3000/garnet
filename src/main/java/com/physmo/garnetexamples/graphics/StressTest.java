package com.physmo.garnetexamples.graphics;

import com.physmo.garnet.ColorUtils;
import com.physmo.garnet.Garnet;
import com.physmo.garnet.GarnetApp;
import com.physmo.garnet.graphics.Texture;
import com.physmo.garnet.graphics.TileSheet;
import com.physmo.garnet.toolkit.GameObject;
import com.physmo.garnetexamples.graphics.support.FloatingInvaderComponent;

// NOTE: On MacOS the following VM argument is required: -XstartOnFirstThread
public class StressTest extends GarnetApp {

    String imageFileName = "space.png";
    TileSheet tileSheet;
    Texture texture;
    int numSprites = 25000 / 2;
    Context context;

    public StressTest(Garnet garnet, String name) {
        super(garnet, name);
    }

    public static void main(String[] args) {
        Garnet garnet = new Garnet(640, 480);
        GarnetApp app = new StressTest(garnet, "");

        garnet.setApp(app);

        garnet.init();
        garnet.run();
    }

    @Override
    public void init(Garnet garnet) {
        // Create a context to hold game objects
        context = new Context();

        // Load the texture
        texture = Texture.loadTexture(imageFileName);
        tileSheet = new TileSheet(texture, 16, 16);
        garnet.getGraphics().addTexture(texture);

        // Add the tileSheet and graphics object to the context so the sprite entities can access them.
        context.add(tileSheet);
        context.add(garnet.getGraphics());

        // Create a number of entities and add them to the context.
        for (int i = 0; i < numSprites; i++) {
            GameObject gameObject = new GameObject("");
            gameObject.addComponent(new FloatingInvaderComponent(garnet.getDisplay().getWindowWidth(), garnet.getDisplay().getWindowHeight()));
            context.add(gameObject);
        }

        // Configure the debug text.
        garnet.getDebugDrawer().setScale(2);
        garnet.getDebugDrawer().setUserString("Sprite count:", String.valueOf(numSprites));
        garnet.getDebugDrawer().setDrawFps(true);
        garnet.getDebugDrawer().setVisible(true);
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
        context.draw();

        g.setColor(0x00000070);
        g.filledRect(0, 0, 640, 80);
    }

}
