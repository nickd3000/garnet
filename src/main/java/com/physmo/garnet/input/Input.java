package com.physmo.garnet.input;

import com.physmo.garnet.Garnet;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.glfwGetCursorPos;

public class Input {

    Garnet garnet;
    List<ButtonConfig> buttonConfigList;
    long windowHandle;
    private boolean[] buttonState;
    private boolean[] buttonStatePrev;
    private final int[] mousePosition = new int[2];
    private final int[] mousePositionPrev = new int[2];

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
        int[] mp = new int[]{(int) (mousePosition[0] / scale), (int) (mousePosition[1] / scale)};
        return mp;
    }

    public void init() {

        buttonConfigList = new ArrayList<>();
        buttonConfigList.add(new ButtonConfig(123, VirtualButton.LEFT));
        buttonConfigList.add(new ButtonConfig(124, VirtualButton.RIGHT));
        buttonConfigList.add(new ButtonConfig(126, VirtualButton.UP));
        buttonConfigList.add(new ButtonConfig(125, VirtualButton.DOWN));
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
                if (buttonState[buttonConfig.scancode] &&
                        !buttonStatePrev[buttonConfig.scancode]) return true;
            }
        }
        return false;
    }

    public boolean isPressedThisFrame(VirtualButton button) {

        return false;
    }

    public void setWindowHandle(long windowHandle) {
        this.windowHandle = windowHandle;
    }

    public enum VirtualButton {
        LEFT, RIGHT, UP, DOWN, FIRE1, FIRE2, MENU
    }
}
