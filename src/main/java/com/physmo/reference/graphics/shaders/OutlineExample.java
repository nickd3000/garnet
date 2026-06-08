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
// OutlineExample demonstrates the outline.frag shader, which draws a solid
// colour border around the opaque silhouette of each sprite.
//
// The shader samples the 4 cardinal neighbours of every transparent pixel; if
// any neighbour is opaque the pixel is filled with outlineColor, producing a
// crisp 1-texel outline without any geometry changes.
//
// Three sprites are shown with different outline colours (red, yellow, cyan).
//
// WHY THREE SHADER INSTANCES?
// Each ShaderProgram has its own set of uniform values stored on the GPU.
// If you reuse a single ShaderProgram and update the outline colour uniform
// colour before each sprite, all sprites end up the same colour — because
// Garnet batches draw calls and only executes them at the end of the frame,
// by which point the last uniform update has overwritten all the earlier
// ones.  The fix is to compile a separate ShaderProgram per colour and bake
// the uniform values in at init() time, so each sprite carries its own
// immutable GPU state into the batch.
public class OutlineExample extends GarnetApp {

    static final int WINDOW_W = 400;
    static final int WINDOW_H = 200;

    Texture texture;
    ShaderProgram outlineShaderRed;
    ShaderProgram outlineShaderYellow;
    ShaderProgram outlineShaderCyan;

    public OutlineExample(Garnet garnet, String name) {
        super(garnet, name);
    }

    public static void main(String[] args) {
        Garnet garnet = new Garnet(WINDOW_W, WINDOW_H);
        GarnetApp app = new OutlineExample(garnet, "");
        garnet.setApp(app);
        garnet.init();
        garnet.run();
    }

    @Override
    public void init(Garnet garnet) {
        garnet.getDisplay().setWindowTitle("Outline Shader Example");
        garnet.getGraphics().setBackgroundColor(ColorUtils.DARK_GREY);

        texture = Texture.loadTexture("garnetCrystal.png");
        garnet.getGraphics().addTexture(texture);

        TextureRegion textureRegion = garnet.getGraphics().getTextureRegion(texture);
        float texelW = 1.0f / textureRegion.textureWidth();
        float texelH = 1.0f / textureRegion.textureHeight();

        outlineShaderRed = ShaderProgram.fromFiles("shaders/passthrough.vert", "shaders/outline.frag");
        outlineShaderRed.bind();
        outlineShaderRed.setUniform2f("texelSize", texelW, texelH);
        outlineShaderRed.setUniform4f("outlineColor", 1f, 0.1f, 0.1f, 1f);
        outlineShaderRed.unbind();

        outlineShaderYellow = ShaderProgram.fromFiles("shaders/passthrough.vert", "shaders/outline.frag");
        outlineShaderYellow.bind();
        outlineShaderYellow.setUniform2f("texelSize", texelW, texelH);
        outlineShaderYellow.setUniform4f("outlineColor", 1f, 0.9f, 0f, 1f);
        outlineShaderYellow.unbind();

        outlineShaderCyan = ShaderProgram.fromFiles("shaders/passthrough.vert", "shaders/outline.frag");
        outlineShaderCyan.bind();
        outlineShaderCyan.setUniform2f("texelSize", texelW, texelH);
        outlineShaderCyan.setUniform4f("outlineColor", 0f, 1f, 0.9f, 1f);
        outlineShaderCyan.unbind();
    }

    @Override
    public void tick(double delta) {
    }

    @Override
    public void draw(Graphics g) {
        int tw = texture.getWidth();
        int th = texture.getHeight();

        int startY = (WINDOW_H - th) / 2;
        int spacing = 110;
        int startX = (WINDOW_W - spacing * 2 - tw) / 2;

        // Each sprite uses its own shader instance with uniforms baked in at init time
        g.setColor(ColorUtils.WHITE);
        g.drawImage(texture, startX, startY).setShader(outlineShaderRed);                  // red outline
        g.drawImage(texture, startX + spacing, startY).setShader(outlineShaderYellow);     // yellow outline
        g.drawImage(texture, startX + spacing * 2, startY).setShader(outlineShaderCyan);   // cyan outline
    }
}
