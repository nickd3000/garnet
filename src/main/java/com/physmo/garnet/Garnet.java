package com.physmo.garnet;

import com.physmo.garnet.audio.Sound;
import com.physmo.garnet.graphics.Graphics;
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
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_SCISSOR_TEST;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glIsEnabled;

// NOTE: on MacOS we need to add a vm argument: -XstartOnFirstThread

/**
 * The Garnet class manages the core application loop, initializing and
 * coordinating subsystems such as input, graphics, sound, and debugging tools.
 */
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

    /**
     * Constructs a new Garnet object initializing the key components required for the framework.
     *
     * @param windowWidth  the width of the window to be created
     * @param windowHeight the height of the window to be created
     */
    public Garnet(int windowWidth, int windowHeight) {
        display = new Display(windowWidth, windowHeight);
        input = new Input(this);
        graphics = new Graphics(display);
        sound = new Sound();
        debugDrawer = new DebugDrawer(input);
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
        float[] bgCols = ColorUtils.rgbToFloat(graphics.getBackgroundColor());
        glClearColor(bgCols[0], bgCols[1], bgCols[2], bgCols[3]);

        boolean scissorEnabled = glIsEnabled(GL_SCISSOR_TEST);
        if (scissorEnabled) glDisable(GL_SCISSOR_TEST);

        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer

        if (scissorEnabled) glEnable(GL_SCISSOR_TEST);

        garnetApp.draw(graphics);
        graphics.render();

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
