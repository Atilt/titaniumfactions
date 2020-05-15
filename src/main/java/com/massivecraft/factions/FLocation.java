package com.massivecraft.factions;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.massivecraft.factions.util.FastMath;
import com.massivecraft.factions.util.MiscUtil;
import com.massivecraft.factions.util.WorldUtil;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectLinkedOpenHashSet;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import javax.annotation.ParametersAreNonnullByDefault;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public final class FLocation implements Serializable {

    private static final Object2ObjectMap<String, LoadingCache<Long, FLocation>> LOCATIONS = new Object2ObjectOpenHashMap<>();

    private static final long serialVersionUID = -8292915234027387983L;
    private static boolean worldBorderSupport;

    private final String worldName;
    private final int x;
    private final int z;

    static {
        try {
            Class.forName("org.bukkit.WorldBorder");
            worldBorderSupport = true;
        } catch (ClassNotFoundException ignored) {
            worldBorderSupport = false;
        }
    }

    public FLocation() {
        this.worldName = Bukkit.getWorlds().get(0).getName();
        this.x = 0;
        this.z = 0;
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
        return wrap(location.getWorld().getName(), WorldUtil.blockToChunk(location.getBlockX()), WorldUtil.blockToChunk(location.getBlockZ()));
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
    }

    @Deprecated
    public FLocation(Location location) {
        this(location.getWorld().getName(), WorldUtil.blockToChunk(location.getBlockX()), WorldUtil.blockToChunk(location.getBlockZ()));
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
        return "" + x + "," + z;
    }

    public Chunk getChunk() {
        return getWorld().getChunkAt(x, z);
    }

    @Override
    public String toString() {
        return "[" + this.getWorldName() + "," + this.getCoordString() + "]";
    }
/*
    public static FLocation fromString(String string) {
        int index = string.indexOf(',');
        int start = 1;
        String worldName = string.substring(start, index);
        start = index + 1;
        index = string.indexOf(',', start);
        int x = Integer.parseInt(string.substring(start, index));
        int y = Integer.parseInt(string.substring(index + 1, string.length() - 1));
        return new FLocation(worldName, x, y);
    }*/

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
        return loc.getWorld().getName().equals(getWorldName()) && loc.getBlockX() >> 4 == x && loc.getBlockZ() >> 4 == z;
    }

    /**
     * Checks if the chunk represented by this FLocation is outside the world border
     *
     * @param buffer the number of chunks from the border that will be treated as "outside"
     * @return whether this location is outside of the border
     */
    public boolean isOutsideWorldBorder(int buffer) {
        if (!worldBorderSupport) {
            return false;
        }
        WorldBorder border = getWorld().getWorldBorder();
        Location center = border.getCenter();

        double size = border.getSize() / 2.0D;

        int bufferBlocks = buffer << 4;

        double borderMinX = (center.getX() - size) + bufferBlocks;
        double borderMinZ = (center.getZ() - size) + bufferBlocks;
        double borderMaxX = (center.getX() + size) - bufferBlocks;
        double borderMaxZ = (center.getZ() + size) - bufferBlocks;

        int chunkMinX = this.x << 4;
        int chunkMaxX = chunkMinX | 15;
        int chunkMinZ = this.z << 4;
        int chunkMaxZ = chunkMinZ | 15;

        return (chunkMinX >= borderMaxX) || (chunkMinZ >= borderMaxZ) || (chunkMaxX <= borderMinX) || (chunkMaxZ <= borderMinZ);
    }


    //----------------------------------------------//
    // Some Geometry
    //----------------------------------------------//
    public Set<FLocation> getCircle(double radius) {
        double radiusSquared = radius * radius;

        if (radius <= 0) {
            return new HashSet<>(0);
        }

        int total = (int) Math.ceil(radius * 2);
        ObjectSet<FLocation> ret = new ObjectLinkedOpenHashSet<>((total * total) + 1);

        int xfrom = FastMath.floor(this.x - radius);
        int xto = (int) Math.ceil(this.x + radius);
        int zfrom = FastMath.floor(this.z - radius);
        int zto = (int) Math.ceil(this.z + radius);

        for (int x = xfrom; x <= xto; x++) {
            for (int z = zfrom; z <= zto; z++) {
                if (this.getDistanceSquaredTo(x, z) <= radiusSquared) {
                    ret.add(FLocation.wrap(this.worldName, x, z));
                }
            }
        }
        return ret;
    }

    public static Set<FLocation> getArea(FLocation from, FLocation to) {
        ObjectSet<FLocation> ret = new ObjectOpenHashSet<>();

        for (long x : MiscUtil.range(from.getX(), to.getX())) {
            for (long z : MiscUtil.range(from.getZ(), to.getZ())) {
                ret.add(FLocation.wrap(from.getWorldName(), (int) x, (int) z));
            }
        }

        return ret;
    }

    //----------------------------------------------//
    // Comparison
    //----------------------------------------------//

    @Override
    public int hashCode() {
        return (this.x << 9) + this.z + (this.worldName != null ? this.worldName.hashCode() : 0);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof FLocation)) {
            return false;
        }

        FLocation that = (FLocation) obj;
        return this.x == that.x && this.z == that.z && (Objects.equals(this.worldName, that.worldName));
    }

    public boolean is(Location bukkit) {
        return WorldUtil.blockToChunk(bukkit.getBlockX()) == this.x && WorldUtil.blockToChunk(bukkit.getBlockZ()) == this.z && bukkit.getWorld().getName().equals(this.worldName);
    }

    public long toKey() {
        return WorldUtil.encodeChunk(this.x, this.z);
    }
}
