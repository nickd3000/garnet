package com.physmo.garnet.text;

import com.physmo.garnet.FileUtils;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;

@Disabled("BitmapFont loads an OpenGL texture and needs an active OpenGL context")
class BitmapFontTest {

    @Test
    void canLoadBitmapFontWhenOpenGlContextExists() throws IOException {
        String pathForDefinition = FileUtils.getPathForResource(this, "5x5.fnt");
        String pathForTexture = FileUtils.getPathForResource(this, "5x5_0.png");

        new BitmapFont(pathForTexture, pathForDefinition);
    }
}
