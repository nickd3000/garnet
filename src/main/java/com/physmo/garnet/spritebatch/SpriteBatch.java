package com.physmo.garnet.spritebatch;

import com.physmo.garnet.Texture;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;

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

    public void render() {
        glEnable(GL_TEXTURE_2D);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        for (BatchElement be : elements) {
            be.render();
        }
    }

    public void clear() {
        elements.clear();
    }
}
