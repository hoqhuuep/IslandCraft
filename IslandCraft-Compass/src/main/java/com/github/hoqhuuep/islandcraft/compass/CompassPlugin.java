package com.github.hoqhuuep.islandcraft.compass;

import java.util.Arrays;
import java.util.List;

import javax.persistence.PersistenceException;

import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

public class CompassPlugin extends JavaPlugin {
	@Override
	public void onEnable() {
		saveDefaultConfig();

		// Hack to ensure database exists
		try {
			getDatabase().find(CompassBean.class).findRowCount();
		} catch (PersistenceException e) {
			installDDL();
		}

		final CompassDatabase database = new CompassDatabase(getDatabase());
		final CompassConfig config = new CompassConfig(getConfig());
		final CompassManager manager = new CompassManager(database, config);
		final WaypointCommandExecutor commandExecutor = new WaypointCommandExecutor(manager);
		getServer().getPluginManager().registerEvents(new CompassListener(manager), this);
		final PluginCommand waypointCommand = getCommand("waypoint");
		waypointCommand.setExecutor(commandExecutor);
		waypointCommand.setTabCompleter(commandExecutor);
	}

	@Override
	public List<Class<?>> getDatabaseClasses() {
		final Class<?>[] classes = { CompassBean.class, WaypointBean.class };
		return Arrays.asList(classes);
	}
}
