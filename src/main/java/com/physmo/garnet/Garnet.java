package com.physmo.garnet;

import com.physmo.garnet.audio.Sound;
import com.physmo.garnet.clock.GameClock;
import com.physmo.garnet.graphics.Graphics;
import com.physmo.garnet.graphics.ViewportManager;
import com.physmo.garnet.input.Input;
import com.physmo.garnet.input.KeyboardCallback;
import com.physmo.garnet.toolkit.GraphDrawer;
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

// NOTE: on MacOS we need to add a vm argument: -XstartOnFirstThread

/**
 * The Garnet class manages the core application loop, initializing and
 * coordinating subsystems such as input, graphics, sound, and debugging tools.
 */
public class Garnet {

    private final List<KeyboardCallback> keyboardCallbacks = new ArrayList<>();
    private final GameClock gameClock = new GameClock();
    private final Input input;
    private final Display display;
    private final Graphics graphics;
    private final Sound sound;
    private final DebugDrawer debugDrawer;
    private final boolean drawFrameGraph = false;
    private double tickRate = 1;
    private GarnetApp garnetApp;
    private double runningLogicDelta = 0;

    private boolean useInternalBuffer = false;
    private com.physmo.garnet.graphics.RenderTexture internalBuffer;
    private com.physmo.garnet.graphics.ShaderProgram internalBufferShader;

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

    /**
     * Returns the current tick rate multiplier applied to logic updates.
     *
     * @return the tick rate multiplier
     */
    public double getTickRate() {
        return tickRate;
    }

    /**
     * Sets the tick rate multiplier applied to logic updates.
     * A value of 1.0 runs at normal speed; values above or below speed up or slow down game logic.
     *
     * @param tickRate the tick rate multiplier to set
     */
    public void setTickRate(double tickRate) {
        this.tickRate = tickRate;
    }

    /**
     * Sets the application to be managed by this Garnet instance.
     *
     * @param garnetApp the {@link GarnetApp} to run
     */
    public void setGarnetApp(GarnetApp garnetApp) {
        this.garnetApp = garnetApp;
    }

    /**
     * Returns the debug drawer used for rendering debug overlays.
     *
     * @return the {@link DebugDrawer} instance
     */
    public DebugDrawer getDebugDrawer() {
        return debugDrawer;
    }

    /**
     * Returns the game clock used for timing logic and render cycles.
     *
     * @return the {@link GameClock} instance
     */
    public GameClock getGameClock() {
        return gameClock;
    }

    /**
     * Returns the graphics subsystem used for rendering.
     *
     * @return the {@link Graphics} instance
     */
    public Graphics getGraphics() {
        return graphics;
    }

