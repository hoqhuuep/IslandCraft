package com.github.hoqhuuep.islandcraft.compass;

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
        final CompassManager compassManager = new CompassManager(database, getConfig());
        final WaypointCommandExecutor waypointCommandExecutor = new WaypointCommandExecutor(compassManager);
        getServer().getPluginManager().registerEvents(new CompassListener(compassManager), this);
        final PluginCommand waypointCommand = getCommand("waypoint");
        waypointCommand.setExecutor(waypointCommandExecutor);
        waypointCommand.setTabCompleter(waypointCommandExecutor);
    }

    @Override
    public List<Class<?>> getDatabaseClasses() {
        return CompassDatabase.getDatabaseClasses();
    }
}
