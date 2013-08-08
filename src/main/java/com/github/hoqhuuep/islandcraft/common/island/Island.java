package com.github.hoqhuuep.islandcraft.common.island;

import java.util.List;

import com.github.hoqhuuep.islandcraft.common.IslandMath;
import com.github.hoqhuuep.islandcraft.common.api.ICDatabase;
import com.github.hoqhuuep.islandcraft.common.api.ICPlayer;
import com.github.hoqhuuep.islandcraft.common.api.ICProtection;
import com.github.hoqhuuep.islandcraft.common.type.ICLocation;

public class Island {
    private static final int TAX_MAX = 2016;
    private static final int TAX_INC = 504;
    private static final Integer TAX_INITIAL = new Integer(504);
    private final ICDatabase database;
    private final ICProtection protection;
    private final int maxIslands;

    public Island(final ICDatabase database, final ICProtection protection, final int maxIslands) {
        this.database = database;
        this.protection = protection;
        this.maxIslands = maxIslands;
    }

    /**
     * To be called when a player tries to abandon the island at their current
     * location.
     * 
     * @param player
     */
    public final void onAbandon(final ICPlayer player) {
        final IslandMath islandMath = player.getWorld().getIslandMath();
        if (null == islandMath) {
            player.message("island-abandon-world-error"); //$NON-NLS-1$
            return;
        }
        final ICLocation location = player.getLocation();
        final ICLocation islandLocation = islandMath.islandAt(location);
        if (isOcean(islandLocation)) {
            player.message("island-abandon-ocean-error"); //$NON-NLS-1$
            return;
        }
        if (!isOwner(player, islandLocation)) {
            player.message("island-abandon-owner-error"); //$NON-NLS-1$
            return;
        }

        // Success
        database.saveOwnership(islandLocation, null);
        database.saveTax(islandLocation, null);
        protection.removeRegion(islandMath.visibleRegion(islandLocation));
        protection.removeRegion(islandMath.protectedRegion(islandLocation));
        player.message("island-abandon"); //$NON-NLS-1$
    }

    /**
     * To be called when a player tries to examine the island at their current
     * location.
     * 
     * @param player
     */
    public final void onExamine(final ICPlayer player) {
        final IslandMath islandMath = player.getWorld().getIslandMath();
        if (null == islandMath) {
            player.message("island-examine-world-error"); //$NON-NLS-1$
            return;
        }
        final ICLocation location = player.getLocation();
        final ICLocation islandLocation = islandMath.islandAt(location);
        if (isOcean(islandLocation)) {
            player.message("island-examine-ocean-error"); //$NON-NLS-1$
            return;
        }

        final String owner = database.loadOwnership(islandLocation);
        final Long seed = database.loadSeed(islandLocation);
        final String biome;
        if (null == seed) {
            biome = "Unknown";
        } else {
            biome = islandMath.biome(seed.longValue()).getName();
        }

        if (null == owner) {
            // TODO Get real regeneration here
            player.message("island-examine-available", islandLocation, biome, "<n>"); //$NON-NLS-2$ //$NON-NLS-1$
        } else if (owner.equalsIgnoreCase("<reserved>")) { //$NON-NLS-1$
            player.message("island-examine-reserved", islandLocation, biome); //$NON-NLS-1$
        } else if (owner.equalsIgnoreCase("<resource>")) { //$NON-NLS-1$
            // TODO Get real regeneration here
            player.message("island-examine-resource", islandLocation, biome, "<n>"); //$NON-NLS-2$ //$NON-NLS-1$
        } else {
            final Integer tax = database.loadTax(islandLocation);
            player.message("island-examine-private", islandLocation, biome, owner, tax); //$NON-NLS-2$ //$NON-NLS-1$
        }

        // TODO Abandoned island
    }

