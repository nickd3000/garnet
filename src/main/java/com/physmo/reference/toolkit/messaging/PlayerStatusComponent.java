package com.physmo.reference.toolkit.messaging;

import com.physmo.garnet.ColorUtils;
import com.physmo.garnet.graphics.Graphics;
import com.physmo.garnet.text.RegularFont;
import com.physmo.garnet.toolkit.Component;

final class PlayerStatusComponent extends Component {
    private final RegularFont font;
    private final int x;
    private final int y;
    private int health = 100;
    private int coins;
    private String lastMessage = "none";

    PlayerStatusComponent(RegularFont font, int x, int y) {
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
        MessageText.panel(g, x, y, 200, 210, ColorUtils.WINTER_BLUE);
        MessageText.draw(font, g, ColorUtils.WHITE, "PLAYER COMPONENT", x + 15, y + 15, 1);
        MessageText.draw(font, g, ColorUtils.WINTER_YELLOW, "Health: " + health, x + 15, y + 55, 1);
        MessageText.draw(font, g, ColorUtils.WINTER_GREEN, "Coins: " + coins, x + 15, y + 80, 1);
        MessageText.draw(font, g, ColorUtils.WINTER_WHITE, "Last: " + lastMessage, x + 15, y + 115, 1);
        MessageText.draw(font, g, ColorUtils.WINTER_WHITE, "Receives direct", x + 15, y + 155, 1);
        MessageText.draw(font, g, ColorUtils.WINTER_WHITE, "and broadcast", x + 15, y + 175, 1);
    }

    @Override
    public void onMessage(String name, Object data) {
        lastMessage = name;
        if (MessageSystemExample.DAMAGE.equals(name)) {
            health = Math.max(0, health - (Integer) data);
        }
        if (MessageSystemExample.COIN_COLLECTED.equals(name)) {
            coins += (Integer) data;
        }
    }
}
