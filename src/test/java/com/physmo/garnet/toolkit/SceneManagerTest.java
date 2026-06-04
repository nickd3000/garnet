package com.physmo.garnet.toolkit;

import com.physmo.garnet.toolkit.scene.SceneManager;
import com.physmo.garnet.toolkit.support.InventorySubScene;
import com.physmo.garnet.toolkit.support.MainGameScene;
import com.physmo.garnet.toolkit.support.SceneManagerTestSupport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SceneManagerTest {

    @BeforeEach
    void resetSceneManager() {
        SceneManagerTestSupport.resetSceneManager();
    }

    @Test
    void activeSceneReceivesLifecycleAndTickCallbacks() {
        List<String> messageList = new ArrayList<>();

        MainGameScene mainGameState = new MainGameScene(uniqueName("Main Game"));
        mainGameState.setMessageList(messageList);
        SceneManager.addScene(mainGameState);
        SceneManager.setActiveScene(mainGameState.getName());

        for (int i = 0; i < 3; i++) {
            SceneManager.tick(1);
        }

        assertEquals(List.of(
                "MainGameScene init",
                "MainGameScene onMakeActive",
                "MainGameScene tick 0",
                "MainGameScene tick 1",
                "MainGameScene tick 2"
        ), messageList);
    }

    private static String uniqueName(String prefix) {
        return prefix + "-" + System.nanoTime();
    }

    @Test
    void subSceneReceivesTicksUntilItPopsItself() {
        List<String> messageList = new ArrayList<>();

        MainGameScene mainGameState = new MainGameScene(uniqueName("Main Game"));
        mainGameState.setMessageList(messageList);
        mainGameState._init();


        InventorySubScene inventorySubScene = new InventorySubScene(uniqueName("Inventory"));
        inventorySubScene.setMessageList(messageList);
        inventorySubScene._init();


        SceneManager.addScene(mainGameState);
        SceneManager.addScene(inventorySubScene);
        SceneManager.setActiveScene(mainGameState.getName());

        for (int i = 0; i < 3; i++) {
            SceneManager.tick(1);
        }

        SceneManager.pushSubScene(inventorySubScene.getName());
        for (int i = 0; i < 5; i++) {
            SceneManager.tick(1);
        }

        assertEquals(List.of(
                "MainGameScene init",
                "InventorySubScene init",
                "MainGameScene init",
                "MainGameScene onMakeActive",
                "MainGameScene tick 0",
                "MainGameScene tick 1",
                "MainGameScene tick 2",
                "InventorySubScene init",
                "InventorySubScene onMakeActive",
                "InventorySubScene tick 0",
                "InventorySubScene tick 1",
                "InventorySubScene tick 2",
                "InventorySubScene requesting pop, name: " + inventorySubScene.getName(),
                "InventorySubScene onMakeInactive",
                "MainGameScene tick 3",
                "MainGameScene tick 4"
        ), messageList);
    }
}
