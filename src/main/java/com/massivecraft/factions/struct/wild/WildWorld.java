package com.massivecraft.factions.struct.wild;

import java.util.concurrent.ThreadLocalRandom;

public final class WildWorld {

    private final int minX, maxX;
    private final int minZ, maxZ;

    public WildWorld(int minX, int maxX, int minZ, int maxZ) {
        this.minX = minX;
        this.maxX = maxX;
        this.minZ = minZ;
        this.maxZ = maxZ;
    }

    public int getMinX() {
        return minX;
    }

    public int getMaxX() {
        return maxX;
    }

    public int getMinZ() {
        return minZ;
    }

    public int getMaxZ() {
        return maxZ;
    }

    public int selectX() {
        return ThreadLocalRandom.current().nextInt(this.minX, this.maxX + 1);
    }

    public int selectZ() {
        return ThreadLocalRandom.current().nextInt(this.minZ, this.maxZ + 1);
    }
}
