package com.github.hoqhuuep.craftislands.generator.island;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Random;

import com.github.hoqhuuep.craftislands.generator.Biome;

public class ShoreIslandGenerator implements IslandGenerator {

    private final int size = 256;
    private final Color water = Biome.OCEAN;
    private final Color biome;
    private final Color shore;

    public ShoreIslandGenerator(final Color biome, final Color shore) {
        this.biome = biome;
        this.shore = shore;
    }

    @Override
    public BufferedImage generate(final long seed) {
        final Random random = new Random(seed);

        final BufferedImage backgroundImage = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        final BufferedImage foregroundImage = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);

        final Graphics backgroundGraphics = backgroundImage.getGraphics();
        final Graphics foregroundGraphics = foregroundImage.getGraphics();

        // Fill with water
        backgroundGraphics.setColor(water);
        backgroundGraphics.fillRect(0, 0, size, size);

        backgroundGraphics.setColor(shore);
        foregroundGraphics.setColor(biome);

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

                // Shore
                backgroundGraphics.fillOval(x - 13, y - 13, 25, 25);

                // Biome
                foregroundGraphics.fillOval(x - 5, y - 5, 9, 9);

                // Step randomly
                x += random.nextInt(5) - 2;
                y += random.nextInt(5) - 2;
            }
        }

        // Clean up
        foregroundGraphics.dispose();

        // Compose layers
        backgroundGraphics.drawImage(foregroundImage, 0, 0, null);

        // Clean up
        backgroundGraphics.dispose();

        return backgroundImage;
    }

}
