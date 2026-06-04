package com.physmo.garnet.toolkit;

import com.physmo.garnet.graphics.Graphics;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MessageSystemTest {

    @Test
    void localMessageIsDeliveredToComponents() {
        GameObject gameObject = new GameObject("TestObject");
        TestComponent component = new TestComponent();
        gameObject.addComponent(component);

        gameObject.sendMessage("HELLO", "WORLD");

        assertTrue(component.receivedMessages.contains("HELLO"));
        assertEquals("WORLD", component.lastData);
    }

    @Test
    void contextBroadcastIsDeliveredToAllGameObjectComponents() {
        Context context = new Context();
        context.init();

        GameObject obj1 = new GameObject("Obj1");
        TestComponent comp1 = new TestComponent();
        obj1.addComponent(comp1);

        GameObject obj2 = new GameObject("Obj2");
        TestComponent comp2 = new TestComponent();
        obj2.addComponent(comp2);

        context.add(obj1);
        context.add(obj2);

        context.broadcastMessage("BROADCAST", 123);

        assertTrue(comp1.receivedMessages.contains("BROADCAST"));
        assertEquals(123, comp1.lastData);
        assertTrue(comp2.receivedMessages.contains("BROADCAST"));
        assertEquals(123, comp2.lastData);
    }

    @Test
    void componentCanInitiateBroadcastThroughParentContext() {
        Context context = new Context();
        context.init();

        GameObject obj1 = new GameObject("Obj1");
        TestComponent comp1 = new TestComponent();
        obj1.addComponent(comp1);

        GameObject obj2 = new GameObject("Obj2");
        TestComponent comp2 = new TestComponent();
        obj2.addComponent(comp2);

        context.add(obj1);
        context.add(obj2);

        comp1.broadcastMessage("FROM_COMP", "DATA");

        assertTrue(comp1.receivedMessages.contains("FROM_COMP"));
        assertTrue(comp2.receivedMessages.contains("FROM_COMP"));
        assertEquals("DATA", comp2.lastData);
    }

    @Test
    void gameObjectCanInitiateBroadcastThroughContext() {
        Context context = new Context();
        context.init();

        GameObject obj1 = new GameObject("Obj1");
        TestComponent comp1 = new TestComponent();
        obj1.addComponent(comp1);

        GameObject obj2 = new GameObject("Obj2");
        TestComponent comp2 = new TestComponent();
        obj2.addComponent(comp2);

        context.add(obj1);
        context.add(obj2);

        obj1.broadcastMessage("FROM_OBJ");

        assertTrue(comp1.receivedMessages.contains("FROM_OBJ"));
        assertTrue(comp2.receivedMessages.contains("FROM_OBJ"));
    }

    @Test
    void defaultMessageHandlerDoesNotThrow() {
        GameObject gameObject = new GameObject("TestObject");
        Component normalComp = new Component() {
            @Override
            public void init() {
            }

            @Override
            public void tick(double t) {
            }

            @Override
            public void draw(Graphics g) {
            }
        };
        gameObject.addComponent(normalComp);

        assertDoesNotThrow(() -> gameObject.sendMessage("TEST"));
    }

    @Test
    void getObjectByTagReturnsNullForUnknownTag() {
        Context context = new Context();
        context.init();
        GameObject obj = context.getObjectByTag("NON_EXISTENT");

        assertNull(obj);
    }

    @Test
    void componentParentContextAccessThrowsWhenParentIsMissing() {
        TestComponent component = new TestComponent();

        assertThrows(RuntimeException.class, () -> component.getObjectByTypeFromParentContext(GameObject.class));
    }

    static class TestComponent extends Component {
        public List<String> receivedMessages = new ArrayList<>();
        public Object lastData = null;

        @Override
        public void init() {
        }

        @Override
        public void tick(double t) {
        }

        @Override
        public void draw(Graphics g) {
        }

        @Override
        public void onMessage(String name, Object data) {
            receivedMessages.add(name);
            lastData = data;
        }
    }
}
