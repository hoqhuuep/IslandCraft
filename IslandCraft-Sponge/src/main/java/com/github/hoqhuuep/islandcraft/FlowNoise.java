package com.github.hoqhuuep.islandcraft;

import com.flowpowered.noise.NoiseQuality;
import com.github.hoqhuuep.islandcraft.core.Noise;

public class FlowNoise implements Noise {
	private final int seed;

	public FlowNoise(long seed) {
		this.seed = (int) seed;
	}

	@Override
	public double noise(double x, double z) {
		return com.flowpowered.noise.Noise.valueCoherentNoise3D(x, 0.0, z, seed, NoiseQuality.STANDARD);
	}
}
