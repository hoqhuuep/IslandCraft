package com.github.hoqhuuep.islandcraft.bukkit;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import javax.persistence.PersistenceException;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.EbeanServerFactory;
import com.avaje.ebean.config.DataSourceConfig;
import com.avaje.ebean.config.ServerConfig;
import com.avaje.ebean.config.dbplatform.SQLitePlatform;
import com.avaje.ebeaninternal.api.SpiEbeanServer;
import com.avaje.ebeaninternal.server.ddl.DdlGenerator;
import com.avaje.ebeaninternal.server.lib.sql.TransactionIsolation;
import com.github.hoqhuuep.islandcraft.core.ICLogger;

public class EbeanServerUtil {
    public static EbeanServer build(final IslandCraftPlugin javaPlugin) {
        ICLogger.logger.info("Creating EbeanServer for plugin with name: " + javaPlugin.getDescription().getName());
        final String name = javaPlugin.getDescription().getName();
        final ConfigurationSection configurationSection = javaPlugin.getConfig().getConfigurationSection("database");
        final String driver = configurationSection.getString("driver");
        final String url = replaceDatabaseString(configurationSection.getString("url"), javaPlugin);
        final String username = configurationSection.getString("username");
        final String password = configurationSection.getString("password");
        final int isolationLevel = TransactionIsolation.getLevel(configurationSection.getString("isolation"));
        final List<Class<?>> classes = javaPlugin.getDatabaseClasses();

        final ServerConfig serverConfig = new ServerConfig();
        serverConfig.setDefaultServer(false);
        serverConfig.setRegister(false);
        serverConfig.setClasses(classes);
        serverConfig.setName(name);

        final DataSourceConfig dataSourceConfig = new DataSourceConfig();
        dataSourceConfig.setDriver(driver);
        dataSourceConfig.setUrl(url);
        dataSourceConfig.setUsername(username);
        dataSourceConfig.setPassword(password);
        dataSourceConfig.setIsolationLevel(isolationLevel);

        if (driver.contains("sqlite")) {
            serverConfig.setDatabasePlatform(new SQLitePlatform());
            serverConfig.getDatabasePlatform().getDbDdlSyntax().setIdentity("");
        }
        serverConfig.setDataSourceConfig(dataSourceConfig);

        javaPlugin.getDataFolder().mkdirs();
        final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        Thread.currentThread().setContextClassLoader(getClassLoader(javaPlugin));
        final EbeanServer ebeanServer = EbeanServerFactory.create(serverConfig);
        Thread.currentThread().setContextClassLoader(classLoader);

        // Hack to ensure database exists
        try {
            ebeanServer.find(classes.get(0)).findRowCount();
        } catch (final PersistenceException e) {
            createDdl(ebeanServer);
        }

        return ebeanServer;
    }

    public static void createDdl(final EbeanServer ebeanServer) {
        final SpiEbeanServer spiEbeanServer = (SpiEbeanServer) ebeanServer;
        final DdlGenerator ddlGenerator = spiEbeanServer.getDdlGenerator();
        ddlGenerator.runScript(false, ddlGenerator.generateCreateDdl());
    }

    public static void dropDdl(final EbeanServer ebeanServer) {
        final SpiEbeanServer spiEbeanServer = (SpiEbeanServer) ebeanServer;
        final DdlGenerator ddlGenerator = spiEbeanServer.getDdlGenerator();
        ddlGenerator.runScript(true, ddlGenerator.generateDropDdl());
    }

    private static String replaceDatabaseString(String input, final JavaPlugin plugin) {
        input = input.replaceAll("\\{DIR\\}", plugin.getDataFolder().getPath().replaceAll("\\\\", "/") + "/");
        input = input.replaceAll("\\{NAME\\}", plugin.getDescription().getName().replaceAll("[^\\w_-]", ""));
        return input;
    }

    private static ClassLoader getClassLoader(final JavaPlugin javaPlugin) {
        try {
            final Method method = JavaPlugin.class.getDeclaredMethod("getClassLoader");
            method.setAccessible(true);
            return (ClassLoader) method.invoke(javaPlugin);
        } catch (SecurityException e) {
            throw new RuntimeException("Failed to retrieve the ClassLoader of the plugin using Reflection", e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Failed to retrieve the ClassLoader of the plugin using Reflection", e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Failed to retrieve the ClassLoader of the plugin using Reflection", e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException("Failed to retrieve the ClassLoader of the plugin using Reflection", e);
        }
    }

    private EbeanServerUtil() {
        // Utility class
    }
}
