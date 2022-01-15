package com.physmo.garnet.input;

public class ButtonConfig {
    public int scancode;
    public Input.VirtualButton virtualButton;
    public boolean pressed;

    public ButtonConfig(int scancode, Input.VirtualButton virtualButton) {

        this.scancode = scancode;
        this.virtualButton = virtualButton;
    }
}
