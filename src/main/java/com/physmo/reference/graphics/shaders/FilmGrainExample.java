package com.physmo.reference.graphics.shaders;

import com.physmo.garnet.ColorUtils;
import com.physmo.garnet.Garnet;
import com.physmo.garnet.GarnetApp;
import com.physmo.garnet.graphics.Graphics;
import com.physmo.garnet.graphics.RenderTexture;
import com.physmo.garnet.graphics.ShaderProgram;
import com.physmo.garnet.graphics.Texture;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;

// NOTE: On MacOS the following VM argument is required: -XstartOnFirstThread
//
// FilmGrainExample demonstrates the filmgrain.frag shader applied as a
// full-screen post-process effect using a two-pass render pipeline:
//
//   Pass 1 — scene render: draws orbiting sprites into an off-screen
//             RenderTexture (FBO) at the canvas resolution.
//
//   Pass 2 — post-process: draws the FBO texture full-screen through
//             filmgrain.frag, which adds animated random noise over the
//             whole frame to simulate film grain or a dirty screen.
//
// The 'time' uniform is updated every frame so the grain animates.
// The 'strength' uniform controls how heavy the grain is.
public class FilmGrainExample extends GarnetApp {

    static final int W = 400;
    static final int H = 300;
    // Orbiting sprite state
    static final int NUM_SPRITES = 6;
    Texture texture;
    RenderTexture renderTexture;
    ShaderProgram filmGrainShader;
    double time = 0;
    double[] angle = new double[NUM_SPRITES];

    public static void main(String[] args) {
        Garnet.launch(W, H, FilmGrainExample::new);
    }

    @Override
    public void init() {
        garnet.getDisplay().setWindowTitle("Film Grain Post-Process Example");
        garnet.getGraphics().setBackgroundColor(ColorUtils.DARK_GREY);

        texture = garnet.getGraphics().loadTexture("garnetCrystal.png");

        renderTexture = new RenderTexture(W, H);
        garnet.getGraphics().addTexture(renderTexture.getTexture());

        filmGrainShader = ShaderProgram.fromFiles("shaders/passthrough.vert", "shaders/filmgrain.frag");

        for (int i = 0; i < NUM_SPRITES; i++) {
            angle[i] = i * (2 * Math.PI / NUM_SPRITES);
        }
    }

    @Override
    public void tick(double delta) {
        time += delta;
        for (int i = 0; i < NUM_SPRITES; i++) {
            angle[i] += delta * 0.8;
        }

        // Update animated uniforms before draw() is called
        filmGrainShader.bind();
        filmGrainShader.setUniform1f("time", (float) time);
        filmGrainShader.setUniform1f("strength", 0.18f);
        filmGrainShader.unbind();
    }

    @Override
    public void draw(Graphics g) {
        int tw = texture.getWidth();
        int th = texture.getHeight();
        float cx = W / 2f;
        float cy = H / 2f;
        float radius = 100f;

        // --- Pass 1: render scene into FBO ---
        renderTexture.bind(g);
        glClear(GL_COLOR_BUFFER_BIT);
        g.setDrawOrder(0);
        for (int i = 0; i < NUM_SPRITES; i++) {
            float sx = cx + (float) (Math.cos(angle[i]) * radius) - tw / 2f;
            float sy = cy + (float) (Math.sin(angle[i]) * radius) - th / 2f;
            g.setColor(ColorUtils.WHITE);
            g.drawImage(texture, (int) sx, (int) sy);
        }
        g.render();
        renderTexture.unbind(g, garnet.getDisplay());

        // --- Pass 2: draw FBO texture through film grain shader ---
        g.setDrawOrder(0);
        g.setColor(ColorUtils.WHITE);
        g.drawImage(renderTexture.getTexture(), 0, 0);

        g.setDrawOrder(1);
        g.setColor(ColorUtils.WHITE);
        g.drawImage(renderTexture.getTexture(), 0, 0).setShader(filmGrainShader);
    }
}
