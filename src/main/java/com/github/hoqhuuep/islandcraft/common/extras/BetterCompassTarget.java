package com.github.hoqhuuep.islandcraft.common.extras;

public enum BetterCompassTarget {
    SPAWN("spawn"), BED("bed"), DEATH_POINT("death point");

    private final String string;

    private BetterCompassTarget(final String string) {
        this.string = string;
    }

    public BetterCompassTarget next() {
        switch (this) {
        case SPAWN:
            return BED;
        case BED:
            return DEATH_POINT;
        default:
        case DEATH_POINT:
            return SPAWN;
        }
    }

    public BetterCompassTarget previous() {
        switch (this) {
        case BED:
            return SPAWN;
        case DEATH_POINT:
            return BED;
        default:
        case SPAWN:
            return DEATH_POINT;
        }
    }

    public String prettyString() {
        return this.string;
    }
}
