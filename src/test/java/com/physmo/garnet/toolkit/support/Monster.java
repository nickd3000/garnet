package com.physmo.garnet.toolkit.support;

import com.physmo.garnet.toolkit.GameObject;

public class Monster extends GameObject {
    public Monster(String name) {
        super(name);
    }

    @Override
    public void init() {
        System.out.println("Monster init");
    }

    @Override
    public void tick(double t) {
        System.out.println("Monster ticked");
    }
}
