package com.physmo.reference.graphics.shaders;

import com.physmo.garnet.ColorUtils;
import com.physmo.garnet.Garnet;
import com.physmo.garnet.GarnetApp;
import com.physmo.garnet.drawablebatch.BlendMode;
import com.physmo.garnet.graphics.Graphics;
import com.physmo.garnet.graphics.RenderTexture;
import com.physmo.garnet.graphics.ShaderProgram;
import com.physmo.garnet.graphics.Texture;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glUniform2f;
import static org.lwjgl.opengl.GL20.glUseProgram;

// NOTE: On MacOS the following VM argument is required: -XstartOnFirstThread
//
// PostProcessExample demonstrates a two-pass post-processing effect using an FBO.
//
// Pass 1 — scene render:
//   Several sprites are drawn into a RenderTexture (off-screen FBO) instead of
//   directly to the screen.
//
// Pass 2 — halo/blur overlay:
//   The FBO colour texture is drawn to the screen normally (draw order 0).
//   It is then drawn a second time with the halo.frag shader and ADDITIVE blending
//   (draw order 1), which blurs and brightens the image to create a light-halo glow
//   around every sprite.
//
// The texelSize uniform is uploaded each frame so the shader knows the pixel size
// of the render texture.
public class PostProcessExample extends GarnetApp {

    static final int WINDOW_W = 480;
    static final int WINDOW_H = 320;

    Texture texture;
    RenderTexture renderTexture;
    ShaderProgram haloShader;

    double time = 0;

    public PostProcessExample(Garnet garnet, String name) {
        super(garnet, name);
    }

    public static void main(String[] args) {
        Garnet garnet = new Garnet(WINDOW_W, WINDOW_H);
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

        renderTexture = new RenderTexture(WINDOW_W, WINDOW_H);
        garnet.getGraphics().addTexture(renderTexture.getTexture());

        haloShader = ShaderProgram.fromFiles("shaders/passthrough.vert", "shaders/halo.frag");
    }

    @Override
    public void tick(double delta) {
        time += delta;
    }

    @Override
    public void draw(Graphics g) {

        // ── Pass 1: render sprites into the off-screen FBO ──────────────────
        renderTexture.bind();
        glClearColor(0, 0, 0, 0);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

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

        // Flush the batch into the FBO
        g.render();
        renderTexture.unbind(garnet.getDisplay());

        // ── Pass 2: composite onto the screen ───────────────────────────────

        // Upload texelSize uniform before the batch is submitted
        haloShader.bind();
        int loc = glGetUniformLocation(haloShader.getProgramId(), "texelSize");
        glUniform2f(loc, 1.0f / WINDOW_W, 1.0f / WINDOW_H);
        glUseProgram(0);

        Texture rt = renderTexture.getTexture();

        // Layer 0: normal copy of the scene
        g.setColor(ColorUtils.WHITE);
        g.setDrawOrder(0);
        g.drawImage(rt, 0, 0);

        // Layer 1: blurred/glowing copy drawn additively on top
        g.setColor(ColorUtils.WHITE);
        g.setDrawOrder(1);
        g.drawImage(rt, 0, 0)
                .setShader(haloShader)
                .setBlendMode(BlendMode.ADDITIVE);
    }
}
