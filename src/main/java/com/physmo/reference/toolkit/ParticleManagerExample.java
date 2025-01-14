package com.physmo.reference.toolkit;

import com.physmo.garnet.Garnet;
import com.physmo.garnet.GarnetApp;
import com.physmo.garnet.graphics.Graphics;
import com.physmo.garnet.graphics.Texture;
import com.physmo.garnet.graphics.TileSheet;
import com.physmo.garnet.structure.Vector3;
import com.physmo.garnet.toolkit.color.ColorSupplierLinear;
import com.physmo.garnet.toolkit.particle.Emitter;
import com.physmo.garnet.toolkit.particle.ParticleManager;
import com.physmo.garnet.toolkit.particle.ParticleTemplate;

public class ParticleManagerExample extends GarnetApp {

    private static final String fileName1 = "space.PNG";
    Texture texture;
    TileSheet tileSheet;
    ParticleManager particleManager;

    public ParticleManagerExample(Garnet garnet, String name) {
        super(garnet, name);
    }

    public static void main(String[] args) {
        Garnet garnet = new Garnet(640, 480);
        GarnetApp app = new ParticleManagerExample(garnet, "");

        garnet.setApp(app);

        garnet.init();
        garnet.run();
    }

    @Override
    public void init(Garnet garnet) {
        Graphics g = garnet.getGraphics();

        // Load the texture resource and create a tilesheet to access it.
        texture = Texture.loadTexture(fileName1);
        tileSheet = new TileSheet(texture, 16, 16);
        g.addTexture(texture);

        // Create the particle manager and set a default render function.
        particleManager = new ParticleManager(1000);

        particleManager.setParticleDrawer(p -> {
            float pAge = (float) (p.age / p.lifeTime);
            g.setColor(p.colorSupplier.getColor(pAge));
            g.drawImage(tileSheet, (int) (p.position.x) - 8, (int) (p.position.y) - 8, 4, 0);
        });


        ParticleTemplate pt1 = new ParticleTemplate();
        pt1.setLifeTime(1, 2);
        pt1.setColorSupplier(new ColorSupplierLinear(new int[]{0xff00ffff, 0x00ffffff}));

        ParticleTemplate pt2 = new ParticleTemplate();
        pt2.setLifeTime(1, 2);
        pt2.setColorSupplier(new ColorSupplierLinear(new int[]{0xff0000ff, 0x00ff00ff}));


        // Add a custom drawer to this particle type.
        pt2.setParticleDrawer(p -> {
            g.setColor(p.colorSupplier.getColor(p.age / p.lifeTime));
            g.drawImage(tileSheet, p.position.x, p.position.y, 3, 0);
        });

        Emitter e1 = new Emitter(new Vector3(100, 100, 0), 5, pt1);
        e1.setEmitPerSecond(150);
        Emitter e2 = new Emitter(new Vector3(200, 100, 0), 5, pt2);
        e2.setEmitPerSecond(150);

        particleManager.addEmitter(e1);
        particleManager.addEmitter(e2);
    }

    @Override
    public void tick(double delta) {
        particleManager.tick(delta);
    }

    @Override
    public void draw(Graphics g) {
        particleManager.draw(g);
    }
}
