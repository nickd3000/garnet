package com.physmo.reference.context.support;

import com.physmo.garnet.graphics.Graphics;
import com.physmo.garnet.toolkit.Component;

public class ContextExampleComponent extends Component {
    @Override
    public void tick(double t) {
        System.out.println("Ticked:" + parent.getName());
    }

    @Override
    public void init() {
        System.out.println("Init:" + parent.getName());
    }

    @Override
    public void draw(Graphics g) {

    }
}
