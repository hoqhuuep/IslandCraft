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
		double n1 = com.flowpowered.noise.Noise.gradientCoherentNoise3D(x * 1.5, 0.0, z * 1.5, seed+0, NoiseQuality.STANDARD) * 3.0 - 1.0;
		double n2 = com.flowpowered.noise.Noise.gradientCoherentNoise3D(x * 3, 0.0, z * 3, seed+1, NoiseQuality.STANDARD) * 3.0 - 1.0;
		double res = (n1 * 2 + n2 * 1) / 3 - 0.07;
		return res <= 0.0 ? 0.0 : res >= 1.0 ? 1.0 : res;
	}
}
