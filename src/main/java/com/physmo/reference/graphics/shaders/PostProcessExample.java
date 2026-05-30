package com.physmo.reference.graphics.shaders;

import com.physmo.garnet.ColorUtils;
import com.physmo.garnet.Garnet;
import com.physmo.garnet.GarnetApp;
import com.physmo.garnet.graphics.Graphics;
import com.physmo.garnet.graphics.ShaderProgram;
import com.physmo.garnet.graphics.Texture;

import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glUniform2f;
import static org.lwjgl.opengl.GL20.glUseProgram;

// NOTE: On MacOS the following VM argument is required: -XstartOnFirstThread
//
// PostProcessExample demonstrates a post-processing effect using the automatic internal buffer.
//
// The scene is automatically rendered into an internal RenderTexture.
// In the final presentation pass, the haloShader is applied.
//
// The halo.frag shader has been updated to combine the original image with 
// a blurred/glowing version in a single pass.
//
// The texelSize uniform is uploaded each frame so the shader knows the pixel size
// of the render texture.
public class PostProcessExample extends GarnetApp {

    static final int WINDOW_W = 480;
    static final int WINDOW_H = 320;

    Texture texture;
    ShaderProgram haloShader;

    double time = 0;

    public PostProcessExample(Garnet garnet, String name) {
        super(garnet, name);
    }

    public static void main(String[] args) {
        Garnet garnet = new Garnet(WINDOW_W, WINDOW_H);
        garnet.setInternalBufferMode(true);
        GarnetApp app = new PostProcessExample(garnet, "");
        garnet.setApp(app);
        garnet.init();
        garnet.run();
    }

    @Override
    public void init(Garnet garnet) {
        garnet.getDisplay().setWindowTitle("Post-Process Halo Example");
        garnet.getGraphics().setBackgroundColor(ColorUtils.BLACK);

        texture = Texture.loadTexture("garnetCrystal.png");
        garnet.getGraphics().addTexture(texture);

        haloShader = ShaderProgram.fromFiles("shaders/passthrough.vert", "shaders/halo.frag");
        garnet.setInternalBufferShader(haloShader);
    }

    @Override
    public void tick(double delta) {
        time += delta;
    }

    @Override
    public void draw(Graphics g) {

        int cx = WINDOW_W / 2;
        int cy = WINDOW_H / 2;
        int orbitR = 100;
        int numSprites = 6;
        for (int i = 0; i < numSprites; i++) {
            double angle = time * 0.8 + i * (Math.PI * 2.0 / numSprites);
            int sx = (int) (cx + Math.cos(angle) * orbitR) - texture.getWidth() / 2;
            int sy = (int) (cy + Math.sin(angle) * orbitR) - texture.getHeight() / 2;
            g.setColor(ColorUtils.WHITE);
            g.setDrawOrder(0);
            g.drawImage(texture, sx, sy);
        }

        // Upload texelSize uniform for the final pass
        haloShader.bind();
        int loc = glGetUniformLocation(haloShader.getProgramId(), "texelSize");
        glUniform2f(loc, 1.0f / WINDOW_W, 1.0f / WINDOW_H);
        glUseProgram(0);
    }
}
