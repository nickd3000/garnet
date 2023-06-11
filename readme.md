# Garnet

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.github.nickd3000/garnet/badge.svg)](https://maven-badges.herokuapp.com/maven-central/io.github.nickd3000/garnet)
![GitHub](https://img.shields.io/github/license/nickd3000/garnet)

A cross-platform 2D game engine for Java, built on LWJGL.

Garnet is still in development but the main functionality is all working.
Feedback is really useful at this stage, so please feel free to create a small project and let me know how you get on.

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
    <version>0.2.2</version>
</dependency>
```

### Example project

See the [garnet-examples](https://github.com/nickd3000/garnetexamples) project for examples.

### Toolkit

A [companion toolkit](https://github.com/nickd3000/garnettoolkit) is also available with lots of useful game features,
like an entity-component system, tilemap drawer, collision system and much more.

### Minimal example

This small example loads and draws a texture to the display.

``` java
import com.physmo.garnet.Garnet;
import com.physmo.garnet.GarnetApp;
import com.physmo.garnet.graphics.Graphics;
import com.physmo.garnet.graphics.Texture;

// NOTE: on MacOS we need to add a vm argument: -XstartOnFirstThread
public class SimpleSpriteExample extends GarnetApp {

    Texture texture;

    public SimpleSpriteExample(Garnet garnet, String name) {
        super(garnet, name);
    }

    public static void main(String[] args) {
        Garnet garnet = new Garnet(600, 400);
        GarnetApp app = new SimpleSpriteExample(garnet, "");
        garnet.setApp(app);
        garnet.init();
        garnet.run();
    }

    @Override
    public void init(Garnet garnet) {
        texture = Texture.loadTexture("garnetCrystal.png");
        garnet.getGraphics().addTexture(texture);
    }

    @Override
    public void tick(double delta) {
    }

    @Override
    public void draw(Graphics g) {
        int[] mousePosition = garnet.getInput().getMousePosition();
        g.drawImage(texture, mousePosition[0], mousePosition[1]);
    }
}

```

