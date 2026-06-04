package com.physmo.garnet.toolkit.support;

import com.physmo.garnet.toolkit.Context;
import com.physmo.garnet.toolkit.scene.Scene;
import com.physmo.garnet.toolkit.scene.SceneManager;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

public final class SceneManagerTestSupport {

    private SceneManagerTestSupport() {
    }

    public static void resetSceneManager() {
        try {
            getMap("scenes").clear();
            getList("activeSubScenes").clear();
            getList("subScenePushRequests").clear();
            getList("subScenePopRequests").clear();
            setField("sharedContext", new Context());
            setField("activeScene", null);
            setField("targetScene", null);
            setField("tickCount", 0L);
        } catch (ReflectiveOperationException e) {
            throw new AssertionError("Unable to reset SceneManager static state", e);
        }
    }

    @SuppressWarnings("unchecked")
    private static Map<String, Scene> getMap(String fieldName) throws ReflectiveOperationException {
        return (Map<String, Scene>) getField(fieldName).get(null);
    }

    @SuppressWarnings("unchecked")
    private static List<?> getList(String fieldName) throws ReflectiveOperationException {
        return (List<?>) getField(fieldName).get(null);
    }

    private static void setField(String fieldName, Object value) throws ReflectiveOperationException {
        getField(fieldName).set(null, value);
    }

    private static Field getField(String fieldName) throws ReflectiveOperationException {
        Field field = SceneManager.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        return field;
    }
}
