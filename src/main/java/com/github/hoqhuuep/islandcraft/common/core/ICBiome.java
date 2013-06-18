package com.github.hoqhuuep.islandcraft.common.core;

public class ICBiome {
	public static final int OCEAN = 0;
	public static final int PLAINS = 1;
	public static final int DESERT = 2;
	public static final int EXTREME_HILLS = 3;
	public static final int FOREST = 4;
	public static final int TAIGA = 5;
	public static final int SWAMPLAND = 6;
	public static final int RIVER = 7;
	public static final int HELL = 8;
	public static final int SKY = 9;
	public static final int FROZEN_OCEAN = 10;
	public static final int FROZEN_RIVER = 11;
	public static final int ICE_PLAINS = 12;
	public static final int ICE_MOUNTAINS = 13;
	public static final int MUSHROOM_ISLAND = 14;
	public static final int MUSHROOM_ISLAND_SHORE = 15;
	public static final int BEACH = 16;
	public static final int DESERT_HILLS = 17;
	public static final int FOREST_HILLS = 18;
	public static final int TAIGA_HILLS = 19;
	public static final int EXTREME_HILLS_EDGE = 20;
	public static final int JUNGLE = 21;
	public static final int JUNGLE_HILLS = 22;
	public static final int UNCALCULATED = -1;

	public static final String name(int biome) {
		switch (biome) {
		case OCEAN:
			return "Ocean";
		case PLAINS:
			return "Plains";
		case DESERT:
			return "Desert";
		case EXTREME_HILLS:
			return "Extreme Hills";
		case FOREST:
			return "Forest";
		case TAIGA:
			return "Taiga";
		case SWAMPLAND:
			return "Swampland";
		case RIVER:
			return "River";
		case HELL:
			return "Hell";
		case SKY:
			return "Sky";
		case FROZEN_OCEAN:
			return "Frozen Ocean";
		case FROZEN_RIVER:
			return "Frozen River";
		case ICE_PLAINS:
			return "Ice Plains";
		case ICE_MOUNTAINS:
			return "Ice Mountains";
		case MUSHROOM_ISLAND:
			return "Mushroom Island";
		case MUSHROOM_ISLAND_SHORE:
			return "Mushroom Island Shore";
		case BEACH:
			return "Beach";
		case DESERT_HILLS:
			return "Desert Hills";
		case FOREST_HILLS:
			return "Forest Hills";
		case TAIGA_HILLS:
			return "Taiga Hills";
		case EXTREME_HILLS_EDGE:
			return "Extreme Hills Edge";
		case JUNGLE:
			return "Jungle";
		case JUNGLE_HILLS:
			return "Jungle Hills";
		default:
		case UNCALCULATED:
			return "Uncalculated";
		}
	}
}
