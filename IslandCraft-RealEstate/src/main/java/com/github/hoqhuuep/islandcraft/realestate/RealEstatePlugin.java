package com.github.hoqhuuep.islandcraft.realestate;

import java.util.Arrays;
import java.util.List;

import javax.persistence.PersistenceException;

import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.avaje.ebean.EbeanServer;

public class RealEstatePlugin extends JavaPlugin {
	public void onEnable() {
		saveDefaultConfig();
		final EbeanServer database = getDatabase();
		// Hack to ensure database exists
		try {
			database.find(IslandDeed.class).findRowCount();
		} catch (final PersistenceException e) {
			installDDL();
		}

		final RealEstateConfig config = new RealEstateConfig(getConfig());

		// Manager
		final RealEstateManager realEstateManager = new RealEstateManager(new RealEstateDatabase(getDatabase()), config);

		// Commands
		final IslandCommandExecutor islandCommandExecutor = new IslandCommandExecutor(realEstateManager);
		final PluginCommand islandCommand = getCommand("island");
		islandCommand.setExecutor(islandCommandExecutor);
		islandCommand.setTabCompleter(islandCommandExecutor);

		final ICSetCommandExecutor icSetCommandExecutor = new ICSetCommandExecutor(realEstateManager);
		final PluginCommand icSetCommand = getCommand("icset");
		icSetCommand.setExecutor(icSetCommandExecutor);
		icSetCommand.setTabCompleter(icSetCommandExecutor);

		// Events
		final PluginManager pluginManager = getServer().getPluginManager();
		pluginManager.registerEvents(new WorldLoadListener(realEstateManager), this);
		new DawnScheduler(this).run();
		pluginManager.registerEvents(new DawnListener(realEstateManager), this);
		pluginManager.registerEvents(new ChunkLoadListener(realEstateManager), this);
		pluginManager.registerEvents(new PlayerMoveListener(realEstateManager), this);
	}

	@Override
	public List<Class<?>> getDatabaseClasses() {
		final Class<?>[] classes = { IslandDeed.class, SerializableLocation.class, SerializableRegion.class };
		return Arrays.asList(classes);
	}
}
