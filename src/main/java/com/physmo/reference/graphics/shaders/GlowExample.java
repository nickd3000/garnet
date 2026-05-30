package com.physmo.reference.graphics.shaders;

import com.physmo.garnet.ColorUtils;
import com.physmo.garnet.Garnet;
import com.physmo.garnet.GarnetApp;
import com.physmo.garnet.graphics.Graphics;
import com.physmo.garnet.graphics.ShaderProgram;
import com.physmo.garnet.graphics.Texture;

import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glUniform1f;
import static org.lwjgl.opengl.GL20.glUniform2f;
import static org.lwjgl.opengl.GL20.glUniform4f;
import static org.lwjgl.opengl.GL20.glUseProgram;

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
// draw calls and flushes them at end-of-frame, so any glUniform call made
// before a draw() is overwritten by later calls before rendering occurs.
// The fix is one ShaderProgram per distinct uniform value, with the value
// baked in at init() time (see OutlineExample for a full explanation).
public class GlowExample extends GarnetApp {

    static final int WINDOW_W = 500;
    static final int WINDOW_H = 200;

    Texture texture;
    ShaderProgram glowBlue;
    ShaderProgram glowOrange;
    ShaderProgram glowGreen;

    public GlowExample(Garnet garnet, String name) {
        super(garnet, name);
    }

    public static void main(String[] args) {
        Garnet garnet = new Garnet(WINDOW_W, WINDOW_H);
        GarnetApp app = new GlowExample(garnet, "");
        garnet.setApp(app);
        garnet.init();
        garnet.run();
    }

    @Override
    public void init(Garnet garnet) {
        garnet.getDisplay().setWindowTitle("Glow Shader Example");
        garnet.getGraphics().setBackgroundColor(ColorUtils.DARK_GREY);

        texture = Texture.loadTexture("garnetCrystal.png");
        garnet.getGraphics().addTexture(texture);

        float texelW = 1.0f / texture.getWidth();
        float texelH = 1.0f / texture.getHeight();

        glowBlue = ShaderProgram.fromFiles("shaders/passthrough.vert", "shaders/glow.frag");
        glowBlue.bind();
        glUniform2f(glGetUniformLocation(glowBlue.getProgramId(), "texelSize"), texelW, texelH);
        glUniform4f(glGetUniformLocation(glowBlue.getProgramId(), "glowColor"), 0.2f, 0.5f, 1.0f, 1.0f);
        glUniform1f(glGetUniformLocation(glowBlue.getProgramId(), "glowRadius"), 3.0f);
        glUseProgram(0);

        glowOrange = ShaderProgram.fromFiles("shaders/passthrough.vert", "shaders/glow.frag");
        glowOrange.bind();
        glUniform2f(glGetUniformLocation(glowOrange.getProgramId(), "texelSize"), texelW, texelH);
        glUniform4f(glGetUniformLocation(glowOrange.getProgramId(), "glowColor"), 1.0f, 0.5f, 0.1f, 1.0f);
        glUniform1f(glGetUniformLocation(glowOrange.getProgramId(), "glowRadius"), 5.0f);
        glUseProgram(0);

        glowGreen = ShaderProgram.fromFiles("shaders/passthrough.vert", "shaders/glow.frag");
        glowGreen.bind();
        glUniform2f(glGetUniformLocation(glowGreen.getProgramId(), "texelSize"), texelW, texelH);
        glUniform4f(glGetUniformLocation(glowGreen.getProgramId(), "glowColor"), 0.2f, 1.0f, 0.3f, 1.0f);
        glUniform1f(glGetUniformLocation(glowGreen.getProgramId(), "glowRadius"), 2.0f);
        glUseProgram(0);
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
