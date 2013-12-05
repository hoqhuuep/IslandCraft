package com.github.hoqhuuep.islandcraft.explicitsuicide;

import org.bukkit.command.CommandExecutor;
import org.bukkit.plugin.java.JavaPlugin;

public class ExplicitSuicidePlugin extends JavaPlugin {
	private final ExplicitSuicideManager explicitSuicideManager;

	public ExplicitSuicidePlugin() {
		explicitSuicideManager = new ExplicitSuicideManager();
	}

	public ExplicitSuicideManager getExplicitSuicideManager() {
		return explicitSuicideManager;
	}

	@Override
	public void onEnable() {
		final CommandExecutor suicideCommandExecutor = new SuicideCommandExecutor(
				explicitSuicideManager);
		getCommand("suicide").setExecutor(suicideCommandExecutor);
	}
}
