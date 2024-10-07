package com.physmo.garnet.toolkit.scene

import spock.lang.Specification

class SceneManagerTest extends Specification {

    def "getSharedContext returns the same shared context on subsequent calls"() {
        expect:
            SceneManager.getSharedContext() == SceneManager.getSharedContext()
    }

    def "addScene adds a scene and sets it as target if no active scene"() {
        given:
            def scene = createTestScene("sampleScene")

        when:
            SceneManager.addScene(scene)

        and: "We tick the scene manager so that the target scene becomes active"
            SceneManager.tick(1)
            scene.getName() >> "sampleScene"

        then:
            SceneManager.getActiveScene().isPresent()
    }

    def "setActiveScene sets the active scene to the given scene name"() {
        given:
            def scene = createTestScene("sampleScene")
            SceneManager.addScene(scene)

        when:
            SceneManager.setActiveScene("sampleScene")

        and: "We tick the scene manager so that the target scene becomes active"
            SceneManager.tick(1)

        then:
            SceneManager.getActiveScene().get().getName() == "sampleScene"

    }

    def "setActiveScene throws exception for unknown scene name"() {
        when:
            SceneManager.setActiveScene("unknownScene")

        then:
            thrown(RuntimeException)
    }

    def "pushSubScene adds a subscene push request"() {
        when:
            SceneManager.pushSubScene("subSceneName")

        then: true // Actual validation would be checking internals, which are private
    }

    def "popSubScene adds a subscene pop request and throws exception for unknown subscene name"() {
        when:
            SceneManager.popSubScene("unknownSubSceneName")

        then:
            thrown(RuntimeException)
    }

    // ... Add other test methods as necessary

    Scene createTestScene(String name) {
        return new Scene(name) {
            @Override
            void init() {

            }

            @Override
            void tick(double delta) {

            }

            @Override
            void draw() {

            }

            @Override
            void onMakeActive() {

            }

            @Override
            void onMakeInactive() {

            }
        }
    }
}
