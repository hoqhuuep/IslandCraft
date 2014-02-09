package com.github.hoqhuuep.islandcraft.realestate;

import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class RealEstatePlugin extends JavaPlugin {
    public void onEnable() {
        final RealEstateManager realEstateManager = new RealEstateManager(new RealEstateDatabase(getDatabase()), config.getMaxIslandsPerPlayer(),
                config.getPurchaseCostItem(), config.getPurchaseCostAmount(), config.getPurchaseCostAmount(), config.getTaxCostItem(),
                config.getTaxCostAmount(), config.getTaxCostIncrease(), config.getTaxDaysInitial(), config.getTaxDaysIncrease(), config.getTaxDaysMax());

        // Commands
        final IslandCommandExecutor islandCommandExecutor = new IslandCommandExecutor(realEstateManager);
        final PluginCommand islandCommand = getCommand("island");
        islandCommand.setExecutor(islandCommandExecutor);
        islandCommand.setTabCompleter(islandCommandExecutor);

        // Events
        final PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new WorldInitListener(this), this);
        pluginManager.registerEvents(new DawnListener(realEstateManager), this);
        pluginManager.registerEvents(new ChunkLoadListener(realEstateManager), this);
        pluginManager.registerEvents(new PlayerMoveListener(realEstateManager), this);
    }
}
