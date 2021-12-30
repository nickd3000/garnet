package com.physmo.garnet.input;

public class ButtonConfig {
    public int keyboardKey;
    public Input.VirtualButton virtualButton;
    public boolean pressed;

    public ButtonConfig(int keyboardKey, Input.VirtualButton virtualButton) {

        this.keyboardKey = keyboardKey;
        this.virtualButton = virtualButton;
    }
}
