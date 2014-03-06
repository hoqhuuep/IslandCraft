package com.github.hoqhuuep.islandcraft.worldgenerator;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

import org.bukkit.util.noise.OctaveGenerator;
import org.bukkit.util.noise.SimplexOctaveGenerator;

import com.github.hoqhuuep.islandcraft.worldgenerator.mosaic.Poisson;
import com.github.hoqhuuep.islandcraft.worldgenerator.mosaic.Site;

public class IslandGenerator {
	private static final double minDistance = 8;
	private static final double NOISE = 2.7;
	private static final double CIRCLE = 2;
	private static final double SQUARE = 0;
	private static final double THRESHOLD = 2;

	public int[] generate(int xSize, int zSize, long islandSeed) {
		final Poisson poisson = new Poisson(xSize, zSize, minDistance);
		final List<Site> sites = poisson.generate(new Random(islandSeed));

		final SimplexOctaveGenerator shapeNoise = new SimplexOctaveGenerator(islandSeed, 2);
		final SimplexOctaveGenerator hillsNoise = new SimplexOctaveGenerator(islandSeed + 1, 2);
		final SimplexOctaveGenerator specialNoise = new SimplexOctaveGenerator(islandSeed + 2, 2);
		final SimplexOctaveGenerator mNoise = new SimplexOctaveGenerator(islandSeed + 3, 2);
		final BiomeSelection biomeSelection = BiomeSelection.select(islandSeed);

		// Find borders
		final Queue<Site> ocean = new LinkedList<Site>();
		for (final Site p : sites) {
			if (p.polygon == null) {
				p.isOcean = true;
				ocean.add(p);
			}
		}

		final List<Site> suspectCoast = new ArrayList<Site>();
		final List<Site> coast = new ArrayList<Site>();

		// Find oceans and coasts
		while (!ocean.isEmpty()) {
			final Site site = ocean.remove();
			for (final Site n : site.neighbors) {
				if (site.polygon == null) {
					if (!n.isOcean) {
						n.isOcean = true;
						ocean.add(n);
					}
				} else {
					final double dx = (double) (n.x - (xSize / 2)) / (double) (xSize / 2);
					final double dz = (double) (n.z - (zSize / 2)) / (double) (zSize / 2);
					if (NOISE * noise(dx, dz, shapeNoise) + CIRCLE * circle(dx, dz) + SQUARE * square(dx, dz) > THRESHOLD) {
						if (!n.isOcean) {
							n.isOcean = true;
							ocean.add(n);
						}
					} else {
						n.isInnerCoast = true;
						suspectCoast.add(n);
					}
				}
			}
		}

		// Create coast
		SITE: for (final Site site : suspectCoast) {
			for (final Site n : site.neighbors) {
				if (!n.isOcean && !n.isInnerCoast) {
					coast.add(site);
					continue SITE;
				}
			}
			site.isInnerCoast = false;
			site.isOcean = true;
		}

		// Create shallow ocean
		for (final Site site : coast) {
			for (final Site n : site.neighbors) {
				if (n.isOcean) {
					n.isOcean = false;
					n.isOuterCoast = true;
				}
			}
		}

		// Create blank image
		final BufferedImage image = new BufferedImage(xSize, zSize, BufferedImage.TYPE_INT_ARGB);
		final Graphics2D graphics = image.createGraphics();
		graphics.setComposite(AlphaComposite.Src);
		graphics.setBackground(new Color(biomeSelection.ocean, true));
		graphics.clearRect(0, 0, xSize, zSize);

		// Render island
		for (final Site site : sites) {
			if (site.isOcean) {
				continue;
			} else if (site.isOuterCoast) {
				graphics.setColor(new Color(biomeSelection.outerCoast, true));
			} else if (site.isInnerCoast) {
				graphics.setColor(new Color(biomeSelection.innerCoast, true));
			} else if (noise2(site, 0.25, 320.0, mNoise)) {
				if (noise2(site, 0.4, 80.0, hillsNoise)) {
					graphics.setColor(new Color(biomeSelection.hillsM, true));
				} else if (noise2(site, 0.2, 640.0, specialNoise)) {
					graphics.setColor(new Color(biomeSelection.specialM, true));
				} else {
					graphics.setColor(new Color(biomeSelection.normalM, true));
				}
			} else {
				if (noise2(site, 0.4, 80.0, hillsNoise)) {
					graphics.setColor(new Color(biomeSelection.hills, true));
				} else if (noise2(site, 0.2, 640.0, specialNoise)) {
					graphics.setColor(new Color(biomeSelection.special, true));
				} else {
					graphics.setColor(new Color(biomeSelection.normal, true));
				}
			}
			graphics.fillPolygon(site.polygon);
			graphics.drawPolygon(site.polygon);
		}

		// Save result
		graphics.dispose();
		final int[] result = new int[xSize * zSize];
		for (int i = 0; i < result.length; ++i) {
			final int x = i % xSize;
			final int z = i / xSize;
			result[i] = image.getRGB(x, z);
		}
		return result;
	}

	public static boolean noise2(final Site site, final double threshold, final double period, final OctaveGenerator octaveGenerator) {
		return octaveGenerator.noise(site.x / period, site.z / period, 2.0, 0.5, true) / 2.0 + 0.5 < threshold;
	}

	public static double noise(final double dx, final double dz, final OctaveGenerator octaveGenerator) {
		return octaveGenerator.noise(dx, dz, 2.0, 0.5, true) / 2.0 + 0.5;
	}

	public static double circle(final double dx, final double dz) {
		return (dx * dx + dz * dz) / 2;
	}

	public static double square(final double dx, final double dz) {
		return Math.max(Math.abs(dx), Math.abs(dz));
	}
}
