package com.github.hoqhuuep.islandcraft.bukkit;

import com.github.hoqhuuep.islandcraft.core.Noise;
import com.github.hoqhuuep.islandcraft.core.NoiseBuilder;

public class BukkitNoiseBuilder implements NoiseBuilder {
	@Override
	public Noise build(long seed) {
		return new BukkitNoise(seed);
	}
}
