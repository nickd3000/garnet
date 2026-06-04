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
// UnderwaterExample demonstrates the underwater.frag shader applied as a
// full-screen post-process effect using a two-pass render pipeline:
//
//   Pass 1 — scene render: draws orbiting sprites into an off-screen
//             RenderTexture (FBO) at the canvas resolution.
//
//   Pass 2 — post-process: draws the FBO texture full-screen through
//             underwater.frag, which applies:
//               - slow dual-axis sine-wave UV distortion
//               - blue-green colour tint
//               - caustic shimmer (subtle brightness oscillation)
//
// The 'time' uniform is updated every frame to drive the animation.
public class UnderwaterExample extends GarnetApp {

    static final int W = 400;
    static final int H = 300;
    static final int NUM_SPRITES = 7;
    Texture texture;
    RenderTexture renderTexture;
    ShaderProgram underwaterShader;
    double time = 0;
    double[] angle = new double[NUM_SPRITES];

    public UnderwaterExample(Garnet garnet, String name) {
        super(garnet, name);
    }

    public static void main(String[] args) {
        Garnet garnet = new Garnet(W, H);
        GarnetApp app = new UnderwaterExample(garnet, "");
        garnet.setApp(app);
        garnet.init();
        garnet.run();
    }

    @Override
    public void init(Garnet garnet) {
        garnet.getDisplay().setWindowTitle("Underwater Post-Process Example");
        garnet.getGraphics().setBackgroundColor(ColorUtils.rgb(10, 30, 60, 255));

        texture = Texture.loadTexture("garnetCrystal.png");
        garnet.getGraphics().addTexture(texture);

        renderTexture = new RenderTexture(W, H);
        garnet.getGraphics().addTexture(renderTexture.getTexture());

        underwaterShader = ShaderProgram.fromFiles("shaders/passthrough.vert", "shaders/underwater.frag");

        // Bake in the static uniforms
        underwaterShader.bind();
        underwaterShader.setUniform1f("waveAmp", 0.008f);
        underwaterShader.setUniform1f("waveSpeed", 1.0f);
        underwaterShader.setUniform1f("tintStr", 0.7f);
        underwaterShader.unbind();

        for (int i = 0; i < NUM_SPRITES; i++) {
            angle[i] = i * (2 * Math.PI / NUM_SPRITES);
        }
    }

    @Override
    public void tick(double delta) {
        time += delta;
        for (int i = 0; i < NUM_SPRITES; i++) {
            angle[i] += delta * 0.5;
        }

        // Update the animated time uniform
        underwaterShader.bind();
        underwaterShader.setUniform1f("time", (float) time);
        underwaterShader.unbind();
    }

    @Override
    public void draw(Graphics g) {
        int tw = texture.getWidth();
        int th = texture.getHeight();
        float cx = W / 2f;
        float cy = H / 2f;
        float radius = 100f;

        // --- Pass 1: render scene into FBO ---
        renderTexture.bind();
        glClear(GL_COLOR_BUFFER_BIT);
        g.setDrawOrder(0);
        for (int i = 0; i < NUM_SPRITES; i++) {
            float sx = cx + (float) (Math.cos(angle[i]) * radius) - tw / 2f;
            float sy = cy + (float) (Math.sin(angle[i]) * radius) - th / 2f;
            g.setColor(ColorUtils.WHITE);
            g.drawImage(texture, (int) sx, (int) sy);
        }
        g.render();
        renderTexture.unbind(garnet.getDisplay());

        // --- Pass 2: draw FBO texture through underwater shader ---
        g.setDrawOrder(0);
        g.setColor(ColorUtils.WHITE);
        g.drawImage(renderTexture.getTexture(), 0, 0).setShader(underwaterShader);
    }
}
