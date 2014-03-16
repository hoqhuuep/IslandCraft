package com.github.hoqhuuep.islandcraft.realestate;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import javax.persistence.PersistenceException;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import com.avaje.ebean.EbeanServer;

public class RealEstatePlugin extends JavaPlugin {
	private static final Logger log = Logger.getLogger("Minecraft");
	private Economy economy = null;
	private RealEstateDatabase database = null;
	private RealEstateConfig config = null;

	public void onEnable() {
		final PluginManager pluginManager = getServer().getPluginManager();

		final String[] files = { "messages_en.properties" };
		for (String name : files) {
			final File file = new File(getDataFolder(), name);
			if (!file.exists()) {
				saveResource("messages_en.properties", false);
			}
		}
		try {
			final URL[] urls = { getDataFolder().toURI().toURL() };
			final ClassLoader loader = new URLClassLoader(urls);
			final ResourceBundle messages = ResourceBundle.getBundle("messages", Locale.getDefault(), loader);
			Message.setBundle(messages);
		} catch (final MalformedURLException e) {
			e.printStackTrace();
		}

		// Database
		if (!setupDatabase()) {
			log.severe(String.format("[%s] - Disabled due to database error!", getDescription().getName()));
			pluginManager.disablePlugin(this);
			return;
		}

		// Configuration
		if (!setupConfig()) {
			log.severe(String.format("[%s] - Disabled due to config error!", getDescription().getName()));
			pluginManager.disablePlugin(this);
			return;
		}

		// Vault Economy
		if (!setupEconomy()) {
			log.severe(String.format("[%s] - Disabled due to no Vault economy dependency found!", getDescription().getName()));
			pluginManager.disablePlugin(this);
			return;
		}

		// Manager
		final RealEstateManager realEstateManager = new RealEstateManager(database, config, economy);

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
		pluginManager.registerEvents(new WorldLoadListener(realEstateManager, config), this);
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

	private boolean setupEconomy() {
		try {
			final RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(Economy.class);
			if (economyProvider != null) {
				economy = economyProvider.getProvider();
			}
			return economy != null;
		} catch (final Exception e) {
			return false;
		}
	}

	private boolean setupDatabase() {
		try {
			// Hack to ensure database exists
			try {
				getDatabase().find(IslandDeed.class).findRowCount();
			} catch (final PersistenceException e) {
				installDDL();
			}
			final EbeanServer ebean = getDatabase();
			if (ebean == null) {
				return false;
			}
			database = new RealEstateDatabase(ebean);
			return true;
		} catch (final Exception e) {
			return false;
		}
	}

	private boolean setupConfig() {
		saveDefaultConfig();
		final FileConfiguration fileConfig = getConfig();
		if (fileConfig == null) {
			return false;
		}
		config = new RealEstateConfig(fileConfig);
		return true;
	}
}
