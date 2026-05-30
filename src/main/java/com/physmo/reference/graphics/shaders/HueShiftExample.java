package com.physmo.reference.graphics.shaders;

import com.physmo.garnet.ColorUtils;
import com.physmo.garnet.Garnet;
import com.physmo.garnet.GarnetApp;
import com.physmo.garnet.graphics.Graphics;
import com.physmo.garnet.graphics.ShaderProgram;
import com.physmo.garnet.graphics.Texture;

import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glUniform1f;
import static org.lwjgl.opengl.GL20.glUseProgram;

// NOTE: On MacOS the following VM argument is required: -XstartOnFirstThread
//
// HueShiftExample demonstrates the hueshift.frag shader, which rotates the
// hue of every pixel by a given angle while leaving saturation and brightness
// unchanged.
//
// The first five sprites are shown side-by-side with fixed hue rotations:
//   0° (original), 60°, 120°, 180°, 240°
//
// A sixth sprite (rightmost) animates its hue continuously, cycling through
// all colours over time.  This shows how to pass a per-frame uniform value.
//
// This is useful for recolouring sprites at runtime without needing separate
// art assets — e.g. team colours, palette cycling, or hit-flash tinting.
//
// WHY FIVE SHADER INSTANCES FOR THE STATIC SPRITES?
// Each ShaderProgram stores its own GPU uniform state.  Garnet batches all
// draw calls and flushes them at end-of-frame, so any glUniform call made
// before a draw() is overwritten by later calls before rendering occurs.
// The fix is one ShaderProgram per distinct uniform value, with the value
// baked in at init() time (see OutlineExample for a full explanation).
//
// WHY DOES THE ANIMATED SPRITE WORK WITH A SINGLE SHADER INSTANCE?
// The animated sprite is the only one using animatedHueShader, so there is
// no conflict.  The uniform is updated every tick() — the GPU reads the
// value when the batch flushes at frame-end, by which point the latest
// value is in effect.  This pattern works whenever only one sprite uses
// a given ShaderProgram instance.
public class HueShiftExample extends GarnetApp {

    static final int WINDOW_W = 720;
    static final int WINDOW_H = 200;

    Texture texture;
    ShaderProgram[] hueShaders;
    ShaderProgram animatedHueShader;
    float[] hueAngles = {0f, 60f, 120f, 180f, 240f};
    double time = 0;

    public HueShiftExample(Garnet garnet, String name) {
        super(garnet, name);
    }

    public static void main(String[] args) {
        Garnet garnet = new Garnet(WINDOW_W, WINDOW_H);
        GarnetApp app = new HueShiftExample(garnet, "");
        garnet.setApp(app);
        garnet.init();
        garnet.run();
    }

    @Override
    public void init(Garnet garnet) {
        garnet.getDisplay().setWindowTitle("Hue Shift Shader Example");
        garnet.getGraphics().setBackgroundColor(ColorUtils.DARK_GREY);

        texture = Texture.loadTexture("garnetCrystal.png");
        garnet.getGraphics().addTexture(texture);

        hueShaders = new ShaderProgram[hueAngles.length];
        for (int i = 0; i < hueAngles.length; i++) {
            hueShaders[i] = ShaderProgram.fromFiles("shaders/passthrough.vert", "shaders/hueshift.frag");
            hueShaders[i].bind();
            glUniform1f(glGetUniformLocation(hueShaders[i].getProgramId(), "hue"), hueAngles[i]);
            glUseProgram(0);
        }

        // Animated shader: a single instance whose 'hue' uniform is updated every tick()
        animatedHueShader = ShaderProgram.fromFiles("shaders/passthrough.vert", "shaders/hueshift.frag");
    }

    @Override
    public void tick(double delta) {
        time += delta;
        // Cycle hue through 360° over 4 seconds
        float animatedHue = (float) ((time * 90.0) % 360.0);
        animatedHueShader.bind();
        glUniform1f(glGetUniformLocation(animatedHueShader.getProgramId(), "hue"), animatedHue);
        glUseProgram(0);
    }

    @Override
    public void draw(Graphics g) {
        int tw = texture.getWidth();
        int th = texture.getHeight();
        int startY = (WINDOW_H - th) / 2;
        int spacing = 110;
        int totalSprites = hueAngles.length + 1; // 5 fixed + 1 animated
        int startX = (WINDOW_W - spacing * (totalSprites - 1) - tw) / 2;

        for (int i = 0; i < hueAngles.length; i++) {
            g.setColor(ColorUtils.WHITE);
            g.drawImage(texture, startX + i * spacing, startY).setShader(hueShaders[i]);
        }

        // Sixth sprite: animated hue updated every frame via tick()
        g.setColor(ColorUtils.WHITE);
        g.drawImage(texture, startX + hueAngles.length * spacing, startY).setShader(animatedHueShader);
    }
}
