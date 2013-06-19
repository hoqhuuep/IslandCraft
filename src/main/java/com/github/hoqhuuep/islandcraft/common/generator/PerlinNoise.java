package com.github.hoqhuuep.islandcraft.common.generator;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.imageio.ImageIO;

public final class PerlinNoise {
    private static Map<Long, BufferedImage> cache = new HashMap<Long, BufferedImage>();

    private static BufferedImage circleCache;

    private static BufferedImage circle() {
        if (circleCache == null) {
            circleCache = new BufferedImage(64, 64, BufferedImage.TYPE_4BYTE_ABGR_PRE);
            for (int z = 0; z < 64; ++z) {
                for (int x = 0; x < 64; ++x) {
                    int dx = 32 - x;
                    int dz = 32 - z;
                    int d = Math.max(0x70 - ((dx * dx * dx * dx + dz * dz * dz * dz) * 0x70 / (32 * 32 * 32 * 16)), 0);
                    circleCache.setRGB(x, z, 0xFFFFFF | d << 24);
                }
            }
        }
        return circleCache;
    }

    public static BufferedImage island(final long seed) {
        BufferedImage res = cache.get(new Long(seed));
        if (res == null) {
            res = perlinNoise(new Random(seed));
            cache.put(new Long(seed), res);
        }
        return res;
    }

    public static void main(final String[] args) {
        BufferedImage result2 = perlinNoise(new Random());
        try {
            ImageIO.write(result2, "png", new File("test.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static BufferedImage perlinNoise(final Random random) {
        BufferedImage result = new BufferedImage(256, 256, BufferedImage.TYPE_4BYTE_ABGR_PRE);
        Graphics2D g = result.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g.setBackground(new Color(0xFF000000));
        g.clearRect(0, 0, 256, 256);
        g.drawImage(whiteNoise(8, 8, random), 0, 0, 256, 256, null);
        g.drawImage(whiteNoise(12, 12, random), 0, 0, 256, 256, null);
        g.drawImage(whiteNoise(16, 16, random), 0, 0, 256, 256, null);
        g.drawImage(circle(), 0, 0, 256, 256, null);
        g.dispose();
        BufferedImage result2 = new BufferedImage(256, 256, BufferedImage.TYPE_BYTE_BINARY);
        Graphics2D g2 = result2.createGraphics();
        g2.drawImage(result, 0, 0, 256, 256, null);
        g2.dispose();
        return result2;
    }

    private static BufferedImage whiteNoise(final int width, final int height, final Random random) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR_PRE);
        for (int z = 0; z < width; ++z) {
            for (int x = 0; x < height; ++x) {
                if (random.nextBoolean()) {
                    image.setRGB(x, z, 0x40FFFFFF);
                } else {
                    image.setRGB(x, z, 0x00000000);
                }
            }
        }
        return image;
    }

    private PerlinNoise() {
        // Utility class
    }
}
