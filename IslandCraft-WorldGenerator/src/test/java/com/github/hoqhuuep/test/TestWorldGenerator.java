package com.github.hoqhuuep.test;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.github.hoqhuuep.islandcraft.worldgenerator.Biome;
import com.github.hoqhuuep.islandcraft.worldgenerator.WorldGenerator;
import com.github.hoqhuuep.islandcraft.worldgenerator.hack.CustomWorldChunkManager;

import net.minecraft.server.v1_7_R1.WorldChunkManager;
import net.minecraft.server.v1_7_R1.WorldType;

public class TestWorldGenerator {
	private static final int WIDTH = 1920 * 2;
	private static final int HEIGHT = 1080 * 2;

	public static void main(final String[] args) throws IOException {
		testCustom();
		// testDefault();
	}

	public static void testCustom() throws IOException {
		final WorldChunkManager worldChunkManager = new CustomWorldChunkManager(new WorldGenerator(0, 288, 320, Biome.DEEP_OCEAN.getId()));
		final BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
		for (int j = 0; j < HEIGHT; ++j) {
			for (int i = 0; i < WIDTH; ++i) {
				image.setRGB(i, j, Biome.fromId[worldChunkManager.getBiome(i - WIDTH / 2, j - HEIGHT / 2).id].getColor().getRGB());
			}
			if (j % 120 == 0) {
				System.out.println("LINE: " + j);
			}
		}
		ImageIO.write(image, "png", new File("target/world-custom.png"));
		System.out.println("DONE");
	}

	public static void testDefault() throws IOException {
		final WorldChunkManager worldChunkManager = new WorldChunkManager(0, WorldType.NORMAL);
		final BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
		for (int j = 0; j < HEIGHT; ++j) {
			for (int i = 0; i < WIDTH; ++i) {
				image.setRGB(i, j, Biome.fromId[worldChunkManager.getBiome(i - WIDTH / 2, j - HEIGHT / 2).id].getColor().getRGB());
			}
			if (j % 120 == 0) {
				System.out.println("LINE: " + j);
			}
		}
		ImageIO.write(image, "png", new File("target/world-default.png"));
		System.out.println("DONE");
	}
}
