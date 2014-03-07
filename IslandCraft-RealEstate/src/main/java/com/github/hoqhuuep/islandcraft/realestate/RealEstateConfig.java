package com.github.hoqhuuep.islandcraft.realestate;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;

public class RealEstateConfig {
	public static class Message {
		private final String format;

		private Message(final String format) {
			this.format = format;
		}

		public void send(final CommandSender to, final Object... args) {
			to.sendMessage(String.format(format, args));
		}
	}

	public static class WorldConfig {
		public final int ISLAND_SIZE;
		public final int ISLAND_SEPARATION;
		public final double RESOURCE_ISLAND_RARITY;

		public WorldConfig(final ConfigurationSection config) {
			ISLAND_SIZE = config.getInt("island-size");
			ISLAND_SEPARATION = config.getInt("island-separation");
			RESOURCE_ISLAND_RARITY = config.getInt("resource-island-rarity");

			// Validate configuration values
			if (ISLAND_SIZE <= 0 || ISLAND_SIZE % 32 != 0) {
				Bukkit.getLogger().severe("IslandCraft-RealEstate config.yml issue. " + config.getCurrentPath() + ".island-size must be a positive multiple of 32");
			}
			if (ISLAND_SEPARATION <= ISLAND_SIZE || ISLAND_SEPARATION % 32 != 0) {
				Bukkit.getLogger().severe("IslandCraft-RealEstate config.yml issue. " + config.getCurrentPath() + ".island-separation must be a multiple of 32 greater than " + config.getCurrentPath() + ".island-size");
			}
			if (RESOURCE_ISLAND_RARITY < 0.0 || RESOURCE_ISLAND_RARITY > 1.0) {
				Bukkit.getLogger().severe("IslandCraft-RealEstate config.yml issue. " + config.getCurrentPath() + ".resource-island-rarity must be between 0.0 and 1.0");
			}
		}
	}

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

