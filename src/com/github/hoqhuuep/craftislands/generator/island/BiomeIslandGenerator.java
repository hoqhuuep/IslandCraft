package com.github.hoqhuuep.craftislands.generator.island;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Random;

import com.github.hoqhuuep.craftislands.generator.Biome;

public class BiomeIslandGenerator implements IslandGenerator {

    private final int size = 256;
    private final Color water = Biome.OCEAN;
    private final Color biome;

    public BiomeIslandGenerator(final Color biome) {
        this.biome = biome;
    }

    @Override
    public BufferedImage generate(final long seed) {
        final Random random = new Random(seed);

        final BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);

        final Graphics graphics = image.getGraphics();

        // Fill with water
        graphics.setColor(water);
        graphics.fillRect(0, 0, size, size);

        graphics.setColor(biome);

        final int center = size / 2;

        for (int walk = 0; walk < 6; ++walk) {
            int x = center;
            int y = center;

            for (int step = 0; step < 3200; ++step) {
                if (x - 12 <= 0 || y - 12 <= 0 || x + 12 >= size || y + 12 >= size) {
                    // Reached edge, jump back to center
                    x = center;
                    y = center;
                }

                // Biome
                graphics.fillOval(x - 13, y - 13, 25, 25);

                // Step randomly
                x += random.nextInt(5) - 2;
                y += random.nextInt(5) - 2;
            }
        }

        // Clean up
        graphics.dispose();

        return image;
    }

}
