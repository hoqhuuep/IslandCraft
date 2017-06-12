package com.github.hoqhuuep.islandcraft.bukkit;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
// import org.mcstats.Metrics;

import com.avaje.ebean.EbeanServer;
import com.github.hoqhuuep.islandcraft.api.ICLocation;
import com.github.hoqhuuep.islandcraft.api.ICRegion;
import com.github.hoqhuuep.islandcraft.api.IslandCraft;
import com.github.hoqhuuep.islandcraft.core.DefaultIslandCraft;
import com.github.hoqhuuep.islandcraft.core.ICLogger;
import com.github.hoqhuuep.islandcraft.core.IslandDatabase;
import com.github.hoqhuuep.islandcraft.nms.NmsWrapper;

public class IslandCraftPlugin extends JavaPlugin {

	private DefaultIslandCraft islandCraft = null;

	@Override
	public void onEnable() {
		ICLogger.logger = new JavaUtilLogger(getLogger());

		// https://github.com/Hidendra/Plugin-Metrics/wiki/Usage
// Temporarily disabled due to incompatibility with 1.9
//		try {
//			final Metrics metrics = new Metrics(this);
//			metrics.start();
//		} catch (final Exception e) {
//			ICLogger.logger.warning("Failed to start MCStats");
//		}

		saveDefaultConfig();
		FileConfiguration config = getConfig();
		if (!config.contains("config-version") || !config.isString("config-version")) {
			ICLogger.logger.error("No string-value for 'config-version' found in config.yml");
			ICLogger.logger.error("Check for updates at http://dev.bukkit.org/bukkit-plugins/islandcraft/");
			setEnabled(false);
			return;
		}
		final String configVersion = config.getString("config-version");
		if (!configVersion.equals("1.0.0")) {
			ICLogger.logger.error("Incompatible config-version found in config.yml");
			ICLogger.logger.error("Check for updates at http://dev.bukkit.org/bukkit-plugins/islandcraft/");
			setEnabled(false);
			return;
		}

		if (!config.contains("verbose-logging") || !config.isBoolean("verbose-logging")) {
			ICLogger.logger.warning("No boolean-value for 'verbose-logging' found in config.yml");
			ICLogger.logger.warning("Default value 'false' will be used");
		}
		final boolean verboseLogging = config.getBoolean("verbose-logging", false);
		getLogger().setLevel(verboseLogging ? Level.ALL : Level.WARNING);

		final NmsWrapper nms = NmsWrapper.getInstance(getServer());
		if (nms == null) {
			ICLogger.logger.error("IslandCraft does not currently support this CraftBukkit version");
			ICLogger.logger.error("Check for updates at http://dev.bukkit.org/bukkit-plugins/islandcraft/");
			setEnabled(false);
			return;
		}

		IslandDatabase database;
		try {
			final EbeanServer ebeanServer = EbeanServerUtil.build(this);
			database = new EbeanServerIslandDatabase(ebeanServer);
		} catch (final Exception e) {
			ICLogger.logger.error("Error creating EbeanServer database");
			ICLogger.logger.error("Check for updates at http://dev.bukkit.org/bukkit-plugins/islandcraft/");
			ICLogger.logger.error("Exception message: " + e.getMessage());
			setEnabled(false);
			return;
		}

		try {
			islandCraft = new DefaultIslandCraft();
			final Listener listener = new BiomeGeneratorListener(this, database, nms);
			getServer().getPluginManager().registerEvents(listener, this);
		} catch (final Exception e) {
			ICLogger.logger.error("Error creating or registering BiomeGeneratorListener");
			ICLogger.logger.error("Check for updates at http://dev.bukkit.org/bukkit-plugins/islandcraft/");
			ICLogger.logger.error("Exception message: " + e.getMessage());
			setEnabled(false);
			return;
		}
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender instanceof Player) {
			sender.sendMessage("This command can only be executed by console!");
			return false;
		}
		if (args.length == 0) {
			sender.sendMessage("/ic <reload|create>");
			return false;
		}
		if ("reload".equals(args[0])) {
			sender.sendMessage("Reloaded IslandCraft configuration");
			reloadConfig();
		} else if ("create".equals(args[0])) {
			// ic create 2 1 ConstantBiomeDistribution IslandGeneratorAlpha
			if (args.length < 4) {
				sender.sendMessage("/ic create <world> <island-size> <ocean-size>");
				sender.sendMessage("OR");
				sender.sendMessage("/ic create <world> <island-size> <ocean-size> [distribution] [generator]");
				sender.sendMessage("Note: An island size of 1 = 4 chunks");
				return false;
			}
			String world = args[1];
			int island;
			int ocean;
			try {
				island = Integer.parseInt(args[2]) * 32;
				ocean = Integer.parseInt(args[3]) * 32;
			} catch (Exception e) {
				sender.sendMessage("Invalid size for:");
				sender.sendMessage("/ic create <world> <island-size> <ocean-size> [distribution] [generator]");
				return false;
			}
			String distribution = "SquareIslandDistribution";
			String generator = "IslandGeneratorAlpha";
			if (args.length > 4) {
				distribution = args[4];
			}
			if (args.length > 5) {
				distribution = args[5];
			}
			FileConfiguration config = getConfig();
			String path = "worlds." + world;
			config.set(path + "." + "ocean",
					"com.github.hoqhuuep.islandcraft.core.ConstantBiomeDistribution DEEP_OCEAN");
			config.set(path + "." + "island-distribution",
					"com.github.hoqhuuep.islandcraft.core." + distribution + " " + island + " " + ocean);
			String[] gen_types = {
					"com.github.hoqhuuep.islandcraft.core." + generator
							+ " BIRCH_FOREST BIRCH_FOREST_M BIRCH_FOREST_HILLS BIRCH_FOREST_HILLS_M ~ ~ OCEAN BEACH RIVER",
					"com.github.hoqhuuep.islandcraft.core." + generator
							+ " COLD_TAIGA COLD_TAIGA_M COLD_TAIGA_HILLS ~ ~ ~ OCEAN COLD_BEACH FROZEN_RIVER",
					"com.github.hoqhuuep.islandcraft.core." + generator
							+ " DESERT DESERT_M DESERT_HILLS ~ ~ ~ OCEAN BEACH RIVER",
					"com.github.hoqhuuep.islandcraft.core." + generator
							+ " EXTREME_HILLS EXTREME_HILLS_M EXTREME_HILLS_PLUS EXTREME_HILLS_PLUS_M EXTREME_HILLS_EDGE ~ OCEAN STONE_BEACH RIVER",
					"com.github.hoqhuuep.islandcraft.core." + generator
							+ " FOREST ~ FOREST_HILLS ~ FLOWER_FOREST ~ OCEAN BEACH RIVER",
					"com.github.hoqhuuep.islandcraft.core." + generator
							+ " ICE_PLAINS ~ ICE_MOUNTAINS ~ ICE_PLAINS_SPIKES ~ OCEAN FROZEN_OCEAN FROZEN_RIVER",
					"com.github.hoqhuuep.islandcraft.core." + generator
							+ " JUNGLE JUNGLE_M JUNGLE_HILLS ~ JUNGLE_EDGE JUNGLE_EDGE_M OCEAN BEACH RIVER",
					"com.github.hoqhuuep.islandcraft.core." + generator
							+ " MEGA_TAIGA MEGA_SPRUCE_TAIGA MEGA_TAIGA_HILLS MEGA_SPRUCE_TAIGA_HILLS ~ ~ OCEAN BEACH RIVER",
					"com.github.hoqhuuep.islandcraft.core." + generator
							+ " MESA MESA_BRYCE MESA_PLATEAU MESA_PLATEAU_M MESA_PLATEAU_F MESA_PLATEAU_F_M OCEAN MESA RIVER",
					"com.github.hoqhuuep.islandcraft.core." + generator
							+ " MUSHROOM_ISLAND ~ ~ ~ ~ ~ OCEAN MUSHROOM_ISLAND_SHORE RIVER",
					"com.github.hoqhuuep.islandcraft.core." + generator
							+ " PLAINS ~ SUNFLOWER_PLAINS ~ ~ ~ OCEAN BEACH RIVER",
					"com.github.hoqhuuep.islandcraft.core." + generator
							+ " ROOFED_FOREST ROOFED_FOREST_M ~ ~ ~ ~ OCEAN BEACH RIVER",
					"com.github.hoqhuuep.islandcraft.core." + generator
							+ " SAVANNA SAVANNA_M SAVANNA_PLATEAU SAVANNA_PLATEAU_M ~ ~ OCEAN BEACH RIVER",
					"com.github.hoqhuuep.islandcraft.core." + generator
							+ " SWAMPLAND SWAMPLAND_M ~ ~ ~ ~ OCEAN BEACH RIVER",
					"com.github.hoqhuuep.islandcraft.core." + generator
							+ " TAIGA TAIGA_M TAIGA_HILLS ~ ~ ~ OCEAN BEACH RIVER" };

			config.set(path + "." + "island-generators", gen_types);

			sender.sendMessage("Saving configuration!");
			saveConfig();
			reloadConfig();
			sender.sendMessage("Generating world; please wait...");
			if ((Bukkit.getPluginManager().getPlugin("Multiverse-Core") != null)
					&& Bukkit.getPluginManager().getPlugin("Multiverse-Core").isEnabled()) {
				Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(),
						"mv create " + world + " normal");
			} else {
				if ((Bukkit.getPluginManager().getPlugin("MultiWorld") != null)
						&& Bukkit.getPluginManager().getPlugin("MultiWorld").isEnabled()) {
					Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "mw create " + world);
				} else {
					Bukkit.createWorld(new WorldCreator(world).environment(World.Environment.NORMAL));
				}
			}
			sender.sendMessage("Done!");
		} else {
			sender.sendMessage("/ic <reload|create>");
		}
		return true;
	}

	@Override
	public void onDisable() {
		ICLogger.logger = null;
	}

	public List<Class<?>> getDatabaseClasses() {
		final Class<?>[] classes = { EbeanServerIslandDatabase.IslandBean.class,
				EbeanServerIslandDatabase.IslandPK.class };
		return Arrays.asList(classes);
	}

	public IslandCraft getIslandCraft() {
		return islandCraft;
	}

	private static final int BLOCKS_PER_CHUNK = 16;

	public void regenerate(final World world, final ICRegion region) {
		final ICLocation min = region.getMin();
		final ICLocation max = region.getMax();
		final int minX = min.getX() / BLOCKS_PER_CHUNK;
		final int minZ = min.getZ() / BLOCKS_PER_CHUNK;
		final int maxX = max.getX() / BLOCKS_PER_CHUNK;
		final int maxZ = max.getZ() / BLOCKS_PER_CHUNK;
		// Must loop from high to low for trees to generate correctly
		for (int x = maxX - 1; x >= minX; --x) {
			for (int z = maxZ - 1; z >= minZ; --z) {
				// TODO queue these?
				world.regenerateChunk(x, z);
			}
		}
	}
}
