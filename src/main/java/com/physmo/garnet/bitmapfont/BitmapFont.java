package com.physmo.garnet.bitmapfont;

import com.physmo.garnet.Texture;
import com.physmo.garnet.Utils;
import com.physmo.garnet.graphics.Graphics;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class BitmapFont {

    private static final int coordsPerChar = 16;
    Map<Integer, GlyphGeometry> geometry;
    Texture texture;

    public BitmapFont(String texturePath, String definitionPath) throws IOException {
        geometry = new HashMap<>();
        loadDefinitionData(definitionPath);
        loadTexture(texturePath);
    }

    private void loadTexture(String texturePath) {
        texture = Texture.loadTexture(texturePath);
    }

    public String loadDefinitionData(String definitionPath) throws IOException {
        InputStream inputStream = Utils.getFileFromResourceAsStream(definitionPath);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));

        String outString = "";
        String line = "";
        while ((line = bufferedReader.readLine()) != null) {
            outString += line + System.lineSeparator();
        }

        bufferedReader.close();
        inputStream.close();

        parseDefinitionData(outString);

        return outString;
    }


    private void parseDefinitionData(String fileData) {
        fileData.lines().forEach(s -> {
            if (s.startsWith("char ")) {
                GlyphGeometry bmfChar = new GlyphGeometry();
                bmfChar.parseLine(s);
                geometry.put(bmfChar.id, bmfChar);
            }
        });
    }

    public void drawString(Graphics graphics, String text, int x, int y) {
        graphics.addTexture(texture);

        float[] floats = generateCoordsForStrings(text, x, y);

        float[] v = new float[8];
        float[] t = new float[8];

        int idx = 0;
        int numPoints = floats.length / 4;
        int numQuads = numPoints / 4;

        for (int q = 0; q < numQuads; q++) {
            int ti = 0;
            int vi = 0;
            for (int i = 0; i < 4; i++) {
                v[vi++] = floats[idx++];
                v[vi++] = floats[idx++];
                t[ti++] = floats[idx++];
                t[ti++] = floats[idx++];
            }
            graphics.drawImage(texture, t, v);
        }

    }

    private float[] generateCoordsForStrings(String text, int x, int y) {
        int xOffset = 0;
        float[] coords = new float[text.length() * coordsPerChar];
        int cidx = 0;

        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            GlyphGeometry bmfChar = geometry.get((int) c);
            if (bmfChar == null) continue;

            float[] floats = generateCoordsForCharacter(bmfChar, x + xOffset, y + bmfChar.yoffset);
            xOffset += bmfChar.xadvance;

            for (int j = 0; j < floats.length; j++) {
                coords[cidx++] = floats[j];
            }
        }

        return coords;
    }

    // texture, vertex clockwise
    private float[] generateCoordsForCharacter(GlyphGeometry bmfChar, int x, int y) {

        float[] coords = new float[16];
        float[] tex = coords;

        int i = 0;
        tex[i++] = bmfChar.x;
        tex[i++] = bmfChar.y;
        coords[i++] = x;
        coords[i++] = y;

        tex[i++] = bmfChar.x + bmfChar.width;
        tex[i++] = bmfChar.y;
        coords[i++] = x + bmfChar.width;
        coords[i++] = y;

        tex[i++] = bmfChar.x + bmfChar.width;
        tex[i++] = bmfChar.y + bmfChar.height;
        coords[i++] = x + bmfChar.width;
        coords[i++] = y + bmfChar.height;

        tex[i++] = bmfChar.x;
        tex[i++] = bmfChar.y + bmfChar.height;
        coords[i++] = x;
        coords[i++] = y + bmfChar.height;


        return coords;
    }

    public int getStringWidth(String text) {
        int stringWidth = 0;
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            GlyphGeometry bmfChar = geometry.get((int) c);
            if (bmfChar == null) continue;
            stringWidth += bmfChar.xadvance;
        }
        return stringWidth;
    }

}
