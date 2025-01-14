package com.physmo.reference.text;

import com.physmo.garnet.ColorUtils;
import com.physmo.garnet.Garnet;
import com.physmo.garnet.GarnetApp;
import com.physmo.garnet.graphics.Graphics;
import com.physmo.garnet.text.BitmapFont;

import java.io.IOException;

// NOTE: On MacOS the following VM argument is required: -XstartOnFirstThread
public class BMFontExample extends GarnetApp {

    BitmapFont bmfFont;

    public BMFontExample(Garnet garnet, String name) {
        super(garnet, name);
    }

    public static void main(String[] args) {
        Garnet garnet = new Garnet(400, 300);
        GarnetApp app = new BMFontExample(garnet, "");

        garnet.setApp(app);

        garnet.init();
        garnet.run();
    }

    @Override
    public void init(Garnet garnet) {

        String imagePath = "bitmapfonts/ptmono16_0.png";
        String definitionPath = "bitmapfonts/ptmono16.fnt";

        try {
            bmfFont = new BitmapFont(imagePath, definitionPath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    @Override
    public void tick(double delta) {
    }

    @Override
    public void draw(Graphics g) {

        g.setZoom(1);
        g.setColor(com.physmo.garnet.ColorUtils.SUNSET_GREEN);
        bmfFont.setScale(1);
        bmfFont.drawText(g, "hello", 0, 10);

        g.setColor(com.physmo.garnet.ColorUtils.SUNSET_YELLOW);
        bmfFont.setScale(2);
        bmfFont.drawText(g, "hello but bigger", 0, 50);

        g.setColor(ColorUtils.SUNSET_YELLOW);
        bmfFont.setScale(3);
        bmfFont.drawText(g, "hello but even bigger", 0, 80);
    }

}
