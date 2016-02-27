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

import com.github.hoqhuuep.islandcraft.api.IslandGenerator;
import com.github.hoqhuuep.islandcraft.core.mosaic.Poisson;
import com.github.hoqhuuep.islandcraft.core.mosaic.Site;
import com.github.hoqhuuep.islandcraft.core.noise.Noise;
import com.github.hoqhuuep.islandcraft.core.noise.OctaveNoise;
import com.github.hoqhuuep.islandcraft.util.StringUtils;

public class IslandGeneratorAlpha<Biome> implements IslandGenerator<Biome> {
	private static final double MIN_DISTANCE = 8;
	private static final double NOISE = 2.7;
	private static final double CIRCLE = 2;
	private static final double SQUARE = 0;
	private static final double THRESHOLD = 2;
	private static final Color oceanColor = new Color(0);
	private static final Color normalColor = new Color(1);
	private static final Color mountainsColor = new Color(2);
	private static final Color hillsColor = new Color(3);
	private static final Color hillsMountainsColor = new Color(4);
	private static final Color forestColor = new Color(5);
	private static final Color forestMountainsColor = new Color(6);
	private static final Color outerCoastColor = new Color(7);
	private static final Color innerCoastColor = new Color(8);
	// private static final Color riverColor = new Color(9); // unused for now

	private final BiomeRegistry<Biome> biomeRegistry;
	private final String normalName;
	private final Biome ocean;
	private final Biome normal;
	private final Biome mountains;
	private final Biome hills;
	private final Biome hillsMountains;
	private final Biome forest;
	private final Biome forestMountains;
	private final Biome outerCoast;
	private final Biome innerCoast;
	// private final Biome river; // unused for now
	private final Biome[] biomeFromIndex;

	public IslandGeneratorAlpha(BiomeRegistry<Biome> biomeRegistry, String[] args) {
		this.biomeRegistry = biomeRegistry;
		ICLogger.logger.info("Creating IslandGeneratorAlpha with args: " + StringUtils.join(args, " "));
		if (args.length != 9) {
			ICLogger.logger.error("IslandGeneratorAlpha requrires 9 parameters, " + args.length + " given");
			throw new IllegalArgumentException("IslandGeneratorAlpha requrires 9 parameters");
		}

		// Biome names
		String oceanName = null;
		normalName = backup(args[0], oceanName);
		String mountainsName = backup(args[1], normalName);
		String hillsName = backup(args[2], normalName);
		String hillsMountainsName = backup(args[3], hillsName);
		String forestName = backup(args[4], normalName);
		String forestMountainsName = backup(args[5], forestName);
		String outerCoastName = backup(args[6], normalName);
		String innerCoastName = backup(args[7], normalName);

		// Lookup biomes from names
		ocean = biomeRegistry.biomeFromName(oceanName);
		normal = biomeRegistry.biomeFromName(normalName);
		mountains = biomeRegistry.biomeFromName(mountainsName);
		hills = biomeRegistry.biomeFromName(hillsName);
		hillsMountains = biomeRegistry.biomeFromName(hillsMountainsName);
		forest = biomeRegistry.biomeFromName(forestName);
		forestMountains = biomeRegistry.biomeFromName(forestMountainsName);
		outerCoast = biomeRegistry.biomeFromName(outerCoastName);
		innerCoast = biomeRegistry.biomeFromName(innerCoastName);

		biomeFromIndex = biomeRegistry.newBiomeArray(9);
		biomeFromIndex[0] = ocean;
		biomeFromIndex[1] = normal;
		biomeFromIndex[2] = mountains;
		biomeFromIndex[3] = hills;
		biomeFromIndex[4] = hillsMountains;
		biomeFromIndex[5] = forest;
		biomeFromIndex[6] = forestMountains;
		biomeFromIndex[7] = outerCoast;
		biomeFromIndex[8] = innerCoast;
	}

