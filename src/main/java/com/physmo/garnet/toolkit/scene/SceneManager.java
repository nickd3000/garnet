package com.physmo.garnet.toolkit.scene;

import com.physmo.garnet.graphics.Graphics;
import com.physmo.garnet.toolkit.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * The single global scene manager.
 */
public class SceneManager {

    private static final List<String> subScenePushRequests;
    private static final List<String> subScenePopRequests;
    private static final Map<String, Scene> scenes;
    private static Context sharedContext;
    private static Scene activeScene;
    private static Scene targetScene;
    private static List<Scene> activeSubScenes;
    private static long tickCount = 0;

    static {
        scenes = new HashMap<>();
        activeSubScenes = new ArrayList<>();
        subScenePushRequests = new ArrayList<>();
        subScenePopRequests = new ArrayList<>();
        sharedContext = new Context();
    }

    private SceneManager() {
        // Don't allow user creation of a scene manager.
    }

    /**
     * Retrieve the shared context for the game or application.
     * If the shared context is null, a new instance is created and returned.
     * <p>
     * NOTE: The shared context is useful for data and objects that are shared
     * between scenes, or want to persist over the lifetime of the game.
     *
     * @return The shared context.
     */
    public static Context getSharedContext() {
        if (sharedContext == null) {
            sharedContext = new Context();
        }

        return sharedContext;
    }

    /**
     * Updates and ticks the scenes based on the passed delta time.
     * Ticks either the main scene or the last active subscene.
     *
     * @param delta Time since the last tick.
     */
    public static void tick(double delta) {
        update();

        tickCount++;

        if (!sharedContext.isInitialised()) sharedContext.init();
        sharedContext.tick(delta);

        // Only tick main scene if there are no active sub scenes.
        if (activeSubScenes.isEmpty() && activeScene.isInitialized()) activeScene._tick(delta);

        // Find the last active subscene and tick it.
        if (!activeSubScenes.isEmpty())
            activeSubScenes.get(activeSubScenes.size() - 1)._tick(delta);

    }

    /**
     * Handles scene management operations after game state ticks.
     * This ensures safe addition, removal, or changes to scenes.
     */
    public static void update() {
        handleSceneChange();
        handleSubscenePop();
        handleSubscenePush();
    }

    /**
     * Manages the transition between the current and target scenes.
     * Initializes and activates the target scene if set.
     */
    public static void handleSceneChange() {
        if (targetScene != null) {
            targetScene._init();

            if (activeScene != targetScene) {
                targetScene.onMakeActive();
                if (activeScene != null) activeScene.onMakeInactive();
            }

            activeScene = targetScene;
            targetScene = null;
        }
    }

    /**
     * Processes the requests to push subscenes to the active list.
     * If a subscene is pushed, it's initialized and activated.
     */
    public static void handleSubscenePush() {
        if (subScenePushRequests.isEmpty()) return;

        for (String subsceneName : subScenePushRequests) {

            Scene scene = scenes.get(subsceneName);
            //scene._init();
            if (scene == null) {
                System.out.println("Subscene not found: " + subsceneName);
                return;
            }
            scene._init();
            scene.onMakeActive();
            activeSubScenes.add(scene);
        }

        subScenePushRequests.clear();
    }


    /**
     * Processes the requests to pop subscenes from the active list.
     * Deactivates and removes subscenes that are requested to be popped.
     */
    public static void handleSubscenePop() {
        if (subScenePopRequests.isEmpty()) return;

        List<Scene> newActiveSubScenes = new ArrayList<>();

        for (Scene activeSubScene : activeSubScenes) {
            boolean skip = false;
            for (String subScenePopRequest : subScenePopRequests) {
                if (activeSubScene.getName().equalsIgnoreCase(subScenePopRequest)) {
                    skip = true;
                    System.out.println("remove");
                    activeSubScene.onMakeInactive();
                }
            }

            if (!skip) {
                newActiveSubScenes.add(activeSubScene);
            }
        }
        subScenePopRequests.clear();
        activeSubScenes = newActiveSubScenes;

    }

    /**
     * Renders the active scene and all the active subscenes.
     * Uses the shared context for drawing.
     */
    public static void draw(Graphics g) {
        if (tickCount == 0) return;

        sharedContext.draw(g);

        if (activeScene != null && activeScene.isInitialized()) {
            activeScene._draw(g);
        }

        for (Scene activeSubScene : activeSubScenes) {
            activeSubScene._draw(g);
        }
    }

    /**
     * Retrieves the currently active scene.
     *
     * @return An Optional containing the active scene if present.
     */
    public static Optional<Scene> getActiveScene() {
        return Optional.ofNullable(activeScene);
    }

    public static Optional<Scene> getSceneByName(String name) {
        throwExceptionIfSceneNameNotFound(name);

        for (String scene : scenes.keySet()) {
            if (scene.equalsIgnoreCase(name)) {
                return Optional.of(scenes.get(name));
            }
        }

        return Optional.empty();
    }


    /**
     * Sets the scene with the given name as the active scene.
     * Throws an exception if the scene name is not found.
     *
     * @param name Name of the scene to be set as active.
     */
    public static void setActiveScene(String name) {
        throwExceptionIfSceneNameNotFound(name);

        for (String scene : scenes.keySet()) {
            if (scene.equalsIgnoreCase(name)) {
                targetScene = scenes.get(name);
                System.out.println("Switching scene to " + name);
                return;
            }
        }

        System.out.println("Scene name not found: " + name);
    }

    /**
     * Throws a runtime exception if the provided scene name isn't found in the list.
     *
     * @param name Name of the scene to be checked.
     * @throws RuntimeException if the scene name is not found.
     */
    public static void throwExceptionIfSceneNameNotFound(String name) {
        for (String str : scenes.keySet()) {
            if (str.equalsIgnoreCase(name)) return;
        }
        throw new RuntimeException("Unknown Scene name " + name + ".");
    }

    /**
     * Adds a new scene to the scene manager.
     * If no active scene is set, the provided scene is set as the target scene.
     *
     * @param scene The scene to be added.
     */
    public static void addScene(Scene scene) {
        System.out.println("Adding scene");
        if (activeScene == null) targetScene = scene;
        scenes.put(scene.getName(), scene);
        //scene.setSceneManager(this);
    }

    /**
     * Requests to push a subscene with the given name to the active list.
     * The actual push happens in the update method.
     *
     * @param name Name of the subscene to be pushed.
     */
    public static void pushSubScene(String name) {
        subScenePushRequests.add(name);
    }

    /**
     * Requests to pop a subscene with the given name from the active list.
     * The actual pop happens in the update method.
     * Throws an exception if the scene name is not found.
     *
     * @param name Name of the subscene to be popped.
     */
    public static void popSubScene(String name) {
        subScenePopRequests.add(name);

        throwExceptionIfSceneNameNotFound(name);
    }
}
