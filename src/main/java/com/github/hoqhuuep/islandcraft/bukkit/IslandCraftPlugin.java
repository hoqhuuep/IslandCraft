package com.github.hoqhuuep.islandcraft.bukkit;

import java.util.List;

import javax.persistence.PersistenceException;

import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.hoqhuuep.islandcraft.bukkit.command.BetterCompassCommandExecutor;
import com.github.hoqhuuep.islandcraft.bukkit.command.LocalChatCommandExecutor;
import com.github.hoqhuuep.islandcraft.bukkit.command.PartyChatCommandExecutor;
import com.github.hoqhuuep.islandcraft.bukkit.command.PrivateMessageCommandExecutor;
import com.github.hoqhuuep.islandcraft.bukkit.command.PurchasingCommandExecutor;
import com.github.hoqhuuep.islandcraft.bukkit.command.SuicideCommandExecutor;
import com.github.hoqhuuep.islandcraft.bukkit.ebeanserver.CompassTargetBean;
import com.github.hoqhuuep.islandcraft.bukkit.ebeanserver.EbeanServerDatabase;
import com.github.hoqhuuep.islandcraft.bukkit.event.BetterClockListener;
import com.github.hoqhuuep.islandcraft.bukkit.event.BetterCompassListener;
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
import com.github.hoqhuuep.islandcraft.common.purchasing.Purchasing;
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
        try {
            getDatabase().find(CompassTargetBean.class).findRowCount();
        } catch (PersistenceException e) {
            installDDL();
        }

        // Generator
        TerrainControl.getBiomeModeManager().register("IslandCraft", IslandCraftBiomeGenerator.class);

        // Purchasing
        PurchasingCommandExecutor purchasing = new PurchasingCommandExecutor(new Purchasing(getICDatabase(), getICConfig(), getICProtection()), getICServer());
        getCommand("purchase").setExecutor(purchasing);
        getCommand("abandon").setExecutor(purchasing);
        getCommand("examine").setExecutor(purchasing);
        getCommand("rename").setExecutor(purchasing);

        // Chat
        LocalChatCommandExecutor localChat = new LocalChatCommandExecutor(new LocalChat(getICConfig()), getICServer());
        getCommand("l").setExecutor(localChat);
        PartyChatCommandExecutor partyChat = new PartyChatCommandExecutor(new PartyChat(getICDatabase()), getICServer());
        getCommand("p").setExecutor(partyChat);
        getCommand("join").setExecutor(partyChat);
        getCommand("leave").setExecutor(partyChat);
        getCommand("members").setExecutor(partyChat);
        PrivateMessageCommandExecutor privateMessage = new PrivateMessageCommandExecutor(getICServer());
        getCommand("m").setExecutor(privateMessage);

        // UsefulExtras
        register(new BetterClockListener(getICServer()));
        BetterCompass betterCompass = new BetterCompass(getICDatabase());
        register(new BetterCompassListener(betterCompass, getICServer()));
        getCommand("suicide").setExecutor(new SuicideCommandExecutor(getICServer()));
        getCommand("waypoint").setExecutor(new BetterCompassCommandExecutor(betterCompass, getICServer()));
    }

    public ICServer getICServer() {
        if (this.server == null) {
            this.server = new BukkitServer(getServer());
        }
        return this.server;
    }

    public ICConfig getICConfig() {
        if (this.config == null) {
            this.config = new FileConfigurationConfig(getConfig());
        }
        return this.config;
    }

    public ICDatabase getICDatabase() {
        if (this.database == null) {
            this.database = new EbeanServerDatabase(getDatabase());
        }
        return this.database;
    }

    public ICProtection getICProtection() {
        if (this.protection == null) {
            this.protection = new WorldGuardProtection(getWorldGuard());
        }
        return this.protection;
    }

    private void register(final Listener listener) {
        final PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(listener, this);
    }

    private WorldGuardPlugin getWorldGuard() {
        PluginManager pluginManager = getServer().getPluginManager();
        Plugin plugin = pluginManager.getPlugin("WorldGuard");

        // WorldGuard may not be loaded
        if (plugin == null || !(plugin instanceof WorldGuardPlugin)) {
            return null;
        }

        return (WorldGuardPlugin) plugin;
    }
}
