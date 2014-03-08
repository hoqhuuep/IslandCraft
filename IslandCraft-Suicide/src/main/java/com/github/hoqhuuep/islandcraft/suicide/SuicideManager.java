package com.github.hoqhuuep.islandcraft.suicide;

import org.bukkit.entity.Player;

public class SuicideManager {
	public void suicide(final Player player) {
		player.setHealth(0.0);
	}
}
