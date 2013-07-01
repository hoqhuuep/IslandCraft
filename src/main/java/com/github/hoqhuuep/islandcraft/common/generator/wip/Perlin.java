package com.github.hoqhuuep.islandcraft.common.generator.wip;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.Random;

public final class Perlin {
    public static BufferedImage generate(final int size, final Random random) {
        final BufferedImage noise7 = scaledNoise(size, 7, 7, new Random(random.nextLong()));
        final BufferedImage noise11 = scaledNoise(size, 11, 11, new Random(random.nextLong()));
        final BufferedImage noise42 = scaledNoise(size, 42, 42, new Random(random.nextLong()));
        final BufferedImage perlin = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        for (int z = 0; z < size; ++z) {
            for (int x = 0; x < size; ++x) {
                int value = noise7.getRGB(x, z) & 0xFF;
                value += noise11.getRGB(x, z) & 0xFF;
                value += noise42.getRGB(x, z) & 0xFF;
                value /= 3;
                perlin.setRGB(x, z, (0xFF << 24) | value);
            }
        }
        return perlin;
    }

    public static BufferedImage scaledNoise(final int size, final int xWave, final int zWave, final Random random) {
        final BufferedImage noise = new BufferedImage(xWave, zWave, BufferedImage.TYPE_INT_ARGB);
        for (int z = 0; z < zWave; ++z) {
            for (int x = 0; x < xWave; ++x) {
                int value = random.nextInt(256);
                noise.setRGB(x, z, (0xFF << 24) | value);
            }
        }
        final BufferedImage scaledNoise = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        final Graphics2D graphics = scaledNoise.createGraphics();
        graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        graphics.drawImage(noise, 0, 0, size, size, null);
        return scaledNoise;
    }

    private Perlin() {
        // Utility class
    }
}
