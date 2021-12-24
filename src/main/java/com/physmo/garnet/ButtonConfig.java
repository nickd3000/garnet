package com.physmo.garnet;

class ButtonConfig {
    int keyboardKey;
    Input.VirtualButton virtualButton;
    boolean pressed;

    public ButtonConfig(int keyboardKey, Input.VirtualButton virtualButton) {

        this.keyboardKey = keyboardKey;
        this.virtualButton = virtualButton;
    }
}
