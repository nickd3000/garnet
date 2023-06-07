package com.physmo.garnet;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

/**
 *
 */
public class Display {

    private long windowHandle;
    private final int windowWidth;
    private final int windowHeight;

    public Display(int windowWidth, int windowHeight) {
        this.windowWidth = windowWidth;
        this.windowHeight = windowHeight;
    }

    public int getWindowWidth() {
        return windowWidth;
    }

    public int getWindowHeight() {
        return windowHeight;
    }


    public long getWindowHandle() {
        return windowHandle;
    }

    public void init() {

        // Setup an error callback. The default implementation
        // will print the error message in System.err.
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW. Most GLFW functions will not work before doing this.
        if (!glfwInit())
            throw new IllegalStateException("Unable to initialize GLFW");


        // Configure GLFW
        glfwDefaultWindowHints(); // optional, the current window hints are already the default
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable
        //glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE);

        // Create the window
        windowHandle = glfwCreateWindow(windowWidth, windowHeight, "Garnet Framework", NULL, NULL);
        if (windowHandle == NULL)
            throw new RuntimeException("Failed to create the GLFW window");

        // Get the thread stack and push a new frame
        try (MemoryStack stack = stackPush()) {
            IntBuffer pWidth = stack.mallocInt(1); // int*
            IntBuffer pHeight = stack.mallocInt(1); // int*

            // Get the window size passed to glfwCreateWindow
            glfwGetWindowSize(windowHandle, pWidth, pHeight);

            // Get the resolution of the primary monitor
            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            // Center the window
            assert vidmode != null;
            glfwSetWindowPos(
                    windowHandle,
                    (vidmode.width() - pWidth.get(0)) / 2,
                    (vidmode.height() - pHeight.get(0)) / 2
            );
        } // the stack frame is popped automatically

        // Make the OpenGL context current
        glfwMakeContextCurrent(windowHandle);

        // Enable v-sync
        glfwSwapInterval(1);

        // Make the window visible
        glfwShowWindow(windowHandle);

        GL.createCapabilities();

        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(0.0f, windowWidth, windowHeight, 0.0f, 0.0f, 1.0f);

    }

    public double[] getWindowToPixelsScale() {
        int[] bufferSize = getWindowSize();
        double w = (double) bufferSize[0] / (double) windowWidth;
        double h = (double) bufferSize[1] / (double) windowHeight;
        return new double[]{w, h};
    }

    public int[] getWindowSize() {
        int[] w2 = new int[1], h2 = new int[1];
        glfwGetWindowSize(windowHandle, w2, h2);

        return new int[]{w2[0], h2[0]};
    }

    public double[] getWindowToBufferScale() {
        int[] bufferSize = getBufferSize();
        double w = (double) bufferSize[0] / (double) windowWidth;
        double h = (double) bufferSize[1] / (double) windowHeight;
        return new double[]{w, h};
    }

    public int[] getBufferSize() {
        int[] w = new int[1], h = new int[1];
        glfwGetFramebufferSize(windowHandle, w, h);

        return new int[]{w[0], h[0]};
    }

    public void setWindowTitle(String title) {
        glfwSetWindowTitle(windowHandle, title);
    }
}
