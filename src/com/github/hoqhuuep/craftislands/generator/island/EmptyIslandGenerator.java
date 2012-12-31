package com.github.hoqhuuep.craftislands.generator.island;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;


public class EmptyIslandGenerator implements IslandGenerator {

    private final int size = 256;
    private final Color biome;

    public EmptyIslandGenerator(final Color biome) {
        this.biome = biome;
    }

    @Override
    public BufferedImage generate(final long seed) {
        final BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        final Graphics graphics = image.getGraphics();
        graphics.setColor(biome);
        graphics.fillRect(0, 0, image.getWidth(), image.getHeight());
        graphics.dispose();
        return image;
    }

}
