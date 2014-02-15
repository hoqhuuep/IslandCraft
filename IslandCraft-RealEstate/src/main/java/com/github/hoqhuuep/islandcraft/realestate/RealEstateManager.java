package com.github.hoqhuuep.islandcraft.realestate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
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
    private final ConfigurationSection config;
    private final Map<String, IslandDeed> lastIsland;
    private final Map<String, Geometry> geometryMap;

    public RealEstateManager(final RealEstateDatabase database, final ConfigurationSection config) {
        this.database = database;
        this.config = config;
        lastIsland = new HashMap<String, IslandDeed>();
        geometryMap = new HashMap<String, Geometry>();
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
        final Geometry geometry = getGeometry(world.getName());
        if (geometry == null) {
            // Not an IslandCraft world
            return;
        }
        for (final SerializableLocation island : geometry.getOuterIslands(location)) {
            IslandDeed deed = database.loadIsland(island);
            if (deed == null) {
                deed = new IslandDeed();
                deed.setId(new SerializableLocation(island.getWorld(), island.getX(), island.getY(), island.getZ()));
                deed.setInnerRegion(geometry.getInnerRegion(island));
                deed.setOuterRegion(geometry.getOuterRegion(island));
                deed.setOwner(null);
                deed.setTax(-1);
                if (geometry.isSpawn(island)) {
                    deed.setStatus(IslandStatus.RESERVED);
                    deed.setTitle("Spawn Island");
                } else if (geometry.isResource(island, worldSeed)) {
                    deed.setStatus(IslandStatus.RESOURCE);
                    deed.setTitle("Resource Island");
                } else {
                    deed.setStatus(IslandStatus.NEW);
                    deed.setTitle("New Island");
                }
                database.saveIsland(deed);
            }
            Bukkit.getPluginManager().callEvent(new IslandLoadEvent(deed));
        }
    }

    /**
     * To be called when a player tries to abandon the island at their current location.
     * 
     * @param player
     */
    public final void onAbandon(final Player player) {
        final Geometry geometry = getGeometry(player.getWorld().getName());
        if (geometry == null) {
            message(player, "island-abandon-world-error");
            return;
        }
        final Location location = player.getLocation();
        final SerializableLocation island = geometry.getInnerIsland(location);
        if (geometry.isOcean(island)) {
            message(player, "island-abandon-ocean-error");
            return;
        }
        final IslandDeed deed = database.loadIsland(island);
        if (deed.getStatus() != IslandStatus.PRIVATE || !StringUtils.equals(deed.getOwner(), player.getName())) {
            message(player, "island-abandon-owner-error");
            return;
        }

        // Success
        deed.setStatus(IslandStatus.ABANDONED);
        deed.setTax(-1);
        database.saveIsland(deed);
        message(player, "island-abandon");
        Bukkit.getPluginManager().callEvent(new IslandAbandonEvent(deed));
    }

    /**
     * To be called when a player tries to examine the island at their current location.
     * 
     * @param player
     */
    public final void onExamine(final Player player) {
        final Geometry geometry = getGeometry(player.getWorld().getName());
        if (geometry == null) {
            message(player, "island-examine-world-error");
            return;
        }
        final Location location = player.getLocation();
        final SerializableLocation island = geometry.getInnerIsland(location);
        if (geometry.isOcean(island)) {
            message(player, "island-examine-ocean-error");
            return;
        }

        final String world = island.getWorld();
        final int x = island.getX();
        final int z = island.getZ();
        final IslandDeed deed = database.loadIsland(island);
        final IslandStatus status = deed.getStatus();
        final String title = deed.getTitle();
        final String owner = deed.getOwner();
        final int tax = deed.getTax();
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
        final Geometry geometry = getGeometry(player.getWorld().getName());
        if (geometry == null) {
            message(player, "island-purchase-world-error");
            return;
        }
        final Location location = player.getLocation();
        final SerializableLocation island = geometry.getInnerIsland(location);
        if (geometry.isOcean(island)) {
            message(player, "island-purchase-ocean-error");
            return;
        }

        final IslandDeed deed = database.loadIsland(island);
        final IslandStatus status = deed.getStatus();
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
            final String owner = deed.getOwner();
            if (StringUtils.equals(owner, name)) {
                message(player, "island-purchase-self-error");
            } else {
                message(player, "island-purchase-other-error");
            }
            return;
        }
        if (islandCount(name) >= config.getInt("max-islands-per-player")) {
            message(player, "island-purchase-max-error");
            return;
        }

        final int cost = calculatePurchaseCost(name);

        if (!takeItems(player, config.getString("purchase-cost-item"), cost)) {
            // Insufficient funds
            message(player, "island-purchase-funds-error", Integer.toString(cost));
            return;
        }

        // Success
        deed.setStatus(IslandStatus.PRIVATE);
        deed.setOwner(name);
        deed.setTitle("Private Island");
        deed.setTax(config.getInt("tax-days-initial"));
        database.saveIsland(deed);
        message(player, "island-purchase");
        Bukkit.getPluginManager().callEvent(new IslandPurchaseEvent(deed));
    }

    public void onTax(final Player player) {
        final Geometry geometry = getGeometry(player.getWorld().getName());
        if (geometry == null) {
            message(player, "island-tax-world-error");
            return;
        }
        final Location location = player.getLocation();
        final SerializableLocation island = geometry.getInnerIsland(location);
        if (geometry.isOcean(island)) {
            message(player, "island-tax-ocean-error");
            return;
        }
        final String name = player.getName();
        final IslandDeed deed = database.loadIsland(island);
        if (deed.getStatus() != IslandStatus.PRIVATE || !deed.getOwner().equals(name)) {
            message(player, "island-tax-owner-error");
            return;
        }

        final int newTax = deed.getTax() + config.getInt("tax-days-increase");
        if (newTax > config.getInt("tax-days-max")) {
            message(player, "island-tax-max-error");
            return;
        }

        final int cost = calculateTaxCost(name);

        if (!takeItems(player, config.getString("tax-cost-item"), cost)) {
            // Insufficient funds
            message(player, "island-tax-funds-error", Integer.toString(cost));
            return;
        }

        // Success
        deed.setTax(newTax);
        database.saveIsland(deed);
        message(player, "island-tax");
    }

    public void onDawn(final String world) {
        final Geometry geometry = getGeometry(world);
        if (geometry == null) {
            // Not an IslandCraft world
            return;
        }
        final List<IslandDeed> deeds = database.loadIslandsByWorld(world);
        for (IslandDeed deed : deeds) {
            final int tax = deed.getTax();
            if (tax > 0) {
                // Decrement tax
                deed.setTax(tax - 1);
                database.saveIsland(deed);
            } else if (tax == 0) {
                final IslandStatus status = deed.getStatus();
                if (status == IslandStatus.PRIVATE) {
                    // Repossess island
                    deed.setStatus(IslandStatus.REPOSSESSED);
                    deed.setTax(-1);
                    database.saveIsland(deed);
                    Bukkit.getPluginManager().callEvent(new IslandRepossessEvent(deed));
                } else {
                    // TODO regenerate island
                    if (status == IslandStatus.REPOSSESSED || status == IslandStatus.ABANDONED) {
                        deed.setStatus(IslandStatus.NEW);
                        deed.setOwner(null);
                        deed.setTitle("New Island");
                        deed.setTax(-1);
                        database.saveIsland(deed);
                        Bukkit.getPluginManager().callEvent(new IslandRegenerateEvent(deed));
                    }
                }
            }
            // tax < 0 => infinite
        }
    }

    /**
     * To be called when the player tries to rename the island at their current location.
     * 
     * @param player
     * @param title
     */
    public final void onRename(final Player player, final String title) {
        final Geometry geometry = getGeometry(player.getWorld().getName());
        if (geometry == null) {
            message(player, "island-rename-world-error");
            return;
        }
        final Location location = player.getLocation();
        final SerializableLocation island = geometry.getInnerIsland(location);
        if (geometry.isOcean(island)) {
            message(player, "island-rename-ocean-error");
            return;
        }
        final IslandDeed deed = database.loadIsland(island);
        if (deed.getStatus() != IslandStatus.PRIVATE || !StringUtils.equals(deed.getOwner(), player.getName())) {
            message(player, "island-rename-owner-error");
            return;
        }

        // Success
        deed.setTitle(title);
        database.saveIsland(deed);
        message(player, "island-rename");
        Bukkit.getPluginManager().callEvent(new IslandRenameEvent(deed));
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
        return config.getInt("purchase-cost-amount") + islandCount(player) * config.getInt("purchase-cost-increase");
    }

    private int calculateTaxCost(final String player) {
        return config.getInt("tax-cost-amount") + (islandCount(player) - 1) * config.getInt("tax-cost-increase");
    }

    private int islandCount(final String player) {
        final List<IslandDeed> deeds = database.loadIslandsByOwner(player);
        int count = 0;
        for (final IslandDeed deed : deeds) {
            if (deed.getStatus() == IslandStatus.PRIVATE) {
                ++count;
            }
        }
        return count;
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
        final Geometry geometry = getGeometry(to.getWorld().getName());
        final IslandDeed toIsland;
        if (geometry != null) {
            final SerializableLocation toIslandLocation = geometry.getInnerIsland(to);
            if (toIslandLocation != null) {
                toIsland = database.loadIsland(toIslandLocation);
            } else {
                toIsland = null;
            }
        } else {
            toIsland = null;
        }
        final IslandDeed fromIsland = lastIsland.get(name);
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
        // TODO also send greeting/farewell message on rename, purchase, repossess, etc.
        // TODO renaming causes leave message but not enter
    }

    private void enterIsland(final Player player, final IslandDeed deed) {
        final IslandStatus status = deed.getStatus();
        final String title = deed.getTitle();
        final String owner = deed.getOwner();
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

    private void leaveIsland(final Player player, final IslandDeed deed) {
        final IslandStatus status = deed.getStatus();
        final String title = deed.getTitle();
        final String owner = deed.getOwner();
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

    public void addGeometry(final String world, final Geometry geometry) {
        geometryMap.put(world, geometry);
    }

    private Geometry getGeometry(final String world) {
        return geometryMap.get(world);
    }
}
