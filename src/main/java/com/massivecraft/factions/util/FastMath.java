package com.massivecraft.factions.util;

public final class FastMath {

    private FastMath() {
        throw new UnsupportedOperationException("This class cannot be instantiated");
    }

    public static int floor(double x) {
        int truncated = (int)x;
        return x < (double)truncated ? truncated - 1 : truncated;
    }

    public static int floor(float x) {
        int truncated = (int)x;
        return (double)x < (double)truncated ? truncated - 1 : truncated;
    }
}
