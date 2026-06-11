package com.physmo.reference.toolkit.messaging;

import com.physmo.garnet.ColorUtils;
import com.physmo.garnet.graphics.Graphics;
import com.physmo.garnet.text.RegularFont;
import com.physmo.garnet.toolkit.Component;

final class MessageLogComponent extends Component {
    private final RegularFont font;
    private final int x;
    private final int y;
    private int messageCount;
    private String lastMessage = "none";
    private String lastData = "none";

    MessageLogComponent(RegularFont font, int x, int y) {
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
        MessageText.panel(g, x, y, 210, 210, ColorUtils.WINTER_INDIGO);
        MessageText.draw(font, g, ColorUtils.WHITE, "LOG COMPONENT", x + 15, y + 15, 1);
        MessageText.draw(font, g, ColorUtils.WINTER_WHITE, "Messages: " + messageCount, x + 15, y + 55, 1);
        MessageText.draw(font, g, ColorUtils.WINTER_WHITE, "Last: " + lastMessage, x + 15, y + 85, 1);
        MessageText.draw(font, g, ColorUtils.WINTER_WHITE, "Data: " + lastData, x + 15, y + 110, 1);
        MessageText.draw(font, g, ColorUtils.WINTER_WHITE, "Only broadcasts", x + 15, y + 155, 1);
        MessageText.draw(font, g, ColorUtils.WINTER_WHITE, "reach this object", x + 15, y + 175, 1);
    }

    @Override
    public void onMessage(String name, Object data) {
        messageCount++;
        lastMessage = name;
        lastData = String.valueOf(data);
    }
}
