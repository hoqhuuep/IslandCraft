package com.github.hoqhuuep.islandcraft.worldguard;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.github.hoqhuuep.islandcraft.realestate.IslandDeed;
import com.github.hoqhuuep.islandcraft.realestate.IslandStatus;
import com.github.hoqhuuep.islandcraft.realestate.event.IslandAbandonEvent;
import com.github.hoqhuuep.islandcraft.realestate.event.IslandLoadEvent;
import com.github.hoqhuuep.islandcraft.realestate.event.IslandPurchaseEvent;
import com.github.hoqhuuep.islandcraft.realestate.event.IslandRepossessEvent;

public class IslandListener implements Listener {
    final WorldGuardManager worldGuardManager;

    public IslandListener(WorldGuardManager worldGuardManager) {
        this.worldGuardManager = worldGuardManager;
    }

    @EventHandler
    public void onIslandLoad(final IslandLoadEvent event) {
        final IslandDeed deed = event.getInfo();
        final IslandStatus status = deed.getStatus();
        if (status == IslandStatus.RESOURCE) {
            worldGuardManager.setPublic(deed.getOuterRegion());
        } else if (status == IslandStatus.PRIVATE) {
            worldGuardManager.setPrivate(deed.getOuterRegion(), deed.getOwner());
        } else {
            worldGuardManager.setReserved(deed.getOuterRegion());
        }
    }

    @EventHandler
    public void onIslandPurchase(final IslandPurchaseEvent event) {
        final IslandDeed deed = event.getInfo();
        worldGuardManager.setPrivate(deed.getOuterRegion(), deed.getOwner());
    }

    @EventHandler
    public void onIslandAbandon(final IslandAbandonEvent event) {
        final IslandDeed deed = event.getInfo();
        worldGuardManager.setReserved(deed.getOuterRegion());
    }

    @EventHandler
    public void onIslandRepossess(final IslandRepossessEvent event) {
        final IslandDeed deed = event.getInfo();
        worldGuardManager.setReserved(deed.getOuterRegion());
    }
}
