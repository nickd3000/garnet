package com.physmo.reference.graphics.shaders;

import com.physmo.garnet.ColorUtils;
import com.physmo.garnet.Garnet;
import com.physmo.garnet.GarnetApp;
import com.physmo.garnet.graphics.Graphics;
import com.physmo.garnet.graphics.Texture;

// NOTE: On MacOS the following VM argument is required: -XstartOnFirstThread
//
// InternalBufferExample demonstrates the new automatic internal buffer mode.
// The library handles the FBO creation and scaling automatically.
public class InternalBufferExample extends GarnetApp {

    static final int CANVAS_W = 320;
    static final int CANVAS_H = 240;

    Texture texture;
    double time = 0;

    public static void main(String[] args) {
        Garnet.launch(CANVAS_W, CANVAS_H, InternalBufferExample::new,
                garnet -> garnet.setInternalBufferMode(true));
    }

    @Override
    public void init() {
        garnet.getDisplay().setWindowTitle("Internal Buffer Example");
        garnet.getGraphics().setBackgroundColor(ColorUtils.rgb(20, 40, 60, 255));

        texture = Texture.loadTexture("garnetCrystal.png");
        garnet.getGraphics().addTexture(texture);
    }

    @Override
    public void tick(double delta) {
        time += delta;
    }

    @Override
    public void draw(Graphics g) {
        int cx = CANVAS_W / 2;
        int cy = CANVAS_H / 2;

        for (int i = 0; i < 8; i++) {
            double angle = time + i * (Math.PI * 2.0 / 8);
            int sx = (int) (cx + Math.cos(angle) * 80) - texture.getWidth() / 2;
            int sy = (int) (cy + Math.sin(angle) * 60) - texture.getHeight() / 2;
            g.setColor(ColorUtils.WHITE);
            g.drawImage(texture, sx, sy);
        }

        // Draw mouse cursor to verify coordinate mapping
        int[] mousePos = garnet.getInput().getMouse().getPosition();
        g.setColor(ColorUtils.RED);
        g.filledRect(mousePos[0] - 2, mousePos[1] - 2, 4, 4);
    }
}