	public final Message M_ISLAND_ABANDON;
	public final Message M_ISLAND_ABANDON_WORLD_ERROR;
	public final Message M_ISLAND_ABANDON_OCEAN_ERROR;
	public final Message M_ISLAND_ABANDON_OWNER_ERROR;
	public final Message M_ISLAND_PURCHASE;
	public final Message M_ISLAND_PURCHASE_WORLD_ERROR;
	public final Message M_ISLAND_PURCHASE_OCEAN_ERROR;
	public final Message M_ISLAND_PURCHASE_SELF_ERROR;
	public final Message M_ISLAND_PURCHASE_OTHER_ERROR;
	public final Message M_ISLAND_PURCHASE_RESERVED_ERROR;
	public final Message M_ISLAND_PURCHASE_RESOURCE_ERROR;
	public final Message M_ISLAND_PURCHASE_FUNDS_ERROR;
	public final Message M_ISLAND_PURCHASE_MAX_ERROR;
	public final Message M_ISLAND_TAX;
	public final Message M_ISLAND_TAX_WORLD_ERROR;
	public final Message M_ISLAND_TAX_OCEAN_ERROR;
	public final Message M_ISLAND_TAX_OWNER_ERROR;
	public final Message M_ISLAND_TAX_MAX_ERROR;
	public final Message M_ISLAND_TAX_FUNDS_ERROR;
	public final Message M_ISLAND_RENAME;
	public final Message M_ISLAND_RENAME_WORLD_ERROR;
	public final Message M_ISLAND_RENAME_OCEAN_ERROR;
	public final Message M_ISLAND_RENAME_OWNER_ERROR;
	public final Message M_ISLAND_EXAMINE_RESOURCE;
	public final Message M_ISLAND_EXAMINE_RESERVED;
	public final Message M_ISLAND_EXAMINE_NEW;
	public final Message M_ISLAND_EXAMINE_ABANDONED;
	public final Message M_ISLAND_EXAMINE_REPOSSESSED;
	public final Message M_ISLAND_EXAMINE_PRIVATE;
	public final Message M_ISLAND_EXAMINE_WORLD_ERROR;
	public final Message M_ISLAND_EXAMINE_OCEAN_ERROR;
	public final Message M_ISLAND_ENTER_RESERVED;
	public final Message M_ISLAND_LEAVE_RESERVED;
	public final Message M_ISLAND_ENTER_RESOURCE;
	public final Message M_ISLAND_LEAVE_RESOURCE;
	public final Message M_ISLAND_ENTER_NEW;
	public final Message M_ISLAND_LEAVE_NEW;
	public final Message M_ISLAND_ENTER_ABANDONED;
	public final Message M_ISLAND_LEAVE_ABANDONED;
	public final Message M_ISLAND_ENTER_REPOSSESSED;
	public final Message M_ISLAND_LEAVE_REPOSSESSED;
	public final Message M_ISLAND_ENTER_PRIVATE;
	public final Message M_ISLAND_LEAVE_PRIVATE;
	public final Message M_ICSET_WORLD_ERROR;
	public final Message M_ICSET_OCEAN_ERROR;
	public final Message M_ICSET;

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
		M_ISLAND_ABANDON = new Message(message.getString("island-abandon"));
		M_ISLAND_ABANDON_WORLD_ERROR = new Message(message.getString("island-abandon-world-error"));
		M_ISLAND_ABANDON_OCEAN_ERROR = new Message(message.getString("island-abandon-ocean-error"));
		M_ISLAND_ABANDON_OWNER_ERROR = new Message(message.getString("island-abandon-owner-error"));
		M_ISLAND_PURCHASE = new Message(message.getString("island-purchase"));
		M_ISLAND_PURCHASE_WORLD_ERROR = new Message(message.getString("island-purchase-world-error"));
		M_ISLAND_PURCHASE_OCEAN_ERROR = new Message(message.getString("island-purchase-ocean-error"));
		M_ISLAND_PURCHASE_SELF_ERROR = new Message(message.getString("island-purchase-self-error"));
		M_ISLAND_PURCHASE_OTHER_ERROR = new Message(message.getString("island-purchase-other-error"));
		M_ISLAND_PURCHASE_RESERVED_ERROR = new Message(message.getString("island-purchase-reserved-error"));
		M_ISLAND_PURCHASE_RESOURCE_ERROR = new Message(message.getString("island-purchase-resource-error"));
		M_ISLAND_PURCHASE_FUNDS_ERROR = new Message(message.getString("island-purchase-funds-error"));
		M_ISLAND_PURCHASE_MAX_ERROR = new Message(message.getString("island-purchase-max-error"));
		M_ISLAND_TAX = new Message(message.getString("island-tax"));
		M_ISLAND_TAX_WORLD_ERROR = new Message(message.getString("island-tax-world-error"));
		M_ISLAND_TAX_OCEAN_ERROR = new Message(message.getString("island-tax-ocean-error"));
		M_ISLAND_TAX_OWNER_ERROR = new Message(message.getString("island-tax-owner-error"));
		M_ISLAND_TAX_MAX_ERROR = new Message(message.getString("island-tax-max-error"));
		M_ISLAND_TAX_FUNDS_ERROR = new Message(message.getString("island-tax-funds-error"));
		M_ISLAND_RENAME = new Message(message.getString("island-rename"));
		M_ISLAND_RENAME_WORLD_ERROR = new Message(message.getString("island-rename-world-error"));
		M_ISLAND_RENAME_OCEAN_ERROR = new Message(message.getString("island-rename-ocean-error"));
		M_ISLAND_RENAME_OWNER_ERROR = new Message(message.getString("island-rename-owner-error"));
		M_ISLAND_EXAMINE_RESOURCE = new Message(message.getString("island-examine-resource"));
		M_ISLAND_EXAMINE_RESERVED = new Message(message.getString("island-examine-reserved"));
		M_ISLAND_EXAMINE_NEW = new Message(message.getString("island-examine-new"));
		M_ISLAND_EXAMINE_ABANDONED = new Message(message.getString("island-examine-abandoned"));
		M_ISLAND_EXAMINE_REPOSSESSED = new Message(message.getString("island-examine-repossessed"));
		M_ISLAND_EXAMINE_PRIVATE = new Message(message.getString("island-examine-private"));
		M_ISLAND_EXAMINE_WORLD_ERROR = new Message(message.getString("island-examine-world-error"));
		M_ISLAND_EXAMINE_OCEAN_ERROR = new Message(message.getString("island-examine-ocean-error"));
		M_ISLAND_ENTER_RESERVED = new Message(message.getString("island-enter-reserved"));
		M_ISLAND_LEAVE_RESERVED = new Message(message.getString("island-leave-reserved"));
		M_ISLAND_ENTER_RESOURCE = new Message(message.getString("island-enter-resource"));
		M_ISLAND_LEAVE_RESOURCE = new Message(message.getString("island-leave-resource"));
		M_ISLAND_ENTER_NEW = new Message(message.getString("island-enter-new"));
		M_ISLAND_LEAVE_NEW = new Message(message.getString("island-leave-new"));
		M_ISLAND_ENTER_ABANDONED = new Message(message.getString("island-enter-abandoned"));
		M_ISLAND_LEAVE_ABANDONED = new Message(message.getString("island-leave-abandoned"));
		M_ISLAND_ENTER_REPOSSESSED = new Message(message.getString("island-enter-repossessed"));
		M_ISLAND_LEAVE_REPOSSESSED = new Message(message.getString("island-leave-repossessed"));
		M_ISLAND_ENTER_PRIVATE = new Message(message.getString("island-enter-private"));
		M_ISLAND_LEAVE_PRIVATE = new Message(message.getString("island-leave-private"));
		M_ICSET_WORLD_ERROR = new Message(message.getString("icset-world-error"));
		M_ICSET_OCEAN_ERROR = new Message(message.getString("icset-ocean-error"));
		M_ICSET = new Message(message.getString("icset"));
	}
}
