package com.physmo.garnet.input;

import com.physmo.garnet.Garnet;

import java.util.ArrayList;
import java.util.List;

public class Input {

    static Garnet garnet;
    static List<ButtonConfig> buttonConfigList;

    boolean[] buttonState;
    boolean[] buttonStatePrev;

    public Input(Garnet garnet) {
        this.garnet = garnet;
    }

    public void postStateChangeTask() {
        tick();
    }

    public void tick() {
        for (int i = 0; i < buttonState.length; i++) {
            buttonStatePrev[i] = buttonState[i];
        }
    }

    public void init() {

        buttonConfigList = new ArrayList<>();
        buttonConfigList.add(new ButtonConfig(123, VirtualButton.LEFT));
        buttonConfigList.add(new ButtonConfig(124, VirtualButton.RIGHT));
        buttonConfigList.add(new ButtonConfig(6, VirtualButton.FIRE1));
        buttonConfigList.add(new ButtonConfig(48, VirtualButton.MENU)); // tab

        buttonState = new boolean[0xff];
        buttonStatePrev = new boolean[0xff];

        garnet.addKeyboardCallback((key, scancode, action, mods) -> {
            //System.out.println("keyboard handler - key:" + key + " scancode:" + scancode + "  action:" + action);

            // a=0, d=2, space =49 / action 1/0 down/up

            if (action == 1) {
                buttonState[scancode] = true;
            } else if (action == 0) {
                buttonState[scancode] = false;
            }

        });

    }

    public boolean isPressed(VirtualButton button) {
        for (ButtonConfig buttonConfig : buttonConfigList) {
            if (buttonConfig.virtualButton == button) {
                return buttonState[buttonConfig.scancode];
            }
        }
        return false;
    }

    public boolean isFirstPress(VirtualButton button) {
        for (ButtonConfig buttonConfig : buttonConfigList) {
            if (buttonConfig.virtualButton == button) {
                if (buttonState[buttonConfig.scancode] == true &&
                        buttonStatePrev[buttonConfig.scancode] == false) return true;
            }
        }
        return false;
    }

    public boolean isPressedThisFrame(VirtualButton button) {

        return false;
    }

    public enum VirtualButton {
        LEFT, RIGHT, FIRE1, FIRE2, MENU
    }
}
