package com.github.hoqhuuep.islandcraft.common.api;

import java.util.List;

import com.github.hoqhuuep.islandcraft.common.type.ICLocation;

public interface ICWorld {
    List<ICPlayer> getPlayers();

    ICServer getServer();

    ICLocation getSpawnLocation();

    String getTime();

    String getName();

    boolean isNormalWorld();
}
