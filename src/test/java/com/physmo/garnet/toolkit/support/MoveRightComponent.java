package com.physmo.garnet.toolkit.support;

import com.physmo.garnet.toolkit.Component;

public class MoveRightComponent extends Component {
    @Override
    public void tick(double t) {
        System.out.println("MoveRight ticked");
    }

    @Override
    public void init() {
        System.out.println("MoveRight init");
    }

    @Override
    public void draw() {

    }
}
