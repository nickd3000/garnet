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
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glUniform1f;
import static org.lwjgl.opengl.GL20.glUniform2f;
import static org.lwjgl.opengl.GL20.glUseProgram;

// NOTE: On MacOS the following VM argument is required: -XstartOnFirstThread
//
// GaussianBlurExample demonstrates a separable two-pass Gaussian blur applied
// as a full-screen post-process effect.
//
// A separable Gaussian blur splits the 2-D convolution into two cheaper 1-D
// passes.  A naive 2-D blur with a 9×9 kernel costs 81 texture samples per
// pixel; the separable version costs only 9 + 9 = 18.
//
// Render pipeline (three FBOs):
//
//   Pass 1 — scene:      draw orbiting sprites into rtScene  (W × H FBO)
//   Pass 2 — blur H:     draw rtScene full-screen through blur_h.frag
//                        into rtBlurH  (horizontal 9-tap Gaussian)
//   Pass 3 — blur V:     draw rtBlurH full-screen through blur_v.frag
//                        to the screen (vertical 9-tap Gaussian)
//
// The result is a fully blurred scene.  Adjust BLUR_PASSES to apply the
// two-pass blur multiple times for a stronger effect.
//
// Move the mouse horizontally to control the blur radius:
//   left edge  → no blur  (radius = 0)
//   right edge → maximum blur (radius = MAX_BLUR_RADIUS)
public class GaussianBlurExample extends GarnetApp {

    static final int W = 400;
    static final int H = 300;
    static final int NUM_SPRITES = 8;

    // Repeat the two blur passes this many times for a stronger blur
    static final int BLUR_PASSES = 3;

    // Maximum blur-radius multiplier (mouse all the way right)
    static final float MAX_BLUR_RADIUS = 5.0f;

    Texture texture;

    // FBO that holds the raw scene
    RenderTexture rtScene;
    // FBO that holds the horizontally blurred intermediate result
    RenderTexture rtBlurH;

    ShaderProgram blurHShader;
    ShaderProgram blurVShader;

    double time = 0;
    double[] angle = new double[NUM_SPRITES];
    float blurRadius = 1.0f;

    public GaussianBlurExample(Garnet garnet, String name) {
        super(garnet, name);
    }

    public static void main(String[] args) {
        Garnet garnet = new Garnet(W, H);
        GarnetApp app = new GaussianBlurExample(garnet, "");
        garnet.setApp(app);
        garnet.init();
        garnet.run();
    }

    @Override
    public void init(Garnet garnet) {
        garnet.getDisplay().setWindowTitle("Two-Pass Gaussian Blur Example");
        garnet.getGraphics().setBackgroundColor(ColorUtils.BLACK);

        texture = Texture.loadTexture("garnetCrystal.png");
        garnet.getGraphics().addTexture(texture);

        rtScene = new RenderTexture(W, H);
        garnet.getGraphics().addTexture(rtScene.getTexture());

        rtBlurH = new RenderTexture(W, H);
        garnet.getGraphics().addTexture(rtBlurH.getTexture());

        float texelW = 1.0f / W;
        float texelH = 1.0f / H;

        blurHShader = ShaderProgram.fromFiles("shaders/passthrough.vert", "shaders/blur_h.frag");
        blurHShader.bind();
        glUniform2f(glGetUniformLocation(blurHShader.getProgramId(), "texelSize"), texelW, texelH);
        glUniform1f(glGetUniformLocation(blurHShader.getProgramId(), "blurRadius"), blurRadius);
        glUseProgram(0);

        blurVShader = ShaderProgram.fromFiles("shaders/passthrough.vert", "shaders/blur_v.frag");
        blurVShader.bind();
        glUniform2f(glGetUniformLocation(blurVShader.getProgramId(), "texelSize"), texelW, texelH);
        glUniform1f(glGetUniformLocation(blurVShader.getProgramId(), "blurRadius"), blurRadius);
        glUseProgram(0);

        for (int i = 0; i < NUM_SPRITES; i++) {
            angle[i] = i * (2 * Math.PI / NUM_SPRITES);
        }
    }

    @Override
    public void tick(double delta) {
        time += delta;
        for (int i = 0; i < NUM_SPRITES; i++) {
            angle[i] += delta * 0.4;
        }

        // Map normalised mouse X [0,1] to blur radius [0, MAX_BLUR_RADIUS]
        double[] mouseNorm = garnet.getInput().getMouse().getPositionNormalised();
        blurRadius = (float) (mouseNorm[0] * MAX_BLUR_RADIUS);

        blurHShader.bind();
        glUniform1f(glGetUniformLocation(blurHShader.getProgramId(), "blurRadius"), blurRadius);
        glUseProgram(0);

        blurVShader.bind();
        glUniform1f(glGetUniformLocation(blurVShader.getProgramId(), "blurRadius"), blurRadius);
        glUseProgram(0);
    }

    @Override
    public void draw(Graphics g) {
        int tw = texture.getWidth();
        int th = texture.getHeight();
        float cx = W / 2f;
        float cy = H / 2f;

        // ── Pass 1: render scene into rtScene ────────────────────────────
        rtScene.bind();
        glClear(GL_COLOR_BUFFER_BIT);
        g.setDrawOrder(0);
        for (int i = 0; i < NUM_SPRITES; i++) {
            float sx = cx + (float) (Math.cos(angle[i]) * 110) - tw / 2f;
            float sy = cy + (float) (Math.sin(angle[i]) * 80) - th / 2f;
            g.setColor(ColorUtils.WHITE);
            g.drawImage(texture, (int) sx, (int) sy);
        }
        g.render();
        rtScene.unbind(garnet.getDisplay());

        // ── Passes 2+: ping-pong blur between rtScene/rtBlurH ────────────
        // Each iteration applies one horizontal pass then one vertical pass.
        // The first horizontal pass reads from rtScene; subsequent ones read
        // from rtBlurH (which holds the previous iteration's vertical output).
        RenderTexture src = rtScene;

        for (int pass = 0; pass < BLUR_PASSES; pass++) {

            // Horizontal blur: src → rtBlurH
            rtBlurH.bind();
            glClear(GL_COLOR_BUFFER_BIT);
            g.setDrawOrder(0);
            g.setColor(ColorUtils.WHITE);
            g.drawImage(src.getTexture(), 0, 0).setShader(blurHShader);
            g.render();
            rtBlurH.unbind(garnet.getDisplay());

            // Vertical blur: rtBlurH → screen (last pass) or back to rtScene
            // For simplicity we always write the vertical result to the screen
            // on the final pass, and to rtScene for intermediate passes so the
            // next iteration can read from it again.
            if (pass < BLUR_PASSES - 1) {
                rtScene.bind();
                glClear(GL_COLOR_BUFFER_BIT);
                g.setDrawOrder(0);
                g.setColor(ColorUtils.WHITE);
                g.drawImage(rtBlurH.getTexture(), 0, 0).setShader(blurVShader);
                g.render();
                rtScene.unbind(garnet.getDisplay());
                src = rtScene;
            }
        }

        // ── Final pass: vertical blur to screen ───────────────────────────
        g.setDrawOrder(0);
        g.setColor(ColorUtils.WHITE);
        g.drawImage(rtBlurH.getTexture(), 0, 0).setShader(blurVShader);
    }
}
