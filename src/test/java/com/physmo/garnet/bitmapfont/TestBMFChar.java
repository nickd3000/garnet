package com.physmo.garnet.bitmapfont;

import org.junit.Test;

public class TestBMFChar {

    @Test
    public void t1() {
        BMFChar bmfChar = new BMFChar();
        bmfChar.parseLine("char id=32   x=66    y=43    width=6     height=1     xoffset=-2    yoffset=9     xadvance=2     page=0  chnl=15");
        System.out.println(1);
    }

}