	@Override
	public Biome[] generate(int xSize, int zSize, long islandSeed) {
		ICLogger.logger.info(String.format(
				"Generating island from IslandGeneratorAlpha with xSize: %d, zSize: %d, islandSeed: %d, biome: %s",
				xSize, zSize, islandSeed, normalName));
		Poisson poisson = new Poisson(xSize, zSize, MIN_DISTANCE);
		List<Site> sites = poisson.generate(new Random(islandSeed));
		Noise shapeNoise = new OctaveNoise(islandSeed);
		Noise hillsNoise = new OctaveNoise(islandSeed + 1);
		Noise forestNoise = new OctaveNoise(islandSeed + 2);
		Noise mountainsNoise = new OctaveNoise(islandSeed + 3);
		// Find borders
		Queue<Site> oceanSites = new LinkedList<>();
		for (Site site : sites) {
			if (site.polygon == null) {
				site.isOcean = true;
				oceanSites.add(site);
			}
		}
		List<Site> suspectCoast = new ArrayList<>();
		List<Site> coast = new ArrayList<>();
		// Find oceans and coasts
		while (!oceanSites.isEmpty()) {
			Site site = oceanSites.remove();
			for (Site neighbor : site.neighbors) {
				if (site.polygon == null) {
					if (!neighbor.isOcean) {
						neighbor.isOcean = true;
						oceanSites.add(neighbor);
					}
				} else {
					double dx = (double) (neighbor.x - (xSize / 2)) / (double) (xSize / 2);
					double dz = (double) (neighbor.z - (zSize / 2)) / (double) (zSize / 2);
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
		SITE: for (Site site : suspectCoast) {
			for (Site neighbor : site.neighbors) {
				if (!neighbor.isOcean && !neighbor.isInnerCoast) {
					coast.add(site);
					continue SITE;
				}
			}
			site.isInnerCoast = false;
			site.isOcean = true;
		}
		// Create shallow ocean
		for (Site site : coast) {
			for (Site neighbor : site.neighbors) {
				if (neighbor.isOcean) {
					neighbor.isOcean = false;
					neighbor.isOuterCoast = true;
				}
			}
		}
		// Create blank image
		BufferedImage image = new BufferedImage(xSize, zSize, BufferedImage.TYPE_INT_ARGB);
		Graphics2D graphics = image.createGraphics();
		graphics.setComposite(AlphaComposite.Src);
		graphics.setBackground(oceanColor);
		graphics.clearRect(0, 0, xSize, zSize);
		// Render island
		for (Site site : sites) {
			if (site.isOcean) {
				continue;
			} else if (site.isOuterCoast) {
				graphics.setColor(outerCoastColor);
			} else if (site.isInnerCoast) {
				graphics.setColor(innerCoastColor);
			} else if (noise(site, 0.375, 160.0, mountainsNoise)) {
				if (noise(site, 0.375, 80.0, hillsNoise)) {
					graphics.setColor(hillsMountainsColor);
				} else if (noise(site, 0.375, 160.0, forestNoise)) {
					graphics.setColor(forestMountainsColor);
				} else {
					graphics.setColor(mountainsColor);
				}
			} else {
				if (noise(site, 0.375, 80.0, hillsNoise)) {
					graphics.setColor(hillsColor);
				} else if (noise(site, 0.375, 160.0, forestNoise)) {
					graphics.setColor(forestColor);
				} else {
					graphics.setColor(normalColor);
				}
			}
			graphics.fillPolygon(site.polygon);
			graphics.drawPolygon(site.polygon);
		}
		// Save result
		graphics.dispose();
		Biome[] result = biomeRegistry.newBiomeArray(xSize * zSize);
		int maxIndex = biomeFromIndex.length;
		for (int i = 0; i < result.length; ++i) {
			int x = i % xSize;
			int z = i / xSize;
			int index = image.getRGB(x, z);
			if (index < maxIndex) {
				result[i] = biomeFromIndex[index];
			}
		}
		return result;
	}

	private static String backup(String name, String backup) {
		if (name.equals("~")) {
			return backup;
		}
		return name;
	}

	private static boolean noise(Site site, double threshold, double period, Noise noise) {
		return noise.noise(site.x / period, site.z / period) < threshold;
	}

	private static double noise(double dx, double dz, Noise noise) {
		return noise.noise(dx, dz);
	}

	private static double circle(double dx, double dz) {
		return (dx * dx + dz * dz) / 2;
	}

	private static double square(double dx, double dz) {
		return Math.max(Math.abs(dx), Math.abs(dz));
	}
}
