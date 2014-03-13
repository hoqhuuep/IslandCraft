package com.github.hoqhuuep.islandcraft.worldgenerator;

import java.util.Arrays;
import java.util.List;

import javax.persistence.PersistenceException;

import org.bukkit.plugin.java.JavaPlugin;

import com.avaje.ebean.EbeanServer;
import com.github.hoqhuuep.islandcraft.worldgenerator.hack.HackListener;

public class WorldGeneratorPlugin extends JavaPlugin {
	@Override
	public void onEnable() {
		saveDefaultConfig();
		final EbeanServer database = getDatabase();
		// Hack to ensure database exists
		try {
			database.find(IslandBean.class).findRowCount();
		} catch (final PersistenceException e) {
			installDDL();
		}

		final WorldGeneratorConfig config = new WorldGeneratorConfig(getConfig());
		getServer().getPluginManager().registerEvents(new HackListener(config, new WorldGeneratorDatabase(getDatabase(), config)), this);
	}

	@Override
	public List<Class<?>> getDatabaseClasses() {
		final Class<?>[] classes = { IslandBean.class, SerializableLocation.class };
		return Arrays.asList(classes);
	}
}
