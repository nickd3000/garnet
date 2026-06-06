package com.physmo.reference.toolkit.scenemanager;

import com.physmo.garnet.ColorUtils;
import com.physmo.garnet.Garnet;
import com.physmo.garnet.GarnetApp;
import com.physmo.garnet.graphics.Graphics;
import com.physmo.garnet.input.InputKeys;
import com.physmo.garnet.text.RegularFont;
import com.physmo.garnet.toolkit.scene.SceneManager;

// NOTE: On MacOS the following VM argument is required: -XstartOnFirstThread
public class SceneManagerExample extends GarnetApp {
    private RegularFont font;
    private boolean pauseOpen;

    public SceneManagerExample(Garnet garnet, String name) {
        super(garnet, name);
    }

    public static void main(String[] args) {
        Garnet garnet = new Garnet(640, 480);
        GarnetApp app = new SceneManagerExample(garnet, "Scene Manager Example");
        garnet.setApp(app);
        garnet.init();
        garnet.run();
    }

    @Override
    public void init(Garnet garnet) {
        garnet.getDisplay().setWindowTitle("Scene Manager Example");
        garnet.getGraphics().setBackgroundColor(ColorUtils.WINTER_BLACK);

        font = new RegularFont("regularfonts/12x12Font.png", 12, 12);
        font.setHorizontalPad(-5);

        SceneManager.getSharedContext().add(new DemoState());
        SceneManager.addScene(new MenuScene(font));
        SceneManager.addScene(new GameScene(font));
        SceneManager.addScene(new PauseSubScene(font));
        SceneManager.setActiveScene(MenuScene.NAME);
    }

    @Override
    public void tick(double delta) {
        if (isFirstPress(InputKeys.KEY_1)) {
            SceneManager.setActiveScene(MenuScene.NAME);
        }
        if (isFirstPress(InputKeys.KEY_2)) {
            SceneManager.setActiveScene(GameScene.NAME);
        }
        if (isFirstPress(InputKeys.KEY_P) && !pauseOpen) {
            pauseOpen = true;
            SceneManager.pushSubScene(PauseSubScene.NAME);
        }
        if (isFirstPress(InputKeys.KEY_O) && pauseOpen) {
            pauseOpen = false;
            SceneManager.popSubScene(PauseSubScene.NAME);
        }

        SceneManager.tick(delta);
    }

    private boolean isFirstPress(int keyCode) {
        boolean[] current = garnet.getInput().getKeyboard().getKeyboardState();
        boolean[] previous = garnet.getInput().getKeyboard().getKeyboardStatePrev();
        return current[keyCode] && !previous[keyCode];
    }

    @Override
    public void draw(Graphics g) {
        SceneManager.draw(g);
    }
}
