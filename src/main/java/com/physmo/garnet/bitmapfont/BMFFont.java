package com.physmo.garnet.bitmapfont;

import com.physmo.garnet.Texture;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glVertex2f;

public class BMFFont {

    Map<Integer, BMFChar> chars = new HashMap<>();

    public String init(String path) throws IOException {
        FileReader fileReader = new FileReader(path);
        BufferedReader bufferedReader = new BufferedReader(fileReader);

        String outString = "";
        String line = "";
        while ((line = bufferedReader.readLine()) != null) {
            outString += line + System.lineSeparator();
        }

        bufferedReader.close();
        fileReader.close();

        parse(outString);

        return outString;
    }

    public void parse(String fileData) {
        fileData.lines().forEach(s -> {
            if (s.startsWith("char ")) {
                BMFChar bmfChar = new BMFChar();
                bmfChar.parseLine(s);
                chars.put(bmfChar.id, bmfChar);
            }
        });
    }

    public void drawString(Texture bmfFontTexture, String text, int x, int y, int scale) {
        float[] floats = generateCoordsForStrings(text, x, y);
        float tScale = 1.0f / bmfFontTexture.getWidth();
        //float tScale = bmfFontTexture.getWidth();

        int idx = 0;

        glBegin(GL_QUADS);
        {
            for (int i = 0; i < floats.length / 4; i++) {

                glTexCoord2f(floats[idx++] * tScale, floats[idx++] * tScale);
                glVertex2f(floats[idx++] * scale, floats[idx++] * scale);
            }
        }
        glEnd();
    }

    public float[] generateCoordsForStrings(String text, int x, int y) {
        int xOffset = 0;
        float[] coords = new float[text.length() * 16];
        int cidx = 0;

        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            BMFChar bmfChar = chars.get((int) c);
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
    public float[] generateCoordsForCharacter(BMFChar bmfChar, int x, int y) {

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
        ;
        coords[i++] = x;
        coords[i++] = y + bmfChar.height;


        return coords;
    }

}
