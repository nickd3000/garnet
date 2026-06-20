# Garnet Examples

The runnable examples live under:

```text
garnet-examples/src/main/java/com/physmo/garnet/examples
```

Example-only assets live under:

```text
garnet-examples/src/main/resources/examples
```

Run `mvn test` from the repository root to compile the examples against the current Garnet source. Run an individual
example's `main` method from the IDE for interactive testing.

## Suggested Learning Order

1. `gettingstarted/HelloWorld.java` - application lifecycle and drawing text
2. `graphics/PrimitiveDrawingExample.java` - basic shapes
3. `graphics/SimpleSpriteExample.java` - loading and drawing a texture
4. `input/KeyboardExample.java` - keyboard input and movement
5. `input/MouseExample.java` - mouse input
6. `text/RegularFontExample.java` and `text/BMFontExample.java` - text rendering
7. `audio/SimpleSoundExample.java` - loading and playing sounds
8. `graphics/AnimationExample.java` and `graphics/TileSheetExample.java` - sprite sheets
9. `collision/CollisionExample.java` - collision toolkit
10. `toolkit/ContextExample.java` and `context/BasicContextExample.java` - contexts and components
11. `toolkit/StateMachineExample.java` - state machines
12. `toolkit/scenemanager/SceneManagerExample.java` - scenes
13. `toolkit/messaging/MessageSystemExample.java` - messages
14. `graphics/ViewportExample.java` - viewports and scaling
15. `shaders/` - advanced shader and post-processing examples
16. `diagnostics/StressTest.java` - rendering stress testing

Graphics examples require a display. Audio examples require a working audio device. Normal Maven tests compile these
examples but do not launch them.
