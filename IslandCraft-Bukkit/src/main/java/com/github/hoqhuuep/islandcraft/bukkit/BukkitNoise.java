package com.github.hoqhuuep.islandcraft.bukkit;

import org.bukkit.util.noise.OctaveGenerator;
import org.bukkit.util.noise.SimplexOctaveGenerator;

import com.github.hoqhuuep.islandcraft.core.Noise;

public class BukkitNoise implements Noise {
	private final OctaveGenerator octaveGenerator;

	public BukkitNoise(long seed) {
		octaveGenerator = new SimplexOctaveGenerator(seed, 2);
	}

	@Override
	public double noise(double x, double z) {
		return octaveGenerator.noise(x, z, 2.0, 0.5, true) / 2.0 + 0.5;
	}
}
