package com.physmo.reference.graphics;

import com.physmo.garnet.ColorUtils;
import com.physmo.garnet.Garnet;
import com.physmo.garnet.GarnetApp;
import com.physmo.garnet.graphics.Graphics;
import com.physmo.garnet.graphics.Texture;
import com.physmo.garnet.input.Mouse;

// NOTE: On MacOS the following VM argument is required: -XstartOnFirstThread
public class WindowScaleExample extends GarnetApp {

    Texture texture;
    int windowScale = 3;

    public static void main(String[] args) {
        Garnet.launch(320, 240, WindowScaleExample::new);
    }

    @Override
    public void init() {
        // Load texture
        texture = Texture.loadTexture("garnetCrystal.png");

        // Add texture to graphics system.
        garnet.getGraphics().addTexture(texture);
        garnet.getGraphics().setBackgroundColor(ColorUtils.DARK_GREY);

        garnet.getDisplay().setWindowScale(windowScale, true);

        System.out.println("* Press left mouse button to cycle window scale");
    }

    @Override
    public void tick(double delta) {
        if (garnet.getInput().getMouse().isButtonFirstPress(Mouse.BUTTON_LEFT)) {
            windowScale++;
            if (windowScale > 3) windowScale = 1;
            garnet.getDisplay().setWindowScale(windowScale, true);
        }
    }

    @Override
    public void draw(Graphics g) {
        int[] mousePosition = garnet.getInput().getMouse().getPosition();
        g.drawImage(texture, mousePosition[0] - texture.getWidth() / 2, mousePosition[1] - texture.getHeight() / 2);
    }
}

