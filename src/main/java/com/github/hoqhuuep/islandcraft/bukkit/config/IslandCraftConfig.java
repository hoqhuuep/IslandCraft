package com.github.hoqhuuep.islandcraft.bukkit.config;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;

public class IslandCraftConfig {
	private final ConfigurationSection config;

	public IslandCraftConfig(final ConfigurationSection config) {
		this.config = config;
	}

	public final int getLocalChatRadius() {
		return config.getInt("local-chat-radius", 128);
	}

	public final String getPurchaseCostItem() {
		final String name = config.getString("purchase-cost-item", "Diamond");
		final Material material = Material.matchMaterial(name);
		if (null == material) {
			return Material.DIAMOND.name();
		}
		return material.name();
	}

	public final int getPurchaseCostAmount() {
		return config.getInt("purchase-cost-amount", 1);
	}

	public final int getPurchaseCostIncrease() {
		return config.getInt("purchase-cost-increase", 1);
	}

	public final String getTaxCostItem() {
		final String name = config.getString("tax-cost-item", "Diamond");
		final Material material = Material.matchMaterial(name);
		if (null == material) {
			return Material.DIAMOND.name();
		}
		return material.name();
	}

	public final int getTaxCostAmount() {
		return config.getInt("tax-cost-amount", 1);
	}

	public final int getTaxCostIncrease() {
		return config.getInt("tax-cost-increase", 1);
	}

	public final WorldConfig getWorldConfig(final String world) {
		return new WorldConfig(config.getConfigurationSection("worlds." + world));
	}

	public final int getMaxIslandsPerPlayer() {
		return config.getInt("max-islands-per-player", 7);
	}

	public int getTaxDaysInitial() {
		return config.getInt("tax-days-initial", 500);
	}

	public int getTaxDaysIncrease() {
		return config.getInt("tax-days-increase", 500);
	}

	public int getTaxDaysMax() {
		return config.getInt("tax-days-max", 2000);
	}
}
