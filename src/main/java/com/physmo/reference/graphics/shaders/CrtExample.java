package com.physmo.reference.graphics.shaders;

import com.physmo.garnet.ColorUtils;
import com.physmo.garnet.Garnet;
import com.physmo.garnet.GarnetApp;
import com.physmo.garnet.graphics.Graphics;
import com.physmo.garnet.graphics.ShaderProgram;
import com.physmo.garnet.graphics.Texture;


// NOTE: On MacOS the following VM argument is required: -XstartOnFirstThread
//
// CrtExample demonstrates a full-screen CRT post-process effect applied to a
// normal scene rendered into an off-screen FBO (RenderTexture).
//
// The crt.frag shader applies, in order:
//   1. Barrel distortion  — slight screen curvature with black border
//   2. Scanlines           — horizontal dark bands between pixel rows
//   3. Colour bleeding     — chromatic aberration shifts R left and B right
//   4. Vignette            — edges of the screen are darkened
//
// Scene: several sprites orbit the centre of the screen.  The whole scene is
// first rendered into a RenderTexture, then the CRT shader is applied to that
// texture as a full-screen quad.
public class CrtExample extends GarnetApp {

    static final int WINDOW_W = 480;
    static final int WINDOW_H = 320;

    Texture texture;
    ShaderProgram crtShader;

    double time = 0;

    public static void main(String[] args) {
        Garnet.launch(WINDOW_W, WINDOW_H, CrtExample::new,
                garnet -> garnet.setInternalBufferMode(true));
    }

    @Override
    public void init() {
        garnet.getDisplay().setWindowTitle("CRT Shader Example");
        garnet.getGraphics().setBackgroundColor(ColorUtils.BLACK);

        texture = Texture.loadTexture("garnetCrystal.png");
        garnet.getGraphics().addTexture(texture);

        crtShader = ShaderProgram.fromFiles("shaders/passthrough.vert", "shaders/crt.frag");

        garnet.setInternalBufferShader(crtShader);
    }

    @Override
    public void tick(double delta) {
        time += delta;
    }

    @Override
    public void draw(Graphics g) {

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

        // Apply uniforms for the next time the CRT shader is used (at the end of the frame)
        crtShader.bind();
        crtShader.setUniform2f("resolution", WINDOW_W, WINDOW_H);
        crtShader.unbind();
    }
}
