package com.github.hoqhuuep.islandcraft.common.generator.wip;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.Random;

public class Perlin {
    public static BufferedImage generate(int width, int height, long seed) {
        Random random = new Random(seed);
        BufferedImage noise7 = noise(7, 7, random.nextLong());
        BufferedImage noise11 = noise(11, 11, random.nextLong());
        BufferedImage noise42 = noise(42, 42, random.nextLong());
        BufferedImage n1 = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        BufferedImage n2 = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        BufferedImage n3 = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g1 = n1.createGraphics();
        Graphics2D g2 = n2.createGraphics();
        Graphics2D g3 = n3.createGraphics();
        g1.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g3.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g1.drawImage(noise7, 0, 0, width, height, null);
        g2.drawImage(noise11, 0, 0, width, height, null);
        g3.drawImage(noise42, 0, 0, width, height, null);

        BufferedImage n = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = n.createGraphics();
        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < height; ++x) {
                int c = n1.getRGB(x, y) & 0xFF;
                c += n2.getRGB(x, y) & 0xFF;
                c += n3.getRGB(x, y) & 0xFF;
                c /= 3;
                g.setColor(new Color(c, c, c));
                g.fillRect(x, y, 1, 1);
            }
        }
        return n;
    }

    public static BufferedImage noise(int width, int height, long seed) {
        Random random = new Random(seed);
        BufferedImage noise = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = noise.createGraphics();
        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < height; ++x) {
                int c = random.nextInt(256);
                g.setColor(new Color(c, c, c));
                g.fillRect(x, y, 1, 1);
            }
        }
        return noise;
    }
}
