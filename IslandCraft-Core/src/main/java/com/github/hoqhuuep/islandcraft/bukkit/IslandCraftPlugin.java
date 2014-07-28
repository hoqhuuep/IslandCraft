package com.github.hoqhuuep.islandcraft.bukkit;

import java.util.Arrays;
import java.util.List;

import javax.persistence.PersistenceException;

import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import com.avaje.ebean.EbeanServer;
import com.github.hoqhuuep.islandcraft.api.IslandCraft;
import com.github.hoqhuuep.islandcraft.bukkit.nms.NmsWrapper;
import com.github.hoqhuuep.islandcraft.core.ConcreteIslandCraft;
import com.github.hoqhuuep.islandcraft.database.Database;
import com.github.hoqhuuep.islandcraft.database.IslandBean;
import com.github.hoqhuuep.islandcraft.database.IslandPK;

public class IslandCraftPlugin extends JavaPlugin {
    private IslandCraft islandCraft = null;

    @Override
    public void onEnable() {
        final NmsWrapper nms = NmsWrapper.getInstance(getServer());
        if (nms == null) {
            getLogger().severe("Could not find support for this CraftBukkit version");
            getLogger().info("Check for updates at http://dev.bukkit.org/bukkit-plugins/islandcraft/");
            setEnabled(false);
            return;
        }
        saveDefaultConfig();
        final EbeanServer ebean = getDatabase();
        // Hack to ensure database exists
        try {
            ebean.find(IslandBean.class).findRowCount();
        } catch (final PersistenceException e) {
            installDDL();
        }
        islandCraft = new ConcreteIslandCraft();
        final Database database = new Database(islandCraft, getDatabase());
        final Listener listener = new BiomeGeneratorListener(islandCraft, database, nms);
        getServer().getPluginManager().registerEvents(listener, this);
    }

    @Override
    public List<Class<?>> getDatabaseClasses() {
        final Class<?>[] classes = { IslandBean.class, IslandPK.class };
        return Arrays.asList(classes);
    }

    public IslandCraft getIslandCraft() {
        return islandCraft;
    }
}
