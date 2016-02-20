package com.github.hoqhuuep.islandcraft.core;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

import com.github.hoqhuuep.islandcraft.api.ICBiome;
import com.github.hoqhuuep.islandcraft.api.IslandGenerator;
import com.github.hoqhuuep.islandcraft.core.mosaic.Poisson;
import com.github.hoqhuuep.islandcraft.core.mosaic.Site;
import com.github.hoqhuuep.islandcraft.core.noise.Noise;
import com.github.hoqhuuep.islandcraft.core.noise.OctaveNoise;
import com.github.hoqhuuep.islandcraft.util.StringUtils;

public class IslandGeneratorAlpha implements IslandGenerator {
	private static final double MIN_DISTANCE = 8;
	private static final double NOISE = 2.7;
	private static final double CIRCLE = 2;
	private static final double SQUARE = 0;
	private static final double THRESHOLD = 2;
	private final Color ocean;
	private final Color normal;
	private final Color mountains;
	private final Color hills;
	private final Color hillsMountains;
	private final Color forest;
	private final Color forestMountains;
	private final Color outerCoast;
	private final Color innerCoast;

	// private final Color river; // unused for now

	public IslandGeneratorAlpha(final String[] args) {
		ICLogger.logger.info("Creating IslandGeneratorAlpha with args: " + StringUtils.join(args, " "));
		if (args.length != 9) {
			ICLogger.logger.error("IslandGeneratorAlpha requrires 9 parameters, " + args.length + " given");
			throw new IllegalArgumentException("IslandGeneratorAlpha requrires 9 parameters");
		}
		ocean = new Color(ICBiome.values().length, true);
		normal = biomeColor(args[0], ocean);
		mountains = biomeColor(args[1], normal);
		hills = biomeColor(args[2], normal);
		hillsMountains = biomeColor(args[3], hills);
		forest = biomeColor(args[4], normal);
		forestMountains = biomeColor(args[5], forest);
		outerCoast = biomeColor(args[6], normal);
		innerCoast = biomeColor(args[7], normal);
		// river = biomeColor(args[8], normal); // unused for now
	}

	@Override
	public ICBiome[] generate(final int xSize, final int zSize, final long islandSeed) {
		ICLogger.logger.info(String.format(
				"Generating island from IslandGeneratorAlpha with xSize: %d, zSize: %d, islandSeed: %d, biome: %s",
				xSize, zSize, islandSeed, ICBiome.values()[normal.getRGB()]));
		final Poisson poisson = new Poisson(xSize, zSize, MIN_DISTANCE);
		final List<Site> sites = poisson.generate(new Random(islandSeed));
		final Noise shapeNoise = new OctaveNoise(islandSeed);
		final Noise hillsNoise = new OctaveNoise(islandSeed + 1);
		final Noise forestNoise = new OctaveNoise(islandSeed + 2);
		final Noise mountainsNoise = new OctaveNoise(islandSeed + 3);
		// Find borders
		final Queue<Site> oceanSites = new LinkedList<Site>();
		for (final Site site : sites) {
			if (site.polygon == null) {
				site.isOcean = true;
				oceanSites.add(site);
			}
		}
		final List<Site> suspectCoast = new ArrayList<Site>();
		final List<Site> coast = new ArrayList<Site>();
		// Find oceans and coasts
		while (!oceanSites.isEmpty()) {
			final Site site = oceanSites.remove();
			for (final Site neighbor : site.neighbors) {
				if (site.polygon == null) {
					if (!neighbor.isOcean) {
						neighbor.isOcean = true;
						oceanSites.add(neighbor);
					}
				} else {
					final double dx = (double) (neighbor.x - (xSize / 2)) / (double) (xSize / 2);
					final double dz = (double) (neighbor.z - (zSize / 2)) / (double) (zSize / 2);
					if (NOISE * noise(dx, dz, shapeNoise) + CIRCLE * circle(dx, dz)
							+ SQUARE * square(dx, dz) > THRESHOLD) {
						if (!neighbor.isOcean) {
							neighbor.isOcean = true;
							oceanSites.add(neighbor);
						}
					} else {
						neighbor.isInnerCoast = true;
						suspectCoast.add(neighbor);
					}
				}
			}
		}
		// Create coast
		SITE: for (final Site site : suspectCoast) {
			for (final Site neighbor : site.neighbors) {
				if (!neighbor.isOcean && !neighbor.isInnerCoast) {
					coast.add(site);
					continue SITE;
				}
			}
			site.isInnerCoast = false;
			site.isOcean = true;
		}
		// Create shallow ocean
		for (final Site site : coast) {
			for (final Site neighbor : site.neighbors) {
				if (neighbor.isOcean) {
					neighbor.isOcean = false;
					neighbor.isOuterCoast = true;
				}
			}
		}
		// Create blank image
		final BufferedImage image = new BufferedImage(xSize, zSize, BufferedImage.TYPE_INT_ARGB);
		final Graphics2D graphics = image.createGraphics();
		graphics.setComposite(AlphaComposite.Src);
		graphics.setBackground(ocean);
		graphics.clearRect(0, 0, xSize, zSize);
		// Render island
		for (final Site site : sites) {
			if (site.isOcean) {
				continue;
			} else if (site.isOuterCoast) {
				graphics.setColor(outerCoast);
			} else if (site.isInnerCoast) {
				graphics.setColor(innerCoast);
			} else if (noise(site, 0.375, 160.0, mountainsNoise)) {
				if (noise(site, 0.375, 80.0, hillsNoise)) {
					graphics.setColor(hillsMountains);
				} else if (noise(site, 0.375, 160.0, forestNoise)) {
					graphics.setColor(forestMountains);
				} else {
					graphics.setColor(mountains);
				}
			} else {
				if (noise(site, 0.375, 80.0, hillsNoise)) {
					graphics.setColor(hills);
				} else if (noise(site, 0.375, 160.0, forestNoise)) {
					graphics.setColor(forest);
				} else {
					graphics.setColor(normal);
				}
			}
			graphics.fillPolygon(site.polygon);
			graphics.drawPolygon(site.polygon);
		}
		// Save result
		graphics.dispose();
		final ICBiome[] result = new ICBiome[xSize * zSize];
		final ICBiome[] values = ICBiome.values();
		final int maxOrdinal = values.length;
		for (int i = 0; i < result.length; ++i) {
			final int x = i % xSize;
			final int z = i / xSize;
			final int ordinal = image.getRGB(x, z);
			if (ordinal < maxOrdinal) {
				result[i] = values[ordinal];
			}
		}
		return result;
	}

	private static Color biomeColor(final String name, final Color backup) {
		if (name.equals("~")) {
			return backup;
		}
		return new Color(ICBiome.valueOf(name).ordinal(), true);
	}

	private static boolean noise(final Site site, final double threshold, final double period, final Noise noise) {
		return noise.noise(site.x / period, site.z / period) < threshold;
	}

	private static double noise(final double dx, final double dz, final Noise noise) {
		return noise.noise(dx, dz);
	}

	private static double circle(final double dx, final double dz) {
		return (dx * dx + dz * dz) / 2;
	}

	private static double square(final double dx, final double dz) {
		return Math.max(Math.abs(dx), Math.abs(dz));
	}
}
