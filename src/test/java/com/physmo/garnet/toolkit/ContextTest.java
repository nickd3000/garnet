package com.physmo.garnet.toolkit;

import com.physmo.garnet.toolkit.support.Monster;
import com.physmo.garnet.toolkit.support.MoveRightComponent;
import com.physmo.garnet.toolkit.support.SoundEngine;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ContextTest {

    @Test
    void initAndTickProcessGameObjectComponents() {
        Context context = new Context();
        Monster monster = new Monster("");
        MoveRightComponent moveRight = new MoveRightComponent();
        monster.addComponent(moveRight);
        context.add(monster);

        context.init();
        context.tick(1);

        assertEquals(1, context.getObjectCount());
        assertSame(monster, context.getObjectByType(Monster.class));
        assertSame(moveRight, context.getComponent(MoveRightComponent.class));
    }

    @Test
    void getObjectByTypeReturnsObjectAndTickedState() {
        Context context = new Context();
        context.add(new SoundEngine("sound engine"));
        context.init();
        context.tick(1);

        SoundEngine soundEngine = context.getObjectByType(SoundEngine.class);

        assertNotNull(soundEngine);
        assertInstanceOf(SoundEngine.class, soundEngine);
        assertEquals(1, soundEngine.getTickCount());
    }

    @Test
    void getObjectByTypeCanBeRepeatedWithoutChangingObjectState() {
        Context context = new Context();
        context.add(new SoundEngine("sound engine"));
        context.init();
        context.tick(1);

        SoundEngine soundEngine = context.getObjectByType(SoundEngine.class);

        assertSame(soundEngine, context.getObjectByType(SoundEngine.class));
        assertEquals(1, soundEngine.getTickCount());
    }

    @Test
    void destroyedGameObjectsAreRemovedButPlainObjectsRemain() {
        Context context = new Context();
        Monster monster = new Monster("");
        monster.addComponent(new MoveRightComponent());
        context.add(monster);
        context.add("String based object");
        context.init();
        context.tick(1);

        monster.destroy();
        context.tick(1);

        assertThrows(RuntimeException.class, () -> context.getObjectByType(Monster.class));
        assertEquals("String based object", context.getObjectByType(String.class));
        assertEquals(1, context.getObjectCount());
    }

    @Test
    void getObjectByTagReturnsTaggedObject() {
        Context context = new Context();
        Monster monster = new Monster("");
        monster.addTag("TestTag");
        context.add(monster);

        context.init();
        context.tick(1);

        assertSame(monster, context.getObjectByTag("TestTag"));
    }
}
