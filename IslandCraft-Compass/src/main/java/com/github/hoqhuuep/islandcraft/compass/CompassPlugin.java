package com.github.hoqhuuep.islandcraft.compass;

import java.util.List;

import javax.persistence.PersistenceException;

import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

public class CompassPlugin extends JavaPlugin {
    @Override
    public void onEnable() {
        // Hack to ensure database exists
        try {
            getDatabase().find(CompassBean.class).findRowCount();
        } catch (PersistenceException e) {
            installDDL();
        }

        final CompassManager compassManager = new CompassManager(new CompassDatabase(getDatabase()));
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
