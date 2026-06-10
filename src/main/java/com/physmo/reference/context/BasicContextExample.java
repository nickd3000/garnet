package com.physmo.reference.context;

import com.physmo.garnet.toolkit.Context;
import com.physmo.garnet.toolkit.GameObject;
import com.physmo.reference.context.support.ContextExampleComponent;

public class BasicContextExample {
    public static void main(String[] args) {

        // Create the context.
        Context context = new Context();

        // Create two game objects.
        GameObject gameObject1 = GameObject.named("Game Object 1")
                .with(new ContextExampleComponent())
                .inContext(context);

        GameObject.named("Game Object 2")
                .with(new ContextExampleComponent())
                .inContext(context);

        context.init();

        System.out.println("\n- Ticking context 3 times");
        context.tick(1);
        context.tick(1);
        context.tick(1);

        System.out.println("\n- Context object count: " + context.getObjectCount());

        System.out.println("\n- Deleting one game object");
        gameObject1.destroy();

        System.out.println("\n- Ticking context 2 times");
        context.tick(1);
        context.tick(1);

        System.out.println("\n- Context object count: " + context.getObjectCount());
    }
}
