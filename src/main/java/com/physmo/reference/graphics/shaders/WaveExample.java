package com.physmo.reference.graphics.shaders;

import com.physmo.garnet.ColorUtils;
import com.physmo.garnet.Garnet;
import com.physmo.garnet.GarnetApp;
import com.physmo.garnet.graphics.Graphics;
import com.physmo.garnet.graphics.ShaderProgram;
import com.physmo.garnet.graphics.Texture;

// NOTE: On MacOS the following VM argument is required: -XstartOnFirstThread
//
// WaveExample demonstrates the wave.frag shader, which distorts UV coordinates
// using a pair of sine waves to produce a rippling, wobbly effect.
//
// The time uniform is updated every frame so the distortion animates smoothly.
// Two sprites are shown side-by-side — one normal, one with the wave shader —
// so the distortion is easy to compare.
public class WaveExample extends GarnetApp {

    static final int WINDOW_W = 400;
    static final int WINDOW_H = 200;

    Texture texture;
    ShaderProgram waveShader;
    double time = 0;

    public static void main(String[] args) {
        Garnet.launch(WINDOW_W, WINDOW_H, WaveExample::new);
    }

    @Override
    public void init() {
        garnet.getDisplay().setWindowTitle("Wave Shader Example");
        garnet.getGraphics().setBackgroundColor(ColorUtils.DARK_GREY);

        texture = Texture.loadTexture("garnetCrystal.png");
        garnet.getGraphics().addTexture(texture);

        waveShader = ShaderProgram.fromFiles("shaders/passthrough.vert", "shaders/wave.frag");
    }

    @Override
    public void tick(double delta) {
        time += delta;
    }

    @Override
    public void draw(Graphics g) {
        int tw = texture.getWidth();
        int th = texture.getHeight();
        int startY = (WINDOW_H - th) / 2;
        int spacing = 180;
        int startX = (WINDOW_W - spacing - tw) / 2;

        // Upload animated uniforms
        waveShader.bind();
        waveShader.setUniform1f("time", (float) time);
        waveShader.setUniform1f("amplitude", 0.0025f);
        waveShader.setUniform1f("frequency", 122.0f);
        waveShader.unbind();

        // Column 1 — no shader (normal)
        g.setColor(ColorUtils.WHITE);
        g.drawImage(texture, startX, startY);

        // Column 2 — wave distortion
        g.setColor(ColorUtils.WHITE);
        g.drawImage(texture, startX + spacing, startY).setShader(waveShader);
    }
}
