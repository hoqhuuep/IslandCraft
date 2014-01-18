package com.github.hoqhuuep.islandcraft.worldgenerator;

import com.github.hoqhuuep.islandcraft.customworldchunkmanager.BiomeGenerator;

public class WorldGenerator implements BiomeGenerator {
	@Override
	public int[] validSpawnBiomes() {
		final int[] result = { 14 };
		return result;
	}

	@Override
	public int[] generateZoomed(final int x, final int z, final int xSize,
			final int zSize) {
		final int[] result = new int[xSize * zSize];
		for (int j = 0; j < zSize; ++j) {
			final int zz = z + j;
			for (int i = 0; i < xSize; ++i) {
				final int xx = x + i;
				final int r = xx * xx + zz * zz;
				if (r < 15 * 15) {
					result[j * xSize + i] = 14; // MushroomIsland
				} else if (r < 16 * 16) {
					result[j * xSize + i] = 15; // MushroomIslandShore
				} else if (Math.abs(xx) < 20 && Math.abs(zz) < 20) {
					result[j * xSize + i] = 0; // Ocean
				} else {
					result[j * xSize + i] = 24; // DeepOcean
				}
			}
		}
		return result;
	}

	@Override
	public int[] generateUnzoomed(final int x, final int z, final int xSize,
			final int zSize) {
		final int[] result = new int[xSize * zSize];
		for (int j = 0; j < zSize; ++j) {
			final int zz = z + j;
			for (int i = 0; i < xSize; ++i) {
				final int xx = x + i;
				final int r = xx * xx + zz * zz;
				if (r < 60 * 60) {
					result[j * xSize + i] = 14; // MushroomIsland
				} else if (r < 64 * 64) {
					result[j * xSize + i] = 15; // MushroomIslandShore
				} else if (Math.abs(xx) < 80 && Math.abs(zz) < 80) {
					result[j * xSize + i] = 0; // Ocean
				} else {
					result[j * xSize + i] = 24; // DeepOcean
				}
			}
		}
		return result;
	}
}
