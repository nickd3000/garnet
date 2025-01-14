package com.physmo.reference.graphics;

import com.physmo.garnet.ColorUtils;
import com.physmo.garnet.Garnet;
import com.physmo.garnet.GarnetApp;
import com.physmo.garnet.graphics.Animation;
import com.physmo.garnet.graphics.Graphics;
import com.physmo.garnet.graphics.SubImage;
import com.physmo.garnet.graphics.Texture;

// NOTE: On MacOS the following VM argument is required: -XstartOnFirstThread
public class AnimationExample extends GarnetApp {

    private static final String fileName1 = "space.PNG";

    Animation animation1;
    Animation animation2;
    Animation animation3;
    SubImage subImage1 = new SubImage();
    SubImage subImage2 = new SubImage();
    SubImage subImage3 = new SubImage();
    private Texture texture1;


    public AnimationExample(Garnet garnet, String name) {
        super(garnet, name);
    }

    public static void main(String[] args) {
        Garnet garnet = new Garnet(640, 480);
        GarnetApp app = new AnimationExample(garnet, "");

        garnet.setApp(app);

        garnet.init();
        garnet.run();
    }

    @Override
    public void init(Garnet garnet) {

        texture1 = Texture.loadTexture(fileName1);

        garnet.getGraphics().addTexture(texture1);

        // Create an animation and add each frame individually.
        animation1 = new Animation(texture1, 16, 16, 3);
        animation1.addFrame(2, 0);
        animation1.addFrame(3, 0);
        animation1.addFrame(4, 0);
        animation1.addFrame(5, 0);
        animation1.addFrame(6, 0);

        // Create an animation using 2D array to specify the image locations.
        animation2 = new Animation(texture1, 16, 16, 1);
        int[][] frames = new int[][]{{3, 0}, {4, 0}, {5, 0}, {6, 0}};
        animation2.addFrames(frames);


        animation3 = new Animation(texture1, 16, 16, 0.5);
        animation3.addFrame(2, 0);
        animation3.addFrame(3, 0);
        animation3.addFrame(4, 0);
        animation3.addFrame(5, 0);
        animation3.addFrame(6, 0);


    }

    @Override
    public void tick(double delta) {
        animation1.tick(delta);
        animation2.tick(delta);
        animation3.tick(delta);
    }

    @Override
    public void draw(Graphics g) {
        Graphics graphics = garnet.getGraphics();

        graphics.setColor(ColorUtils.WHITE);

        // Draw unscaled sprites using sprite sheet
        graphics.setZoom(3);

        animation1.getImage(subImage1);
        animation2.getImage(subImage2);
        animation3.getImage(subImage3);

        graphics.drawImage(subImage1, 0, 10);
        graphics.drawImage(subImage2, 0, 10 + 30);
        graphics.drawImage(subImage3, 0, 10 + 60);
    }


}
