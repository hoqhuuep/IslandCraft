package com.github.hoqhuuep.islandcraft;

import com.github.hoqhuuep.islandcraft.core.IslandDatabase;

public class TempFakeDatabase implements IslandDatabase {
	@Override
	public void save(String worldName, int centerX, int centerZ, long islandSeed, String generator) {
	}

	@Override
	public Result load(String worldName, int centerX, int centerZ) {
		return null;
	}

	@Override
	public boolean isEmpty(String worldName) {
		return false;
	}
}
