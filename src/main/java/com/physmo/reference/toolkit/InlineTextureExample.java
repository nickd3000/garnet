package com.physmo.reference.toolkit;

import com.physmo.garnet.Garnet;
import com.physmo.garnet.GarnetApp;
import com.physmo.garnet.graphics.Graphics;
import com.physmo.garnet.graphics.Texture;
import com.physmo.garnet.toolkit.InlineTexture;

/**
 * InlineTextureExample is an application that demonstrates loading and utilizing inline textures
 * within the Garnet framework. It extends the GarnetApp class.
 */
public class InlineTextureExample extends GarnetApp {

    Texture texture1;
    Texture texture2;

    public static void main(String[] args) {
        Garnet.launch(300, 300, InlineTextureExample::new);
    }

    @Override
    public void init() {
        String data1 = """
                  XXXX
                 X    X
                X o  o X
                X      X
                X o  o X
                X  oo  X
                 X    X
                  XXXX
                """;

        String data2 = """
                  XXXX
                 X    X
                X o  o X
                X      X
                X  oo  X
                X o  o X
                 X    X
                  XXXX
                """;

        // Create the inline textures
        texture1 = InlineTexture.create(data1, 8, 8);
        texture2 = InlineTexture.create(data2, 8, 8);

        // Add textures to graphics system.
        garnet.getGraphics().addTexture(texture1);
        garnet.getGraphics().addTexture(texture2);
    }

    @Override
    public void tick(double delta) {
    }

    @Override
    public void draw(Graphics g) {
        g.setZoom(5);
        g.drawImage(texture1, 5, 5);
        g.drawImage(texture2, 25, 5);
    }
}
