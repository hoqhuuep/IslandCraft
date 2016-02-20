package com.github.hoqhuuep.islandcraft.core.noise;

import java.util.Random;

public class OctaveNoise implements Noise {
	private SimplexNoise octave1;
	private SimplexNoise octave2;

	public OctaveNoise(long seed) {
		Random random = new Random(seed);
		octave1 = new SimplexNoise(random);
		octave2 = new SimplexNoise(random);
	}

	@Override
	public double noise(double x, double y) {
		double noise1 = octave1.noise(x, y, 0.0);
		double noise2 = octave2.noise(x * 2.0, y * 2.0, 0.0) * 0.5;
		return (noise1 + noise2) / 3.0 + 0.5;
	}
}
