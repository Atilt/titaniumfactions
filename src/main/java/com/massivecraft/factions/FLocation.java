package com.massivecraft.factions;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.massivecraft.factions.math.FastMath;
import com.massivecraft.factions.util.WorldUtil;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import javax.annotation.ParametersAreNonnullByDefault;
import java.io.Serializable;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public final class FLocation implements Serializable {

    private static final Object2ObjectMap<String, LoadingCache<Long, FLocation>> LOCATIONS = new Object2ObjectOpenHashMap<>();

    private static final long serialVersionUID = -8292915234027387983L;
    private static boolean WORLD_BORDER_SUPPORT;

    private final String worldName;
    private final int x;
    private final int z;

    private final String readable;

    static {
        try {
            Class.forName("org.bukkit.WorldBorder");
            WORLD_BORDER_SUPPORT = true;
        } catch (ClassNotFoundException ignored) {
            WORLD_BORDER_SUPPORT = false;
        }
    }

    public FLocation() {
        this.worldName = Bukkit.getWorlds().get(0).getName();
        this.x = 0;
        this.z = 0;
        this.readable = this.x + "," + this.z;
    }

    public static FLocation empty() {
        return new FLocation();
    }

    public static FLocation wrap(String world, int x, int z) {
        try {
            return LOCATIONS.computeIfAbsent(world, key ->
                    CacheBuilder.newBuilder()
                        .maximumSize(1000) //needs experimenting
                        .weakValues()
                        .expireAfterAccess(5, TimeUnit.MINUTES)
                        .build(new CacheLoader<Long, FLocation>() {
                            @ParametersAreNonnullByDefault
                            @Override
                            public FLocation load(Long key) {
                                return new FLocation(world, (int) key.longValue(), (int) (key >> 32));
                            }
                        })

            ).get((long) x & 0xffffffffL | ((long) z & 0xffffffffL) << 32);
        } catch (ExecutionException e) {
            return new FLocation(world, x, z);
        }
    }

    public static FLocation wrap(Chunk chunk) {
        return wrap(chunk.getWorld().getName(), chunk.getX(), chunk.getZ());
    }

    public static FLocation wrap(Location location) {
        return wrap(location.getWorld().getName(), WorldUtil.blockToChunk(FastMath.floor(location.getX())), WorldUtil.blockToChunk(FastMath.floor(location.getZ())));
    }

    public static FLocation wrap(Block block) {
        return wrap(block.getLocation());
    }

    public static FLocation wrap(Player player) {
        return wrap(player.getLocation());
    }

    public static FLocation wrap(FPlayer fPlayer) {
        return wrap(fPlayer.getPlayer());
    }

    @Deprecated
    public FLocation(String worldName, int x, int z) {
        this.worldName = worldName;
        this.x = x;
        this.z = z;
        this.readable = x + "," + z;
    }

    @Deprecated
    public FLocation(Location location) {
        this(location.getWorld().getName(), WorldUtil.blockToChunk(FastMath.floor(location.getX())), WorldUtil.blockToChunk(FastMath.floor(location.getZ())));
    }

    @Deprecated
    public FLocation(Player player) {
        this(player.getLocation());
    }

    @Deprecated
    public FLocation(FPlayer fplayer) {
        this(fplayer.getPlayer());
    }

    @Deprecated
    public FLocation(Block block) {
        this(block.getLocation());
    }

    //----------------------------------------------//
    // Getters and Setters
    //----------------------------------------------//

    public String getWorldName() {
        return worldName;
    }

    public World getWorld() {
        return Bukkit.getWorld(worldName);
    }

    public int getX() {
        return x;
    }

    public int getZ() {
        return z;
    }

    public String getCoordString() {
        return this.readable;
    }

    public Chunk getChunk() {
        return getWorld().getChunkAt(this.x, this.z);
    }

    @Override
    public String toString() {
        return "[" + this.worldName + "," + this.readable + "]";
    }

    public FLocation getRelative(int dx, int dz) {
        return wrap(this.worldName, this.x + dx, this.z + dz);
    }

    public double getDistanceTo(FLocation that) {
        return Math.sqrt(getDistanceSquaredTo(that));
    }

    public double getDistanceSquaredTo(FLocation that) {
        return getDistanceSquaredTo(that.x, that.z);
    }

    public double getDistanceSquaredTo(int thatx, int thatz) {
        double dx = thatx - this.x;
        double dz = thatz - this.z;
        return dx * dx + dz * dz;
    }

    public boolean isInChunk(Location loc) {
        if (loc == null) {
            return false;
        }
        return loc.getWorld().getName().equals(getWorldName()) && FastMath.floor(loc.getX()) >> 4 == x && FastMath.floor(loc.getZ()) >> 4 == z;
    }

    /**
     * Checks if the chunk represented by this FLocation is outside the world border
     *
     * @param buffer the number of chunks from the border that will be treated as "outside"
     * @return whether this location is outside of the border
     */
    public boolean isOutsideWorldBorder(int buffer) {
        return isOutsideWorldBorder(getWorld(), buffer);
    }

    public boolean isOutsideWorldBorder(World world, int buffer) {
        if (!WORLD_BORDER_SUPPORT) {
            return false;
        }
        WorldBorder border = world.getWorldBorder();
        if (border.getSize() == 0) {
            return false;
        }
        Location center = border.getCenter();

        double size = border.getSize() / 2.0D;

        int bufferBlocks = buffer << 4;

        double borderMinX = (center.getX() - size) + bufferBlocks;
        double borderMinZ = (center.getZ() - size) + bufferBlocks;
        double borderMaxX = (center.getX() + size) - bufferBlocks;
        double borderMaxZ = (center.getZ() + size) - bufferBlocks;

        int chunkMinX = WorldUtil.chunkToBlock(this.x);
        int chunkMaxX = chunkMinX | 15;
        int chunkMinZ = WorldUtil.chunkToBlock(this.z);
        int chunkMaxZ = chunkMinZ | 15;

        return (chunkMinX >= borderMaxX) || (chunkMinZ >= borderMaxZ) || (chunkMaxX <= borderMinX) || (chunkMaxZ <= borderMinZ);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FLocation fLocation = (FLocation) o;
        return x == fLocation.x &&
                z == fLocation.z &&
                Objects.equals(worldName, fLocation.worldName);
    }

    @Override
    public int hashCode() {
        return (this.x << 9) ^ this.z + (this.worldName != null ? this.worldName.hashCode() : 0);
    }

    public boolean is(Location bukkit) {
        return WorldUtil.blockToChunk(FastMath.floor(bukkit.getX())) == this.x && WorldUtil.blockToChunk(FastMath.floor(bukkit.getZ())) == this.z && bukkit.getWorld().getName().equals(this.worldName);
    }

    public long toKey() {
        return WorldUtil.encodeChunk(this.x, this.z);
    }
}
