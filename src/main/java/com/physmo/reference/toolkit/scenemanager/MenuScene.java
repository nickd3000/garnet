package com.physmo.reference.toolkit.scenemanager;

import com.physmo.garnet.ColorUtils;
import com.physmo.garnet.graphics.Graphics;
import com.physmo.garnet.text.RegularFont;
import com.physmo.garnet.toolkit.scene.Scene;
import com.physmo.garnet.toolkit.scene.SceneManager;

public class MenuScene extends Scene {
    public static final String NAME = "scene-manager-menu";

    private final RegularFont font;
    private DemoState state;
    private double pulse;

    public MenuScene(RegularFont font) {
        super(NAME);
        this.font = font;
    }

    @Override
    public void init() {
        state = SceneManager.getSharedContext().getObjectByType(DemoState.class);
        state.lastLifecycleEvent = "MenuScene init";
    }

    @Override
    public void tick(double delta) {
        pulse += delta;
    }

    @Override
    public void draw(Graphics g) {
        g.setBackgroundColor(ColorUtils.WINTER_BLACK);
        g.setColor(ColorUtils.SUNSET_BLUE);
        g.filledRect(0, 0, 640, 480);

        float markerX = (float) (320 + Math.sin(pulse * 3.0) * 80);
        g.setColor(ColorUtils.SUNSET_YELLOW);
        g.filledCircle(markerX, 165, 40, 40);

        SceneText.draw(font, g, ColorUtils.WHITE, "SceneManager Example", 40, 35, 2);
        SceneText.draw(font, g, ColorUtils.SUNSET_YELLOW, "Menu Scene", 40, 80, 3);
        SceneText.draw(font, g, ColorUtils.WHITE, "Press 2: switch to game scene", 40, 145, 1);
        SceneText.draw(font, g, ColorUtils.WHITE, "Press P: push pause subscene", 40, 165, 1);
        SceneText.draw(font, g, ColorUtils.WHITE, "Shared switches: " + state.sceneSwitchCount, 40, 220, 1);
        SceneText.draw(font, g, ColorUtils.WHITE, "Pause opens: " + state.pauseOpenCount, 40, 240, 1);
        SceneText.draw(font, g, ColorUtils.SUNSET_GREEN, "Last event: " + state.lastLifecycleEvent, 40, 285, 1);
    }

    @Override
    public void onMakeActive() {
        if (state != null) {
            state.activeSceneName = "Menu";
            state.sceneSwitchCount++;
            state.lastLifecycleEvent = "MenuScene onMakeActive";
        }
    }

    @Override
    public void onMakeInactive() {
        if (state != null) {
            state.lastLifecycleEvent = "MenuScene onMakeInactive";
        }
    }
}
