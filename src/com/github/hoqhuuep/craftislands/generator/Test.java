package com.github.hoqhuuep.craftislands.generator;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.github.hoqhuuep.craftislands.generator.island.RandomIslandGenerator;

public class Test {
    public static void main(String[] args) {
        final int size = 640;
        WorldGenerator worldGenerator = new WorldGenerator(new ImageCache(), new ImageStore(), new RandomIslandGenerator());

        BufferedImage image = new BufferedImage(size * 2, size * 2, BufferedImage.TYPE_INT_ARGB);
        Graphics graphics = image.getGraphics();

        for (int z = -size; z < size; ++z) {
            for (int x = -size; x < size; ++x) {
                graphics.setColor(new Color(worldGenerator.getBiome(123456789L, x, z)));
                graphics.drawRect(x + size, z + size, 1, 1);
            }
        }

        try {
            ImageIO.write(image, "png", new File("test.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
