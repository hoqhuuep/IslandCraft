package com.github.hoqhuuep.islandcraft.realestate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import com.github.hoqhuuep.islandcraft.realestate.event.IslandAbandonEvent;
import com.github.hoqhuuep.islandcraft.realestate.event.IslandLoadEvent;
import com.github.hoqhuuep.islandcraft.realestate.event.IslandPurchaseEvent;
import com.github.hoqhuuep.islandcraft.realestate.event.IslandRegenerateEvent;
import com.github.hoqhuuep.islandcraft.realestate.event.IslandRenameEvent;
import com.github.hoqhuuep.islandcraft.realestate.event.IslandRepossessEvent;

public class RealEstateManager {
    private final RealEstateDatabase database;
    private final int maxIslands;
    private final String purchaseItem;
    private final int purchaseCostAmount;
    private final int purchaseCostIncrease;
    private final String taxItem;
    private final int taxCostAmount;
    private final int taxCostIncrease;
    private final int taxDaysInitial;
    private final int taxDaysIncrease;
    private final int taxDaysMax;
    private final Map<String, IslandInfo> lastIsland;

    public RealEstateManager(final RealEstateDatabase database, final int maxIslands, final String purchaseItem, final int purchaseCostAmount,
            final int purchaseCostIncrease, final String taxItem, final int taxCostAmount, final int taxCostIncrease, final int taxDaysInitial,
            final int taxDaysIncrease, final int taxDaysMax) {
        this.database = database;
        this.maxIslands = maxIslands;
        this.purchaseItem = purchaseItem;
        this.purchaseCostAmount = purchaseCostAmount;
        this.purchaseCostIncrease = purchaseCostIncrease;
        this.taxItem = taxItem;
        this.taxCostAmount = taxCostAmount;
        this.taxCostIncrease = taxCostIncrease;
        this.taxDaysInitial = taxDaysInitial;
        this.taxDaysIncrease = taxDaysIncrease;
        this.taxDaysMax = taxDaysMax;
        lastIsland = new HashMap<String, IslandInfo>();
    }

    /**
     * To be called when a chunk is loaded. Creates WorldGuard regions if they do not exist.
     * 
     * @param x
     * @param z
     */
    public void onLoad(final Location location, final long worldSeed) {
        final World world = location.getWorld();
        if (world == null) {
            // Not ready
            return;
        }
        final Geometry geometry = getGeometry(world);
        if (geometry == null) {
            // Not an IslandCraft world
            return;
        }
        for (final Location islandLocation : geometry.getOuterIslands(location)) {
            IslandInfo info = database.loadIsland(islandLocation);
            if (info == null) {
                if (geometry.isSpawn(islandLocation)) {
                    database.saveIsland(islandLocation, IslandStatus.RESERVED, null, "Spawn Island", -1);
                } else if (geometry.isResource(islandLocation, worldSeed)) {
                    database.saveIsland(islandLocation, IslandStatus.RESOURCE, null, "Resource Island", -1);
                } else {
                    database.saveIsland(islandLocation, IslandStatus.NEW, null, "New Island", -1);
                }
                Bukkit.getPluginManager().callEvent(new IslandLoadEvent(info));
            }
        }
    }

    /**
     * To be called when a player tries to abandon the island at their current location.
     * 
     * @param player
     */
    public final void onAbandon(final Player player) {
        final Geometry geometry = getGeometry(player.getWorld());
        if (null == geometry) {
            message(player, "island-abandon-world-error");
            return;
        }
        final Location location = player.getLocation();
        final Location islandLocation = geometry.getInnerIsland(location);
        if (geometry.isOcean(islandLocation)) {
            message(player, "island-abandon-ocean-error");
            return;
        }
        final IslandInfo info = database.loadIsland(islandLocation);
        if (info.getStatus() != IslandStatus.PRIVATE || !info.getOwner().equals(player.getName())) {
            message(player, "island-abandon-owner-error");
            return;
        }

        // Success
        final String title = info.getTitle();
        database.saveIsland(islandLocation, IslandStatus.ABANDONED, player.getName(), title, -1);
        message(player, "island-abandon");
        Bukkit.getPluginManager().callEvent(new IslandAbandonEvent(info, player));
    }

