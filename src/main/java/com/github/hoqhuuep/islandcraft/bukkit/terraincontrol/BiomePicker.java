package com.github.hoqhuuep.islandcraft.bukkit.terraincontrol;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import com.github.hoqhuuep.islandcraft.bukkit.IslandCraftPlugin;
import com.github.hoqhuuep.islandcraft.common.api.ICConfig;
import com.github.hoqhuuep.islandcraft.common.generator.IslandBiomes;

public class BiomePicker {
    private static IslandBiomes[] biomes;

    public static IslandBiomes pick(final Random random) {
        if (biomes == null) {
            // Hacks to get configuration from IslandCraft
            final Plugin plugin = Bukkit.getPluginManager().getPlugin("IslandCraft");
            if (plugin == null || !(plugin instanceof IslandCraftPlugin)) {
                throw new Error("Could not find IslandCraft plugin");
            }
            final IslandCraftPlugin islandCraft = (IslandCraftPlugin) plugin;
            final ICConfig config = islandCraft.getICConfig();
            biomes = config.getIslandBiomes();
        }
        return biomes[random.nextInt(biomes.length)];
    }
}