    /**
     * Initialises all subsystems including display, sound, input, and the application.
     * Must be called before {@link #run()}.
     */
    public void init() {

        display.init();

        if (useInternalBuffer) {
            graphics.setInternalBufferMode(true);
            internalBuffer = new com.physmo.garnet.graphics.RenderTexture(display.getCanvasSize()[0], display.getCanvasSize()[1]);
            graphics.addTexture(internalBuffer.getTexture());
        }

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

    /**
     * Starts the main game loop, processing logic and rendering each frame until the window is closed.
     * Calls {@link #init()} should be made before this method.
     */
    public void run() {

        glfwMakeContextCurrent(display.getWindowHandle());
        GL.createCapabilities();

        long newTime = System.nanoTime();
        long prevTime = newTime;


        // Run the rendering loop until the user has attempted to close
        // the window or has pressed the ESCAPE key.
        while (!glfwWindowShouldClose(display.getWindowHandle())) {

            newTime = System.nanoTime();
            long elapsedTime = newTime - prevTime;
            prevTime = newTime;

            try {
                Thread.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            gameClock.getTimer(GameClock.TIMER_LOGIC_AND_RENDER).start();
            updateLogicAndRender(elapsedTime / 1_000_000_000.0);
            gameClock.getTimer(GameClock.TIMER_LOGIC_AND_RENDER).stop();

        }
    }

    /**
     * Advances game logic and renders a single frame.
     * Logic is updated at a fixed rate independent of the render frame rate.
     *
     * @param delta elapsed time in seconds since the last call
     */
    public void updateLogicAndRender(double delta) {

        int logicUpdatesPerSecond = 60 * 8;

        double logicTime = 1.0 / logicUpdatesPerSecond;

        runningLogicDelta += delta;

        // --------------- LOGIC
        while (runningLogicDelta >= logicTime) {

            runningLogicDelta -= logicTime;

            garnetApp.tick(logicTime * tickRate);

            gameClock.logLogicTick();

            input.tick();
        }

        // --------------- RENDER
        // Set the clear color

        gameClock.getTimer(GameClock.TIMER_RENDER).start();

        if (useInternalBuffer) {
            graphics.setInternalBufferMode(true);
            internalBuffer.bind();
        }

        // Clear the current target (either the Screen or the FBO)
        float[] bgCols = ColorUtils.rgbToFloat(graphics.getBackgroundColor());
        glClearColor(bgCols[0], bgCols[1], bgCols[2], bgCols[3]);

        graphics.resetSettings();

        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer

        garnetApp.draw(graphics);
        graphics.render();

        if (useInternalBuffer) {
            internalBuffer.unbind(display);
            drawInternalBufferToScreen();
            graphics.setInternalBufferMode(true);
        }

        debugDrawer.setFPS(gameClock.getFps());
        debugDrawer.draw(graphics);

        if (drawFrameGraph) {
            drawFrameGraph();
        }

        gameClock.logFrame();

        glfwSwapBuffers(display.getWindowHandle()); // swap the color buffers

        // Poll for window events. The key callback above will only be
        // invoked during this call.
        gameClock.getTimer(GameClock.TIMER_DEBUG).start();
        glfwPollEvents();
        gameClock.getTimer(GameClock.TIMER_DEBUG).stop();

        gameClock.getTimer(GameClock.TIMER_RENDER).stop();
    }

    /**
     * Blits the internal render buffer onto the screen framebuffer.
     * Bypasses the {@link Graphics} batching system to avoid viewport or scrolling interference.
     * An optional shader can be applied via {@link #setInternalBufferShader}.
     */
    private void drawInternalBufferToScreen() {
        glDisable(GL_SCISSOR_TEST);
        display.placeGlViewport(); // Ensure correct screen viewport is set
        graphics.setInternalBufferMode(false);
        graphics.clearRenderTargetSize();

        float[] bgCols2 = ColorUtils.rgbToFloat(graphics.getBackgroundColor());
        glClearColor(bgCols2[0], bgCols2[1], bgCols2[2], bgCols2[3]);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        graphics.resetSettings();
        int previousViewportId = graphics.getViewportManager().getActiveViewport().getId();
        double previousZoom = graphics.getZoom();
        graphics.setActiveViewport(ViewportManager.DEBUG_VIEWPORT);
        graphics.setZoom(1);
        graphics.setColor(ColorUtils.WHITE);
        graphics.setDrawOrder(0);
        float width = internalBuffer.getWidth();
        float height = internalBuffer.getHeight();
        // FBO colour attachments are presented upside down relative to the
        // engine's top-left canvas coordinates. The old immediate blit flipped
        // V here; keep that explicit while drawing through the batch path.
        graphics.drawImage(
                internalBuffer.getTexture(),
                new float[]{0, 0, width, 0, width, height, 0, height},
                new float[]{0, height, width, height, width, 0, 0, 0}
        ).setShader(internalBufferShader);
        graphics.render();
        graphics.setActiveViewport(previousViewportId);
        graphics.setZoom(previousZoom);
    }

    /**
     * Draws timing graphs for render, logic, and debug timers as an overlay.
     * Only called when {@code drawFrameGraph} is {@code true}.
     */
    private void drawFrameGraph() {
        graphics.setColor(0xff00ffff);
        GraphDrawer.drawGraph(graphics, gameClock.getTimer(GameClock.TIMER_RENDER).getTimes(), 10, 50, 250, 100, 1.0 / 60, 256);
        graphics.setColor(0x00ffffff);
        GraphDrawer.drawGraph(graphics, gameClock.getTimer(GameClock.TIMER_LOGIC_AND_RENDER).getTimes(), 270, 50, 250, 100, 1.0 / 60, 256);
        graphics.setColor(0xffff00ff);
        GraphDrawer.drawGraph(graphics, gameClock.getTimer(GameClock.TIMER_DEBUG).getTimes(), 10, 50 + 100 + 10, 250, 100, 1.0 / 60, 256);
    }

    /**
     * Registers a keyboard callback that will be invoked on key events.
     *
     * @param keyboardCallback the callback to register
     */
    public void addKeyboardCallback(KeyboardCallback keyboardCallback) {
        System.out.println("addKeyboardCallback");
        keyboardCallbacks.add(keyboardCallback);
    }

    /**
     * Returns the input subsystem for querying keyboard and mouse state.
     *
     * @return the {@link Input} instance
     */
    public Input getInput() {
        return input;
    }

    /**
     * Returns the display managing the application window.
     *
     * @return the {@link Display} instance
     */
    public Display getDisplay() {
        return display;
    }

    /**
     * Returns the sound subsystem for playing audio.
     *
     * @return the {@link Sound} instance
     */
    public Sound getSound() {
        return sound;
    }

    /**
     * Sets the application to be managed by this Garnet instance.
     * Equivalent to {@link #setGarnetApp(GarnetApp)}.
     *
     * @param garnetApp the {@link GarnetApp} to run
     */
    public void setApp(GarnetApp garnetApp) {
        this.garnetApp = garnetApp;
    }

    /**
     * Enables or disables rendering to an internal off-screen buffer.
     * When enabled, the scene is rendered to a framebuffer object and then composited onto the screen,
     * allowing post-processing shaders to be applied.
     *
     * @param val {@code true} to enable internal buffer mode, {@code false} to disable
     */
    public void setInternalBufferMode(boolean val) {
        this.useInternalBuffer = val;
    }

    /**
     * Sets the shader program applied when drawing the internal buffer to the screen.
     * Pass {@code null} to use the default fixed-function pipeline.
     *
     * @param shader the {@link com.physmo.garnet.graphics.ShaderProgram} to apply, or {@code null} for none
     */
    public void setInternalBufferShader(com.physmo.garnet.graphics.ShaderProgram shader) {
        this.internalBufferShader = shader;
    }

}
