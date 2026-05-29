package com.physmo.reference.graphics;

import com.physmo.garnet.ColorUtils;
import com.physmo.garnet.Garnet;
import com.physmo.garnet.GarnetApp;
import com.physmo.garnet.drawablebatch.BlendMode;
import com.physmo.garnet.graphics.Graphics;
import com.physmo.garnet.graphics.Texture;

// NOTE: On MacOS the following VM argument is required: -XstartOnFirstThread
//
// BlendModeExample demonstrates the BlendMode options and colorOverride on DrawableElement.
//
// Each column renders a solid red background square, then a sprite on top:
//
//   Column 1 - NORMAL        : standard alpha transparency
//   Column 2 - ADDITIVE      : colours add together (glow/fire effect)
//   Column 3 - SUBTRACTIVE   : colours subtract (shadow/ink effect)
//   Column 4 - MULTIPLY      : destination multiplied by source (tinting)
//   Column 5 - MATTE         : writes only to alpha channel — sprite appears blank/invisible by design
//   Column 6 - colorOverride : every visible pixel rendered as the vertex colour (hit-flash effect)
//
// The blend mode is set by calling sprite.setBlendMode(BlendMode.XXX) on the
// Sprite2D returned by g.drawImage(texture, x, y).
// colorOverride is set by calling sprite.setColorOverride(true).
public class BlendModeExample extends GarnetApp {

    static final int WINDOW_W = 720;
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
        int numColumns = 6;
        int tileSize = 100;
        int padding = 10;
        int startX = 25;
        int startY = 80;

        int spriteOffX = (tileSize - texture.getWidth()) / 2;
        int spriteY = mousePos[1] - texture.getHeight() / 2;

        for (int i = 0; i < numColumns; i++) {
            int x = startX + i * (tileSize + padding);

            // Background square
            g.setColor(ColorUtils.rgb(220, 60, 60, 255));
            g.setDrawOrder(0);
            g.filledRect(x, startY, tileSize, tileSize);

            // Sprite — blend mode or effect varies per column
            g.setDrawOrder(1);
            if (i == 5) {
                // Column 6 - colorOverride: every visible pixel rendered as the vertex colour
                g.setColor(ColorUtils.rgb(255, 255, 255, 255));
                g.drawImage(texture, x + spriteOffX, spriteY)
                        .setColorOverride(true);
            } else {
                g.setColor(ColorUtils.rgb(60, 100, 220, 0xff));
                if (i == 0) {
                    // Column 1 - NORMAL: standard alpha transparency
                    g.drawImage(texture, x + spriteOffX, spriteY)
                            .setBlendMode(BlendMode.NORMAL);
                } else if (i == 1) {
                    // Column 2 - ADDITIVE: colours add together (glow/fire effect)
                    g.drawImage(texture, x + spriteOffX, spriteY)
                            .setBlendMode(BlendMode.ADDITIVE);
                } else if (i == 2) {
                    // Column 3 - SUBTRACTIVE: colours subtract (shadow/ink effect)
                    g.drawImage(texture, x + spriteOffX, spriteY)
                            .setBlendMode(BlendMode.SUBTRACTIVE);
                } else if (i == 3) {
                    // Column 4 - MULTIPLY: destination multiplied by source (tinting)
                    g.drawImage(texture, x + spriteOffX, spriteY)
                            .setBlendMode(BlendMode.MULTIPLY);
                } else if (i == 4) {
                    // Column 5 - MATTE: writes only to alpha channel — sprite appears blank/invisible by design
                    g.drawImage(texture, x + spriteOffX, spriteY)
                            .setBlendMode(BlendMode.MATTE);
                }
            }
        }
    }
}
