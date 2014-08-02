package com.github.hoqhuuep.islandcraft.bukkit;

import java.util.Arrays;
import java.util.List;

import org.bukkit.World;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.hoqhuuep.islandcraft.api.ICLocation;
import com.github.hoqhuuep.islandcraft.api.ICRegion;
import com.github.hoqhuuep.islandcraft.api.IslandCraft;
import com.github.hoqhuuep.islandcraft.core.DefaultIslandCraft;
import com.github.hoqhuuep.islandcraft.core.EbeanServerIslandDatabase;
import com.github.hoqhuuep.islandcraft.core.EbeanServerUtil;
import com.github.hoqhuuep.islandcraft.core.IslandDatabase;
import com.github.hoqhuuep.islandcraft.nms.NmsWrapper;

public class IslandCraftPlugin extends JavaPlugin {
    private DefaultIslandCraft islandCraft = null;

    @Override
    public void onEnable() {
        final NmsWrapper nms = NmsWrapper.getInstance(getServer());
        if (nms == null) {
            getLogger().severe("Could not find support for this CraftBukkit version");
            getLogger().info("Check for updates at http://dev.bukkit.org/bukkit-plugins/islandcraft/");
            setEnabled(false);
            return;
        }

        saveDefaultConfig();
        if (!getConfig().getString("config-version").equals("1.0.0")) {
            getLogger().severe("Incompatible config-version found in config.yml");
            getLogger().info("Check for updates at http://dev.bukkit.org/bukkit-plugins/islandcraft/");
            setEnabled(false);
            return;
        }

        final IslandDatabase database = new EbeanServerIslandDatabase(EbeanServerUtil.build(this));

        islandCraft = new DefaultIslandCraft();

        final Listener listener = new BiomeGeneratorListener(islandCraft, database, getConfig(), nms);

        getServer().getPluginManager().registerEvents(listener, this);
    }

    @Override
    public List<Class<?>> getDatabaseClasses() {
        final Class<?>[] classes = { EbeanServerIslandDatabase.IslandBean.class, EbeanServerIslandDatabase.IslandPK.class };
        return Arrays.asList(classes);
    }

    public IslandCraft getIslandCraft() {
        return islandCraft;
    }

    private static final int BLOCKS_PER_CHUNK = 16;

    public void regenerate(final World world, final ICRegion region) {
        final ICLocation min = region.getMin();
        final ICLocation max = region.getMax();
        final int minX = min.getX() / BLOCKS_PER_CHUNK;
        final int minZ = min.getZ() / BLOCKS_PER_CHUNK;
        final int maxX = max.getX() / BLOCKS_PER_CHUNK;
        final int maxZ = max.getZ() / BLOCKS_PER_CHUNK;
        // Must loop from high to low for trees to generate correctly
        for (int x = maxX - 1; x >= minX; --x) {
            for (int z = maxZ - 1; z >= minZ; --z) {
                // TODO queue these?
                world.regenerateChunk(x, z);
            }
        }
    }
}
