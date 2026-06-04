package com.physmo.garnet.toolkit.scene;

import com.physmo.garnet.graphics.Graphics;
import com.physmo.garnet.toolkit.Context;
import com.physmo.garnet.toolkit.support.SceneManagerTestSupport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SceneManagerTest {

    @BeforeEach
    void resetSceneManager() {
        SceneManagerTestSupport.resetSceneManager();
    }

    @Test
    void getSharedContextReturnsSameContextOnSubsequentCalls() {
        Context sharedContext = SceneManager.getSharedContext();

        assertNotNull(sharedContext);
        assertSame(sharedContext, SceneManager.getSharedContext());
    }

    @Test
    void addSceneStoresSceneAndAllowsItToBecomeActive() {
        RecordingScene scene = new RecordingScene(uniqueName("sampleScene"));
        SceneManager.addScene(scene);

        SceneManager.setActiveScene(scene.getName());
        SceneManager.tick(1);

        assertTrue(SceneManager.getActiveScene().isPresent());
        assertEquals(scene.getName(), SceneManager.getActiveScene().get().getName());
        assertEquals(1, scene.initCount);
        assertEquals(1, scene.activeCount);
        assertEquals(1, scene.tickCount);
    }

    private static String uniqueName(String prefix) {
        return prefix + "-" + System.nanoTime();
    }

    @Test
    void setActiveSceneRejectsUnknownSceneName() {
        assertThrows(RuntimeException.class, () -> SceneManager.setActiveScene(uniqueName("unknownScene")));
    }

    @Test
    void pushSubSceneActivatesAndTicksRequestedSubScene() {
        RecordingScene mainScene = new RecordingScene(uniqueName("mainScene"));
        RecordingScene subScene = new RecordingScene(uniqueName("subScene"));
        SceneManager.addScene(mainScene);
        SceneManager.addScene(subScene);
        SceneManager.setActiveScene(mainScene.getName());
        SceneManager.tick(1);

        SceneManager.pushSubScene(subScene.getName());
        SceneManager.tick(1);

        assertEquals(1, subScene.initCount);
        assertEquals(1, subScene.activeCount);
        assertEquals(1, subScene.tickCount);
    }

    @Test
    void popSubSceneRejectsUnknownSubSceneName() {
        assertThrows(RuntimeException.class, () -> SceneManager.popSubScene(uniqueName("unknownSubScene")));
    }

    private static final class RecordingScene extends Scene {
        int initCount;
        int tickCount;
        int activeCount;
        int inactiveCount;

        private RecordingScene(String name) {
            super(name);
        }

        @Override
        public void init() {
            initCount++;
        }

        @Override
        public void tick(double delta) {
            tickCount++;
        }

        @Override
        public void draw(Graphics g) {
        }

        @Override
        public void onMakeActive() {
            activeCount++;
        }

        @Override
        public void onMakeInactive() {
            inactiveCount++;
        }
    }
}
