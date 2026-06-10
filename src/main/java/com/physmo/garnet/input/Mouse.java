package com.physmo.garnet.input;

import com.physmo.garnet.Garnet;
import com.physmo.garnet.Utils;
import com.physmo.garnet.graphics.Viewport;
import com.physmo.garnet.renderer.ViewportTransform;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_MIDDLE;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_RIGHT;
import static org.lwjgl.glfw.GLFW.glfwGetCursorPos;
import static org.lwjgl.glfw.GLFW.glfwGetMouseButton;

/**
 * Tracks mouse cursor position and button state each frame.
 * <p>
 * Cursor coordinates are transformed from GLFW window space through the
 * framebuffer and viewport scales so that {@link #getPosition()} returns
 * values in canvas/world space matching the active viewport.
 */
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


    /**
     * Samples the current cursor position and button states from GLFW.
     * Called once per logic tick by {@link com.physmo.garnet.input.Input}.
     */
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

        // 3. Convert canvas-space screen coordinates into active viewport world coordinates.
        Viewport activeViewport = garnet.getGraphics().getViewportManager().getActiveViewport();
        x = ViewportTransform.worldX(activeViewport, x);
        y = ViewportTransform.worldY(activeViewport, y);

        position[0] = (int) x;
        position[1] = (int) y;

        System.arraycopy(buttonState, 0, buttonStatePrev, 0, buttonState.length);
        buttonState[BUTTON_LEFT] = glfwGetMouseButton(windowHandle, BUTTON_LEFT) > 0;
        buttonState[BUTTON_MIDDLE] = glfwGetMouseButton(windowHandle, BUTTON_MIDDLE) > 0;
        buttonState[BUTTON_RIGHT] = glfwGetMouseButton(windowHandle, BUTTON_RIGHT) > 0;
    }


    /**
     * Returns the current mouse position in canvas/world coordinates as {@code [x, y]}.
     *
     * @return the mouse position array
     */
    public int[] getPosition() {
        return position;
    }

    /**
     * Returns the mouse position divided by the given scale factor.
     * Useful when the game world uses a different coordinate scale than the canvas.
     *
     * @param scale the divisor applied to both x and y
     * @return the scaled position as {@code [x, y]}
     */
    public int[] getPositionScaled(double scale) {
        return new int[]{(int) (position[0] / scale), (int) (position[1] / scale)};
    }

    /**
     * Returns the mouse position normalised to the range [0, 1] for both axes.
     * (0, 0) is the top-left corner of the canvas; (1, 1) is the bottom-right.
     *
     * @return a two-element array {@code [normalisedX, normalisedY]}
     */
    public double[] getPositionNormalised() {
        int windowWidth = garnet.getDisplay().getWindowWidth();
        int windowHeight = garnet.getDisplay().getWindowHeight();
        double x = (double) position[0] / (double) windowWidth;
        double y = (double) position[1] / (double) windowHeight;

        return new double[]{Utils.clampUnit(x), Utils.clampUnit(y)};
    }


    /**
     * Returns whether the specified mouse button is currently held down.
     *
     * @param mouseButtonId one of {@link #BUTTON_LEFT}, {@link #BUTTON_RIGHT}, or {@link #BUTTON_MIDDLE}
     * @return {@code true} if the button is pressed
     */
    public boolean isButtonPressed(int mouseButtonId) {
        return buttonState[mouseButtonId];
    }

    /**
     * Returns {@code true} only on the first frame the specified button is pressed.
     * Subsequent frames while the button is held return {@code false}.
     *
     * @param mouseButtonId one of {@link #BUTTON_LEFT}, {@link #BUTTON_RIGHT}, or {@link #BUTTON_MIDDLE}
     * @return {@code true} if the button was just pressed this frame
     */
    public boolean isButtonFirstPress(int mouseButtonId) {
        return (buttonState[mouseButtonId] && !buttonStatePrev[mouseButtonId]);
    }


}
