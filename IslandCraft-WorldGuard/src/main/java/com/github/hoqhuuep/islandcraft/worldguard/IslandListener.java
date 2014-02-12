package com.github.hoqhuuep.islandcraft.worldguard;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.github.hoqhuuep.islandcraft.realestate.IslandInfo;
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
        final IslandInfo info = event.getInfo();
        final IslandStatus status = info.getStatus();
        if (status == IslandStatus.RESOURCE) {
            worldGuardManager.setPublic(info.getMin(), info.getMax());
        } else {
            worldGuardManager.setReserved(info.getMin(), info.getMax());
        }
    }

    @EventHandler
    public void onIslandPurchase(final IslandPurchaseEvent event) {
        final IslandInfo info = event.getInfo();
        worldGuardManager.setPrivate(info.getMin(), info.getMax(), event.getPurchaser());
    }

    @EventHandler
    public void onIslandAbandon(final IslandAbandonEvent event) {
        final IslandInfo info = event.getInfo();
        worldGuardManager.setReserved(info.getMin(), info.getMax());
    }

    @EventHandler
    public void onIslandRepossess(final IslandRepossessEvent event) {
        final IslandInfo info = event.getInfo();
        worldGuardManager.setReserved(info.getMin(), info.getMax());
    }
}
