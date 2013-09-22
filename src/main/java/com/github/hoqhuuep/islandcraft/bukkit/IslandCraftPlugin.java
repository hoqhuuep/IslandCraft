package com.github.hoqhuuep.islandcraft.bukkit;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.logging.Level;

import javax.persistence.PersistenceException;

import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.hoqhuuep.islandcraft.bukkit.command.ICSudoCommandExecutor;
import com.github.hoqhuuep.islandcraft.bukkit.command.IslandCommandExecutor;
import com.github.hoqhuuep.islandcraft.bukkit.command.LocalMessageCommandExecutor;
import com.github.hoqhuuep.islandcraft.bukkit.command.PartyCommandExecutor;
import com.github.hoqhuuep.islandcraft.bukkit.command.PartyMessageCommandExecutor;
import com.github.hoqhuuep.islandcraft.bukkit.command.PrivateMessageCommandExecutor;
import com.github.hoqhuuep.islandcraft.bukkit.command.SuicideCommandExecutor;
import com.github.hoqhuuep.islandcraft.bukkit.command.WaypointCommandExecutor;
import com.github.hoqhuuep.islandcraft.bukkit.config.IslandCraftConfig;
import com.github.hoqhuuep.islandcraft.bukkit.database.CompassBean;
import com.github.hoqhuuep.islandcraft.bukkit.database.EbeanServerDatabase;
import com.github.hoqhuuep.islandcraft.bukkit.event.ClockListener;
import com.github.hoqhuuep.islandcraft.bukkit.event.CompassListener;
import com.github.hoqhuuep.islandcraft.bukkit.event.DawnListener;
import com.github.hoqhuuep.islandcraft.bukkit.event.WorldInitListener;
import com.github.hoqhuuep.islandcraft.bukkit.terraincontrol.IslandCraftBiomeGenerator;
import com.github.hoqhuuep.islandcraft.bukkit.worldguard.WorldGuardProtection;
import com.github.hoqhuuep.islandcraft.common.api.ICDatabase;
import com.github.hoqhuuep.islandcraft.common.api.ICProtection;
import com.github.hoqhuuep.islandcraft.common.api.ICServer;
import com.github.hoqhuuep.islandcraft.common.chat.LocalChat;
import com.github.hoqhuuep.islandcraft.common.chat.PartyChat;
import com.github.hoqhuuep.islandcraft.common.extras.BetterClock;
import com.github.hoqhuuep.islandcraft.common.extras.BetterCompass;
import com.github.hoqhuuep.islandcraft.common.extras.Suicide;
import com.github.hoqhuuep.islandcraft.common.island.Island;
import com.khorn.terraincontrol.TerrainControl;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

public final class IslandCraftPlugin extends JavaPlugin {
    private IslandCraftConfig config;
    private ICDatabase database;

    @Override
    public List<Class<?>> getDatabaseClasses() {
        return EbeanServerDatabase.getDatabaseClasses();
    }

