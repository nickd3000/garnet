package com.physmo.garnet.toolkit;

import com.physmo.garnet.graphics.Graphics;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GameObjectTest {

    @Test
    void namedCreatesGameObjectWithName() {
        GameObject gameObject = GameObject.named("player");

        assertEquals("player", gameObject.getName());
    }

    @Test
    void atSetsTransformAndReturnsSameObject() {
        GameObject gameObject = GameObject.named("player");

        GameObject returned = gameObject.at(10.5, 20.25, 3);

        assertSame(gameObject, returned);
        assertEquals(10.5, gameObject.getTransform().x);
        assertEquals(20.25, gameObject.getTransform().y);
        assertEquals(3, gameObject.getTransform().z);
    }

    @Test
    void atTwoArgumentsSetsZToZero() {
        GameObject gameObject = GameObject.named("player").at(10, 20);

        assertEquals(10, gameObject.getTransform().x);
        assertEquals(20, gameObject.getTransform().y);
        assertEquals(0, gameObject.getTransform().z);
    }

    @Test
    void taggedAddsTagAndReturnsSameObject() {
        GameObject gameObject = GameObject.named("player");

        GameObject returned = gameObject.tagged("controllable");

        assertSame(gameObject, returned);
        assertTrue(gameObject.hasTag("controllable"));
    }

    @Test
    void withAddsComponentParentAndReturnsSameObject() {
        GameObject gameObject = GameObject.named("player");
        TestComponent component = new TestComponent();

        GameObject returned = gameObject.with(component);

        assertSame(gameObject, returned);
        assertSame(component, gameObject.getComponent(TestComponent.class));
        assertSame(gameObject, component.getParent());
    }

    @Test
    void inContextAddsObjectAndReturnsSameObject() {
        Context context = new Context();
        GameObject gameObject = GameObject.named("player")
                .tagged("controllable")
                .inContext(context);

        context.init();

        assertSame(gameObject, context.getObjectByTag("controllable"));
        assertSame(context, gameObject.getContext());
    }

    private static class TestComponent extends Component {
        GameObject getParent() {
            return parent;
        }

        @Override
        public void init() {
        }

        @Override
        public void tick(double t) {
        }

        @Override
        public void draw(Graphics g) {
        }
    }
}
