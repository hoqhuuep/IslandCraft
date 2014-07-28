package com.github.hoqhuuep.islandcraft.bukkit;

import java.util.Arrays;
import java.util.List;

import javax.persistence.PersistenceException;

import org.bukkit.World;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import com.avaje.ebean.EbeanServer;
import com.github.hoqhuuep.islandcraft.api.ICLocation;
import com.github.hoqhuuep.islandcraft.api.ICRegion;
import com.github.hoqhuuep.islandcraft.api.ICServer;
import com.github.hoqhuuep.islandcraft.core.ConcreteIslandCraft;
import com.github.hoqhuuep.islandcraft.database.Database;
import com.github.hoqhuuep.islandcraft.database.IslandBean;
import com.github.hoqhuuep.islandcraft.database.IslandPK;
import com.github.hoqhuuep.islandcraft.nms.NmsWrapper;

public class IslandCraftPlugin extends JavaPlugin {
    private ICServer islandCraft = null;

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
        final EbeanServer ebean = getDatabase();
        // Hack to ensure database exists
        try {
            ebean.find(IslandBean.class).findRowCount();
        } catch (final PersistenceException e) {
            installDDL();
        }
        islandCraft = new ConcreteIslandCraft();
        final Database database = new Database(getDatabase());
        final Listener listener = new BiomeGeneratorListener(islandCraft, database, nms);
        getServer().getPluginManager().registerEvents(listener, this);
    }

    @Override
    public List<Class<?>> getDatabaseClasses() {
        final Class<?>[] classes = { IslandBean.class, IslandPK.class };
        return Arrays.asList(classes);
    }

    public ICServer getIslandCraft() {
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
