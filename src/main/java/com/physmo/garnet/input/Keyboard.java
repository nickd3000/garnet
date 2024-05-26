package com.physmo.garnet.input;

import com.physmo.garnet.Garnet;

public class Keyboard {

    public boolean printKeyCodes = false;
    Garnet garnet;
    int maxKeys = 512;
    private final boolean[] keyboardState = new boolean[maxKeys];
    private final boolean[] keyboardStatePrev = new boolean[maxKeys];

    public Keyboard(Garnet garnet) {
        this.garnet = garnet;
    }

    public void init() {
        garnet.addKeyboardCallback((key, scancode, action, mods) -> {

            if (printKeyCodes) {
                System.out.println("keyboard handler - key:" + key + " scancode:" + scancode + "  action:" + action);
            }

            if (action == 1) {
                keyboardState[key] = true;
            } else if (action == 0) {
                keyboardState[key] = false;
            }
        });
    }

    public void update() {
        System.arraycopy(keyboardState, 0, keyboardStatePrev, 0, keyboardState.length);
    }

    public boolean[] getKeyboardState() {
        return keyboardState;
    }

    public boolean[] getKeyboardStatePrev() {
        return keyboardStatePrev;
    }

    public void setPrintKeyCodes(boolean printKeyCodes) {
        this.printKeyCodes = printKeyCodes;
    }
}
