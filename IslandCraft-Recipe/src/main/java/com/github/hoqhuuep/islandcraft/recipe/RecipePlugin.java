package com.github.hoqhuuep.islandcraft.recipe;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.TreeSpecies;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.material.SmoothBrick;
import org.bukkit.material.Step;
import org.bukkit.material.Tree;
import org.bukkit.plugin.java.JavaPlugin;

public class RecipePlugin extends JavaPlugin implements Listener {
	private final List<ItemStack> results = new ArrayList<ItemStack>();

	@Override
	public void onEnable() {
		final Server server = getServer();

		// Old logs - Block ID 17
		for (final TreeSpecies logSpecies : new TreeSpecies[] { TreeSpecies.GENERIC, TreeSpecies.REDWOOD, TreeSpecies.BIRCH, TreeSpecies.JUNGLE }) {
			for (final TreeSpecies woodSpecies : TreeSpecies.values()) {
				System.out.println(logSpecies + " + " + woodSpecies);
				final ShapelessRecipe boat = new ShapelessRecipe(new ItemStack(Material.BOAT, 1));
				boat.addIngredient(1, new Tree(logSpecies));
				final Tree wood = new Tree(Material.WOOD);
				wood.setSpecies(woodSpecies);
				boat.addIngredient(1, wood);
				server.addRecipe(boat);
			}
		}
		// Acacia and Dark Oak - Block ID 162
		for (final TreeSpecies logSpecies : new TreeSpecies[] { TreeSpecies.GENERIC, TreeSpecies.REDWOOD }) {
			for (final TreeSpecies woodSpecies : TreeSpecies.values()) {
				System.out.println(logSpecies + " + " + woodSpecies);
				final ShapelessRecipe boat = new ShapelessRecipe(new ItemStack(Material.BOAT, 1));
				final Tree log = new Tree(Material.LOG_2);
				log.setSpecies(logSpecies);
				boat.addIngredient(1, log);
				final Tree wood = new Tree(Material.WOOD);
				wood.setSpecies(woodSpecies);
				boat.addIngredient(1, wood);
				server.addRecipe(boat);
			}
		}

		final ShapelessRecipe mossyStoneBricks = new ShapelessRecipe(new SmoothBrick(Material.MOSSY_COBBLESTONE).toItemStack(1));
		mossyStoneBricks.addIngredient(1, Material.SMOOTH_BRICK);
		mossyStoneBricks.addIngredient(1, Material.VINE);
		server.addRecipe(mossyStoneBricks);

		final ShapelessRecipe crackedStoneBricks = new ShapelessRecipe(new SmoothBrick(Material.COBBLESTONE).toItemStack(1));
		crackedStoneBricks.addIngredient(1, Material.SMOOTH_BRICK);
		crackedStoneBricks.addIngredient(1, Material.FLINT);
		server.addRecipe(crackedStoneBricks);

		final ShapedRecipe chiseledStoneBricks = new ShapedRecipe(new SmoothBrick(Material.SMOOTH_BRICK).toItemStack(1));
		chiseledStoneBricks.shape("s", "s");
		chiseledStoneBricks.setIngredient('s', new Step(Material.SMOOTH_BRICK));
		server.addRecipe(chiseledStoneBricks);

		server.getPluginManager().registerEvents(this, this);
		results.add(new ItemStack(Material.BOAT, 1));
		results.add(new SmoothBrick(Material.MOSSY_COBBLESTONE).toItemStack(1));
		results.add(new SmoothBrick(Material.COBBLESTONE).toItemStack(1));
		results.add(new SmoothBrick(Material.SMOOTH_BRICK).toItemStack(1));
	}

	@EventHandler
	public void onCraftItem(final CraftItemEvent event) {
		// This is a temporary work-around because recipes do not sync with the
		// client correctly. See this bug:
		// https://bukkit.atlassian.net/browse/BUKKIT-117
		if (results.contains(event.getRecipe().getResult())) {
			getServer().getScheduler().runTask(this, new Runnable() {
				@Override
				public void run() {
					for (final HumanEntity entity : event.getViewers()) {
						if (entity instanceof Player) {
							((Player) entity).updateInventory();
						}
					}
				}
			});
		}
	}
}
