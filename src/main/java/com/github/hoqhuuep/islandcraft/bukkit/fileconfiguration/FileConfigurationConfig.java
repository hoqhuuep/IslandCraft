package com.github.hoqhuuep.islandcraft.bukkit.fileconfiguration;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;

import com.github.hoqhuuep.islandcraft.bukkit.terraincontrol.BiomePicker;
import com.github.hoqhuuep.islandcraft.common.api.ICConfig;
import com.github.hoqhuuep.islandcraft.common.generator.IslandBiomes;
import com.khorn.terraincontrol.LocalWorld;

public class FileConfigurationConfig implements ICConfig {
    private final FileConfiguration config;
    private static LocalWorld world;

    public FileConfigurationConfig(final FileConfiguration config) {
        this.config = config;
        // TODO This should be elsewhere (and not static)
        BiomePicker.setBiomes(this);
    }

    @Override
    public final int getIslandGap() {
        return this.config.getInt("island-gap");
    }

    @Override
    public final int getIslandSize() {
        return this.config.getInt("island-size");
    }

    @Override
    public final int getLocalChatRadius() {
        return this.config.getInt("local-chat-radius");
    }

    @Override
    public final String getWorld() {
        return this.config.getString("world");
    }

    @Override
    public IslandBiomes[] getIslandBiomes() {
        // TODO Make this more robust
        @SuppressWarnings("unchecked")
        List<LinkedHashMap<String, ?>> islands = (List<LinkedHashMap<String, ?>>) this.config.getList("biome.island");
        List<IslandBiomes> result = new ArrayList<IslandBiomes>();
        int ocean = biomeId(this.config.getString("biome.ocean"));
        for (LinkedHashMap<String, ?> island : islands) {
            int shore, flats, hills;
            flats = biomeId((String) island.get("flats"));
            try {
                shore = biomeId((String) island.get("shore"));
            } catch (Exception e) {
                shore = flats;
            }
            try {
                hills = biomeId((String) island.get("hills"));
            } catch (Exception e) {
                hills = flats;
            }
            IslandBiomes ib = new IslandBiomes(ocean, shore, flats, hills);
            int r = ((Integer) island.get("rarity")).intValue();
            for (int i = r; i > 0; --i) {
                result.add(ib);
            }
        }
        return result.toArray(new IslandBiomes[result.size()]);
    }

    private static int biomeId(final String name) {
        return world.getBiomeIdByName(name);
    }

    public static void setWorld(final LocalWorld w) {
        world = w;
    }
}
