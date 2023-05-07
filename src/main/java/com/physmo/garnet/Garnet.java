package com.physmo.garnet;

import com.physmo.garnet.audio.Sound;
import com.physmo.garnet.graphics.Graphics;
import com.physmo.garnet.input.Input;
import com.physmo.garnet.input.KeyboardCallback;
import org.lwjgl.opengl.GL;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

// NOTE: on MacOS we need to add a vm argument: -XstartOnFirstThread
public class Garnet {

    private final List<KeyboardCallback> keyboardCallbacks = new ArrayList<>();
    private final GameClock gameClock = new GameClock();
    private final double tickRate = 1;
    private final Input input;
    private final Display display;
    private final Graphics graphics;
    private final Sound sound;
    private final DebugDrawer debugDrawer;
    private int runningLogicDelta = 0;
    private GarnetApp garnetApp;

    public Garnet(int windowWidth, int windowHeight) {
        display = new Display(windowWidth, windowHeight);
        input = new Input(this);
        graphics = new Graphics(display);
        sound = new Sound();
        debugDrawer = new DebugDrawer();
    }

    public DebugDrawer getDebugDrawer() {
        return debugDrawer;
    }

    public GameClock getGameClock() {
        return gameClock;
    }

    public Graphics getGraphics() {
        return graphics;
    }

    public void init() {


        display.init();
        sound.init();
        input.init();
        input.setWindowHandle(display.getWindowHandle());
        garnetApp.init(this);
        debugDrawer.init();

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

        long newTime = System.nanoTime();
        long currentTime = newTime;


        // Run the rendering loop until the user has attempted to close
        // the window or has pressed the ESCAPE key.
        while (!glfwWindowShouldClose(display.getWindowHandle())) {

            // todo: create class for this - track fps too
            newTime = System.nanoTime();
            long frameTime = newTime - currentTime;
            currentTime = newTime;

            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            updateLogicAndRender((int) frameTime);



        }
    }

    public void updateLogicAndRender(int delta) {

        long nanoSecondsPerSecond = 1_000_000_000;
        int logicUpdatesPerSecond = 60 * 5;
        double secondsPerLogicUpdate = 1.0 / (double) logicUpdatesPerSecond;

        long logicTime = nanoSecondsPerSecond / logicUpdatesPerSecond;

        runningLogicDelta += delta;

        // --------------- LOGIC
        while (runningLogicDelta > logicTime) {

            runningLogicDelta -= logicTime;

            garnetApp.tick(secondsPerLogicUpdate * tickRate);

            gameClock.logLogicTick();

            input.tick();
        }

        // --------------- RENDER
        // Set the clear color
        float[] bgCols = Utils.rgbToFloat(graphics.getBackgroundColor());
        glClearColor(bgCols[0], bgCols[1], bgCols[2], bgCols[3]);

        boolean scissorEnabled = glIsEnabled(GL_SCISSOR_TEST);
        if (scissorEnabled) glDisable(GL_SCISSOR_TEST);

        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer

        if (scissorEnabled) glEnable(GL_SCISSOR_TEST);

        garnetApp.draw();


        debugDrawer.setFPS(gameClock.getFps());
        debugDrawer.draw(graphics);

        gameClock.logFrame();

        glfwSwapBuffers(display.getWindowHandle()); // swap the color buffers

        // Poll for window events. The key callback above will only be
        // invoked during this call.
        glfwPollEvents();

    }

    public void addKeyboardCallback(KeyboardCallback keyboardCallback) {
        System.out.println("addKeyboardCallback");
        keyboardCallbacks.add(keyboardCallback);
    }

    public Input getInput() {
        return input;
    }

    public Display getDisplay() {
        return display;
    }

    public Sound getSound() {
        return sound;
    }

    public void setApp(GarnetApp garnetApp) {
        this.garnetApp = garnetApp;
    }

}
