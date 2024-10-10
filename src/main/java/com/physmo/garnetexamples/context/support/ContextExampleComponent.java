package com.physmo.garnetexamples.context.support;

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
    public void draw() {

    }
}
