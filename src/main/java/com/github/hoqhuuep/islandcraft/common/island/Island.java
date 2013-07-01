package com.github.hoqhuuep.islandcraft.common.island;

import com.github.hoqhuuep.islandcraft.common.IslandMath;
import com.github.hoqhuuep.islandcraft.common.api.ICDatabase;
import com.github.hoqhuuep.islandcraft.common.api.ICPlayer;
import com.github.hoqhuuep.islandcraft.common.api.ICProtection;
import com.github.hoqhuuep.islandcraft.common.type.ICLocation;

public class Island {
    private final ICDatabase database;
    private final ICProtection protection;

    public Island(final ICDatabase database, final ICProtection protection) {
        this.database = database;
        this.protection = protection;
    }

    /**
     * To be called when a player tries to abandon the island at their current
     * location.
     * 
     * @param player
     */
    public final void onAbandon(final ICPlayer player) {
        final IslandMath islandMath = player.getWorld().getIslandMath();
        if (islandMath == null) {
            player.message("island-abandon-world-error");
            return;
        }
        final ICLocation location = player.getLocation();
        final ICLocation islandLocation = islandMath.islandAt(location);
        if (isOcean(islandLocation)) {
            player.message("island-abandon-ocean-error");
            return;
        }
        if (!isOwner(player, islandLocation)) {
            player.message("island-abandon-owner-error");
            return;
        }

        // Success
        database.saveOwnership(islandLocation, null);
        protection.removeRegion(islandMath.visibleRegion(islandLocation));
        protection.removeRegion(islandMath.protectedRegion(islandLocation));
        player.message("island-abandon");
    }

    /**
     * To be called when a player tries to examine the island at their current
     * location.
     * 
     * @param player
     */
    public final void onExamine(final ICPlayer player) {
        final IslandMath islandMath = player.getWorld().getIslandMath();
        if (islandMath == null) {
            player.message("island-examine-world-error");
            return;
        }
        final ICLocation location = player.getLocation();
        final ICLocation islandLocation = islandMath.islandAt(location);
        if (isOcean(islandLocation)) {
            player.message("island-examine-ocean-error");
            return;
        }

        final String owner = database.loadOwnership(islandLocation);
        final Long seed = database.loadSeed(islandLocation);
        final String biome;
        if (seed == null) {
            biome = "Unknown";
        } else {
            biome = islandMath.biome(seed.longValue()).getName();
        }

        if (owner == null) {
            // TODO Get real regeneration here
            player.message("island-examine-available", islandLocation, biome, "<n>");
        } else if (owner.equalsIgnoreCase("<reserved>")) {
            player.message("island-examine-reserved", islandLocation, biome);
        } else if (owner.equalsIgnoreCase("<resource>")) {
            // TODO Get real regeneration here
            player.message("island-examine-resource", islandLocation, biome, "<n>");
        } else {
            // TODO Get real tax paid here
            player.message("island-examine-private", islandLocation, biome, owner, "<n>");
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
        if (islandMath == null) {
            player.message("island-purchase-world-error");
            return;
        }
        final ICLocation location = player.getLocation();
        final ICLocation islandLocation = islandMath.islandAt(location);
        if (isOcean(islandLocation)) {
            player.message("island-purchase-ocean-error");
            return;
        }

        final String owner = database.loadOwnership(islandLocation);
        final String name = player.getName();

        if (owner != null) {
            if (owner.equalsIgnoreCase(name)) {
                player.message("island-purchase-self-error");
            } else if (owner.equalsIgnoreCase("<reserved>")) {
                player.message("island-purchase-reserved-error");
            } else if (owner.equalsIgnoreCase("<resource>")) {
                player.message("island-purchase-resource-error");
            } else {
                player.message("island-purchase-other-error");
            }
            return;
        }

        final int cost = calculateCost(name);

        if (!player.takeDiamonds(cost)) {
            // Insufficient funds
            player.message("island-purchase-funds-error", Integer.toString(cost));
            return;
        }

        // Success
        database.saveOwnership(islandLocation, name);
        final String title = name + "'s Island @ " + islandLocation;
        protection.addVisibleRegion(title, islandMath.visibleRegion(islandLocation));
        protection.addProtectedRegion(islandMath.protectedRegion(islandLocation), name);
        player.message("island-purchase");
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
        if (islandMath == null) {
            player.message("island-rename-world-error");
            return;
        }
        final ICLocation location = player.getLocation();
        final ICLocation islandLocation = islandMath.islandAt(location);
        if (isOcean(islandLocation)) {
            player.message("island-rename-ocean-error");
            return;
        }
        if (!isOwner(player, islandLocation)) {
            player.message("island-rename-owner-error");
            return;
        }

        // Success
        protection.renameRegion(islandMath.visibleRegion(islandLocation), title);
        player.message("island-rename");
    }

    private static boolean isOcean(final ICLocation location) {
        return location == null;
    }

    private boolean isOwner(final ICPlayer player, final ICLocation location) {
        final String owner = database.loadOwnership(location);
        final String name = player.getName();
        if (owner == null || !owner.equalsIgnoreCase(name)) {
            return false;
        }
        return true;
    }

    private int calculateCost(final String player) {
        return database.loadOwnershipLocations(player).size() + 1;
    }
}
