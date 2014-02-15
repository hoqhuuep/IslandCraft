package com.github.hoqhuuep.islandcraft.realestate;

import java.util.Arrays;
import java.util.List;

import javax.persistence.PersistenceException;

import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.avaje.ebean.EbeanServer;

public class RealEstatePlugin extends JavaPlugin {
    public void onEnable() {
        final EbeanServer database = getDatabase();
        // Hack to ensure database exists
        try {
            database.find(IslandDeed.class).findRowCount();
        } catch (final PersistenceException e) {
            installDDL();
        }

        // Manager
        final RealEstateManager realEstateManager = new RealEstateManager(new RealEstateDatabase(getDatabase()), getConfig());

        // Commands
        final IslandCommandExecutor islandCommandExecutor = new IslandCommandExecutor(realEstateManager);
        final PluginCommand islandCommand = getCommand("island");
        islandCommand.setExecutor(islandCommandExecutor);
        islandCommand.setTabCompleter(islandCommandExecutor);

        // Events
        final PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new WorldLoadListener(realEstateManager), this);
        pluginManager.registerEvents(new WorldInitListener(this), this);
        pluginManager.registerEvents(new DawnListener(realEstateManager), this);
        pluginManager.registerEvents(new ChunkLoadListener(realEstateManager), this);
        pluginManager.registerEvents(new PlayerMoveListener(realEstateManager), this);
    }

    @Override
    public FileConfiguration getConfig() {
        final FileConfiguration config = super.getConfig();
        config.addDefault("purchase-cost-item", "DIAMOND");
        config.addDefault("purchase-cost-amount", 1);
        config.addDefault("purchase-cost-increase", 1);
        config.addDefault("tax-cost-item", "DIAMOND");
        config.addDefault("tax-cost-amount", 1);
        config.addDefault("tax-cost-increase", 1);
        config.addDefault("tax-days-initial", 500);
        config.addDefault("tax-days-increase", 500);
        config.addDefault("max-islands-per-player", 7);
        return config;
    }

    @Override
    public List<Class<?>> getDatabaseClasses() {
        final Class<?>[] classes = {IslandDeed.class, SerializableLocation.class, SerializableRegion.class};
        return Arrays.asList(classes);
    }
}
