package com.github.hoqhuuep.islandcraft.worldgenerator;

import com.github.hoqhuuep.islandcraft.customworldchunkmanager.BiomeGenerator;

public class WorldGenerator implements BiomeGenerator {

	private static final int CHUNK_SIZE = 16;

	@Override
	public int[] validSpawnBiomes() {
		final int[] result = { 14 };
		return result;
	}

	@Override
	public int biomeAt(int x, int z) {
		final int r = x * x + z * z;
		if (r < 60 * 60) {
			return 14; // MushroomIsland
		} else if (r < 64 * 64) {
			return 15; // MushroomIslandShore
		} else if (Math.abs(x) < 80 && Math.abs(z) < 80) {
			return 1; // Ocean
		} else {
			return 24; // DeepOcean
		}
	}

	@Override
	public int[] biomeChunk(int xMin, int zMin) {
		final int[] result = new int[CHUNK_SIZE * CHUNK_SIZE];
		for (int j = 0; j < CHUNK_SIZE; ++j) {
			final int z = zMin + j;
			final int offset = j * CHUNK_SIZE;
			for (int i = 0; i < CHUNK_SIZE; ++i) {
				result[offset + i] = biomeAt(xMin + i, z);
			}
		}
		return result;
	}
}
