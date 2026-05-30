package com.physmo.reference.graphics.shaders;

import com.physmo.garnet.ColorUtils;
import com.physmo.garnet.Garnet;
import com.physmo.garnet.GarnetApp;
import com.physmo.garnet.graphics.Graphics;
import com.physmo.garnet.graphics.RenderTexture;
import com.physmo.garnet.graphics.ShaderProgram;
import com.physmo.garnet.graphics.Texture;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glUniform2f;
import static org.lwjgl.opengl.GL20.glUseProgram;

// NOTE: On MacOS the following VM argument is required: -XstartOnFirstThread
//
// CrtExample demonstrates a full-screen CRT post-process effect applied to a
// normal scene rendered into an off-screen FBO (RenderTexture).
//
// The crt.frag shader applies, in order:
//   1. Barrel distortion  — slight screen curvature with black border
//   2. Phosphor pixel grid — snaps to coarse CRT dots with RGB sub-pixel stripes
//   3. Scanlines           — horizontal dark bands between phosphor rows
//   4. Colour bleeding     — chromatic aberration shifts R left and B right
//   5. Vignette            — edges of the screen are darkened
//
// Scene: several sprites orbit the centre of the screen.  The whole scene is
// first rendered into a RenderTexture, then the CRT shader is applied to that
// texture as a full-screen quad.
public class CrtExample extends GarnetApp {

    static final int WINDOW_W = 480;
    static final int WINDOW_H = 320;

    Texture texture;
    RenderTexture renderTexture;
    ShaderProgram crtShader;

    double time = 0;

    public CrtExample(Garnet garnet, String name) {
        super(garnet, name);
    }

    public static void main(String[] args) {
        Garnet garnet = new Garnet(WINDOW_W, WINDOW_H);
        GarnetApp app = new CrtExample(garnet, "");
        garnet.setApp(app);
        garnet.init();
        garnet.run();
    }

    @Override
    public void init(Garnet garnet) {
        garnet.getDisplay().setWindowTitle("CRT Shader Example");
        garnet.getGraphics().setBackgroundColor(ColorUtils.BLACK);

        texture = Texture.loadTexture("garnetCrystal.png");
        garnet.getGraphics().addTexture(texture);

        renderTexture = new RenderTexture(WINDOW_W, WINDOW_H);
        garnet.getGraphics().addTexture(renderTexture.getTexture());

        crtShader = ShaderProgram.fromFiles("shaders/passthrough.vert", "shaders/crt.frag");
    }

    @Override
    public void tick(double delta) {
        time += delta;
    }

    @Override
    public void draw(Graphics g) {

        // ── Pass 1: render the scene into the off-screen FBO ────────────────
        renderTexture.bind();
        glClearColor(0.05f, 0.05f, 0.15f, 1.0f);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        int cx = WINDOW_W / 2;
        int cy = WINDOW_H / 2;
        int numSprites = 8;

        // Outer ring — fast orbit
        for (int i = 0; i < numSprites; i++) {
            double angle = time * 0.7 + i * (Math.PI * 2.0 / numSprites);
            int sx = (int) (cx + Math.cos(angle) * 120) - texture.getWidth() / 2;
            int sy = (int) (cy + Math.sin(angle) * 80) - texture.getHeight() / 2;
            g.setColor(ColorUtils.rgb(200, 220, 255, 255));
            g.setDrawOrder(0);
            g.drawImage(texture, sx, sy);
        }

        // Inner ring — slow counter-orbit, tinted warm
        int numInner = 4;
        for (int i = 0; i < numInner; i++) {
            double angle = -time * 1.1 + i * (Math.PI * 2.0 / numInner);
            int sx = (int) (cx + Math.cos(angle) * 55) - texture.getWidth() / 2;
            int sy = (int) (cy + Math.sin(angle) * 55) - texture.getHeight() / 2;
            g.setColor(ColorUtils.rgb(255, 200, 120, 255));
            g.setDrawOrder(0);
            g.drawImage(texture, sx, sy);
        }

        // Flush the batch into the FBO, then restore the default framebuffer
        g.render();
        renderTexture.unbind(garnet.getDisplay());

        // ── Pass 2: draw the FBO texture through the CRT shader ─────────────
        crtShader.bind();
        int loc = glGetUniformLocation(crtShader.getProgramId(), "resolution");
        glUniform2f(loc, WINDOW_W, WINDOW_H);
        glUseProgram(0);

        g.setColor(ColorUtils.WHITE);
        g.setDrawOrder(0);
        g.drawImage(renderTexture.getTexture(), 0, 0)
                .setShader(crtShader);
    }
}
