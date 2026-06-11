package com.physmo.reference.toolkit.messaging;

import com.physmo.garnet.ColorUtils;
import com.physmo.garnet.Garnet;
import com.physmo.garnet.GarnetApp;
import com.physmo.garnet.graphics.Graphics;
import com.physmo.garnet.input.InputKeys;
import com.physmo.garnet.text.RegularFont;
import com.physmo.garnet.toolkit.Context;
import com.physmo.garnet.toolkit.GameObject;

// NOTE: On MacOS the following VM argument is required: -XstartOnFirstThread
public class MessageSystemExample extends GarnetApp {
    static final String DAMAGE = "damage";
    static final String COIN_COLLECTED = "coin-collected";

    private final Context context = new Context();
    private RegularFont font;
    private GameObject player;
    private CoinEmitterComponent coinEmitter;

    public static void main(String[] args) {
        Garnet.launch(700, 480, MessageSystemExample::new);
    }

    @Override
    public void init() {
        garnet.getDisplay().setWindowTitle("Toolkit Message System Example");
        garnet.getGraphics().setBackgroundColor(ColorUtils.WINTER_BLACK);

        font = new RegularFont("regularfonts/12x12Font.png", 12, 12);
        font.setHorizontalPad(-5);

        player = GameObject.named("Player")
                .with(new PlayerStatusComponent(font, 25, 190))
                .inContext(context);

        coinEmitter = new CoinEmitterComponent();
        GameObject.named("HUD")
                .with(coinEmitter)
                .with(new HudComponent(font, 245, 190))
                .inContext(context);

        GameObject.named("Message log")
                .with(new MessageLogComponent(font, 465, 190))
                .inContext(context);

        context.init();
    }

    @Override
    public void tick(double delta) {
        if (isFirstPress(InputKeys.KEY_D)) {
            player.sendMessage(DAMAGE, 10);
        }
        if (isFirstPress(InputKeys.KEY_B)) {
            coinEmitter.collectCoin();
        }

        context.tick(delta);
    }

    private boolean isFirstPress(int keyCode) {
        boolean[] current = garnet.getInput().getKeyboard().getKeyboardState();
        boolean[] previous = garnet.getInput().getKeyboard().getKeyboardStatePrev();
        return current[keyCode] && !previous[keyCode];
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(ColorUtils.WINTER_BLACK);
        g.filledRect(0, 0, 700, 480);

        MessageText.draw(font, g, ColorUtils.WHITE, "Toolkit Message System", 25, 25, 2);
        MessageText.draw(font, g, ColorUtils.WINTER_YELLOW,
                "D: player.sendMessage(\"damage\", 10)", 25, 75, 1);
        MessageText.draw(font, g, ColorUtils.WINTER_GREEN,
                "B: component.broadcastMessage(\"coin-collected\", 1)", 25, 100, 1);
        MessageText.draw(font, g, ColorUtils.WINTER_WHITE,
                "Direct messages reach one GameObject and its components.", 25, 135, 1);
        MessageText.draw(font, g, ColorUtils.WINTER_WHITE,
                "Broadcast messages reach every GameObject in the Context.", 25, 155, 1);

        context.draw(g);
    }
}
