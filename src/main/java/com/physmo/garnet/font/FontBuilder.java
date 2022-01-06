package com.physmo.garnet.font;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.font.FontRenderContext;
import java.awt.font.LineMetrics;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class FontBuilder {

    BufferedImage outputImage;
    Set<Character> characters;
    Map<Character, CharDescriptor> characterDescriptors;

    boolean drawDebugBox = false;
    Color fontColor = new Color(0,0,0);

    public static void main(String[] args) {
        String[] availableFontFamilyNames = GraphicsEnvironment
                .getLocalGraphicsEnvironment()
                .getAvailableFontFamilyNames();
        for (String fn : availableFontFamilyNames) {
            System.out.println(fn);
        }


        FontBuilder fontBuilder = new FontBuilder();
        fontBuilder.buildFont();
    }



    public void buildFont() {
        Font font = loadBuiltInFont("");
        if (font == null) System.out.println("Font is null");

        outputImage = new BufferedImage(256, 256, BufferedImage.TYPE_INT_ARGB);

        Graphics g = outputImage.getGraphics();
        g.setFont(font);
        g.setColor(Color.BLACK);
        //g.drawString("hello", 20, 20);

        FontRenderContext frc = ((Graphics2D) g).getFontRenderContext();

        characters = buildStandardAsciiCharList();
        FontMetrics metrics = g.getFontMetrics(font);
        characterDescriptors = getMetricsForChars(frc, metrics, font, characters);

        packCharactersToImage(outputImage, g, characterDescriptors);

        System.out.println(1);
    }

    private void packCharactersToImage(BufferedImage bufferedImage, Graphics g, Map<Character, CharDescriptor> metricsForChars) {
        int x = 0, y = 0;
        int lineMaxHeight = 0;
        for (Character character : metricsForChars.keySet()) {
            CharDescriptor cd = metricsForChars.get(character);

            if (x + cd.width >= bufferedImage.getWidth()) {
                x = 0;
                y += lineMaxHeight;
                lineMaxHeight = 0;
            }
            drawChar(g, cd, x, y);
            cd.setTexturePosition(x, y);

            x += cd.width + 1;
            if (lineMaxHeight < cd.height) lineMaxHeight = (int) cd.height;
        }
    }

    public void drawChar(Graphics g, CharDescriptor cd, int x, int y) {

        Graphics2D g2 = (Graphics2D) g;

        int w = (int) cd.width;
        int h = (int) cd.height;

        y += cd.ascent;

        g2.setColor(Color.BLUE);
        int yShift = (int) (cd.ascent);
        g2.drawRect(x, (y - yShift), w, h);

        g2.setColor(Color.BLACK);
        g2.drawString(String.valueOf(cd.c), x, y);
    }

    public Map<Character, CharDescriptor> getMetricsForChars(FontRenderContext frc, FontMetrics metrics, Font font, Set<Character> chars) {
        Map<Character, CharDescriptor> fontCharMetricsList = new HashMap<>();
        for (Character c : chars) {
            LineMetrics lineMetrics = font.getLineMetrics(String.valueOf(c), frc);
            int charWidth = metrics.charWidth(c);
            fontCharMetricsList.put(c, new CharDescriptor(c, charWidth, lineMetrics.getHeight(), lineMetrics.getAscent(), lineMetrics.getDescent()));
        }
        return fontCharMetricsList;
    }

    public Font loadBuiltInFont(String resource) {
        Font font = new Font("Dialog", Font.PLAIN, 10);
        return font;
    }

    public Set<Character> buildStandardAsciiCharList() {
        Set<Character> chars = new HashSet<>();
        for (int i = 32; i < 127; i++) {
            chars.add((char) i);
        }
        return chars;
    }

}
