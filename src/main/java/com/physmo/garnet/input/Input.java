package com.physmo.garnet.input;

import com.physmo.garnet.Garnet;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages input subsystems (mouse and keyboard) and a configurable action-mapping system.
 * <p>
 * Logical actions (e.g. {@link InputAction#LEFT}) are mapped to physical key codes via
 * {@link #addKeyboardAction}. Use {@link #isActionKeyPressed} and
 * {@link #isActionKeyFirstPress} to query actions without hard-coding key codes.
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

    /**
     * Called after a scene/state change to ensure input state is up to date.
     */
    public void postStateChangeTask() {
        tick();
    }

    /**
     * Advances the input state by one frame: snapshots previous state and samples current state.
     * Called once per logic tick.
     */
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

    /**
     * Initialises the mouse and keyboard subsystems and registers default action mappings.
     */
    public void init() {

        mouse.init();
        keyboard.init();

        actionConfigList = new ArrayList<>();

        setDefaults();

    }

    /**
     * Registers the default action-to-key mappings (arrow keys, Z for fire, Tab for menu).
     */
    public void setDefaults() {
        addKeyboardAction(InputKeys.KEY_LEFT, InputAction.LEFT);
        addKeyboardAction(InputKeys.KEY_RIGHT, InputAction.RIGHT);
        addKeyboardAction(InputKeys.KEY_UP, InputAction.UP);
        addKeyboardAction(InputKeys.KEY_DOWN, InputAction.DOWN);
        addKeyboardAction(InputKeys.KEY_Z, InputAction.FIRE1);
        addKeyboardAction(InputKeys.KEY_TAB, InputAction.MENU);
    }

    /**
     * Maps a physical key code to a logical action ID.
     *
     * @param keyCode  the GLFW key code (see {@link InputKeys})
     * @param actionId the logical action identifier (see {@link InputAction})
     */
    public void addKeyboardAction(int keyCode, int actionId) {
        actionConfigList.add(new ButtonConfig(keyCode, actionId));
    }


    /**
     * Returns {@code true} if any key mapped to the given action is currently held down.
     *
     * @param actionId the logical action identifier (see {@link InputAction})
     * @return {@code true} if the action is active
     */
    public boolean isActionKeyPressed(int actionId) {
        boolean pressed = false;

        for (ButtonConfig buttonConfig : actionConfigList) {
            if (buttonConfig.actionId == actionId) {
                if (keyboard.getKeyboardState()[buttonConfig.keyCode]) pressed = true;
            }
        }
        return pressed;
    }

    /**
     * Returns {@code true} only on the first frame any key mapped to the given action is pressed.
     *
     * @param actionId the logical action identifier (see {@link InputAction})
     * @return {@code true} if the action was just triggered this frame
     */
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
