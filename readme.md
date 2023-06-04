# Garnet

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.github.nickd3000/garnet/badge.svg)](https://maven-badges.herokuapp.com/maven-central/io.github.nickd3000/garnet)
![GitHub](https://img.shields.io/github/license/nickd3000/garnet)

A 2D game engine for Java, built on LWJGL.

### Main features

- Timed Game Loop
- 2D Sprite and sprite sheets
- Primitive drawing - circle, rectangle, lines
- Texture loading
- Input handling
- Font drawing
- Tile map support
- Sound player

### Maven Dependency

``` xml
<dependency>
    <groupId>io.github.nickd3000</groupId>
    <artifactId>garnet</artifactId>
    <version>0.2.1</version>
</dependency>
```

### Example project

See the [garnet-examples](https://github.com/nickd3000/garnetexamples) project for examples.

### Minimal example with sprites

``` java
package com.physmo.garnetexamples.graphics;

import com.physmo.garnet.Garnet;
import com.physmo.garnet.GarnetApp;
import com.physmo.garnet.Texture;
import com.physmo.garnet.drawablebatch.TileSheet;
import com.physmo.garnet.graphics.Graphics;

// NOTE: on MacOS we need to add a vm argument: -XstartOnFirstThread
public class SimpleSpriteExample extends GarnetApp {

    TileSheet tileSheet;
    Texture texture;
    double xPos = 0;
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
        texture = Texture.loadTexture("space.png");
        tileSheet = new TileSheet(texture, 16, 16);
        Graphics graphics = garnet.getGraphics();
        graphics.addTexture(texture);
    }

    @Override
    public void tick(double delta) {
        xPos += delta * 50;
        if (xPos > 80) xPos = -16;
    }

    @Override
    public void draw(Graphics g) {
        g.setScale(scale);

        int[] mousePosition = garnet.getInput().getMousePositionScaled(scale);

        g.setColor(0xaaff22ff);
        g.drawImage(tileSheet, (int) xPos, 5, 2, 2);

        g.setColor(0xaa22ffff);
        g.drawImage(tileSheet, mousePosition[0], mousePosition[1], 2, 2);
    }
}


```

