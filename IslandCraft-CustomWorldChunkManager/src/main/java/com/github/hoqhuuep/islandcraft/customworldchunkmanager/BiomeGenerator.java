package com.github.hoqhuuep.islandcraft.customworldchunkmanager;

public interface BiomeGenerator {
	int[] validSpawnBiomes();

	/**
	 * Used for base terrain generation.
	 * 
	 * Coordinates are zoomed in. That is a 16x16 area here represents a 64x64
	 * block area.
	 * 
	 * @param x
	 * @param z
	 * @param xSize
	 * @param zSize
	 * @return
	 */
	int[] generateZoomed(int x, int z, int xSize, int zSize);

	/**
	 * Used for BlockPopulators and terrain display in F3.
	 * 
	 * @param x
	 * @param z
	 * @param xSize
	 * @param zSize
	 * @return
	 */
	int[] generateUnzoomed(int x, int z, int xSize, int zSize);
}
