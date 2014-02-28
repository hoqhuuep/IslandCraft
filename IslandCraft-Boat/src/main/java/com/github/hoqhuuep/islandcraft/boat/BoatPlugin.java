package com.github.hoqhuuep.islandcraft.boat;

import java.util.Arrays;
import java.util.List;

import javax.persistence.PersistenceException;

import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class BoatPlugin extends JavaPlugin implements Listener {
	@Override
	public void onEnable() {
		// Hack to ensure database exists
		try {
			getDatabase().find(BoatBean.class).findRowCount();
		} catch (PersistenceException e) {
			installDDL();
		}
		final BoatDatabase database = new BoatDatabase(getDatabase());
		getCommand("boat").setExecutor(new BoatCommandExecutor(database));
		getServer().getPluginManager().registerEvents(new BoatListener(database), this);
	}

	@Override
	public List<Class<?>> getDatabaseClasses() {
		final Class<?>[] classes = { BoatBean.class };
		return Arrays.asList(classes);
	}
}
