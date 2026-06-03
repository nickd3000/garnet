package com.physmo.reference.graphics.shaders;

import com.physmo.garnet.ColorUtils;
import com.physmo.garnet.Garnet;
import com.physmo.garnet.GarnetApp;
import com.physmo.garnet.graphics.Graphics;
import com.physmo.garnet.graphics.ShaderProgram;
import com.physmo.garnet.graphics.Texture;


// NOTE: On MacOS the following VM argument is required: -XstartOnFirstThread
//
// DissolveExample demonstrates the dissolve.frag shader, which makes a sprite
// dissolve away pixel-by-pixel using a multi-octave noise pattern.
//
// A 'threshold' uniform controls how much of the sprite is visible:
//   0.0 = fully visible, 1.0 = fully dissolved.
//
// A glowing burn edge is drawn just ahead of the dissolve front using the
// 'edgeWidth' and 'edgeColor' uniforms, giving a fire/burn appearance.
//
// The threshold is animated over time so the sprite continuously dissolves
// and reappears, making the effect easy to observe.
//
// NOTE ON UNIFORM UPDATES:
// Unlike the outline shader (which bakes uniforms at init time), this shader
// updates 'threshold' every frame in tick().  Because Garnet batches draw calls
// until end-of-frame, the uniform must be uploaded via the shader's bind() and
// setUniform calls before draw() is called — the value is
// read by the GPU when the batch is flushed at frame end, by which point the
// last uploaded value is in effect.  This works correctly here because there
// is only one sprite using this shader; if you had multiple sprites needing
// different threshold values you would need separate ShaderProgram instances
// (see OutlineExample for that pattern).
public class DissolveExample extends GarnetApp {

    static final int WINDOW_W = 400;
    static final int WINDOW_H = 200;

    Texture texture;
    ShaderProgram dissolveShader;
    double time = 0;

    public DissolveExample(Garnet garnet, String name) {
        super(garnet, name);
    }

    public static void main(String[] args) {
        Garnet garnet = new Garnet(WINDOW_W, WINDOW_H);
        GarnetApp app = new DissolveExample(garnet, "");
        garnet.setApp(app);
        garnet.init();
        garnet.run();
    }

    @Override
    public void init(Garnet garnet) {
        garnet.getDisplay().setWindowTitle("Dissolve Shader Example");
        garnet.getGraphics().setBackgroundColor(ColorUtils.DARK_GREY);

        texture = Texture.loadTexture("garnetCrystal.png");
        garnet.getGraphics().addTexture(texture);

        dissolveShader = ShaderProgram.fromFiles("shaders/passthrough.vert", "shaders/dissolve.frag");

        // Bake in the static uniforms at init time
        dissolveShader.bind();
        dissolveShader.setUniform1f("edgeWidth", 0.08f);
        dissolveShader.setUniform4f("edgeColor", 1.0f, 0.4f, 0.0f, 1.0f);
        dissolveShader.unbind();
    }

    @Override
    public void tick(double delta) {
        time += delta;

        // Animate threshold: ping-pong between 0.0 and 1.0 over 3 seconds
        float threshold = (float) (Math.abs(Math.sin(time * Math.PI / 3.0)));

        dissolveShader.bind();
        dissolveShader.setUniform1f("threshold", threshold);
        dissolveShader.unbind();
    }

    @Override
    public void draw(Graphics g) {
        int tw = texture.getWidth();
        int th = texture.getHeight();
        int startY = (WINDOW_H - th) / 2;
        int spacing = 180;
        int startX = (WINDOW_W - spacing - tw) / 2;

        // Column 1 — no shader (normal)
        g.setColor(ColorUtils.WHITE);
        g.drawImage(texture, startX, startY);

        // Column 2 — dissolve shader (animates continuously)
        g.setColor(ColorUtils.WHITE);
        g.drawImage(texture, startX + spacing, startY).setShader(dissolveShader);
    }
}
