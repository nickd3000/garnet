package com.physmo.reference.graphics.shaders;

import com.physmo.garnet.ColorUtils;
import com.physmo.garnet.Garnet;
import com.physmo.garnet.GarnetApp;
import com.physmo.garnet.graphics.Graphics;
import com.physmo.garnet.graphics.ShaderProgram;
import com.physmo.garnet.graphics.Texture;


// NOTE: On MacOS the following VM argument is required: -XstartOnFirstThread
//
// PaletteExample demonstrates the palette.frag shader, which reduces the image
// to a limited number of colour levels per channel and applies a Bayer 4x4
// ordered dither to smooth the hard quantisation steps.
//
// Four columns are shown with decreasing colour depth:
//   Column 1 — no shader  : full colour
//   Column 2 — 8 levels   : slight posterisation, subtle dither
//   Column 3 — 4 levels   : strong posterisation, visible dither
//   Column 4 — 2 levels   : 1-bit per channel (8 colours total), heavy dither
public class PaletteExample extends GarnetApp {

    static final int WINDOW_W = 520;
    static final int WINDOW_H = 200;

    Texture texture;
    ShaderProgram paletteShader;

    public PaletteExample(Garnet garnet, String name) {
        super(garnet, name);
    }

    public static void main(String[] args) {
        Garnet garnet = new Garnet(WINDOW_W, WINDOW_H);
        GarnetApp app = new PaletteExample(garnet, "");
        garnet.setApp(app);
        garnet.init();
        garnet.run();
    }

    @Override
    public void init(Garnet garnet) {
        garnet.getDisplay().setWindowTitle("Palette / Dither Shader Example");
        garnet.getGraphics().setBackgroundColor(ColorUtils.DARK_GREY);

        texture = Texture.loadTexture("garnetCrystal.png");
        garnet.getGraphics().addTexture(texture);

        paletteShader = ShaderProgram.fromFiles("shaders/passthrough.vert", "shaders/palette.frag");
    }

    @Override
    public void tick(double delta) {
    }

    @Override
    public void draw(Graphics g) {
        int tw = texture.getWidth();
        int th = texture.getHeight();
        int startY = (WINDOW_H - th) / 2;
        int spacing = 120;
        int startX = (WINDOW_W - spacing * 3 - tw) / 2;

        // Column 1 — no shader
        g.setColor(ColorUtils.WHITE);
        g.drawImage(texture, startX, startY);

        float[] levels = {8f, 4f, 2f};
        float[] ditherScale = {1f, 1f, 1f};

        for (int i = 0; i < 3; i++) {
            paletteShader.bind();
            paletteShader.setUniform1f("levels", levels[i]);
            paletteShader.setUniform1f("ditherScale", ditherScale[i]);
            paletteShader.setUniform2f("resolution", tw, th);
            paletteShader.unbind();

            g.setColor(ColorUtils.WHITE);
            g.drawImage(texture, startX + spacing * (i + 1), startY).setShader(paletteShader);
        }
    }
}
