package com.physmo.garnet;

import com.physmo.garnet.input.Input;
import com.physmo.garnet.input.KeyboardCallback;
import org.lwjgl.opengl.GL;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.opengl.GL11.*;

// NOTE: on MacOS we need to add a vm argument: -XstartOnFirstThread
public class Garnet {

    List<KeyboardCallback> keyboardCallbacks = new ArrayList<>();
    GameClock gameClock = new GameClock();
    int runningLogicDelta = 0;
    //StateManager stateManager;
    //Map<String, Object> sharedStore;
    Input input;
    Display display;
    GarnetApp garnetApp;
    private int windowWidth, windowHeight;


    public Garnet(int windowWidth, int windowHeight) {

        this.windowWidth = windowWidth;
        this.windowHeight = windowHeight;

        input = new Input(this);
        display = new Display();
    }

    public void init() {

        input.init();
        display.init(windowWidth, windowHeight);

        garnetApp.init(this);

        //windowHandle = display.getWindowHandle();

        // Setup a key callback. It will be called every time a key is pressed, repeated or released.
        glfwSetKeyCallback(display.getWindowHandle(), (window, key, scancode, action, mods) -> {
            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
                glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop

            for (KeyboardCallback kbc : keyboardCallbacks) {
                kbc.invoke(key, scancode, action, mods);
            }
        });

    }

    public void run() {

        glfwMakeContextCurrent(display.getWindowHandle());
        GL.createCapabilities();

        long currentTime = System.nanoTime();
        long oldTime = currentTime;


        // Run the rendering loop until the user has attempted to close
        // the window or has pressed the ESCAPE key.
        while (!glfwWindowShouldClose(display.getWindowHandle())) {

            // todo: create class for this - track fps too
            currentTime = System.nanoTime();
            long delta = currentTime - oldTime;

            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            updateLogicAndRender((int) delta);

            oldTime = currentTime;

        }
    }

    public void updateLogicAndRender(int delta) {

        long nanoSecondsPerSecond = 1_000_000_000;
        int logicUpdatesPerSecond = 60;
        double secondsPerLogicUpdate = 1.0 / (double) logicUpdatesPerSecond;

        long logicTime = nanoSecondsPerSecond / logicUpdatesPerSecond;

        runningLogicDelta += delta;

        // --------------- LOGIC
        while (runningLogicDelta > logicTime) {

            runningLogicDelta -= logicTime;

            //stateManager.tick(secondsPerLogicUpdate);
            garnetApp.tick(secondsPerLogicUpdate);

            gameClock.logLogicTick();

            input.tick();
        }

        // --------------- RENDER
        // Set the clear color
        glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer

        //stateManager.draw();
        garnetApp.draw();

        gameClock.logFrame();

        glfwSwapBuffers(display.getWindowHandle()); // swap the color buffers


        // Poll for window events. The key callback above will only be
        // invoked during this call.
        glfwPollEvents();
        //stateManager.update();


    }

    public void addKeyboardCallback(KeyboardCallback keyboardCallback) {
        System.out.println("addKeyboardCallback");
        keyboardCallbacks.add(keyboardCallback);
    }

    public Input getInput() {
        return input;
    }

    public void setClipWindow(int x, int y, int width, int height) {
        glScissor(x, y, width, height);
        glEnable(GL_SCISSOR_TEST);
    }

    public Display getDisplay() {
        return display;
    }

    public void setApp(GarnetApp garnetApp) {
        this.garnetApp = garnetApp;
    }

    public void setDrawColor(float r, float g, float b, float a) {
        glColor4f(r, g, b, a);
    }

    public void setDrawColor(int rgb) {
        float[] f = Utils.rgbToFloat(rgb);
        glColor4f(f[0], f[1], f[2], f[3]);
    }

    public void setDrawModeWireframe() {
        glDisable(GL_TEXTURE_2D);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
    }

    public void setDrawModeNormal2D() {
        glEnable(GL_TEXTURE_2D);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
    }
}
