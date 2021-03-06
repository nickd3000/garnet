package com.physmo.garnet;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.GLFW_FALSE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;
import static org.lwjgl.glfw.GLFW.GLFW_RESIZABLE;
import static org.lwjgl.glfw.GLFW.GLFW_TRUE;
import static org.lwjgl.glfw.GLFW.GLFW_VISIBLE;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwDefaultWindowHints;
import static org.lwjgl.glfw.GLFW.glfwGetPrimaryMonitor;
import static org.lwjgl.glfw.GLFW.glfwGetVideoMode;
import static org.lwjgl.glfw.GLFW.glfwGetWindowSize;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowPos;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glOrtho;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

// NOTE: on MacOS we need to add a vm argument: -XstartOnFirstThread
public class Garnet {

    List<KeyboardCallback> keyboardCallbacks = new ArrayList<>();
    GameClock gameClock = new GameClock();
    int runningLogicDelta = 0;
    private long windowHandle;
    private int windowWidth, windowHeight;
    private GameContainer gameContainer;

    public Garnet(GameContainer gameContainer, int windowWidth, int windowHeight) {
        this.gameContainer = gameContainer;
        this.windowWidth = windowWidth;
        this.windowHeight = windowHeight;
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

        // Create the window
        windowHandle = glfwCreateWindow(windowWidth, windowHeight, "Garnet Framework", NULL, NULL);
        if (windowHandle == NULL)
            throw new RuntimeException("Failed to create the GLFW window");

        // Setup a key callback. It will be called every time a key is pressed, repeated or released.
        glfwSetKeyCallback(windowHandle, (window, key, scancode, action, mods) -> {
            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
                glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop

            for (KeyboardCallback kbc : keyboardCallbacks) {

                kbc.invoke(key, scancode, action, mods);
            }
        });

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

        gameContainer.init(this);
    }

    public void run() {
        GL.createCapabilities();

        long currentTime = System.nanoTime();
        long oldTime = currentTime;


        // Run the rendering loop until the user has attempted to close
        // the window or has pressed the ESCAPE key.
        while (!glfwWindowShouldClose(windowHandle)) {

            currentTime = System.nanoTime();
            long delta = currentTime - oldTime;

            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            updateLogicAndRender((int) delta);

            oldTime = currentTime;

            //gameContainer.tick(delta);

            //gameContainer.draw();

            //glfwSwapBuffers(windowHandle); // swap the color buffers


            // Poll for window events. The key callback above will only be
            // invoked during this call.
            //glfwPollEvents();
        }
    }

    public void updateLogicAndRender(int delta) {

        long nanoSecondsPerSecond = 1_000_000_000;
        int logicUpdatesPerSecond = 200;
        double secondsPerLogicUpdate = 1.0 / (double) logicUpdatesPerSecond;

        long logicTime = nanoSecondsPerSecond / logicUpdatesPerSecond;

        runningLogicDelta += delta;

        // --------------- LOGIC
        while (runningLogicDelta > logicTime) {
            runningLogicDelta -= logicTime;
            gameContainer.tick(secondsPerLogicUpdate);
            gameClock.logLogicTick();
        }

        // --------------- RENDER
        // Set the clear color
        glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer
        gameContainer.draw();
        gameClock.logFrame();

        glfwSwapBuffers(windowHandle); // swap the color buffers


        // Poll for window events. The key callback above will only be
        // invoked during this call.
        glfwPollEvents();
    }

    public void addKeyboardCallback(KeyboardCallback keyboardCallback) {
        System.out.println("addKeyboardCallback");
        keyboardCallbacks.add(keyboardCallback);
    }

}
