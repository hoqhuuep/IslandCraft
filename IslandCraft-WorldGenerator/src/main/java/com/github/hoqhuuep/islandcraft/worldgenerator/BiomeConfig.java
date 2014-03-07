package com.github.hoqhuuep.islandcraft.worldgenerator;

import org.bukkit.configuration.ConfigurationSection;

public class BiomeConfig {
	public final int OCEAN;
	public final int OUTER_COAST;
	public final int INNER_COAST;
	public final int NORMAL;
	public final int NORMAL_M;
	public final int HILLS;
	public final int HILLS_M;
	public final int SPECIAL;
	public final int SPECIAL_M;

	public BiomeConfig(final String ocean, final String outerCoast, final String innerCoast, final String normal, final String normalM, final String hills, final String hillsM, final String special, final String specialM) {
		this.OCEAN = Biome.valueOf(ocean).ID;
		this.OUTER_COAST = Biome.valueOf(outerCoast).ID;
		this.INNER_COAST = Biome.valueOf(innerCoast).ID;
		this.NORMAL = Biome.valueOf(normal).ID;
		if (normalM != null) {
			NORMAL_M = Biome.valueOf(normalM).ID;
		} else {
			NORMAL_M = NORMAL;
		}
		if (hills != null) {
			HILLS = Biome.valueOf(hills).ID;
		} else {
			HILLS = NORMAL;
		}
		if (hillsM != null) {
			HILLS_M = Biome.valueOf(hillsM).ID;
		} else {
			HILLS_M = HILLS;
		}
		if (special != null) {
			SPECIAL = Biome.valueOf(special).ID;
		} else {
			SPECIAL = NORMAL;
		}
		if (specialM != null) {
			SPECIAL_M = Biome.valueOf(specialM).ID;
		} else {
			SPECIAL_M = SPECIAL;
		}
	}

	public BiomeConfig(final ConfigurationSection config) {
		this(config.getString("ocean"), config.getString("outer-coast"), config.getString("inner-coast"), config.getString("normal"), config.getString("normal-m"), config.getString("hills"), config.getString("hills-m"), config.getString("special"), config.getString("special-m"));
	}
}
