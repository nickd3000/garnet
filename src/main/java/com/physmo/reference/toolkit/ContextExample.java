package com.physmo.reference.toolkit;

import com.physmo.garnet.toolkit.Context;
import com.physmo.reference.toolkit.support.DummyClass;
import com.physmo.reference.toolkit.support.DummyGameObject;

public class ContextExample {

    public static void main(String[] args) {
        Context context = new Context();

        // Add a plain java object.
        context.add(new DummyClass());

        // Add a game object - this can be initialised, ticked and drawn by the context.
        new DummyGameObject("my game object").inContext(context);

        DummyClass objectByType = context.getObjectByType(DummyClass.class);

        assert objectByType != null;

        context.init();
        context.tick(1);
        context.draw(null);

    }


}
