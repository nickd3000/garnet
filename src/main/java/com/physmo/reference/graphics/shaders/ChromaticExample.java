package com.physmo.reference.graphics.shaders;

import com.physmo.garnet.ColorUtils;
import com.physmo.garnet.Garnet;
import com.physmo.garnet.GarnetApp;
import com.physmo.garnet.graphics.Graphics;
import com.physmo.garnet.graphics.ShaderProgram;
import com.physmo.garnet.graphics.Texture;
import com.physmo.garnet.renderer.TextureRegion;

// NOTE: On MacOS the following VM argument is required: -XstartOnFirstThread
//
// ChromaticExample demonstrates the chromatic.frag shader, which splits the
// red, green and blue channels apart horizontally to simulate the colour fringing
// seen in cheap lenses or lo-fi CRT displays.
//
// Four columns are shown with increasing shift amounts so the effect is easy to
// compare at different intensities.
public class ChromaticExample extends GarnetApp {

    static final int WINDOW_W = 520;
    static final int WINDOW_H = 200;

    Texture texture;
    ShaderProgram[] chromaticShaders;

    public static void main(String[] args) {
        Garnet.launch(WINDOW_W, WINDOW_H, ChromaticExample::new);
    }

    @Override
    public void init() {
        garnet.getDisplay().setWindowTitle("Chromatic Aberration Shader Example");
        garnet.getGraphics().setBackgroundColor(ColorUtils.DARK_GREY);

        texture = garnet.getGraphics().loadTexture("garnetCrystal.png");

        TextureRegion textureRegion = garnet.getGraphics().getTextureRegion(texture);
        float[] shifts = {0.005f, 0.015f, 0.03f};
        chromaticShaders = new ShaderProgram[shifts.length];
        for (int i = 0; i < shifts.length; i++) {
            chromaticShaders[i] = ShaderProgram.fromFiles("shaders/passthrough.vert", "shaders/chromatic.frag");
            chromaticShaders[i].bind();
            chromaticShaders[i].setUniform1f("shift", shifts[i] * textureRegion.uWidth());
            chromaticShaders[i].unbind();
        }
    }

    @Override
    public void tick(double delta) {
    }

    @Override
    public void draw(Graphics g) {
        int tw = texture.getWidth();
        int th = texture.getHeight();
        int startY = (WINDOW_H - th) / 2;
        int spacing = 120;
        int startX = (WINDOW_W - spacing * 3 - tw) / 2;

        // Column 1 — no shader
        g.setColor(ColorUtils.WHITE);
        g.drawImage(texture, startX, startY);

        // Columns 2-4 — increasing chromatic shift
        for (int i = 0; i < chromaticShaders.length; i++) {
            g.setColor(ColorUtils.WHITE);
            g.drawImage(texture, startX + spacing * (i + 1), startY).setShader(chromaticShaders[i]);
        }
    }
}