    @Override
    public void onEnable() {
        getConfig().options().copyDefaults(true);
        saveConfig();
        getLanguageConfig().options().copyDefaults(true);
        saveLanguageConfig();
        try {
            getDatabase().find(CompassBean.class).findRowCount();
        } catch (PersistenceException e) {
            installDDL();
        }

        config = new IslandCraftConfig(getConfig());
        database = new EbeanServerDatabase(getDatabase());
        final ICServer server = new BukkitServer(getServer(), config, new Language(getLanguageConfig()));
        final ICProtection protection = new WorldGuardProtection(getWorldGuard());

        // Generator
        TerrainControl.getBiomeModeManager().register("IslandCraft", IslandCraftBiomeGenerator.class);

        // Island Commands
        final Island island = new Island(getICDatabase(), protection, config.getMaxIslandsPerPlayer(), config.getPurchaseCostItem(),
                config.getPurchaseCostAmount(), config.getPurchaseCostAmount(), config.getTaxCostItem(), config.getTaxCostAmount(), config.getTaxCostIncrease());
        final IslandCommandExecutor islandCommandExecutor = new IslandCommandExecutor(island, server);
        final PluginCommand islandCommand = getCommand("island");
        islandCommand.setExecutor(islandCommandExecutor);
        islandCommand.setTabCompleter(islandCommandExecutor);

        // Dawn (for tax system)
        register(new WorldInitListener(this));
        register(new DawnListener(island));

        // Chat Commands
        final PrivateMessageCommandExecutor privateMessageCommandExecutor = new PrivateMessageCommandExecutor(server);
        final LocalMessageCommandExecutor localMessageCommandExecutor = new LocalMessageCommandExecutor(new LocalChat(config.getLocalChatRadius()), server);
        final PartyChat partyChat = new PartyChat(getICDatabase());
        final PartyMessageCommandExecutor partyMessageCommandExecutor = new PartyMessageCommandExecutor(partyChat, server);
        final PartyCommandExecutor partyCommandExecutor = new PartyCommandExecutor(partyChat, server);
        getCommand("m").setExecutor(privateMessageCommandExecutor);
        getCommand("l").setExecutor(localMessageCommandExecutor);
        getCommand("p").setExecutor(partyMessageCommandExecutor);
        final PluginCommand partyCommand = getCommand("party");
        partyCommand.setExecutor(partyCommandExecutor);
        partyCommand.setTabCompleter(partyCommandExecutor);

        // Administrative commands
        final ICSudoCommandExecutor icsudoCommandExecutor = new ICSudoCommandExecutor(server, database);
        final PluginCommand icsudoCommand = getCommand("icsudo");
        icsudoCommand.setExecutor(icsudoCommandExecutor);
        icsudoCommand.setTabCompleter(icsudoCommandExecutor);

        // Extras
        final BetterCompass betterCompass = new BetterCompass(getICDatabase());
        final WaypointCommandExecutor waypointCommandExecutor = new WaypointCommandExecutor(betterCompass, server);
        final SuicideCommandExecutor suicideCommandExecutor = new SuicideCommandExecutor(new Suicide(), server);
        getCommand("suicide").setExecutor(suicideCommandExecutor);
        register(new ClockListener(new BetterClock(), server));
        register(new CompassListener(betterCompass, server));
        final PluginCommand waypointCommand = getCommand("waypoint");
        waypointCommand.setExecutor(waypointCommandExecutor);
        waypointCommand.setTabCompleter(waypointCommandExecutor);
    }

    public IslandCraftConfig getICConfig() {
        return config;
    }

    public ICDatabase getICDatabase() {
        return database;
    }

    private void register(final Listener listener) {
        final PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(listener, this);
    }

    private WorldGuardPlugin getWorldGuard() {
        final PluginManager pluginManager = getServer().getPluginManager();
        final Plugin plugin = pluginManager.getPlugin("WorldGuard");

        // WorldGuard may not be loaded
        if (null == plugin || !(plugin instanceof WorldGuardPlugin)) {
            return null;
        }

        return (WorldGuardPlugin) plugin;
    }

    private File languageConfigFile;
    private FileConfiguration languageConfig;

    private void reloadLanguageConfig() {
        if (null == languageConfigFile) {
            languageConfigFile = new File(getDataFolder(), "language.yml");
        }
        // Look for defaults in the jar
        languageConfig = YamlConfiguration.loadConfiguration(languageConfigFile);
        final InputStream defConfigStream = getResource("language.yml");
        if (null != defConfigStream) {
            final YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
            languageConfig.setDefaults(defConfig);
        }
    }

    private FileConfiguration getLanguageConfig() {
        if (null == languageConfig) {
            reloadLanguageConfig();
        }
        return languageConfig;
    }

    private void saveLanguageConfig() {
        if (null == languageConfig || null == languageConfigFile) {
            return;
        }
        try {
            getLanguageConfig().save(languageConfigFile);
        } catch (IOException ex) {
            this.getLogger().log(Level.SEVERE, "Could not save config to " + languageConfigFile, ex);
        }
    }
}
