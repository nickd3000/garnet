package com.physmo.reference.graphics;

import com.physmo.garnet.ColorUtils;
import com.physmo.garnet.Garnet;
import com.physmo.garnet.GarnetApp;
import com.physmo.garnet.drawablebatch.BlendMode;
import com.physmo.garnet.graphics.Graphics;
import com.physmo.garnet.graphics.Texture;

// NOTE: On MacOS the following VM argument is required: -XstartOnFirstThread
//
// BlendModeExample demonstrates the five BlendMode options on DrawableElement.
//
// Each column renders a solid red background square, then a semi-transparent
// blue overlay sprite drawn on top using a different blend mode:
//
//   Column 1 - NORMAL      : standard alpha transparency
//   Column 2 - ADDITIVE    : colours add together (glow/fire effect)
//   Column 3 - SUBTRACTIVE : colours subtract (shadow/ink effect)
//   Column 4 - MULTIPLY    : destination multiplied by source (tinting)
//   Column 5 - MATTE       : writes only to alpha channel (mask effect) — sprite appears blank/invisible
//
// The blend mode is set by calling sprite.setBlendMode(BlendMode.XXX) on the
// Sprite2D returned by g.drawImage(texture, x, y).
public class BlendModeExample extends GarnetApp {

    static final int WINDOW_W = 600;
    static final int WINDOW_H = 260;

    Texture texture;
    int[] mousePos = {0, 0};

    public BlendModeExample(Garnet garnet, String name) {
        super(garnet, name);
    }

    public static void main(String[] args) {
        Garnet garnet = new Garnet(WINDOW_W, WINDOW_H);
        GarnetApp app = new BlendModeExample(garnet, "");
        garnet.setApp(app);
        garnet.init();
        garnet.run();
    }

    @Override
    public void init(Garnet garnet) {
        garnet.getDisplay().setWindowTitle("Blend Mode Example");
        garnet.getGraphics().setBackgroundColor(ColorUtils.DARK_GREY);

        texture = Texture.loadTexture("garnetCrystal.png");
        garnet.getGraphics().addTexture(texture);
    }

    @Override
    public void tick(double delta) {
        mousePos = garnet.getInput().getMouse().getPosition();
    }

    @Override
    public void draw(Graphics g) {
        int tileSize = 100;
        int padding = 10;
        int startX = 25;
        int startY = 80;

        BlendMode[] modes = {
                BlendMode.NORMAL,
                BlendMode.ADDITIVE,
                BlendMode.SUBTRACTIVE,
                BlendMode.MULTIPLY,
                BlendMode.MATTE
        };

        for (int i = 0; i < modes.length; i++) {
            int x = startX + i * (tileSize + padding);

            // Layer 0: solid red background square
            g.setColor(ColorUtils.rgb(220, 60, 60, 255));
            g.setDrawOrder(0);
            g.filledRect(x, startY, tileSize, tileSize);

            // Layer 1: semi-transparent sprite per column; X is fixed to the column,
            // Y follows the mouse so the sprite slides up/down with the cursor.
            g.setColor(ColorUtils.rgb(60, 100, 220, 0xff));
            g.setDrawOrder(1);
            int spriteX = x + (tileSize - texture.getWidth()) / 2;
            int spriteY = mousePos[1] - texture.getHeight() / 2;
            g.drawImage(texture, spriteX, spriteY)
                    .setBlendMode(modes[i]);
        }
    }
}
