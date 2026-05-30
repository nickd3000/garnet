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
    private final double[] cx = new double[1];
    private final double[] cy = new double[1];

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

        glfwGetCursorPos(windowHandle, cx, cy);

        int[] bufferSize = garnet.getDisplay().getBufferSize();
        int[] viewportOffsets = garnet.getDisplay().glViewportOffsets;
        double[] viewportScale = garnet.getDisplay().glViewportScale;

        // Convert window coordinates to framebuffer coordinates
        double[] windowToBufferScale = garnet.getDisplay().getWindowToBufferScale();

        // 1. Convert cursor from window space to buffer space
        double bx = cx[0] * windowToBufferScale[0];
        double by = cy[0] * windowToBufferScale[1];

        // 2. Adjust for viewport offset. 
        // In OpenGL, viewport Y starts from bottom, but our Ortho makes 0,0 top-left.
        // GLFW cursor 0,0 is top-left.
        // Display.java sets glViewport(xOffset, yOffset, newWidth, newHeight) where yOffset is from bottom.

        double x = (bx - viewportOffsets[0]) * viewportScale[0];

        int[] canvasSize = garnet.getDisplay().getCanvasSize();
        double newHeight = (double) canvasSize[1] / viewportScale[1];
        double viewportTop = (double) bufferSize[1] - ((double) viewportOffsets[1] + newHeight);

        double y = (by - viewportTop) * viewportScale[1];

        // 3. Adjust for active viewport scroll and zoom
        com.physmo.garnet.graphics.Viewport activeViewport = garnet.getGraphics().getViewportManager().getActiveViewport();
        x = (x / activeViewport.getZoom()) + activeViewport.getScrollX();
        y = (y / activeViewport.getZoom()) + activeViewport.getScrollY();

        position[0] = (int) x;
        position[1] = (int) y;

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
