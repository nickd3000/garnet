# Garnet

A 2D LWJGL based game engine for Java.

Featuring

- Timed Game Loop
- 2D Sprite and primitive drawing
- Input handling
- Font drawing
- Tile map support

### Minimal example with sprites

    package com.physmo.garnetexamples.graphics;
    
    import com.physmo.garnet.Garnet;
    import com.physmo.garnet.GarnetApp;
    import com.physmo.garnet.Texture;
    import com.physmo.garnet.Utils;
    import com.physmo.garnet.drawablebatch.TileSheet;
    import com.physmo.garnet.graphics.Graphics;
    
    // NOTE: on MacOS we need to add a vm argument: -XstartOnFirstThread
    public class SimpleSpriteExample extends GarnetApp {

        TileSheet tileSheet;
        Texture texture;
        Graphics graphics;
        double x = 0;
        double scale = 4;
    
        public SimpleSpriteExample(Garnet garnet, String name) {
            super(garnet, name);
        }
    
        public static void main(String[] args) {
            Garnet garnet = new Garnet(400, 400);
            GarnetApp app = new SimpleSpriteExample(garnet, "");
    
            garnet.setApp(app);
    
            garnet.init();
            garnet.run();
        }
    
        @Override
        public void init(Garnet garnet) {
            texture = Texture.loadTexture(Utils.getPathForResource(this, "space.png"));
            tileSheet = new TileSheet(texture, 16, 16);
            graphics = garnet.getGraphics();
            graphics.addTexture(texture);
        }
    
        @Override
        public void tick(double delta) {
            x += delta * 50;
            if (x > 80) x = -16;
        }

        @Override
        public void draw() {
            int[] mousePosition = garnet.getInput().getMousePositionScaled(scale);
            
            graphics.setColor(0xaaff22ff);
            graphics.setScale(scale);
            graphics.drawImage(tileSheet, (int) x, 5, 2, 2);
    
            graphics.setColor(0xaa22ffff);
            graphics.drawImage(tileSheet, mousePosition[0], mousePosition[1], 2, 2);
            graphics.render();
        }
    }


