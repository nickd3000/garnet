package com.physmo.reference.context;

import com.physmo.garnet.toolkit.Context;
import com.physmo.garnet.toolkit.GameObject;
import com.physmo.reference.context.support.ContextExampleComponent;

public class BasicContextExample {
    public static void main(String[] args) {

        // Create the context.
        Context context = new Context();

        // Create two game objects.
        GameObject gameObject1 = new GameObject("Game Object 1");
        gameObject1.addComponent(new ContextExampleComponent());

        GameObject gameObject2 = new GameObject("Game Object 2");
        gameObject2.addComponent(new ContextExampleComponent());

        // Add the two objects to the context
        context.add(gameObject1);
        context.add(gameObject2);

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
