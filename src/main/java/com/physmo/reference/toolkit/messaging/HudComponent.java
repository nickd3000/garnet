package com.physmo.reference.toolkit.messaging;

import com.physmo.garnet.ColorUtils;
import com.physmo.garnet.graphics.Graphics;
import com.physmo.garnet.text.RegularFont;
import com.physmo.garnet.toolkit.Component;

final class HudComponent extends Component {
    private final RegularFont font;
    private final int x;
    private final int y;
    private int coins;
    private String lastMessage = "none";

    HudComponent(RegularFont font, int x, int y) {
        this.font = font;
        this.x = x;
        this.y = y;
    }

    @Override
    public void init() {
    }

    @Override
    public void tick(double t) {
    }

    @Override
    public void draw(Graphics g) {
        MessageText.panel(g, x, y, 200, 210, ColorUtils.WINTER_GREEN);
        MessageText.draw(font, g, ColorUtils.WHITE, "HUD COMPONENT", x + 15, y + 15, 1);
        MessageText.draw(font, g, ColorUtils.WINTER_GREEN, "Coins: " + coins, x + 15, y + 55, 1);
        MessageText.draw(font, g, ColorUtils.WINTER_WHITE, "Last: " + lastMessage, x + 15, y + 90, 1);
        MessageText.draw(font, g, ColorUtils.WINTER_WHITE, "Ignores player-only", x + 15, y + 135, 1);
        MessageText.draw(font, g, ColorUtils.WINTER_WHITE, "damage messages", x + 15, y + 155, 1);
    }

    @Override
    public void onMessage(String name, Object data) {
        if (MessageSystemExample.COIN_COLLECTED.equals(name)) {
            coins += (Integer) data;
            lastMessage = name;
        }
    }
}
