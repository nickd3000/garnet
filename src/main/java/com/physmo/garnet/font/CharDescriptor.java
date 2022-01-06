package com.physmo.garnet.font;

public class CharDescriptor {
    char c;
    double width, height;
    double ascent, descent;
    double x,y;

    public CharDescriptor(char c, double width, double height, double ascent, double descent) {
        this.c = c;
        this.width = width;
        this.ascent = ascent;
        this.descent = descent;
        this.height = height;
        x=0;
        y=0;
    }

    public void setTexturePosition(double x, double y) {
        this.x=x;
        this.y=y;
    }
}
