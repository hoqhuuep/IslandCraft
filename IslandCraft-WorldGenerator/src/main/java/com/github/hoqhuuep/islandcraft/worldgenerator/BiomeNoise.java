package com.github.hoqhuuep.islandcraft.worldgenerator;

import org.bukkit.util.noise.OctaveGenerator;
import org.bukkit.util.noise.SimplexOctaveGenerator;

import com.github.hoqhuuep.islandcraft.worldgenerator.mosaic.Site;

public class BiomeNoise implements BiomeDistribution {
	private final OctaveGenerator octaveGenerator;
	private final double period;
	private final double threshold;
	private final BiomeDistribution above;
	private final BiomeDistribution below;

	public BiomeNoise(final double period, final double threshold, final BiomeDistribution above, final BiomeDistribution below, final long seed) {
		this.octaveGenerator = new SimplexOctaveGenerator(seed, 2);
		this.period = period;
		this.threshold = threshold * 2.0 - 1.0;
		this.above = above;
		this.below = below;
	}

	@Override
	public int getBiome(final Site site) {
		if (octaveGenerator.noise(site.x / period, site.z / period, 2.0, 0.5, true) < threshold) {
			return below.getBiome(site);
		}
		return above.getBiome(site);
	}
}
