package com.physmo.garnet.spritebatch;

import com.physmo.garnet.Texture;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glEnable;

public class SpriteBatch {
    List<BatchElement> elements;
    Texture texture;
    float textureWidth;
    float textureHeight;

    public SpriteBatch(Texture texture) {
        this.texture = texture;
        elements = new ArrayList<>();
        textureWidth = texture.getWidth();
        textureHeight = texture.getHeight();
    }

    public void add(BatchElement batchElement) {
        elements.add(batchElement);
    }

    public int size() {
        return elements.size();
    }

    public void render() {
        texture.bind();
        glEnable(GL_TEXTURE_2D);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        float textureScale = 1.0f / texture.getWidth();

        for (BatchElement be : elements) {
            be.render(textureScale);
        }
    }

    public void clear() {
        elements.clear();
    }
}
