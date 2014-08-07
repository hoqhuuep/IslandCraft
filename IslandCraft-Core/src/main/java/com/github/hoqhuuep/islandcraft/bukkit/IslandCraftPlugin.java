package com.github.hoqhuuep.islandcraft.bukkit;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import com.avaje.ebean.EbeanServer;
import com.github.hoqhuuep.islandcraft.api.ICLocation;
import com.github.hoqhuuep.islandcraft.api.ICRegion;
import com.github.hoqhuuep.islandcraft.api.IslandCraft;
import com.github.hoqhuuep.islandcraft.core.DefaultIslandCraft;
import com.github.hoqhuuep.islandcraft.core.EbeanServerIslandDatabase;
import com.github.hoqhuuep.islandcraft.core.EbeanServerUtil;
import com.github.hoqhuuep.islandcraft.core.ICLogger;
import com.github.hoqhuuep.islandcraft.core.IslandDatabase;
import com.github.hoqhuuep.islandcraft.nms.NmsWrapper;

public class IslandCraftPlugin extends JavaPlugin {
    private DefaultIslandCraft islandCraft = null;

    @Override
    public void onEnable() {
        ICLogger.logger = getLogger();

        saveDefaultConfig();
        final ConfigurationSection config = getConfig();
        if (!config.isString("config-version")) {
            ICLogger.logger.severe("No string-value for 'config-version' found in config.yml");
            ICLogger.logger.severe("Check for updates at http://dev.bukkit.org/bukkit-plugins/islandcraft/");
            setEnabled(false);
            return;
        }
        final String configVersion = config.getString("config-version");
        if (!configVersion.equals("1.0.0")) {
            ICLogger.logger.severe("Incompatible config-version found in config.yml");
            ICLogger.logger.severe("Check for updates at http://dev.bukkit.org/bukkit-plugins/islandcraft/");
            setEnabled(false);
            return;
        }

        if (!config.isBoolean("verbose-logging")) {
            ICLogger.logger.warning("No boolean-value for 'verbose-logging' found in config.yml");
            ICLogger.logger.warning("Default value 'false' will be used");
        }
        final boolean verboseLogging = config.getBoolean("verbose-logging", false);
        ICLogger.logger.setLevel(verboseLogging ? Level.ALL : Level.WARNING);

        final NmsWrapper nms = NmsWrapper.getInstance(getServer());
        if (nms == null) {
            ICLogger.logger.severe("IslandCraft does not currently support this CraftBukkit version");
            ICLogger.logger.severe("Check for updates at http://dev.bukkit.org/bukkit-plugins/islandcraft/");
            setEnabled(false);
            return;
        }

        IslandDatabase database;
        try {
            final EbeanServer ebeanServer = EbeanServerUtil.build(this);
            database = new EbeanServerIslandDatabase(ebeanServer);
        } catch (final Exception e) {
            ICLogger.logger.severe("Error creating EbeanServer database");
            ICLogger.logger.severe("Check for updates at http://dev.bukkit.org/bukkit-plugins/islandcraft/");
            ICLogger.logger.severe("Exception message: " + e.getMessage());
            setEnabled(false);
            return;
        }

        try {
            islandCraft = new DefaultIslandCraft();
            final Listener listener = new BiomeGeneratorListener(islandCraft, config, database, nms);
            getServer().getPluginManager().registerEvents(listener, this);
        } catch (final Exception e) {
            ICLogger.logger.severe("Error creating or registering BiomeGeneratorListener");
            ICLogger.logger.severe("Check for updates at http://dev.bukkit.org/bukkit-plugins/islandcraft/");
            ICLogger.logger.severe("Exception message: " + e.getMessage());
            setEnabled(false);
            return;
        }
    }

    @Override
    public void onDisable() {
        ICLogger.logger = null;
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
