package com.github.hoqhuuep.craftislands.generator;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class ImageCache {

    private Map<Long, BufferedImage> cache = new HashMap<Long, BufferedImage>();

    public BufferedImage get(final long seed) {
        return cache.get(seed);
    }

    public void put(final long seed, final BufferedImage image) {
        cache.put(seed, image);
    }

}
