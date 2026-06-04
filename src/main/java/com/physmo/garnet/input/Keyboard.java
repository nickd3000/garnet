package com.physmo.garnet.input;

import com.physmo.garnet.Garnet;

/**
 * Tracks keyboard key state each frame via a GLFW key callback.
 * <p>
 * Key states are stored as boolean arrays indexed by GLFW key code.
 * Use {@link #isKeyPressed} to test whether a key is currently held, and
 * {@link #isKeyFirstPress} to detect the first frame a key is pressed.
 */
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

    /**
     * Copies the current key state to the previous-frame snapshot.
     * Called once per logic tick by {@link Input} before new events are processed.
     */
    public void update() {
        System.arraycopy(keyboardState, 0, keyboardStatePrev, 0, keyboardState.length);
    }

    public boolean[] getKeyboardState() {
        return keyboardState;
    }

    public boolean[] getKeyboardStatePrev() {
        return keyboardStatePrev;
    }

    /**
     * Sets whether to print key codes when a key event is detected.
     *
     * @param printKeyCodes if true, key codes will be printed to the console;
     *                      if false, key codes will not be printed
     */
    public void setPrintKeyCodes(boolean printKeyCodes) {
        this.printKeyCodes = printKeyCodes;
    }

    /**
     * Checks if a specific key is currently pressed.
     *
     * @param key the key code of the key to check
     * @return true if the key is pressed, false otherwise
     */
    public boolean isKeyPressed(int key) {
        return keyboardState[key];
    }

    /**
     * Checks if a key is being pressed for the first time in the current cycle.
     *
     * @param key the key code of the key being checked
     * @return true if the key is pressed in the current cycle and was not pressed in the previous cycle, otherwise false
     */
    public boolean isKeyFirstPress(int key) {
        return keyboardState[key] && !keyboardStatePrev[key];
    }
}
