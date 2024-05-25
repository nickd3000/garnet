package com.physmo.garnet.input;

import com.physmo.garnet.Garnet;
import com.physmo.garnet.Utils;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_MIDDLE;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_RIGHT;
import static org.lwjgl.glfw.GLFW.glfwGetCursorPos;
import static org.lwjgl.glfw.GLFW.glfwGetMouseButton;

public class Mouse {

    public static final int BUTTON_LEFT = GLFW_MOUSE_BUTTON_LEFT;
    public static final int BUTTON_RIGHT = GLFW_MOUSE_BUTTON_RIGHT;
    public static final int BUTTON_MIDDLE = GLFW_MOUSE_BUTTON_MIDDLE;
    private final int[] position = new int[2];
    private final int[] positionPrev = new int[2];
    private final boolean[] buttonState = new boolean[3];
    private final boolean[] buttonStatePrev = new boolean[3];
    Garnet garnet;
    long windowHandle;

    public Mouse(Garnet garnet) {
        this.garnet = garnet;
    }

    public void init() {
        windowHandle = garnet.getDisplay().getWindowHandle();
    }

    void update() {
        positionPrev[0] = position[0];
        positionPrev[1] = position[1];

        double[] x = new double[1];
        double[] y = new double[1];

        glfwGetCursorPos(windowHandle, x, y);
        double[] windowToPixelsScale = garnet.getDisplay().getWindowToPixelsScale();

        x[0] /= windowToPixelsScale[0];
        y[0] /= windowToPixelsScale[1];

        position[0] = (int) x[0];
        position[1] = (int) y[0];

        System.arraycopy(buttonState, 0, buttonStatePrev, 0, buttonState.length);
        buttonState[BUTTON_LEFT] = glfwGetMouseButton(windowHandle, BUTTON_LEFT) > 0;
        buttonState[BUTTON_MIDDLE] = glfwGetMouseButton(windowHandle, BUTTON_MIDDLE) > 0;
        buttonState[BUTTON_RIGHT] = glfwGetMouseButton(windowHandle, BUTTON_RIGHT) > 0;
    }


    public int[] getPosition() {
        return position;
    }

    public int[] getPositionScaled(double scale) {
        return new int[]{(int) (position[0] / scale), (int) (position[1] / scale)};
    }

    /**
     * Returns the mouse position normalised to 0..1 double values.
     *
     * @return
     */
    public double[] getPositionNormalised() {
        int windowWidth = garnet.getDisplay().getWindowWidth();
        int windowHeight = garnet.getDisplay().getWindowHeight();
        double x = (double) position[0] / (double) windowWidth;
        double y = (double) position[1] / (double) windowHeight;

        return new double[]{Utils.clampUnit(x), Utils.clampUnit(y)};
    }


    public boolean isButtonPressed(int mouseButtonId) {
        return buttonState[mouseButtonId];
    }

    /**
     * True if mouse button first pressed this frame.
     *
     * @param mouseButtonId
     * @return
     */
    public boolean isButtonFirstPress(int mouseButtonId) {
        return (buttonState[mouseButtonId] && !buttonStatePrev[mouseButtonId]);
    }


}
