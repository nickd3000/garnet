package com.physmo.reference.collision;

import com.physmo.garnet.ColorUtils;
import com.physmo.garnet.Garnet;
import com.physmo.garnet.GarnetApp;
import com.physmo.garnet.graphics.Graphics;
import com.physmo.garnet.graphics.TileSheet;
import com.physmo.garnet.toolkit.Context;
import com.physmo.garnet.toolkit.GameObject;
import com.physmo.garnet.toolkit.simplecollision.ColliderComponent;
import com.physmo.garnet.toolkit.simplecollision.CollisionSystem;
import com.physmo.garnet.toolkit.simplecollision.RelativeObject;

import java.util.List;
import java.util.Random;

/**
 * Example showing how to use the CollisionSystem to process objects that
 * are close to each other.
 */
// NOTE: On MacOS the following VM argument is required: -XstartOnFirstThread
public class CollisionExample_CloseObjects extends GarnetApp {

    static int height = 600;
    static int width = 800;
    String imageFileName = "space.png";
    TileSheet tileSheet;
    Context context;
    double scale = 2;
    Random random = new Random(12345);
    CollisionSystem collisionSystem;
    List<RelativeObject> nearestObjects;
    int closeObjectTestCount = 0;

    public static void main(String[] args) {
        Garnet.launch(width, height, CollisionExample_CloseObjects::new);
    }

    @Override
    public void init() {
        context = new Context();

        Graphics graphics = garnet.getGraphics();
        tileSheet = graphics.loadTileSheet(imageFileName, 16, 16);
        context.add(tileSheet);
        context.add(graphics);

        collisionSystem = new CollisionSystem("d");
        context.add(collisionSystem);

        for (int i = 0; i < 200; i++) {
            createObject(context, collisionSystem,
                    random.nextInt((int) (width / scale)),
                    random.nextInt((int) (height / scale)));
        }

        context.init();
        garnet.getDebugDrawer().setVisible(true);
        garnet.getDebugDrawer().setScale(2);
    }

    public void createObject(Context context, CollisionSystem collisionSystem, double x, double y) {

        ColliderComponent collider = new ColliderComponent();
        GameObject.named("obj1")
                .at(x, y)
                .with(collider)
                .tagged("testobject")
                .with(new ComponentCollidingSprite())
                .inContext(context);
        collisionSystem.addCollidable(collider);
    }

    @Override
    public void tick(double delta) {
        context.tick(delta);

        int[] mousePosition = garnet.getInput().getMouse().getPosition();
        List<GameObject> objectsByTag = context.getObjectsByTag("testobject");
        objectsByTag.get(0).getTransform().set(mousePosition[0] - 8, mousePosition[1] - 8, 0);

        closeObjectTestCount = collisionSystem.processCloseObjects(0, 20);
    }

    @Override
    public void draw(Graphics g) {
        g.setBackgroundColor(ColorUtils.asRGBA(0.3f, 0.2f, 0.3f, 1.0f));
        context.draw(g);

        garnet.getDebugDrawer().setUserString("closeObjectTestCount:", String.valueOf(closeObjectTestCount));

        g.setZoom(scale);
    }

}