    /**
     * To be called when a player tries to examine the island at their current location.
     * 
     * @param player
     */
    public final void onExamine(final Player player) {
        final Geometry geometry = getGeometry(player.getWorld());
        if (null == geometry) {
            message(player, "island-examine-world-error");
            return;
        }
        final Location location = player.getLocation();
        if (location == null) {
            message(player, "island-examine-range-error");
            return;
        }
        final Location islandLocation = geometry.getInnerIsland(location);
        if (geometry.isOcean(islandLocation)) {
            message(player, "island-examine-ocean-error");
            return;
        }

        final String world = islandLocation.getWorld().getName();
        final int x = islandLocation.getBlockX();
        final int z = islandLocation.getBlockZ();
        final IslandInfo info = database.loadIsland(islandLocation);
        final IslandStatus status = info.getStatus();
        final String title = info.getTitle();
        final String owner = info.getOwner();
        final int tax = info.getTax();
        final String taxString;
        if (tax < 0) {
            taxString = "infinite";
        } else {
            taxString = String.valueOf(tax);
        }
        if (status == IslandStatus.RESOURCE) {
            message(player, "island-examine-resource", title, world, x, z);
        } else if (status == IslandStatus.RESERVED) {
            message(player, "island-examine-reserved", title, world, x, z);
        } else if (status == IslandStatus.NEW) {
            message(player, "island-examine-new", title, world, x, z);
        } else if (status == IslandStatus.ABANDONED) {
            message(player, "island-examine-abandoned", owner, title, world, x, z);
        } else if (status == IslandStatus.REPOSSESSED) {
            message(player, "island-examine-repossessed", owner, title, world, x, z);
        } else if (status == IslandStatus.PRIVATE) {
            message(player, "island-examine-private", owner, title, taxString, world, x, z);
        }
    }

    /**
     * To be called when a player tries to purchase the island at their current location.
     * 
     * @param player
     */
    public final void onPurchase(final Player player) {
        final Geometry geometry = getGeometry(player.getWorld());
        if (null == geometry) {
            message(player, "island-purchase-world-error");
            return;
        }
        final Location location = player.getLocation();
        final Location islandLocation = geometry.getInnerIsland(location);
        if (geometry.isOcean(islandLocation)) {
            message(player, "island-purchase-ocean-error");
            return;
        }

        final IslandInfo info = database.loadIsland(islandLocation);
        final IslandStatus status = info.getStatus();
        final String name = player.getName();

        if (IslandStatus.RESERVED == status) {
            message(player, "island-purchase-reserved-error");
            return;
        }
        if (IslandStatus.RESOURCE == status) {
            message(player, "island-purchase-resource-error");
            return;
        }
        if (IslandStatus.PRIVATE == status) {
            final String owner = info.getOwner();
            if (owner.equals(name)) {
                message(player, "island-purchase-self-error");
            } else {
                message(player, "island-purchase-other-error");
            }
            return;
        }
        if (islandCount(name) >= maxIslands) {
            message(player, "island-purchase-max-error");
            return;
        }

        final int cost = calculatePurchaseCost(name);

        if (!takeItems(player, purchaseItem, cost)) {
            // Insufficient funds
            message(player, "island-purchase-funds-error", Integer.toString(cost));
            return;
        }

        // Success
        database.saveIsland(islandLocation, IslandStatus.PRIVATE, name, "Private Island", taxDaysInitial);
        message(player, "island-purchase");
        Bukkit.getPluginManager().callEvent(new IslandPurchaseEvent(info, player));
    }

