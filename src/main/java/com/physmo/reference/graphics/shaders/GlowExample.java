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
// GlowExample demonstrates the glow.frag shader, which adds a soft coloured
// halo around the opaque silhouette of a sprite by sampling a ring of
// neighbouring texels and accumulating their alpha contributions.
//
// Three sprites are shown with different glow colours and radii:
//   - blue glow,  radius 3
//   - orange glow, radius 5
//   - green glow,  radius 2
//
// WHY THREE SHADER INSTANCES?
// Each ShaderProgram stores its own GPU uniform state.  Garnet batches all
// draw calls and flushes them at end-of-frame, so any uniform update made before
// a draw() can be overwritten by later calls before rendering occurs.
// The fix is one ShaderProgram per distinct uniform value, with the value
// baked in at init() time (see OutlineExample for a full explanation).
public class GlowExample extends GarnetApp {

    static final int WINDOW_W = 500;
    static final int WINDOW_H = 200;

    Texture texture;
    ShaderProgram glowBlue;
    ShaderProgram glowOrange;
    ShaderProgram glowGreen;

    public static void main(String[] args) {
        Garnet.launch(WINDOW_W, WINDOW_H, GlowExample::new);
    }

    @Override
    public void init() {
        garnet.getDisplay().setWindowTitle("Glow Shader Example");
        garnet.getGraphics().setBackgroundColor(ColorUtils.DARK_GREY);

        texture = Texture.loadTexture("garnetCrystal.png");
        garnet.getGraphics().addTexture(texture);

        TextureRegion textureRegion = garnet.getGraphics().getTextureRegion(texture);
        float texelW = 1.0f / textureRegion.textureWidth();
        float texelH = 1.0f / textureRegion.textureHeight();

        glowBlue = ShaderProgram.fromFiles("shaders/passthrough.vert", "shaders/glow.frag");
        glowBlue.bind();
        glowBlue.setUniform2f("texelSize", texelW, texelH);
        glowBlue.setUniform4f("glowColor", 0.2f, 0.5f, 1.0f, 1.0f);
        glowBlue.setUniform1f("glowRadius", 3.0f);
        glowBlue.unbind();

        glowOrange = ShaderProgram.fromFiles("shaders/passthrough.vert", "shaders/glow.frag");
        glowOrange.bind();
        glowOrange.setUniform2f("texelSize", texelW, texelH);
        glowOrange.setUniform4f("glowColor", 1.0f, 0.5f, 0.1f, 1.0f);
        glowOrange.setUniform1f("glowRadius", 1.5f);
        glowOrange.unbind();

        glowGreen = ShaderProgram.fromFiles("shaders/passthrough.vert", "shaders/glow.frag");
        glowGreen.bind();
        glowGreen.setUniform2f("texelSize", texelW, texelH);
        glowGreen.setUniform4f("glowColor", 0.2f, 1.0f, 0.3f, 1.0f);
        glowGreen.setUniform1f("glowRadius", 2.0f);
        glowGreen.unbind();
    }

    @Override
    public void tick(double delta) {
    }

    @Override
    public void draw(Graphics g) {
        int tw = texture.getWidth();
        int th = texture.getHeight();
        int startY = (WINDOW_H - th) / 2;
        int spacing = 140;
        int startX = (WINDOW_W - spacing * 2 - tw) / 2;

        g.setColor(ColorUtils.WHITE);
        g.drawImage(texture, startX, startY).setShader(glowBlue);
        g.drawImage(texture, startX + spacing, startY).setShader(glowOrange);
        g.drawImage(texture, startX + spacing * 2, startY).setShader(glowGreen);

    }
}
