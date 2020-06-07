package com.massivecraft.factions.util;

import com.massivecraft.factions.math.FastMath;
import org.bukkit.Effect;
import org.bukkit.Location;

import java.util.Collection;
import java.util.concurrent.ThreadLocalRandom;

// http://mc.kev009.com/Protocol
// -----------------------------
// Smoke Directions 
// -----------------------------
// Direction ID    Direction
//            0    South - East
//            1    South
//            2    South - West
//            3    East
//            4    (Up or middle ?)
//            5    West
//            6    North - East
//            7    North
//            8    North - West
//-----------------------------

public final class SmokeUtil {

    private SmokeUtil() {
        throw new UnsupportedOperationException("This class cannot be instantiated");
    }

    public static void spawnSingle(Location location, int direction) {
        if (location == null) {
            return;
        }
        location.getWorld().playEffect(location, Effect.SMOKE, direction);
    }

    public static void spawnSingle(Location location) {
        spawnSingle(location, 4);
    }

    public static void spawnSingleRandom(Location location) {
        spawnSingle(location, ThreadLocalRandom.current().nextInt(9));
    }

    public static void spawnCloudSimple(Location location) {
        for (int i = 0; i < 9; i++) {
            spawnSingle(location, i);
        }
    }

    public static void spawnCloudSimple(Collection<Location> locations) {
        for (Location location : locations) {
            spawnCloudSimple(location);
        }
    }

    private static void spawnCloudRandom(Location location, int singles) {
        for (int i = 0; i < singles; i++) {
            spawnSingleRandom(location);
        }
    }

    public static void spawnCloudRandom(Location location, float thickness) {
        int singles = FastMath.floor(thickness * 9);
        spawnCloudRandom(location, singles);
    }

    public static void spawnCloudRandom(Collection<Location> locations, float thickness) {
        int singles = FastMath.floor(thickness * 9);
        for (Location location : locations) {
            spawnCloudRandom(location, singles);
        }
    }
}
