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
import com.github.hoqhuuep.islandcraft.bukkit.ebeanserver.CompassBean;
import com.github.hoqhuuep.islandcraft.bukkit.ebeanserver.EbeanServerDatabase;
import com.github.hoqhuuep.islandcraft.bukkit.event.ClockListener;
import com.github.hoqhuuep.islandcraft.bukkit.event.CompassListener;
import com.github.hoqhuuep.islandcraft.bukkit.fileconfiguration.FileConfigurationConfig;
import com.github.hoqhuuep.islandcraft.bukkit.terraincontrol.IslandCraftBiomeGenerator;
import com.github.hoqhuuep.islandcraft.bukkit.worldguard.WorldGuardProtection;
import com.github.hoqhuuep.islandcraft.common.api.ICConfig;
import com.github.hoqhuuep.islandcraft.common.api.ICDatabase;
import com.github.hoqhuuep.islandcraft.common.api.ICProtection;
import com.github.hoqhuuep.islandcraft.common.api.ICServer;
import com.github.hoqhuuep.islandcraft.common.chat.LocalChat;
import com.github.hoqhuuep.islandcraft.common.chat.PartyChat;
import com.github.hoqhuuep.islandcraft.common.extras.BetterCompass;
import com.github.hoqhuuep.islandcraft.common.island.Island;
import com.khorn.terraincontrol.TerrainControl;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

public final class IslandCraftPlugin extends JavaPlugin {
    private ICServer server;
    private ICConfig config;
    private ICDatabase database;
    private ICProtection protection;

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

        // Generator
        TerrainControl.getBiomeModeManager().register("IslandCraft", IslandCraftBiomeGenerator.class);

        // Island Commands
        final IslandCommandExecutor islandCommandExecutor = new IslandCommandExecutor(new Island(getICDatabase(), getICConfig(), getICProtection()),
                getICServer());
        final PluginCommand islandCommand = getCommand("island");
        islandCommand.setExecutor(islandCommandExecutor);
        islandCommand.setTabCompleter(islandCommandExecutor);

        // Chat Commands
        final PrivateMessageCommandExecutor privateMessageCommandExecutor = new PrivateMessageCommandExecutor(getICServer());
        final LocalMessageCommandExecutor localMessageCommandExecutor = new LocalMessageCommandExecutor(new LocalChat(getICConfig()), getICServer());
        final PartyChat partyChat = new PartyChat(getICDatabase());
        final PartyMessageCommandExecutor partyMessageCommandExecutor = new PartyMessageCommandExecutor(partyChat, getICServer());
        final PartyCommandExecutor partyCommandExecutor = new PartyCommandExecutor(partyChat, getICServer());
        getCommand("m").setExecutor(privateMessageCommandExecutor);
        getCommand("l").setExecutor(localMessageCommandExecutor);
        getCommand("p").setExecutor(partyMessageCommandExecutor);
        final PluginCommand partyCommand = getCommand("party");
        partyCommand.setExecutor(partyCommandExecutor);
        partyCommand.setTabCompleter(partyCommandExecutor);

        // Administrative commands
        ICSudoCommandExecutor icsudoCommandExecutor = new ICSudoCommandExecutor();
        final PluginCommand icsudoCommand = getCommand("icsudo");
        icsudoCommand.setExecutor(icsudoCommandExecutor);
        icsudoCommand.setTabCompleter(icsudoCommandExecutor);

        // Extras
        final BetterCompass betterCompass = new BetterCompass(getICDatabase());
        final WaypointCommandExecutor waypointCommandExecutor = new WaypointCommandExecutor(betterCompass, getICServer());
        final SuicideCommandExecutor suicideCommandExecutor = new SuicideCommandExecutor(getICServer());
        getCommand("suicide").setExecutor(suicideCommandExecutor);
        register(new ClockListener(getICServer()));
        register(new CompassListener(betterCompass, getICServer()));
        final PluginCommand waypointCommand = getCommand("waypoint");
        waypointCommand.setExecutor(waypointCommandExecutor);
        waypointCommand.setTabCompleter(waypointCommandExecutor);
    }

    public ICServer getICServer() {
        if (server == null) {
            server = new BukkitServer(getServer(), new Language(getLanguageConfig()));
        }
        return server;
    }

    public ICConfig getICConfig() {
        if (config == null) {
            config = new FileConfigurationConfig(getConfig());
        }
        return config;
    }

    public ICDatabase getICDatabase() {
        if (database == null) {
            database = new EbeanServerDatabase(getDatabase());
        }
        return database;
    }

    public ICProtection getICProtection() {
        if (protection == null) {
            protection = new WorldGuardProtection(getWorldGuard());
        }
        return protection;
    }

    private void register(final Listener listener) {
        final PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(listener, this);
    }

    private WorldGuardPlugin getWorldGuard() {
        final PluginManager pluginManager = getServer().getPluginManager();
        final Plugin plugin = pluginManager.getPlugin("WorldGuard");

        // WorldGuard may not be loaded
        if (plugin == null || !(plugin instanceof WorldGuardPlugin)) {
            return null;
        }

        return (WorldGuardPlugin) plugin;
    }

    private File languageConfigFile;
    private FileConfiguration languageConfig;

    private void reloadLanguageConfig() {
        if (languageConfigFile == null) {
            languageConfigFile = new File(getDataFolder(), "language.yml");
        }
        // Look for defaults in the jar
        languageConfig = YamlConfiguration.loadConfiguration(languageConfigFile);
        @SuppressWarnings("resource")
        InputStream defConfigStream = getResource("language.yml");
        if (defConfigStream != null) {
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
            languageConfig.setDefaults(defConfig);
        }
    }

    private FileConfiguration getLanguageConfig() {
        if (languageConfig == null) {
            reloadLanguageConfig();
        }
        return languageConfig;
    }

    private void saveLanguageConfig() {
        if (languageConfig == null || languageConfigFile == null) {
            return;
        }
        try {
            getLanguageConfig().save(languageConfigFile);
        } catch (IOException ex) {
            this.getLogger().log(Level.SEVERE, "Could not save config to " + languageConfigFile, ex);
        }
    }
}
