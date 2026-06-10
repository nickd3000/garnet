package com.physmo.garnet.renderer;

import java.nio.ByteBuffer;

/**
 * CPU-side append-only shelf allocator for one atlas page.
 */
public class TextureAtlasPage {
    private final int textureId;
    private final AtlasPolicy policy;
    private final ByteBuffer rgbaPixels;
    private int cursorX;
    private int cursorY;
    private int shelfHeight;

    public TextureAtlasPage(int textureId, AtlasPolicy policy) {
        this.textureId = textureId;
        this.policy = policy;
        rgbaPixels = ByteBuffer.allocateDirect(policy.pageWidth() * policy.pageHeight() * 4);
        cursorX = policy.padding();
        cursorY = policy.padding();
        shelfHeight = 0;
    }

    public TextureRegion tryAllocate(int width, int height) {
        int paddedWidth = width + (policy.padding() * 2);
        int paddedHeight = height + (policy.padding() * 2);
        if (paddedWidth > policy.pageWidth() || paddedHeight > policy.pageHeight()) return null;

        if (cursorX + width + policy.padding() > policy.pageWidth()) {
            cursorX = policy.padding();
            cursorY += shelfHeight + policy.padding();
            shelfHeight = 0;
        }

        if (cursorY + height + policy.padding() > policy.pageHeight()) return null;

        TextureRegion region = new TextureRegion(textureId, cursorX, cursorY, width, height, policy.pageWidth(), policy.pageHeight(), true);
        cursorX += width + policy.padding();
        shelfHeight = Math.max(shelfHeight, height);
        return region;
    }

    public int getTextureId() {
        return textureId;
    }

    public void writeRegionPixels(TextureRegion region, ByteBuffer sourceRgbaPixels) {
        if (region.textureId() != textureId) {
            throw new IllegalArgumentException("Region belongs to texture " + region.textureId() + " but page texture is " + textureId);
        }
        int expectedSourceBytes = region.width() * region.height() * 4;
        if (sourceRgbaPixels.capacity() < expectedSourceBytes) {
            throw new IllegalArgumentException("Source RGBA buffer is smaller than region width * height * 4");
        }

        int padding = policy.padding();
        int minX = Math.max(0, region.x() - padding);
        int minY = Math.max(0, region.y() - padding);
        int maxX = Math.min(policy.pageWidth(), region.x() + region.width() + padding);
        int maxY = Math.min(policy.pageHeight(), region.y() + region.height() + padding);

        ByteBuffer source = sourceRgbaPixels.duplicate();
        // Fill both the region and its padding. Padding samples clamp to the
        // nearest source pixel, which prevents linear filtering from bleeding
        // neighbouring atlas regions into sprite edges.
        for (int y = minY; y < maxY; y++) {
            int sourceY = clamp(y - region.y(), 0, region.height() - 1);
            for (int x = minX; x < maxX; x++) {
                int sourceX = clamp(x - region.x(), 0, region.width() - 1);
                copyPixel(source, sourceX, sourceY, region.width(), rgbaPixels, x, y, policy.pageWidth());
            }
        }
    }

    private static int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }

    private static void copyPixel(ByteBuffer source, int sourceX, int sourceY, int sourceWidth, ByteBuffer destination, int destinationX, int destinationY, int destinationWidth) {
        int sourceIndex = ((sourceY * sourceWidth) + sourceX) * 4;
        int destinationIndex = ((destinationY * destinationWidth) + destinationX) * 4;
        destination.put(destinationIndex, source.get(sourceIndex));
        destination.put(destinationIndex + 1, source.get(sourceIndex + 1));
        destination.put(destinationIndex + 2, source.get(sourceIndex + 2));
        destination.put(destinationIndex + 3, source.get(sourceIndex + 3));
    }

    public ByteBuffer getRgbaPixelsCopy() {
        ByteBuffer copy = ByteBuffer.allocateDirect(rgbaPixels.capacity());
        ByteBuffer source = rgbaPixels.duplicate();
        source.position(0);
        copy.put(source);
        copy.position(0);
        return copy;
    }

    public AtlasPolicy getPolicy() {
        return policy;
    }
}
