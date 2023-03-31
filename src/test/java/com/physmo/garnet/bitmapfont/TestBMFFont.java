package com.physmo.garnet.bitmapfont;

import com.physmo.garnet.Utils;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;

@Ignore
public class TestBMFFont {

    // TODO: BMFont needs open gl to be active - any way around this?
    @Ignore
    @Test
    public void test1() throws IOException {
        String pathForDefinition = Utils.getPathForResource(this, "5x5.fnt");
        String pathForTexture = Utils.getPathForResource(this, "5x5_0.png");

        BitmapFont bmfFont = new BitmapFont(pathForTexture, pathForDefinition);

        System.out.println(2);
    }
}
