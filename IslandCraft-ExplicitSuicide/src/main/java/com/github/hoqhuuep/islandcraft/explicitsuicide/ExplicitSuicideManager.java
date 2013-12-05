package com.github.hoqhuuep.islandcraft.explicitsuicide;

import org.bukkit.entity.Player;

public class ExplicitSuicideManager {
	public void suicide(final Player player) {
		player.setHealth(0);
	}
}
