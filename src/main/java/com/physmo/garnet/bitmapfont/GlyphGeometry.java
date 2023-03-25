package com.physmo.garnet.bitmapfont;

// char id=32   x=66    y=43    width=6     height=1     xoffset=-2    yoffset=9     xadvance=2     page=0  chnl=15

public class GlyphGeometry {

    String linePrefix = "char ";

    int id;
    int x;
    int y;
    int width;
    int height;
    int xoffset;
    int yoffset;
    int xadvance;
    int page;
    int channel;

    public void parseLine(String line) {
        String[] parts = line.split("\\s+");
        for (String part : parts) {

            parseToken(part);
        }
    }

    public void parseToken(String token) {
        String[] parts = token.split("=");

        if (parts.length < 2) return;
        switch (parts[0].toLowerCase()) {
            case "id":
                id = Integer.parseInt(parts[1]);
                break;
            case "x":
                x = Integer.parseInt(parts[1]);
                break;
            case "y":
                y = Integer.parseInt(parts[1]);
                break;
            case "width":
                width = Integer.parseInt(parts[1]);
                break;
            case "height":
                height = Integer.parseInt(parts[1]);
                break;

            case "xoffset":
                xoffset = Integer.parseInt(parts[1]);
                break;
            case "yoffset":
                yoffset = Integer.parseInt(parts[1]);
                break;
            case "xadvance":
                xadvance = Integer.parseInt(parts[1]);
                break;
            case "page":
                page = Integer.parseInt(parts[1]);
                break;
            case "chnl":
                channel = Integer.parseInt(parts[1]);
                break;
        }
    }


}
