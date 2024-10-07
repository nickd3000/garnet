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
- Game ToolKit

### Maven Dependency

``` xml
<dependency>
    <groupId>io.github.nickd3000</groupId>
    <artifactId>garnet</artifactId>
    <version>0.5.0</version>
</dependency>
```

### Example project

See the [garnet-examples](https://github.com/nickd3000/garnetexamples) project for examples.

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

### Changelist

#### Version 0.5.0 - October 2024

- Moved to Java 17
- Added Surefire Plugin
- Added Spock tests
- Improved documentation
- Brought Toolkit project into Garnet project
- Added getAngle method to Vector3 class
- Added Inline Texture creator

#### Version 0.4.0 - May 2024

- Split input system into sub systems for mouse and keyboard
- Allow Raw access to keyboard
- Document input systems better
- Fix TileGridDrawer not drawing tiles past the right and bottom edges

#### Version 0.3.0 - June 2023

- Camera support has been added to the graphics system
- Cameras support panning, zooming and clipping
- TileGridDrawer updated to use new camera system
- API add: rgbToFloat method that is passed output list to reduce new objects creation.
- API add: Font scaling

#### Version 0.2.2 - June 2023

- API add: Added Animation class
- API add: Added normalized (0..1) mouse coordinate method to Input
- API add: Added SubImage class which describes a sub area of a texture
- API add: Added SubImage supplier to TileSheet class
- API add: Added volume controls for sound playback
- API add: Allow window title to be changed - Display.setWindowTitle(String)
- Optimized sprite drawing - added Sprite2D object pool to reuse sprite objects.
- Package refactoring
- Rewrote paragraph drawer and fixed new line bugs.