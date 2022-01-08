package com.physmo.garnet.bitmapfont;

import com.physmo.garnet.Utils;
import org.junit.Test;

import java.io.IOException;

public class TestBMFFont {
    @Test
    public void test1() {
        String pathForResource = Utils.getPathForResource(this, "/5x5.fnt");

        BMFFont bmfFont = new BMFFont();
        String data = "";
        try {
            data = bmfFont.init(pathForResource);
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(2);
    }
}
