package com.github.hoqhuuep.islandcraft.core;

import java.util.Arrays;
import java.util.List;

import javax.persistence.PersistenceException;

import org.bukkit.plugin.java.JavaPlugin;

import com.avaje.ebean.EbeanServer;
import com.github.hoqhuuep.islandcraft.bukkit.nms.NmsWrapper;

public class ICTerrainGeneratorPlugin extends JavaPlugin {
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
        final EbeanServer database = getDatabase();
        // Hack to ensure database exists
        try {
            database.find(IslandBean.class).findRowCount();
        } catch (final PersistenceException e) {
            installDDL();
        }
        final ICTerrainGeneratorConfig config = new ICTerrainGeneratorConfig(getConfig());
        getServer().getPluginManager().registerEvents(new HackListener(config, new ICTerrainGeneratorDatabase(getDatabase(), config), nms), this);
    }

    @Override
    public List<Class<?>> getDatabaseClasses() {
        final Class<?>[] classes = { IslandBean.class, SerializableLocation.class };
        return Arrays.asList(classes);
    }
}
