package com.physmo.garnetexamples.text;

import com.physmo.garnet.ColorUtils;
import com.physmo.garnet.Garnet;
import com.physmo.garnet.GarnetApp;
import com.physmo.garnet.graphics.Graphics;
import com.physmo.garnet.text.RegularFont;

// NOTE: On MacOS the following VM argument is required: -XstartOnFirstThread
public class RegularFontExample extends GarnetApp {

    RegularFont regularFont;

    public RegularFontExample(Garnet garnet, String name) {
        super(garnet, name);
    }

    public static void main(String[] args) {
        Garnet garnet = new Garnet(500, 400);
        GarnetApp app = new RegularFontExample(garnet, "");

        garnet.setApp(app);

        garnet.init();
        garnet.run();
    }

    @Override
    public void init(Garnet garnet) {
        regularFont = new RegularFont("regularfonts/12x12Font.png", 12, 12);
        garnet.getGraphics().setBackgroundColor(com.physmo.garnet.ColorUtils.SUNSET_BLUE);
    }

    @Override
    public void tick(double delta) {
    }

    @Override
    public void draw(Graphics g) {

        g.setZoom(2);
        g.setColor(com.physmo.garnet.ColorUtils.SUNSET_GREEN);
        regularFont.setScale(1);
        regularFont.drawText(garnet.getGraphics(), "Regular font", 0, 10);

        g.setZoom(3);
        g.setColor(com.physmo.garnet.ColorUtils.SUNSET_YELLOW);
        regularFont.setScale(3);
        regularFont.drawText(garnet.getGraphics(), "example", 0, 26);

        g.setZoom(1);
        g.setColor(com.physmo.garnet.ColorUtils.SUNSET_GREEN);
        regularFont.setHorizontalPad(2);
        regularFont.setScale(2);
        regularFont.drawText(garnet.getGraphics(), "horizontal pad 2", 0, 140);

        g.setZoom(1);
        g.setColor(ColorUtils.SUNSET_YELLOW);
        regularFont.setHorizontalPad(-3);
        regularFont.setScale(2);
        regularFont.drawText(garnet.getGraphics(), "horizontal pad -3", 0, 180);

        regularFont.setHorizontalPad(0);

    }

}
