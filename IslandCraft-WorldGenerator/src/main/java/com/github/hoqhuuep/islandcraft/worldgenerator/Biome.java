package com.github.hoqhuuep.islandcraft.worldgenerator;

import java.awt.Color;

public enum Biome {
	BEACH(16, "Beach", new Color(250, 222, 85)),
	BIRCH_FOREST(27, "Birch Forest", new Color(48, 116, 68)),
	BIRCH_FOREST_HILLS(28, "Birch Forest Hills", new Color(31, 95, 50)),
	BIRCH_FOREST_HILLS_M(156, "Birch Forest Hills M", new Color(71, 135, 90)),
	BIRCH_FOREST_M(155, "Birch Forest M", new Color(88, 156, 108)),
	COLD_BEACH(26, "Cold Beach", new Color(250, 240, 192)),
	COLD_TAIGA(30, "Cold Taiga", new Color(49, 85, 74)),
	COLD_TAIGA_HILLS(31, "Cold Taiga Hills", new Color(36, 63, 54)),
	COLD_TAIGA_M(158, "Cold Taiga M", new Color(89, 125, 114)),
	DEEP_OCEAN(24, "Deep Ocean", new Color(0, 0, 48)),
	DESERT(2, "Desert", new Color(250, 148, 24)),
	DESERT_HILLS(17, "Desert Hills", new Color(210, 95, 18)),
	DESERT_M(130, "Desert M", new Color(255, 188, 64)),
	EXTREME_HILLS(3, "Extreme Hills", new Color(96, 96, 96)),
	EXTREME_HILLS_EDGE(20, "Extreme Hills Edge", new Color(114, 120, 154)),
	EXTREME_HILLS_M(131, "Extreme Hills M", new Color(136, 136, 136)),
	EXTREME_HILLS_PLUS(34, "Extreme Hills+", new Color(80, 112, 80)),
	EXTREME_HILLS_PLUS_M(162, "Extreme Hills+ M", new Color(120, 152, 120)),
	FLOWER_FOREST(132, "Flower Forest", new Color(45, 142, 73)),
	FOREST(4, "Forest", new Color(5, 102, 33)),
	FOREST_HILLS(18, "Forest Hills", new Color(34, 85, 28)),
	FROZEN_OCEAN(10, "Frozen Ocean", new Color(144, 144, 160)),
	FROZEN_RIVER(11, "Frozen River", new Color(160, 160, 255)),
	HELL(8, "Hell", new Color(255, 0, 0)),
	ICE_MOUNTAINS(13, "Ice Mountains", new Color(160, 160, 160)),
	ICE_PLAINS(12, "Ice Plains", new Color(255, 255, 255)),
	ICE_PLAINS_SPIKES(140, "Ice Plains Spikes", new Color(180, 220, 220)),
	JUNGLE(21, "Jungle", new Color(83, 123, 9)),
	JUNGLE_EDGE(23, "Jungle Edge", new Color(98, 139, 23)),
	JUNGLE_EDGE_M(151, "Jungle Edge M", new Color(138, 179, 63)),
	JUNGLE_HILLS(22, "Jungle Hills", new Color(44, 66, 5)),
	JUNGLE_M(149, "Jungle M", new Color(123, 163, 49)),
	MEGA_SPRUCE_TAIGA(160, "Mega Spruce Taiga", new Color(129, 142, 121)),
	MEGA_SPRUCE_TAIGA_HILLS(161, "Mega Spruce Taiga (Hills)", new Color(109, 119, 102)),
	MEGA_TAIGA(32, "Mega Taiga", new Color(89, 102, 81)),
	MEGA_TAIGA_HILLS(33, "Mega Taiga Hills", new Color(69, 79, 62)),
	MESA(37, "Mesa", new Color(217, 69, 21)),
	MESA_BRYCE(165, "Mesa (Bryce)", new Color(255, 109, 61)),
	MESA_PLATEAU(39, "Mesa Plateau", new Color(202, 140, 101)),
	MESA_PLATEAU_F(38, "Mesa Plateau F", new Color(176, 151, 101)),
	MESA_PLATEAU_F_M(166, "Mesa Plateau F M", new Color(216, 191, 141)),
	MESA_PLATEAU_M(167, "Mesa Plateau M", new Color(242, 180, 141)),
	MUSHROOM_ISLAND(14, "Mushroom Island", new Color(255, 0, 255)),
	MUSHROOM_ISLAND_SHORE(15, "Mushroom Island Shore", new Color(160, 0, 255)),
	OCEAN(0, "Ocean", new Color(0, 0, 112)),
	PLAINS(1, "Plains", new Color(141, 179, 96)),
	RIVER(7, "River", new Color(0, 0, 255)),
	ROOFED_FOREST(29, "Roofed Forest", new Color(64, 81, 26)),
	ROOFED_FOREST_M(157, "Roofed Forest M", new Color(104, 121, 66)),
	SAVANNA(35, "Savanna", new Color(189, 178, 95)),
	SAVANNA_M(163, "Savanna M", new Color(229, 218, 135)),
	SAVANNA_PLATEAU(36, "Savanna Plateau", new Color(167, 157, 100)),
	SAVANNA_PLATEAU_M(164, "Savanna Plateau M", new Color(207, 197, 140)),
	SKY(9, "Sky", new Color(128, 128, 255)),
	STONE_BEACH(25, "Stone Beach", new Color(162, 162, 132)),
	SUNFLOWER_PLAINS(129, "Sunflower Plains", new Color(181, 219, 136)),
	SWAMPLAND(6, "Swampland", new Color(7, 249, 178)),
	SWAMPLAND_M(134, "Swampland M", new Color(47, 255, 218)),
	TAIGA(5, "Taiga", new Color(11, 102, 89)),
	TAIGA_HILLS(19, "Taiga Hills", new Color(22, 57, 51)),
	TAIGA_M(133, "Taiga M", new Color(51, 142, 129));

	private final Color color;
	private final int id;
	public static final Biome[] fromId = new Biome[256];
	static {
		for (Biome biome : values()) {
			fromId[biome.getId()] = biome;
		}
	}

	private Biome(int id, String name, Color color) {
		this.id = id;
		this.color = color;
	}

	public int getId() {
		return id;
	}

	public Color getColor() {
		return color;
	}
}
