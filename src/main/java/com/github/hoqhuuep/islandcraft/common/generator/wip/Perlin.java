package com.github.hoqhuuep.islandcraft.common.generator.wip;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.Random;

public class Perlin {
    public static BufferedImage generate(final int xSize, final int zSize, final Random random) {
        final BufferedImage noise7 = scaledNoise(xSize, zSize, 7, 7, new Random(random.nextLong()));
        final BufferedImage noise11 = scaledNoise(xSize, zSize, 11, 11, new Random(random.nextLong()));
        final BufferedImage noise42 = scaledNoise(xSize, zSize, 42, 42, new Random(random.nextLong()));
        final BufferedImage perlin = new BufferedImage(xSize, zSize, BufferedImage.TYPE_INT_ARGB);
        for (int z = 0; z < zSize; ++z) {
            for (int x = 0; x < zSize; ++x) {
                int c = noise7.getRGB(x, z) & 0xFF;
                c += noise11.getRGB(x, z) & 0xFF;
                c += noise42.getRGB(x, z) & 0xFF;
                c /= 3;
                perlin.setRGB(x, z, (0xFF << 24) | c);
            }
        }
        return perlin;
    }

    public static BufferedImage scaledNoise(final int xSize, final int zSize, final int xWave, final int zWave, final Random random) {
        final BufferedImage noise = new BufferedImage(xWave, zWave, BufferedImage.TYPE_INT_ARGB);
        for (int z = 0; z < zWave; ++z) {
            for (int x = 0; x < xWave; ++x) {
                int c = random.nextInt(256);
                noise.setRGB(x, z, (0xFF << 24) | c);
            }
        }
        final BufferedImage scaledNoise = new BufferedImage(xSize, zSize, BufferedImage.TYPE_INT_ARGB);
        final Graphics2D g = scaledNoise.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g.drawImage(noise, 0, 0, xSize, zSize, null);
        return scaledNoise;
    }
}
