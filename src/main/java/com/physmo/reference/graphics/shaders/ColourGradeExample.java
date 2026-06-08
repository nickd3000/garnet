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
// ColourGradeExample demonstrates the colourgrade.frag shader applied as a
// full-screen post-process effect using a two-pass render pipeline:
//
//   Pass 1 — scene render: draws orbiting sprites into an off-screen
//             RenderTexture (FBO) at the canvas resolution.
//
//   Pass 2 — post-process: draws the FBO texture four times side-by-side,
//             each through colourgrade.frag with a different style preset:
//               style 0 = sepia  (warm brown tones)
//               style 1 = cold   (desaturated blue tint)
//               style 2 = warm   (boosted reds/yellows)
//               style 3 = night  (dark green tint)
//
// WHY FOUR SHADER INSTANCES?
// Each ShaderProgram stores its own GPU uniform state.  Garnet batches all
// draw calls and flushes them at end-of-frame, so any uniform update made before
// a draw() can be overwritten by later calls before rendering occurs.
// The fix is one ShaderProgram per distinct uniform value, with the value
// baked in at init() time (see OutlineExample for a full explanation).
public class ColourGradeExample extends GarnetApp {

    static final int W = 800;
    static final int H = 200;
    // Each style panel is W/4 wide
    static final int PANEL_W = W / 4;

    Texture texture;
    RenderTexture renderTexture;
    ShaderProgram[] gradeShaders;   // one per style preset
    double[] angle = new double[5];

    public ColourGradeExample(Garnet garnet, String name) {
        super(garnet, name);
    }

    public static void main(String[] args) {
        Garnet garnet = new Garnet(W, H);
        GarnetApp app = new ColourGradeExample(garnet, "");
        garnet.setApp(app);
        garnet.init();
        garnet.run();
    }

    @Override
    public void init(Garnet garnet) {
        garnet.getDisplay().setWindowTitle("Colour Grade Post-Process Example");
        garnet.getGraphics().setBackgroundColor(ColorUtils.DARK_GREY);

        texture = Texture.loadTexture("garnetCrystal.png");
        garnet.getGraphics().addTexture(texture);

        // FBO is panel-sized — the same scene is reused for all four panels
        renderTexture = new RenderTexture(PANEL_W, H);
        garnet.getGraphics().addTexture(renderTexture.getTexture());

        gradeShaders = new ShaderProgram[4];
        String[] labels = {"sepia", "cold", "warm", "night"};
        for (int i = 0; i < 4; i++) {
            gradeShaders[i] = ShaderProgram.fromFiles("shaders/passthrough.vert", "shaders/colourgrade.frag");
            gradeShaders[i].bind();
            gradeShaders[i].setUniform1f("style", (float) i);
            gradeShaders[i].setUniform1f("blend", 1.0f);
            gradeShaders[i].unbind();
        }

        for (int i = 0; i < angle.length; i++) {
            angle[i] = i * (2 * Math.PI / angle.length);
        }
    }

    @Override
    public void tick(double delta) {
        for (int i = 0; i < angle.length; i++) {
            angle[i] += delta * 0.7;
        }
    }

    @Override
    public void draw(Graphics g) {
        int tw = texture.getWidth();
        int th = texture.getHeight();
        float cx = PANEL_W / 2f;
        float cy = H / 2f;
        float radius = 60f;

        // --- Pass 1: render scene into FBO (panel-sized) ---
        renderTexture.bind(g);
        glClear(GL_COLOR_BUFFER_BIT);
        g.setDrawOrder(0);
        for (int i = 0; i < angle.length; i++) {
            float sx = cx + (float) (Math.cos(angle[i]) * radius) - tw / 2f;
            float sy = cy + (float) (Math.sin(angle[i]) * radius) - th / 2f;
            g.setColor(ColorUtils.WHITE);
            g.drawImage(texture, (int) sx, (int) sy);
        }
        g.render();
        renderTexture.unbind(g, garnet.getDisplay());

        // --- Pass 2: draw each panel with a different colour grade ---
        for (int i = 0; i < 4; i++) {
            g.setDrawOrder(i);
            g.setColor(ColorUtils.WHITE);
            g.drawImage(renderTexture.getTexture(), i * PANEL_W, 0).setShader(gradeShaders[i]);
        }
    }
}
