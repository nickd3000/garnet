package com.physmo.garnet.renderer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.IntSupplier;

/**
 * CPU-side atlas region allocator. Production callers supply real GL texture ids for new pages.
 */
public class AtlasManager {
    private final AtlasPolicy policy;
    private final List<TextureAtlasPage> pages = new ArrayList<>();
    private final IntSupplier pageTextureIdSupplier;

    public AtlasManager(AtlasPolicy policy, int firstPageTextureId) {
        this.policy = policy;
        int[] nextPageTextureId = {firstPageTextureId};
        this.pageTextureIdSupplier = () -> nextPageTextureId[0]++;
    }

    public AtlasManager(AtlasPolicy policy, IntSupplier pageTextureIdSupplier) {
        if (pageTextureIdSupplier == null) throw new IllegalArgumentException("pageTextureIdSupplier must not be null");
        this.policy = policy;
        this.pageTextureIdSupplier = pageTextureIdSupplier;
    }

    public TextureRegion allocate(int width, int height) {
        for (TextureAtlasPage page : pages) {
            TextureRegion region = page.tryAllocate(width, height);
            if (region != null) return region;
        }

        TextureAtlasPage page = new TextureAtlasPage(pageTextureIdSupplier.getAsInt(), policy);
        pages.add(page);
        TextureRegion region = page.tryAllocate(width, height);
        if (region == null) {
            throw new IllegalArgumentException("Region " + width + "x" + height + " exceeds atlas page " + policy.pageWidth() + "x" + policy.pageHeight());
        }
        return region;
    }

    public TextureAtlasPage getPage(int textureId) {
        for (TextureAtlasPage page : pages) {
            if (page.getTextureId() == textureId) return page;
        }
        return null;
    }

    public TextureRegion rawRegion(int textureId, int width, int height) {
        return TextureRegion.raw(textureId, width, height);
    }

    public List<TextureAtlasPage> getPages() {
        return Collections.unmodifiableList(pages);
    }

    public AtlasPolicy getPolicy() {
        return policy;
    }
}
