package com.physmo.garnetexamples.collision;

import com.physmo.garnet.Garnet;
import com.physmo.garnet.GarnetApp;
import com.physmo.garnet.graphics.Graphics;
import com.physmo.garnet.graphics.Texture;
import com.physmo.garnet.graphics.TileSheet;
import com.physmo.garnet.toolkit.Context;
import com.physmo.garnet.toolkit.GameObject;
import com.physmo.garnet.toolkit.simplecollision.ColliderComponent;
import com.physmo.garnet.toolkit.simplecollision.CollisionSystem;
import com.physmo.garnet.toolkit.simplecollision.RelativeObject;

import java.util.List;
import java.util.Random;

// NOTE: On MacOS the following VM argument is required: -XstartOnFirstThread
public class CollisionExample extends GarnetApp {

    static int width = 800;
    static int height = 600;
    String imageFileName = "space.png";
    TileSheet tileSheet;
    Texture texture;
    Context context;
    double scale = 1;
    Random random = new Random(12345);
    CollisionSystem collisionSystem;
    List<RelativeObject> nearestObjects;

    public CollisionExample(Garnet garnet, String name) {
        super(garnet, name);
    }

    public static void main(String[] args) {
        Garnet garnet = new Garnet(width, height);
        GarnetApp app = new CollisionExample(garnet, "");

        garnet.setApp(app);

        garnet.init();
        garnet.run();
    }

    @Override
    public void init(Garnet garnet) {
        context = new Context();

        texture = Texture.loadTexture(imageFileName);
        tileSheet = new TileSheet(texture, 16, 16);
        Graphics graphics = garnet.getGraphics();
        graphics.addTexture(texture);
        context.add(tileSheet);
        context.add(graphics);

        collisionSystem = new CollisionSystem("d");
        context.add(collisionSystem);

        for (int i = 0; i < 450; i++) {
            createObject(context, collisionSystem,
                    random.nextInt((int) (width / scale)),
                    random.nextInt((int) (height / scale)));
        }

        context.init();
        garnet.getDebugDrawer().setVisible(true);
        garnet.getDebugDrawer().setScale(2);
    }

    public void createObject(Context context, CollisionSystem collisionSystem, double x, double y) {

        GameObject obj1 = new GameObject("obj1");
        obj1.getTransform().set(x, y, 0);
        ColliderComponent collider = new ColliderComponent();
        obj1.addComponent(collider);
        collisionSystem.addCollidable(collider);
        obj1.addTag("testobject");
        obj1.addComponent(new ComponentCollidingSprite());
        context.add(obj1);
    }

    @Override
    public void tick(double delta) {
        context.tick(delta);

        int[] mps = garnet.getInput().getMouse().getPositionScaled(scale);
        List<GameObject> objectsByTag = context.getObjectsByTag("testobject");
        objectsByTag.get(0).getTransform().set(mps[0] - 8, mps[1] - 8, 0);

        nearestObjects = collisionSystem.getNearestObjects("testobject", mps[0] - 8, mps[1] - 8, 150);

        collisionSystem.processCloseObjects("testobject", 20);
    }

    @Override
    public void draw(Graphics g) {
        context.draw();

        int[] mp, mps;

        mps = garnet.getInput().getMouse().getPositionScaled(scale);
        mp = garnet.getInput().getMouse().getPosition();
        garnet.getDebugDrawer().setUserString("Mouse pos:       ", mp[0] + "," + mp[1]);
        garnet.getDebugDrawer().setUserString("Mouse pos scaled:", mps[0] + "," + mps[1]);
        garnet.getDebugDrawer().setUserString("collision checks:", String.valueOf(collisionSystem.getTestsPerFrame()));

        g.setColor(0xff444471);
        if (nearestObjects != null) {
            for (RelativeObject nearestObject : nearestObjects) {
                GameObject gameObject = nearestObject.otherObject.collisionGetGameObject();
                g.drawLine((float) mp[0], (float) mp[1], (float) gameObject.getTransform().x, (float) gameObject.getTransform().y);
            }
        }


        g.setZoom(scale);

    }

}
