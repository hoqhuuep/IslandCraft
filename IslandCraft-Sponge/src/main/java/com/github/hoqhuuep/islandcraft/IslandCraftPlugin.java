package com.github.hoqhuuep.islandcraft;

import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.world.gen.WorldGeneratorModifier;

import com.github.hoqhuuep.islandcraft.core.ICLogger;
import com.github.hoqhuuep.islandcraft.core.ICNoise;
import com.google.inject.Inject;

@Plugin(id = "islandcraft", name = "IslandCraft", version = "1.0.3-SNAPSHOT")
public class IslandCraftPlugin {
	@Inject
	private Logger logger;

	@Listener
	public void onGameInitialization(GameInitializationEvent event) {
		ICLogger.logger = new Slf4jLogger(logger);
		ICNoise.builder = new FlowNoiseBuilder();

		Sponge.getRegistry().register(WorldGeneratorModifier.class, new IslandCraftGenerationModifier());
	}
}
