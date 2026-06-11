package com.physmo.reference.toolkit.messaging;

import com.physmo.garnet.graphics.Graphics;
import com.physmo.garnet.text.RegularFont;

final class MessageText {
    private MessageText() {
    }

    static void draw(RegularFont font, Graphics g, int color, String text, int x, int y, int scale) {
        g.setColor(color);
        g.setZoom(1);
        font.setScale(scale);
        font.drawText(g, text, x, y);
    }

    static void panel(Graphics g, int x, int y, int width, int height, int color) {
        g.setColor(0x171735ff);
        g.filledRect(x, y, width, height);
        g.setColor(color);
        g.drawRect(x, y, width, height, 3);
    }
}
