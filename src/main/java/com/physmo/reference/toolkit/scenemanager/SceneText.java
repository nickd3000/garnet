package com.physmo.reference.toolkit.scenemanager;

import com.physmo.garnet.graphics.Graphics;
import com.physmo.garnet.text.RegularFont;

final class SceneText {
    private SceneText() {
    }

    static void draw(RegularFont font, Graphics g, int color, String text, int x, int y, int scale) {
        g.setColor(color);
        g.setZoom(1);
        font.setScale(scale);
        font.drawText(g, text, x, y);
    }
}
