package com.massivecraft.factions;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.massivecraft.factions.math.FastMath;
import com.massivecraft.factions.util.WorldUtil;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import javax.annotation.ParametersAreNonnullByDefault;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public final class FLocation implements Serializable {

    private static final Map<String, LoadingCache<Long, FLocation>> CACHE = new HashMap<>(Bukkit.getWorlds().size());

    private static final long serialVersionUID = -8292915234027387983L;
    private static boolean WORLD_BORDER_SUPPORT;

    private final String world;
    private final int x;
    private final int z;

    static {
        try {
            Class.forName("org.bukkit.WorldBorder");
            WORLD_BORDER_SUPPORT = true;
        } catch (ClassNotFoundException ignored) {
            WORLD_BORDER_SUPPORT = false;
        }
    }

    public FLocation() {
        this.world = "";
        this.x = 0;
        this.z = 0;
    }

    public static FLocation empty() {
        return new FLocation();
    }

    public static FLocation wrap(String world, int x, int z) {
        try {
            return CACHE.computeIfAbsent(world, key ->
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
    public FLocation(String world, int x, int z) {
        this.world = world;
        this.x = x;
        this.z = z;
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

    public World getWorld() {
        return Bukkit.getWorld(this.world);
    }

    public String getWorldName() {
        return getWorld().getName();
    }

    public int getX() {
        return x;
    }

    public int getZ() {
        return z;
    }

    public String getCoordString() {
        return this.x + "," + this.z;
    }

    public Chunk getChunk() {
        return getWorld().getChunkAt(this.x, this.z);
    }

    @Override
    public String toString() {
        return "[" + getWorldName() + "," + getCoordString() + "]";
    }

    public FLocation getRelative(int dx, int dz) {
        return wrap(this.world, this.x + dx, this.z + dz);
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
        return loc != null && (loc.getWorld().getName().equals(getWorldName()) && FastMath.floor(loc.getX()) >> 4 == x && FastMath.floor(loc.getZ()) >> 4 == z);
    }
    
    
    public static boolean isOutsideWorldBorder(World world, int x, int z, int buffer) {
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

        int chunkMinX = WorldUtil.chunkToBlock(x);
        int chunkMaxX = chunkMinX | 15;
        int chunkMinZ = WorldUtil.chunkToBlock(z);
        int chunkMaxZ = chunkMinZ | 15;

        return (chunkMinX >= borderMaxX) || (chunkMinZ >= borderMaxZ) || (chunkMaxX <= borderMinX) || (chunkMaxZ <= borderMinZ);
    }

    public boolean isOutsideWorldBorder(World world, int buffer) {
       return isOutsideWorldBorder(world, this.x, this.z, buffer);
    }

    public boolean isOutsideWorldBorder(int buffer) {
        return isOutsideWorldBorder(getWorld(), buffer);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FLocation fLocation = (FLocation) o;
        return x == fLocation.x &&
                z == fLocation.z &&
                Objects.equals(this.world, fLocation.world);
    }

    @Override
    public int hashCode() {
        return (this.x << 9) ^ this.z + (this.world != null ? this.world.hashCode() : 0);
    }

    public boolean is(Location bukkit) {
        return WorldUtil.blockToChunk(FastMath.floor(bukkit.getX())) == this.x && WorldUtil.blockToChunk(FastMath.floor(bukkit.getZ())) == this.z && bukkit.getWorld().getName().equals(this.world);
    }

    public long toKey() {
        return WorldUtil.encodeChunk(this.x, this.z);
    }
}
