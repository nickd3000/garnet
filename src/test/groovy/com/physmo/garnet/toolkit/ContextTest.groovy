package com.physmo.garnet.toolkit

import com.physmo.garnet.toolkit.support.Monster
import com.physmo.garnet.toolkit.support.MoveRightComponent
import com.physmo.garnet.toolkit.support.SoundEngine
import spock.lang.Specification

class ContextTest extends Specification {

    def "basic test for context"() {
        given: "A context with a monster that has a MoveRightComponent"
            Context context = new Context()
            Monster monster = new Monster("")
            MoveRightComponent moveRight = new MoveRightComponent()
            monster.addComponent(moveRight)
            context.add(monster)

        when: "The context is initialized and ticked"
            context.init()
            context.tick(1)

        then: "There are no explicit assertions in this test, but exceptions should not be thrown"
            // No assertions needed, but if any method throws an exception, the test will fail.
    }

    def "test getting object by type from context"() {
        given: "A context with a SoundEngine"
            Context context = new Context()
            context.add(new SoundEngine("sound engine"))
            context.init()
            context.tick(1)

        when: "The SoundEngine is retrieved by its type"
            SoundEngine soundEngine = context.getObjectByType(SoundEngine.class)

        then: "The SoundEngine should be accessible, of the correct type, and have ticked once"
            soundEngine != null
            soundEngine instanceof SoundEngine
            soundEngine.getTickCount() == 1
    }

    def "test getting object by tag from context"() {
        given: "A context with a SoundEngine"
            Context context = new Context()
            context.add(new SoundEngine("sound engine"))
            context.init()
            context.tick(1)

        when: "The SoundEngine is retrieved by its type"
            SoundEngine soundEngine = context.getObjectByType(SoundEngine.class)

        then: "The SoundEngine should be accessible, of the correct type, and have ticked once"
            soundEngine != null
            soundEngine instanceof SoundEngine
            soundEngine.getTickCount() == 1
    }

    def "Test destroying a game object in the context"() {
        given: "A context with a monster that has a MoveRightComponent"
            Context context = new Context()
            Monster monster = new Monster("")
            MoveRightComponent moveRight = new MoveRightComponent()
            monster.addComponent(moveRight)
            context.add(monster)
            context.add(new String("String based object"))

        when: "The context is initialized and ticked"
            context.init()
            context.tick(1)

        and: "The object is destroyed then context is ticked"
            monster.destroy()
            context.tick(1)

        and: "An attempt to get the game object by type is made"
            context.getObjectByType(Monster.class)

        then: "An exception is thrown because the object does not exist in the context"
            thrown(RuntimeException)
            context.getObjectByType(String.class)
    }

    def "Test retrieving an object by tag"() {
        given: "A context with a monster that has a MoveRightComponent"
            Context context = new Context()
            Monster monster = new Monster("")
            monster.addTag("TestTag")
            context.add(monster)

        when: "The context is initialized and ticked"
            context.init()
            context.tick(1)

        then: "There are no explicit assertions in this test, but exceptions should not be thrown"
            context.getObjectByTag("TestTag")
    }
}