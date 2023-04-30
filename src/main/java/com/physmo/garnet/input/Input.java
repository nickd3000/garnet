package com.physmo.garnet.input;

import com.physmo.garnet.Garnet;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.glfwGetCursorPos;

public class Input {

    private final int[] mousePosition = new int[2];
    private final int[] mousePositionPrev = new int[2];
    public boolean printKeyCodes = false;
    Garnet garnet;
    List<ButtonConfig> actionConfigList;
    long windowHandle;
    int maxKeys = 512;
    private final boolean[] buttonState = new boolean[maxKeys];
    private final boolean[] buttonStatePrev = new boolean[maxKeys];

    public Input(Garnet garnet) {
        this.garnet = garnet;
    }

    public void postStateChangeTask() {
        tick();
    }

    public void tick() {
        System.arraycopy(buttonState, 0, buttonStatePrev, 0, buttonState.length);
        updateMouse();
    }

    private void updateMouse() {
        mousePositionPrev[0] = mousePosition[0];
        mousePositionPrev[1] = mousePosition[1];

        double[] x = new double[1];
        double[] y = new double[1];

        glfwGetCursorPos(windowHandle, x, y);
        double[] windowToPixelsScale = garnet.getDisplay().getWindowToPixelsScale();

        x[0] /= windowToPixelsScale[0];
        y[0] /= windowToPixelsScale[1];

        mousePosition[0] = (int) x[0];
        mousePosition[1] = (int) y[0];
    }

    public int[] getMousePosition() {
        return mousePosition;
    }

    public int[] getMousePositionScaled(double scale) {
        return new int[]{(int) (mousePosition[0] / scale), (int) (mousePosition[1] / scale)};
    }

    public void init() {

        actionConfigList = new ArrayList<>();

        setDefaults();

        garnet.addKeyboardCallback((key, scancode, action, mods) -> {

            if (printKeyCodes) {
                System.out.println("keyboard handler - key:" + key + " scancode:" + scancode + "  action:" + action);
            }

            if (action == 1) {
                buttonState[key] = true;
            } else if (action == 0) {
                buttonState[key] = false;
            }
        });

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


    public boolean isPressed(int actionId) {
        boolean pressed = false;

        for (ButtonConfig buttonConfig : actionConfigList) {
            if (buttonConfig.actionId == actionId) {
                if (buttonState[buttonConfig.keyCode]) pressed = true;
            }
        }
        return pressed;
    }

    public boolean isFirstPress(int actionId) {
        boolean firstPress = false;
        for (ButtonConfig buttonConfig : actionConfigList) {
            if (buttonConfig.actionId == actionId) {
                if (buttonState[buttonConfig.keyCode] &&
                        !buttonStatePrev[buttonConfig.keyCode]) firstPress = true;
            }
        }
        return firstPress;
    }

    public boolean isPressedThisFrame(InputAction button) {

        return false;
    }

    public void setWindowHandle(long windowHandle) {
        this.windowHandle = windowHandle;
    }

}
