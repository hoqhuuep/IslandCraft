package com.github.hoqhuuep.islandcraft.common.purchasing;

import com.github.hoqhuuep.islandcraft.common.IslandMath;
import com.github.hoqhuuep.islandcraft.common.api.ICConfig;
import com.github.hoqhuuep.islandcraft.common.api.ICDatabase;
import com.github.hoqhuuep.islandcraft.common.api.ICPlayer;
import com.github.hoqhuuep.islandcraft.common.api.ICProtection;
import com.github.hoqhuuep.islandcraft.common.generator.BiomePicker;
import com.github.hoqhuuep.islandcraft.common.type.ICLocation;

public class Purchasing {
    private final ICDatabase database;
    private final ICConfig config;
    private final ICProtection protection;
    private final IslandMath islandMath;

    public Purchasing(final ICDatabase database, final ICConfig config, final ICProtection protection) {
        this.database = database;
        this.config = config;
        this.protection = protection;
        islandMath = new IslandMath(config);
    }

    private int calculateCost(final String player) {
        return database.loadOwnershipLocations(player).size() + 1;
    }

    public final void onAbandon(final ICPlayer player) {
        final ICLocation location = player.getLocation();
        if (!location.getWorld().equalsIgnoreCase(config.getWorld())) {
            player.info("You cannot abandon an island from this world");
            return;
        }

        final ICLocation islandLocation = islandMath.islandAt(location);
        if (islandLocation == null) {
            player.info("You cannot abandon the ocean");
            return;
        }

        final String owner = database.loadOwnership(islandLocation);
        final String name = player.getName();
        if (owner == null || !owner.equalsIgnoreCase(name)) {
            player.info("You cannot abandon an island you do not own");
            return;
        }

        // Success
        database.saveOwnership(islandLocation, null);
        protection.removeRegion(islandMath.visibleRegion(islandLocation));
        protection.removeRegion(islandMath.protectedRegion(islandLocation));
        player.info("Island successfully abandoned");
    }

    public final void onExamine(final ICPlayer player) {
        final ICLocation location = player.getLocation();
        if (!location.getWorld().equalsIgnoreCase(config.getWorld())) {
            player.info("You cannot examine an island from this world");
            return;
        }

        final ICLocation islandLocation = islandMath.islandAt(location);
        if (islandLocation == null) {
            player.info("You cannot examine the ocean");
            return;
        }

        final String owner = database.loadOwnership(islandLocation);
        final Long seed = database.loadSeed(islandLocation);
        final String biome;
        if (seed == null) {
            biome = "Unknown";
        } else {
            biome = BiomePicker.pick(seed.longValue()).getName();
        }

        if (owner == null) {
            player.info("Available Island:");
            player.info("  Location: " + islandLocation);
            player.info("  Biome: " + biome);
            // TODO Get real regeneration here
            player.info("  Regeneration: <n> days");
        } else if (owner.equalsIgnoreCase("<reserved>")) {
            player.info("Reserved Island:");
            player.info("  Location: " + islandLocation);
            player.info("  Biome: " + biome);
        } else if (owner.equalsIgnoreCase("<resource>")) {
            player.info("Public Island:");
            player.info("  Location: " + islandLocation);
            player.info("  Biome: " + biome);
            // TODO Get real regeneration here
            player.info("  Regeneration: <n> days");
        } else {
            player.info("Private Island:");
            player.info("  Location: " + islandLocation);
            player.info("  Biome: " + biome);
            player.info("  Owner: " + owner);
            // TODO Get real members and tax here
            player.info("  Members: [<player>, <player>, ...]");
            player.info("  Tax Paid: <n> days");
        }

        // TODO Abandoned island
    }

    public final void onPurchase(final ICPlayer player) {
        final ICLocation location = player.getLocation();
        if (!location.getWorld().equalsIgnoreCase(config.getWorld())) {
            player.info("You cannot purchase an island from this world");
            return;
        }

        final ICLocation islandLocation = islandMath.islandAt(location);
        if (islandLocation == null) {
            player.info("You cannot purchase the ocean");
            return;
        }

        final String owner = database.loadOwnership(islandLocation);
        final String name = player.getName();

        if (owner != null) {
            if (owner.equalsIgnoreCase(name)) {
                player.info("You cannot purchase an island you already own");
            } else if (owner.equalsIgnoreCase("<reserved>")) {
                player.info("You cannot purchase a reserved island");
            } else if (owner.equalsIgnoreCase("<public>")) {
                player.info("You cannot purchase a public island");
            } else {
                player.info("You cannot purchase an island owned by another player");
            }
            return;
        }

        final int cost = calculateCost(name);

        if (!player.takeDiamonds(cost)) {
            // Insufficient funds
            if (cost == 1) {
                player.info("You need a diamond in your inventory to purchase this island");
            } else {
                player.info("You need " + cost + " diamonds in your inventory to purchase this island");
            }
            return;
        }

        // Success
        database.saveOwnership(islandLocation, name);
        final String title = name + "'s Island @ " + islandLocation;
        protection.addVisibleRegion(title, islandMath.visibleRegion(islandLocation));
        protection.addProtectedRegion(islandMath.protectedRegion(islandLocation), name);
        player.info("Island successfully purchased");
    }

    public final void onRename(final ICPlayer player, final String title) {
        final ICLocation location = player.getLocation();
        if (!location.getWorld().equalsIgnoreCase(config.getWorld())) {
            player.info("You cannot rename an island from this world");
            return;
        }

        final ICLocation islandLocation = islandMath.islandAt(location);
        if (islandLocation == null) {
            player.info("You cannot rename the ocean");
            return;
        }

        final String owner = database.loadOwnership(islandLocation);
        final String name = player.getName();
        if (owner == null || !owner.equalsIgnoreCase(name)) {
            player.info("You cannot rename an island you do not own");
            return;
        }

        // Success
        protection.renameRegion(islandMath.visibleRegion(islandLocation), title);
        player.info("Island successfully renamed");
    }
}
