package com.github.hoqhuuep.islandcraft.common.api;

import com.github.hoqhuuep.islandcraft.common.type.ICRegion;

public interface ICProtection {
    boolean addProtectedRegion(ICRegion region, String owner);

    boolean addVisibleRegion(String name, ICRegion region);

    boolean removeRegion(ICRegion region);

    boolean renameRegion(ICRegion region, String title);
}