    public void onTax(final Player player) {
        final Geometry geometry = getGeometry(player.getWorld());
        if (null == geometry) {
            message(player, "island-tax-world-error");
            return;
        }
        final Location location = player.getLocation();
        final Location islandLocation = geometry.getInnerIsland(location);
        if (geometry.isOcean(islandLocation)) {
            message(player, "island-tax-ocean-error");
            return;
        }
        final String name = player.getName();
        final IslandInfo island = database.loadIsland(islandLocation);
        if (island.getStatus() != IslandStatus.PRIVATE || !island.getOwner().equals(name)) {
            message(player, "island-tax-owner-error");
            return;
        }

        final int newTax = island.getTax() + taxDaysIncrease;
        if (newTax > taxDaysMax) {
            message(player, "island-tax-max-error");
            return;
        }

        final int cost = calculateTaxCost(name);

        if (!takeItems(player, taxItem, cost)) {
            // Insufficient funds
            message(player, "island-tax-funds-error", Integer.toString(cost));
            return;
        }

        // Success
        final String title = island.getTitle();
        database.saveIsland(islandLocation, IslandStatus.PRIVATE, name, title, newTax);
        message(player, "island-tax");
    }

    public void onDawn(final World world) {
        final Geometry geometry = getGeometry(world);
        if (geometry == null) {
            // Not an IslandCraft world
            return;
        }
        final List<IslandInfo> islands = database.loadIslandsByWorld(world.getName());
        for (IslandInfo info : islands) {
            final Location islandLocation = info.getLocation();
            final int tax = info.getTax();
            final IslandStatus status = info.getStatus();
            final String owner = info.getOwner();
            final String title = info.getTitle();
            if (tax > 0) {
                // Decrement tax
                database.saveIsland(islandLocation, status, owner, title, tax - 1);
            } else if (tax == 0) {
                if (status == IslandStatus.PRIVATE) {
                    // Repossess island
                    database.saveIsland(islandLocation, IslandStatus.REPOSSESSED, owner, title, -1);
                    Bukkit.getPluginManager().callEvent(new IslandRepossessEvent(info));
                } else {
                    // TODO regenerate island + update tax
                    if (status == IslandStatus.REPOSSESSED || status == IslandStatus.ABANDONED) {
                        database.saveIsland(islandLocation, IslandStatus.NEW, null, "New Island", -1);
                        Bukkit.getPluginManager().callEvent(new IslandRegenerateEvent(info));
                    }
                }
            }
            // tax < 0 == infinite
        }
    }

    /**
     * To be called when the player tries to rename the island at their current location.
     * 
     * @param player
     * @param title
     */
    public final void onRename(final Player player, final String title) {
        final Geometry geometry = getGeometry(player.getWorld());
        if (null == geometry) {
            message(player, "island-rename-world-error");
            return;
        }
        final Location location = player.getLocation();
        final Location islandLocation = geometry.getInnerIsland(location);
        if (geometry.isOcean(islandLocation)) {
            message(player, "island-rename-ocean-error");
            return;
        }
        final String name = player.getName();
        final IslandInfo info = database.loadIsland(islandLocation);
        final IslandStatus status = info.getStatus();
        if (status != IslandStatus.PRIVATE || !info.getOwner().equals(name)) {
            message(player, "island-rename-owner-error");
            return;
        }

        // Success
        final int tax = info.getTax();
        database.saveIsland(islandLocation, status, name, title, tax);
        message(player, "island-rename");
        Bukkit.getPluginManager().callEvent(new IslandRenameEvent(info));
    }

    // public void onWarp(final Player player) {
    // final List<IslandInfo> islands = database.loadIslands();
    // Collections.shuffle(islands);
    // for (final IslandInfo island : islands) {
    // final IslandStatus type = island.getStatus();
    // if (type == IslandStatus.NEW || type == IslandStatus.ABANDONED || type == IslandStatus.REPOSSESSED) {
    // final Location islandLocation = island.getLocation();
    // player.teleport(islandLocation);
    // message(player, "island-warp");
    // return;
    // }
    // }
    // message(player, "island-warp-error");
    // }

    private int calculatePurchaseCost(final String player) {
        return purchaseCostAmount + islandCount(player) * purchaseCostIncrease;
    }

    private int calculateTaxCost(final String player) {
        return taxCostAmount + (islandCount(player) - 1) * taxCostIncrease;
    }

