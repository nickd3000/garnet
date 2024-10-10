package com.physmo.garnetexamples.text;

import com.physmo.garnet.ColorUtils;
import com.physmo.garnet.Garnet;
import com.physmo.garnet.GarnetApp;
import com.physmo.garnet.graphics.Graphics;
import com.physmo.garnet.text.BitmapFont;
import com.physmo.garnet.text.ParagraphDrawer;
import com.physmo.garnet.text.RegularFont;

import java.io.IOException;

// NOTE: On MacOS the following VM argument is required: -XstartOnFirstThread
public class ParagraphExample extends GarnetApp {

    RegularFont regularFont;
    BitmapFont bitmapFont;
    ParagraphDrawer bitmapFontParagraphDrawer;
    ParagraphDrawer regularFontParagraphDrawer;

    public ParagraphExample(Garnet garnet, String name) {
        super(garnet, name);
    }

    public static void main(String[] args) {
        Garnet garnet = new Garnet(800, 600);
        GarnetApp app = new ParagraphExample(garnet, "");

        garnet.setApp(app);

        garnet.init();
        garnet.run();
    }

    @Override
    public void init(Garnet garnet) {
        regularFont = new RegularFont("regularfonts/12x12Font.png", 12, 12);
        regularFont.setHorizontalPad(-5);
        garnet.getGraphics().setBackgroundColor(com.physmo.garnet.ColorUtils.SUNSET_BLUE);

        String bitmapFontImagePath = "bitmapfonts/SmallFont12.png";
        String bitmapFontDefinitionPath = "bitmapfonts/SmallFont12.fnt";

        try {
            bitmapFont = new BitmapFont(bitmapFontImagePath, bitmapFontDefinitionPath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        bitmapFontParagraphDrawer = new ParagraphDrawer(bitmapFont);
        regularFontParagraphDrawer = new ParagraphDrawer(regularFont);
    }

    @Override
    public void tick(double delta) {
    }

    @Override
    public void draw(Graphics g) {

        String paragraphText = "The Paragraph() class can be used to display a large string of text that is constrained to a specified width and height.\n";
        String paragraphText2 = "Move the mouse to change the width of this paragraph.\n The paragraph drawer returns the total height of the lines in the drawn paragraph.";
        String paragraphWithLineBreaks = "There's a line break in the middle of the next word: bro\nken. And now 3 line breaks \n\n\n [end]";
        g.setZoom(1);
        g.setColor(com.physmo.garnet.ColorUtils.SUNSET_GREEN);
        //regularFont.drawText(garnet.getGraphics(), "Regular font", 0, 10);

        regularFontParagraphDrawer.setPadY(3);
        regularFont.setScale(2);
        regularFontParagraphDrawer.drawParagraph(g, paragraphText, 400 - 10, 150, 10, 10);


        g.setColor(ColorUtils.SUNSET_YELLOW);
        regularFont.setScale(3);
        regularFontParagraphDrawer.drawParagraph(g, paragraphWithLineBreaks, 400 - 10, 200, 410, 10);

        int[] mp = garnet.getInput().getMouse().getPositionScaled(1);
        bitmapFontParagraphDrawer.setPadY(4);
        bitmapFont.setScale(2);
        int ph = bitmapFontParagraphDrawer.drawParagraph(g, paragraphText2 + paragraphText2, mp[0], 200, 10, 250);
        g.drawRect(10, 250, mp[0], ph);

    }

}
