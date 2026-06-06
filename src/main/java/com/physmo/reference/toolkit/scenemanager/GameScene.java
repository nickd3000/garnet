package com.physmo.reference.toolkit.scenemanager;

import com.physmo.garnet.ColorUtils;
import com.physmo.garnet.graphics.Graphics;
import com.physmo.garnet.text.RegularFont;
import com.physmo.garnet.toolkit.scene.Scene;
import com.physmo.garnet.toolkit.scene.SceneManager;

public class GameScene extends Scene {
    public static final String NAME = "scene-manager-game";

    private final RegularFont font;
    private DemoState state;
    private double playerX = 40;

    public GameScene(RegularFont font) {
        super(NAME);
        this.font = font;
    }

    @Override
    public void init() {
        state = SceneManager.getSharedContext().getObjectByType(DemoState.class);
        state.lastLifecycleEvent = "GameScene init";
    }

    @Override
    public void tick(double delta) {
        playerX = (playerX + delta * 90) % 640;
    }

    @Override
    public void draw(Graphics g) {
        g.setBackgroundColor(0x1a2a33ff);
        g.setColor(0x1a2a33ff);
        g.filledRect(0, 0, 640, 480);

        g.setColor(ColorUtils.SUNSET_GREEN);
        g.filledRect((float) playerX, 190, 32, 32);
        g.setColor(ColorUtils.SUNSET_YELLOW);
        g.drawRect(25, 165, 590, 80);

        SceneText.draw(font, g, ColorUtils.WHITE, "SceneManager Example", 40, 35, 2);
        SceneText.draw(font, g, ColorUtils.SUNSET_GREEN, "Game Scene", 40, 80, 3);
        SceneText.draw(font, g, ColorUtils.WHITE, "Press 1: switch to menu scene", 40, 275, 1);
        SceneText.draw(font, g, ColorUtils.WHITE, "Press P: push pause subscene", 40, 295, 1);
        SceneText.draw(font, g, ColorUtils.WHITE, "Shared active scene: " + state.activeSceneName, 40, 340, 1);
        SceneText.draw(font, g, ColorUtils.WHITE, "Shared switches: " + state.sceneSwitchCount, 40, 360, 1);
        SceneText.draw(font, g, ColorUtils.SUNSET_YELLOW, "Last event: " + state.lastLifecycleEvent, 40, 405, 1);
    }

    @Override
    public void onMakeActive() {
        if (state != null) {
            state.activeSceneName = "Game";
            state.sceneSwitchCount++;
            state.lastLifecycleEvent = "GameScene onMakeActive";
        }
    }

    @Override
    public void onMakeInactive() {
        if (state != null) {
            state.lastLifecycleEvent = "GameScene onMakeInactive";
        }
    }
}