    private int islandCount(final String player) {
        final List<IslandInfo> islands = database.loadIslandsByOwner(player);
        int count = 0;
        for (final IslandInfo island : islands) {
            if (island.getStatus() == IslandStatus.PRIVATE) {
                ++count;
            }
        }
        return count;
    }

    private Geometry getGeometry(final World world) {
        // TODO
        return null;
    }

    private static final Integer FIRST = new Integer(0);

    public boolean takeItems(final Player player, final String item, final int amount) {
        final Material material = Material.getMaterial(item);
        final PlayerInventory inventory = player.getInventory();
        if (!inventory.containsAtLeast(new ItemStack(material), amount)) {
            // Not enough
            return false;
        }
        final Map<Integer, ItemStack> result = inventory.removeItem(new ItemStack(material, amount));
        if (!result.isEmpty()) {
            // Something went wrong, refund
            final int missing = result.get(FIRST).getAmount();
            inventory.addItem(new ItemStack(material, amount - missing));
            return false;
        }
        // Success
        return true;
    }

    public void onMove(final Player player, final Location to) {
        final String name = player.getName();
        if (to == null) {
            lastIsland.remove(name);
            return;
        }
        final Geometry geometry = getGeometry(to.getWorld());
        final IslandInfo toIsland;
        if (geometry != null) {
            final Location toIslandLocation = geometry.getInnerIsland(to);
            if (toIslandLocation != null) {
                toIsland = database.loadIsland(toIslandLocation);
            } else {
                toIsland = null;
            }
        } else {
            toIsland = null;
        }
        final IslandInfo fromIsland = lastIsland.get(name);
        if (fromIsland != null) {
            if (toIsland == null || !equals(toIsland.getTitle(), fromIsland.getTitle()) || !equals(toIsland.getOwner(), fromIsland.getOwner())) {
                leaveIsland(player, fromIsland);
            }
        } else {
            if (toIsland != null) {
                enterIsland(player, toIsland);
            }
        }
        lastIsland.put(name, toIsland);
        // TODO also send message on rename, purchase, repossess, etc.
        // TODO renaming causes leave message but not enter
    }

    private void enterIsland(final Player player, final IslandInfo info) {
        final IslandStatus status = info.getStatus();
        final String title = info.getTitle();
        final String owner = info.getOwner();
        if (status == IslandStatus.RESOURCE) {
            message(player, "island-enter-resource", title);
        } else if (status == IslandStatus.RESERVED) {
            message(player, "island-enter-reserved", title);
        } else if (status == IslandStatus.NEW) {
            message(player, "island-enter-new", title);
        } else if (status == IslandStatus.ABANDONED) {
            message(player, "island-enter-abandoned", title, owner);
        } else if (status == IslandStatus.REPOSSESSED) {
            message(player, "island-enter-repossessed", title, owner);
        } else if (status == IslandStatus.PRIVATE) {
            message(player, "island-enter-private", title, owner);
        }
    }

    private void leaveIsland(final Player player, final IslandInfo info) {
        final IslandStatus status = info.getStatus();
        final String title = info.getTitle();
        final String owner = info.getOwner();
        if (status == IslandStatus.RESOURCE) {
            message(player, "island-leave-resource", title);
        } else if (status == IslandStatus.RESERVED) {
            message(player, "island-leave-reserved", title);
        } else if (status == IslandStatus.NEW) {
            message(player, "island-leave-new", title);
        } else if (status == IslandStatus.ABANDONED) {
            message(player, "island-leave-abandoned", title, owner);
        } else if (status == IslandStatus.REPOSSESSED) {
            message(player, "island-leave-repossessed", title, owner);
        } else if (status == IslandStatus.PRIVATE) {
            message(player, "island-leave-private", title, owner);
        }
    }

    private boolean equals(final Object a, final Object b) {
        return (a == null && b == null) || (a != null && b != null && a.equals(b));
    }

    // TODO move this to central location and make it actually work
    public final void message(CommandSender to, String id, Object... args) {
        to.sendMessage("<" + id + ">");
        for (final Object arg : args) {
            to.sendMessage("  " + arg.toString());
        }
        to.sendMessage("</" + id + ">");
    }
}
