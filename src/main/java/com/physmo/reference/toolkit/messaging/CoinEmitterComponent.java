package com.physmo.reference.toolkit.messaging;

import com.physmo.garnet.graphics.Graphics;
import com.physmo.garnet.toolkit.Component;

final class CoinEmitterComponent extends Component {
    void collectCoin() {
        broadcastMessage(MessageSystemExample.COIN_COLLECTED, 1);
    }

    @Override
    public void init() {
    }

    @Override
    public void tick(double t) {
    }

    @Override
    public void draw(Graphics g) {
    }
}
