package com.github.hoqhuuep.islandcraft.realestate;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;

public class RealEstateConfig {

	public final Material PURCHASE_COST_ITEM;
	public final int PURCHASE_COST_AMOUNT;
	public final int PURCHASE_COST_INCREASE;
	public final Material TAX_COST_ITEM;
	public final int TAX_COST_AMOUNT;
	public final int TAX_COST_INCREASE;
	public final int TAX_DAYS_INITIAL;
	public final int TAX_DAYS_INCREASE;
	public final int TAX_DAYS_MAX;
	public final int MAX_ISLANDS_PER_PLAYER;
	public final Map<String, WorldConfig> WORLD_CONFIGS;

	public final String M_ISLAND_EXAMINE_RESOURCE;
	public final String M_ISLAND_EXAMINE_RESERVED;
	public final String M_ISLAND_EXAMINE_NEW;
	public final String M_ISLAND_EXAMINE_ABANDONED;
	public final String M_ISLAND_EXAMINE_REPOSSESSED;
	public final String M_ISLAND_EXAMINE_PRIVATE;

	public RealEstateConfig(final ConfigurationSection config) {
		PURCHASE_COST_ITEM = Material.matchMaterial(config.getString("purchase-cost-item"));
		PURCHASE_COST_AMOUNT = config.getInt("purchase-cost-amount");
		PURCHASE_COST_INCREASE = config.getInt("purchase-cost-increase");
		TAX_COST_ITEM = Material.matchMaterial(config.getString("tax-cost-item"));
		TAX_COST_AMOUNT = config.getInt("tax-cost-amount");
		TAX_COST_INCREASE = config.getInt("tax-cost-increase");
		TAX_DAYS_INITIAL = config.getInt("tax-days-initial");
		TAX_DAYS_INCREASE = config.getInt("tax-days-increase");
		TAX_DAYS_MAX = config.getInt("tax-days-max");
		MAX_ISLANDS_PER_PLAYER = config.getInt("max-islands-per-player");

		// Validate configuration values
		if (PURCHASE_COST_ITEM == null) {
			Bukkit.getLogger().severe("IslandCraft-RealEstate config.yml issue. " + config.getCurrentPath() + ".purchase-cost-item must be a valid item name");
		}
		if (PURCHASE_COST_AMOUNT < 0) {
			Bukkit.getLogger().severe("IslandCraft-RealEstate config.yml issue. " + config.getCurrentPath() + ".purchase-cost-amount must not be negative");
		}
		if (PURCHASE_COST_INCREASE < 0) {
			Bukkit.getLogger().severe("IslandCraft-RealEstate config.yml issue. " + config.getCurrentPath() + ".purchase-cost-increase must not be negative");
		}
		if (TAX_COST_ITEM == null) {
			Bukkit.getLogger().severe("IslandCraft-RealEstate config.yml issue. " + config.getCurrentPath() + ".tax-cost-item must be a valid item name");
		}
		if (TAX_COST_AMOUNT < 0) {
			Bukkit.getLogger().severe("IslandCraft-RealEstate config.yml issue. " + config.getCurrentPath() + ".tax-cost-amount must not be negative");
		}
		if (TAX_COST_INCREASE < 0) {
			Bukkit.getLogger().severe("IslandCraft-RealEstate config.yml issue. " + config.getCurrentPath() + ".tax-cost-increase must not be negative");
		}
		if (TAX_DAYS_INITIAL < -1) {
			Bukkit.getLogger().severe("IslandCraft-RealEstate config.yml issue. " + config.getCurrentPath() + ".tax-days-initial must not be less than -1");
		}
		if (TAX_DAYS_INCREASE < 0) {
			Bukkit.getLogger().severe("IslandCraft-RealEstate config.yml issue. " + config.getCurrentPath() + ".tax-increase must not be negative");
		}
		if (TAX_DAYS_MAX < -1) {
			Bukkit.getLogger().severe("IslandCraft-RealEstate config.yml issue. " + config.getCurrentPath() + ".tax-days-max must not be less than -1");
		}
		if (MAX_ISLANDS_PER_PLAYER < -1) {
			Bukkit.getLogger().severe("IslandCraft-RealEstate config.yml issue. " + config.getCurrentPath() + ".tax-days-max must not be less than -1");
		}

		WORLD_CONFIGS = new HashMap<String, WorldConfig>();
		final ConfigurationSection worlds = config.getConfigurationSection("worlds");
		for (final String key : worlds.getKeys(false)) {
			WORLD_CONFIGS.put(key, new WorldConfig(worlds.getConfigurationSection(key)));
		}

		final ConfigurationSection message = config.getConfigurationSection("message");
		M_ISLAND_EXAMINE_RESOURCE = message.getString("island-examine-resource");
		M_ISLAND_EXAMINE_RESERVED = message.getString("island-examine-reserved");
		M_ISLAND_EXAMINE_NEW = message.getString("island-examine-new");
		M_ISLAND_EXAMINE_ABANDONED = message.getString("island-examine-abandoned");
		M_ISLAND_EXAMINE_REPOSSESSED = message.getString("island-examine-repossessed");
		M_ISLAND_EXAMINE_PRIVATE = message.getString("island-examine-private");
	}
}
