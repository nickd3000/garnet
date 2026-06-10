package com.physmo.reference.graphics.shaders;

import com.physmo.garnet.ColorUtils;
import com.physmo.garnet.Garnet;
import com.physmo.garnet.GarnetApp;
import com.physmo.garnet.graphics.Graphics;
import com.physmo.garnet.graphics.ShaderProgram;
import com.physmo.garnet.graphics.Texture;

// NOTE: On MacOS the following VM argument is required: -XstartOnFirstThread
//
// ShaderExample demonstrates user-defined GLSL shaders applied per-sprite.
//
// Two columns are rendered side-by-side using the same texture:
//
//   Column 1 - no shader   : normal colour rendering through the default batch shader
//   Column 2 - greyscale   : custom fragment shader converts colour to greyscale
//
// The shader is set by calling sprite.setShader(shaderProgram) on the
// DrawableElement returned by g.drawImage(texture, x, y).
// Calling clearShader() (or passing null) reverts to the default batch shader.
public class ShaderExample extends GarnetApp {

    static final int WINDOW_W = 300;
    static final int WINDOW_H = 200;

    Texture texture;
    ShaderProgram greyScaleShader;

    public static void main(String[] args) {
        Garnet.launch(WINDOW_W, WINDOW_H, ShaderExample::new);
    }

    @Override
    public void init() {
        garnet.getDisplay().setWindowTitle("Shader Example");
        garnet.getGraphics().setBackgroundColor(ColorUtils.DARK_GREY);

        texture = Texture.loadTexture("garnetCrystal.png");
        garnet.getGraphics().addTexture(texture);

        greyScaleShader = ShaderProgram.fromFiles("shaders/passthrough.vert", "shaders/greyscale.frag");
    }

    @Override
    public void tick(double delta) {
    }

    @Override
    public void draw(Graphics g) {
        int tileSize = 100;
        int padding = 50;
        int startX = 25;
        int startY = 50;

        int spriteOffX = (tileSize - texture.getWidth()) / 2;
        int spriteOffY = (tileSize - texture.getHeight()) / 2;

        // Column 1 - no shader: normal colour rendering
        g.setColor(ColorUtils.WHITE);
        g.drawImage(texture, startX + spriteOffX, startY + spriteOffY);

        // Column 2 - greyscale shader
        g.setColor(ColorUtils.WHITE);
        g.drawImage(texture, startX + tileSize + padding + spriteOffX, startY + spriteOffY)
                .setShader(greyScaleShader);
    }
}
