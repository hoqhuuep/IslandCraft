package com.github.hoqhuuep.islandcraft;

import com.github.hoqhuuep.islandcraft.core.Noise;
import com.github.hoqhuuep.islandcraft.core.NoiseBuilder;

public class FlowNoiseBuilder implements NoiseBuilder {
	@Override
	public Noise build(long seed) {
		return new FlowNoise(seed);
	}
}
