package com.physmo.reference.toolkit.scenemanager;

import com.physmo.garnet.ColorUtils;
import com.physmo.garnet.graphics.Graphics;
import com.physmo.garnet.text.RegularFont;
import com.physmo.garnet.toolkit.scene.Scene;
import com.physmo.garnet.toolkit.scene.SceneManager;

public class PauseSubScene extends Scene {
    public static final String NAME = "scene-manager-pause";

    private final RegularFont font;
    private DemoState state;
    private double age;

    public PauseSubScene(RegularFont font) {
        super(NAME);
        this.font = font;
    }

    @Override
    public void init() {
        state = SceneManager.getSharedContext().getObjectByType(DemoState.class);
        state.lastLifecycleEvent = "PauseSubScene init";
    }

    @Override
    public void tick(double delta) {
        age += delta;
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(0x000000b0);
        g.filledRect(0, 0, 640, 480);

        int panelColor = (Math.sin(age * 5.0) > 0.0) ? 0x27384aff : 0x34485cff;
        g.setColor(panelColor);
        g.filledRect(95, 120, 450, 220);
        g.setColor(ColorUtils.SUNSET_YELLOW);
        g.drawRect(95, 120, 450, 220, 4);

        SceneText.draw(font, g, ColorUtils.SUNSET_YELLOW, "Pause Subscene", 145, 155, 3);
        SceneText.draw(font, g, ColorUtils.WHITE, "Main scene stays drawn underneath.", 145, 225, 1);
        SceneText.draw(font, g, ColorUtils.WHITE, "Only the top subscene is ticked.", 145, 245, 1);
        SceneText.draw(font, g, ColorUtils.WHITE, "Press O: pop this subscene", 145, 285, 1);
        SceneText.draw(font, g, ColorUtils.SUNSET_GREEN, "Opened: " + state.pauseOpenCount + " times", 145, 305, 1);
    }

    @Override
    public void onMakeActive() {
        if (state != null) {
            state.pauseOpenCount++;
            state.lastLifecycleEvent = "PauseSubScene onMakeActive";
        }
    }

    @Override
    public void onMakeInactive() {
        if (state != null) {
            state.lastLifecycleEvent = "PauseSubScene onMakeInactive";
        }
    }
}
