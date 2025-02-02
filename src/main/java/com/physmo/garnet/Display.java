package com.physmo.garnet;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.glfw.GLFWWindowPosCallback;
import org.lwjgl.glfw.GLFWWindowSizeCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;

import static org.lwjgl.glfw.GLFW.GLFW_DONT_CARE;
import static org.lwjgl.glfw.GLFW.GLFW_FALSE;
import static org.lwjgl.glfw.GLFW.GLFW_RESIZABLE;
import static org.lwjgl.glfw.GLFW.GLFW_TRUE;
import static org.lwjgl.glfw.GLFW.GLFW_VISIBLE;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwDefaultWindowHints;
import static org.lwjgl.glfw.GLFW.glfwGetFramebufferSize;
import static org.lwjgl.glfw.GLFW.glfwGetPrimaryMonitor;
import static org.lwjgl.glfw.GLFW.glfwGetVideoMode;
import static org.lwjgl.glfw.GLFW.glfwGetWindowMonitor;
import static org.lwjgl.glfw.GLFW.glfwGetWindowPos;
import static org.lwjgl.glfw.GLFW.glfwGetWindowSize;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwSetWindowMonitor;
import static org.lwjgl.glfw.GLFW.glfwSetWindowPos;
import static org.lwjgl.glfw.GLFW.glfwSetWindowPosCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowSize;
import static org.lwjgl.glfw.GLFW.glfwSetWindowSizeCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowTitle;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glOrtho;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

/**
 *
 */
public class Display {

    public double[] glViewportScale = new double[2];
    public int[] glViewportOffsets = new int[2];
    int primaryMonitorWidth;
    int primaryMonitorHeight;
    double windowScale = 1;
    int storedWindowX = 0;
    int storedWindowY = 0;

    int[] canvasSize = new int[2];
    private long windowHandle;
    int stretchMode = 2;
    private int windowWidth;
    private int windowHeight;

    public Display(int windowWidth, int windowHeight) {
        this.windowWidth = windowWidth;
        this.windowHeight = windowHeight;
        canvasSize[0] = windowWidth;
        canvasSize[1] = windowHeight;
    }

    public int[] getCanvasSize() {
        return canvasSize;
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

    public int[] getBufferSize() {
        int[] w = new int[1], h = new int[1];
        glfwGetFramebufferSize(windowHandle, w, h);

        return new int[]{w[0], h[0]};
    }

    public void setWindowTitle(String title) {
        glfwSetWindowTitle(windowHandle, title);
    }

    public boolean isFullscreen() {
        // Return value will be 0 if in windowed mode.
        return glfwGetWindowMonitor(windowHandle) != 0;
    }

    private void storeWindowPos() {
        int[] x = new int[1], y = new int[1];
        glfwGetWindowPos(windowHandle, x, y);
        storedWindowX = x[0];
        storedWindowY = y[0];
    }

    public void setFullScreen(boolean val) {

        boolean fullScreenActive = isFullscreen();

        if (val && !fullScreenActive) {
            System.out.println("Making full screen");

            storeWindowPos();

            long primaryMonitor = glfwGetPrimaryMonitor();
            GLFWVidMode glfwVidMode = glfwGetVideoMode(primaryMonitor);
            if (glfwVidMode != null) {
                glfwSetWindowMonitor(windowHandle, primaryMonitor, 0, 0, glfwVidMode.width(), glfwVidMode.height(), GLFW_DONT_CARE);
            }
        } else if (!val && fullScreenActive) {
            System.out.println("Making windowed");
            glfwSetWindowMonitor(windowHandle, 0, storedWindowX, storedWindowY, windowWidth, windowHeight, GLFW_DONT_CARE);
        }
    }

    public double[] getWindowToBufferScale() {
        int[] bufferSize = getBufferSize();
        double w = (double) bufferSize[0] / (double) windowWidth;
        double h = (double) bufferSize[1] / (double) windowHeight;
        return new double[]{w, h};

    }

    public void setWindowScale(double windowScale, boolean centerWindow) {
        this.windowScale = windowScale;
        glfwSetWindowSize(windowHandle, (int) (windowWidth * windowScale), (int) (windowHeight * windowScale));
        if (centerWindow) {
            glfwSetWindowPos(windowHandle, primaryMonitorWidth / 2 - (int) (windowWidth * windowScale / 2), primaryMonitorHeight / 2 - (int) (windowHeight * windowScale / 2));
        }
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
            GLFWVidMode videoMode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            // Center the window
            assert videoMode != null;
            primaryMonitorWidth = videoMode.width();
            primaryMonitorHeight = videoMode.height();

            glfwSetWindowPos(
                    windowHandle,
                    (videoMode.width() - pWidth.get(0)) / 2,
                    (videoMode.height() - pHeight.get(0)) / 2
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

        setupWindowResizeHandlers();

        placeGlViewport();
    }

    public void setupWindowResizeHandlers() {
        glfwSetWindowSizeCallback(windowHandle, new GLFWWindowSizeCallback() {
            @Override
            public void invoke(long window, int width, int height) {
                windowWidth = width;
                windowHeight = height;
                placeGlViewport();
            }
        });

        glfwSetWindowPosCallback(windowHandle, new GLFWWindowPosCallback() {
            @Override
            public void invoke(long window, int xPos, int yPos) {
                placeGlViewport();
            }
        });
    }

    public void placeGlViewport() {
        int[] bufferSize = getBufferSize();


        // Stretch to fit.
        if (stretchMode == 1) {
            glViewport(0, 0, bufferSize[0], bufferSize[1]);
            glViewportOffsets[0] = 0;
            glViewportOffsets[1] = 0;
            glViewportScale[0] = (double) canvasSize[0] / bufferSize[0];
            glViewportScale[1] = (double) canvasSize[1] / bufferSize[1];
        }

        // Scale and keep aspect.
        if (stretchMode == 2) {
            double aspect = (double) canvasSize[0] / (double) canvasSize[1];
            double windowAspect = (double) bufferSize[0] / (double) bufferSize[1];

            int newWidth, newHeight;
            double scale;

            if (aspect > windowAspect) {
                scale = (double) canvasSize[0] / bufferSize[0];
            } else {
                scale = (double) canvasSize[1] / bufferSize[1];
            }

            newWidth = (int) (canvasSize[0] / scale);
            newHeight = (int) (canvasSize[1] / scale);
            int xOffset = (bufferSize[0] - newWidth) / 2;
            int yOffset = (bufferSize[1] - newHeight) / 2;

            glViewport(xOffset, yOffset, newWidth, newHeight);
            glViewportOffsets[0] = xOffset;
            glViewportOffsets[1] = yOffset;
            glViewportScale[0] = scale;
            glViewportScale[1] = scale;
        }



    }
}
