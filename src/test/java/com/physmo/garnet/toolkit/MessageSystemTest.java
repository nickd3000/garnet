package com.physmo.garnet.toolkit;

import com.physmo.garnet.graphics.Graphics;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class MessageSystemTest {

    @Test
    public void testLocalMessaging() {
        GameObject gameObject = new GameObject("TestObject");
        TestComponent component = new TestComponent();
        gameObject.addComponent(component);

        gameObject.sendMessage("HELLO", "WORLD");

        Assert.assertTrue(component.receivedMessages.contains("HELLO"));
        Assert.assertEquals("WORLD", component.lastData);
    }

    @Test
    public void testGlobalBroadcasting() {
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

        Assert.assertTrue(comp1.receivedMessages.contains("BROADCAST"));
        Assert.assertEquals(123, comp1.lastData);
        Assert.assertTrue(comp2.receivedMessages.contains("BROADCAST"));
        Assert.assertEquals(123, comp2.lastData);
    }

    @Test
    public void testInitiatingBroadcastFromComponent() {
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

        Assert.assertTrue(comp1.receivedMessages.contains("FROM_COMP"));
        Assert.assertTrue(comp2.receivedMessages.contains("FROM_COMP"));
        Assert.assertEquals("DATA", comp2.lastData);
    }

    @Test
    public void testInitiatingBroadcastFromGameObject() {
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

        Assert.assertTrue(comp1.receivedMessages.contains("FROM_OBJ"));
        Assert.assertTrue(comp2.receivedMessages.contains("FROM_OBJ"));
    }

    @Test
    public void testNoOpDefault() {
        GameObject gameObject = new GameObject("TestObject");
        // Add a component that doesn't override onMessage
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

        // This should not throw any exception
        gameObject.sendMessage("TEST");
    }

    @Test
    public void testContextTagSafety() {
        Context context = new Context();
        context.init();
        GameObject obj = context.getObjectByTag("NON_EXISTENT");
        Assert.assertNull(obj);
    }

    @Test(expected = RuntimeException.class)
    public void testComponentContextSafety() {
        TestComponent component = new TestComponent();
        // This should throw RuntimeException because parent is null
        component.getObjectByTypeFromParentContext(GameObject.class);
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
