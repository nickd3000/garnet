package com.physmo.reference.input;

import com.physmo.garnet.FileUtils;
import com.physmo.garnet.Garnet;
import com.physmo.garnet.GarnetApp;
import com.physmo.garnet.graphics.Graphics;
import com.physmo.garnet.graphics.Texture;
import com.physmo.garnet.graphics.TileSheet;
import com.physmo.garnet.input.InputAction;
import com.physmo.garnet.input.InputKeys;

import java.io.InputStream;

import static com.physmo.garnet.input.InputKeys.KEY_Z;

// NOTE: On MacOS the following VM argument is required: -XstartOnFirstThread
public class KeyboardExample extends GarnetApp {

    String imageFileName = "space.png";
    TileSheet tileSheet;
    Texture texture;

    boolean up, down, left, right, zKey;

    public KeyboardExample(Garnet garnet, String name) {
        super(garnet, name);
    }

    public static void main(String[] args) {
        Garnet garnet = new Garnet(400, 400);
        GarnetApp app = new KeyboardExample(garnet, "");

        garnet.setApp(app);

        garnet.init();
        garnet.run();
    }

    @Override
    public void init(Garnet garnet) {
        InputStream inputStream = FileUtils.getFileFromResourceAsStream(imageFileName);
        texture = Texture.loadTexture(inputStream);
        tileSheet = new TileSheet(texture, 16, 16);

        garnet.getGraphics().addTexture(texture);

        // The input system is given some default config at startup,
        // here we add some extra WASD keys to the movement actions.
        garnet.getInput().addKeyboardAction(InputKeys.KEY_W, InputAction.UP);
        garnet.getInput().addKeyboardAction(InputKeys.KEY_S, InputAction.DOWN);
        garnet.getInput().addKeyboardAction(InputKeys.KEY_A, InputAction.LEFT);
        garnet.getInput().addKeyboardAction(InputKeys.KEY_D, InputAction.RIGHT);

        garnet.getDebugDrawer().setVisible(true);
    }

    @Override
    public void tick(double delta) {

        // Detect action keys being pressed.
        right = garnet.getInput().isActionKeyPressed(InputAction.RIGHT);
        left = garnet.getInput().isActionKeyPressed(InputAction.LEFT);
        up = garnet.getInput().isActionKeyPressed(InputAction.UP);
        down = garnet.getInput().isActionKeyPressed(InputAction.DOWN);

        // The raw keyboard data can also be checked without using the
        // action key system as follows.
        zKey = garnet.getInput().getKeyboard().getKeyboardState()[KEY_Z];

    }

    @Override
    public void draw(Graphics g) {
        garnet.getDebugDrawer().setScale(2);

        garnet.getDebugDrawer().setUserString("UP/W:    ", Boolean.toString(up));
        garnet.getDebugDrawer().setUserString("DOWN/S:  ", Boolean.toString(down));
        garnet.getDebugDrawer().setUserString("LEFT/A:  ", Boolean.toString(left));
        garnet.getDebugDrawer().setUserString("RIGHT/D: ", Boolean.toString(right));

        garnet.getDebugDrawer().setUserString("Z KEY: ", Boolean.toString(zKey));

    }

}
