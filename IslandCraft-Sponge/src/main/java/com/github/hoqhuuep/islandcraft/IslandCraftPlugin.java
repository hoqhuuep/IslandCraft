package com.github.hoqhuuep.islandcraft;

import java.io.IOException;

import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GameStoppedEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.world.gen.WorldGeneratorModifier;

import com.github.hoqhuuep.islandcraft.core.ICLogger;
import com.google.inject.Inject;

import ninja.leaping.configurate.ConfigurationOptions;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;

@Plugin(id = "islandcraft", name = "IslandCraft", version = "1.0.3-SNAPSHOT")
public class IslandCraftPlugin {
	@Inject
	private Logger logger;

	@Inject
	@DefaultConfig(sharedRoot = false)
	private ConfigurationLoader<CommentedConfigurationNode> configLoader;

	private CommentedConfigurationNode config;

	@Listener
	public void onGameInitialization(GameInitializationEvent event) throws IOException {
		ICLogger.logger = new Slf4jLogger(logger);

		// https://github.com/Hidendra/Plugin-Metrics/wiki/Usage
		try {
			Metrics metrics = new Metrics(Sponge.getGame(), Sponge.getPluginManager().fromInstance(this).get());
			metrics.start();
		} catch (final Exception e) {
			ICLogger.logger.warning("Failed to start MCStats");
		}

		config = configLoader.load(ConfigurationOptions.defaults().setShouldCopyDefaults(true));

		Sponge.getRegistry().register(WorldGeneratorModifier.class, new IslandCraftGenerationModifier(config));
	}

	@Listener
	public void onGameStopped(GameStoppedEvent event) throws IOException {
		configLoader.save(config);
	}
}