    /**
     * To be called when a player tries to purchase the island at their current
     * location.
     * 
     * @param player
     */
    public final void onPurchase(final ICPlayer player) {
        final IslandMath islandMath = player.getWorld().getIslandMath();
        if (null == islandMath) {
            player.message("island-purchase-world-error"); //$NON-NLS-1$
            return;
        }
        final ICLocation location = player.getLocation();
        final ICLocation islandLocation = islandMath.islandAt(location);
        if (isOcean(islandLocation)) {
            player.message("island-purchase-ocean-error"); //$NON-NLS-1$
            return;
        }

        final String owner = database.loadOwnership(islandLocation);
        final String name = player.getName();

        if (null != owner) {
            if (owner.equalsIgnoreCase(name)) {
                player.message("island-purchase-self-error"); //$NON-NLS-1$
            } else if (owner.equalsIgnoreCase("<reserved>")) { //$NON-NLS-1$
                player.message("island-purchase-reserved-error"); //$NON-NLS-1$
            } else if (owner.equalsIgnoreCase("<resource>")) { //$NON-NLS-1$
                player.message("island-purchase-resource-error"); //$NON-NLS-1$
            } else {
                player.message("island-purchase-other-error"); //$NON-NLS-1$
            }
            return;
        }

        if (database.loadOwnershipLocations(name).size() >= maxIslands) {
            player.message("island-purchase-max-error");
            return;
        }

        final int cost = calculatePurchaseCost(name);

        if (!player.takeDiamonds(cost)) {
            // Insufficient funds
            player.message("island-purchase-funds-error", Integer.toString(cost)); //$NON-NLS-1$
            return;
        }

        // Success
        database.saveOwnership(islandLocation, name);
        database.saveTax(islandLocation, TAX_INITIAL);
        final String title = name + "'s Island @ " + islandLocation;
        protection.addVisibleRegion(title, islandMath.visibleRegion(islandLocation));
        protection.addProtectedRegion(islandMath.protectedRegion(islandLocation), name);
        player.message("island-purchase"); //$NON-NLS-1$
    }

    public void onTax(final ICPlayer player) {
        final IslandMath islandMath = player.getWorld().getIslandMath();
        if (null == islandMath) {
            player.message("island-tax-world-error"); //$NON-NLS-1$
            return;
        }
        final ICLocation location = player.getLocation();
        final ICLocation islandLocation = islandMath.islandAt(location);
        if (isOcean(islandLocation)) {
            player.message("island-tax-ocean-error"); //$NON-NLS-1$
            return;
        }
        if (!isOwner(player, islandLocation)) {
            player.message("island-tax-owner-error"); //$NON-NLS-1$
            return;
        }

        final int newTax = database.loadTax(islandLocation).intValue() + TAX_INC;
        if (newTax > TAX_MAX) {
            player.message("island-tax-max-error");
            return;
        }

        final String name = player.getName();
        final int cost = calculateTaxCost(name);

        if (!player.takeDiamonds(cost)) {
            // Insufficient funds
            player.message("island-tax-funds-error", Integer.toString(cost)); //$NON-NLS-1$
            return;
        }

        // Success
        database.saveTax(islandLocation, new Integer(newTax));
        player.message("island-tax");
    }

    public void onDawn(final String world) {
        final List<ICLocation> islands = database.loadTaxByWorld(world);
        for (ICLocation island : islands) {
            final Integer tax = database.loadTax(island);
            database.saveTax(island, new Integer(tax.intValue() - 1));
            // TODO handle tax = 0
        }
    }

    /**
     * To be called when the player tries to rename the island at their current
     * location.
     * 
     * @param player
     * @param title
     */
    public final void onRename(final ICPlayer player, final String title) {
        final IslandMath islandMath = player.getWorld().getIslandMath();
        if (null == islandMath) {
            player.message("island-rename-world-error"); //$NON-NLS-1$
            return;
        }
        final ICLocation location = player.getLocation();
        final ICLocation islandLocation = islandMath.islandAt(location);
        if (isOcean(islandLocation)) {
            player.message("island-rename-ocean-error"); //$NON-NLS-1$
            return;
        }
        if (!isOwner(player, islandLocation)) {
            player.message("island-rename-owner-error"); //$NON-NLS-1$
            return;
        }

        // Success
        protection.renameRegion(islandMath.visibleRegion(islandLocation), title);
        player.message("island-rename"); //$NON-NLS-1$
    }

    public void onWarp(final ICPlayer player) {
        // TODO Warp player to random available island
    }

    private static boolean isOcean(final ICLocation location) {
        return null == location;
    }

    private boolean isOwner(final ICPlayer player, final ICLocation location) {
        final String owner = database.loadOwnership(location);
        final String name = player.getName();
        if (null == owner || !owner.equalsIgnoreCase(name)) {
            return false;
        }
        return true;
    }

    private int calculatePurchaseCost(final String player) {
        // TODO Get purchase cost from config
        return database.loadOwnershipLocations(player).size() + 1;
    }

    private int calculateTaxCost(final String player) {
        // TODO Get tax cost from config
        return database.loadOwnershipLocations(player).size();
    }
}
