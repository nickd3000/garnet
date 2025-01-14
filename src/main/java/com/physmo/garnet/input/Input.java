package com.physmo.garnet.input;

import com.physmo.garnet.Garnet;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages input subsystems and action system
 */
public class Input {

    Garnet garnet;
    Mouse mouse;
    Keyboard keyboard;
    List<ButtonConfig> actionConfigList;

    public Input(Garnet garnet) {
        this.garnet = garnet;
        mouse = new Mouse(garnet);
        keyboard = new Keyboard(garnet);
    }

    public void postStateChangeTask() {
        tick();
    }

    public void tick() {
        mouse.update();
        keyboard.update();
    }

    /**
     * Retrieves the Mouse instance used for handling mouse input.
     *
     * @return the Mouse instance associated with this input manager
     */
    public Mouse getMouse() {
        return mouse;
    }

    /**
     * Retrieves the Keyboard instance used for handling keyboard input.
     *
     * @return the Keyboard instance associated with this input manager
     */
    public Keyboard getKeyboard() {
        return keyboard;
    }

    public void init() {

        mouse.init();
        keyboard.init();

        actionConfigList = new ArrayList<>();

        setDefaults();

    }

    public void setDefaults() {
        addKeyboardAction(InputKeys.KEY_LEFT, InputAction.LEFT);
        addKeyboardAction(InputKeys.KEY_RIGHT, InputAction.RIGHT);
        addKeyboardAction(InputKeys.KEY_UP, InputAction.UP);
        addKeyboardAction(InputKeys.KEY_DOWN, InputAction.DOWN);
        addKeyboardAction(InputKeys.KEY_Z, InputAction.FIRE1);
        addKeyboardAction(InputKeys.KEY_TAB, InputAction.MENU);
    }

    public void addKeyboardAction(int keyCode, int actionId) {
        actionConfigList.add(new ButtonConfig(keyCode, actionId));
    }


    public boolean isActionKeyPressed(int actionId) {
        boolean pressed = false;

        for (ButtonConfig buttonConfig : actionConfigList) {
            if (buttonConfig.actionId == actionId) {
                if (keyboard.getKeyboardState()[buttonConfig.keyCode]) pressed = true;
            }
        }
        return pressed;
    }

    public boolean isActionKeyFirstPress(int actionId) {
        boolean firstPress = false;
        for (ButtonConfig buttonConfig : actionConfigList) {
            if (buttonConfig.actionId == actionId) {
                if (keyboard.getKeyboardState()[buttonConfig.keyCode] &&
                        !keyboard.getKeyboardStatePrev()[buttonConfig.keyCode]) firstPress = true;
            }
        }
        return firstPress;
    }
}
