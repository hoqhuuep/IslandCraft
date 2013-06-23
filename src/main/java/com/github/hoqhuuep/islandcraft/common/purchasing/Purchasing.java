package com.github.hoqhuuep.islandcraft.common.purchasing;

import com.github.hoqhuuep.islandcraft.common.IslandMath;
import com.github.hoqhuuep.islandcraft.common.api.ICConfig;
import com.github.hoqhuuep.islandcraft.common.api.ICDatabase;
import com.github.hoqhuuep.islandcraft.common.api.ICPlayer;
import com.github.hoqhuuep.islandcraft.common.api.ICProtection;
import com.github.hoqhuuep.islandcraft.common.generator.BiomePicker;
import com.github.hoqhuuep.islandcraft.common.type.ICIsland;
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
        this.islandMath = new IslandMath(config);
    }

    private int calculateCost(final String player) {
        return this.database.loadIslands(player).size() + 1;
    }

    public final void onAbandon(final ICPlayer player) {
        ICLocation location = player.getLocation();
        if (!location.getWorld().equalsIgnoreCase(this.config.getWorld())) {
            player.info("You cannot abandon an island from this world");
            return;
        }

        ICLocation islandLocation = this.islandMath.islandAt(location);
        if (islandLocation == null) {
            // No island
            player.info("You cannot abandon the ocean");
            return;
        }

        ICIsland island = this.database.loadIsland(islandLocation);
        if (island == null) {
            // Not in database yet
            player.info("You cannot abandon an island you do not own");
            return;
        }

        String name = player.getName();
        String owner = island.getOwner();

        if (owner == null || !owner.equalsIgnoreCase(name)) {
            // Not owned by player
            player.info("You cannot abandon an island you do not own");
            return;
        }

        // Success
        island = new ICIsland(islandLocation, null);
        this.database.saveIsland(island);
        this.protection.removeRegion(this.islandMath.visibleRegion(islandLocation));
        this.protection.removeRegion(this.islandMath.protectedRegion(islandLocation));
        player.info("Island successfully abandoned");
    }

    public final void onExamine(final ICPlayer player) {
        ICLocation location = player.getLocation();
        if (!location.getWorld().equalsIgnoreCase(this.config.getWorld())) {
            player.info("You cannot examine an island from this world");
            return;
        }

        ICLocation islandLocation = this.islandMath.islandAt(location);
        if (islandLocation == null) {
            // No island
            player.info("You cannot examine the ocean");
            return;
        }

        final ICIsland island = this.database.loadIsland(islandLocation);
        final Long seed = this.database.loadIslandSeed(islandLocation);
        final String biome;
        if (seed == null) {
            biome = "Unknown";
        } else {
            biome = BiomePicker.pick(seed.longValue()).getName();
        }
        if (island == null) {
            // Not in database yet
            player.info("Available Island:");
            player.info("  Location: " + islandLocation);
            player.info("  Biome: " + biome);
            // TODO Get real regeneration here
            player.info("  Regeneration: <n> days");
            return;
        }

        String owner = island.getOwner();

        if (owner != null) {
            // Already owned
            if (owner.equalsIgnoreCase("<reserved>")) {
                player.info("Reserved Island:");
                player.info("  Location: " + islandLocation);
                player.info("  Biome: " + biome);
            } else if (owner.equalsIgnoreCase("<public>")) {
                player.info("Public Island:");
                player.info("  Location: " + islandLocation);
                player.info("  Biome: " + biome);
                // TODO Get real regeneration here
                player.info("  Regeneration: <n> days");
            } else {
                player.info("Private Island:");
                player.info("  Location: " + islandLocation);
                player.info("  Biome: " + biome);
                player.info("  Owner: " + island.getOwner());
                // TODO Get real members and taxes here
                player.info("  Members: [<player>, <player>, ...]");
                player.info("  Tax Paid: <n> days");
            }
            return;
        }
        player.info("Available Island:");
        player.info("  Location: " + island.getLocation());
        player.info("  Biome: " + biome);
        // TODO Get real regeneration here
        player.info("  Regeneration: <n> days");

        // TODO Abandoned island
    }

    public final void onPurchase(final ICPlayer player) {
        final ICLocation location = player.getLocation();
        if (!location.getWorld().equalsIgnoreCase(this.config.getWorld())) {
            player.info("You cannot purchase an island from this world");
            return;
        }

        final ICLocation islandLocation = this.islandMath.islandAt(location);
        if (islandLocation == null) {
            // No island
            player.info("You cannot purchase the ocean");
            return;
        }

        final ICIsland island = this.database.loadIsland(islandLocation);

        final String name = player.getName();
        if (island != null) {
            final String owner = island.getOwner();
            if (owner != null) {
                // Already owned
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
        final ICIsland newIsland;
        if (island == null) {
            newIsland = new ICIsland(islandLocation, name);
        } else {
            newIsland = new ICIsland(islandLocation, name);
        }
        this.database.saveIsland(newIsland);
        String islandName = name + "'s Island @ " + islandLocation;
        this.protection.addVisibleRegion(islandName, this.islandMath.visibleRegion(islandLocation));
        this.protection.addProtectedRegion(this.islandMath.protectedRegion(islandLocation), name);
        player.info("Island successfully purchased");
    }

    public final void onRename(final ICPlayer player, final String title) {
        final ICLocation location = player.getLocation();
        if (!location.getWorld().equalsIgnoreCase(this.config.getWorld())) {
            player.info("You cannot rename an island from this world");
            return;
        }
        final ICLocation islandLocation = this.islandMath.islandAt(location);
        if (islandLocation == null) {
            // No island
            player.info("You cannot rename the ocean");
            return;
        }

        final ICIsland island = this.database.loadIsland(islandLocation);

        if (island == null) {
            // No island
            player.info("You cannot rename an island you do not own");
            return;
        }

        final String name = player.getName();
        final String owner = island.getOwner();

        if (owner == null || !owner.equalsIgnoreCase(name)) {
            // Not owned by player
            player.info("You cannot rename an island you do not own");
            return;
        }

        // Success
        this.protection.renameRegion(this.islandMath.visibleRegion(islandLocation), title);
        player.info("Island successfully renamed");
    }
}
