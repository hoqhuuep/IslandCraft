package com.github.hoqhuuep.islandcraft.worldgenerator;

import org.bukkit.configuration.ConfigurationSection;

public class IslandParametersAlpha {
	public final int OUTER_COAST;
	public final int INNER_COAST;
	public final int NORMAL;
	public final int NORMAL_M;
	public final int HILLS;
	public final int HILLS_M;
	public final int SPECIAL;
	public final int SPECIAL_M;

	public IslandParametersAlpha(final String outerCoast, final String innerCoast, final String normal, final String normalM, final String hills, final String hillsM, final String special, final String specialM) {
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

	public IslandParametersAlpha(final ConfigurationSection config) {
		this(config.getString("outer-coast"), config.getString("inner-coast"), config.getString("normal"), config.getString("normal-m"), config.getString("hills"), config.getString("hills-m"), config.getString("special"), config.getString("special-m"));
	}

	public IslandParametersAlpha(final String string) {
		final String[] parameters = string.split(" ");
		OUTER_COAST = Integer.parseInt(parameters[0]);
		INNER_COAST = Integer.parseInt(parameters[1]);
		NORMAL = Integer.parseInt(parameters[2]);
		NORMAL_M = Integer.parseInt(parameters[3]);
		HILLS = Integer.parseInt(parameters[4]);
		HILLS_M = Integer.parseInt(parameters[5]);
		SPECIAL = Integer.parseInt(parameters[6]);
		SPECIAL_M = Integer.parseInt(parameters[7]);
	}

	@Override
	public String toString() {
		return OUTER_COAST + " " + INNER_COAST + " " + NORMAL + " " + NORMAL_M + " " + HILLS + " " + HILLS_M + " " + SPECIAL + " " + SPECIAL_M;
	}
}
