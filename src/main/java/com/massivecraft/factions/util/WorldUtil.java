package com.massivecraft.factions.util;

import com.massivecraft.factions.FactionsPlugin;
import io.papermc.lib.PaperLib;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;

public final class WorldUtil {

    private WorldUtil() {
        throw new UnsupportedOperationException("This class cannot be instantiated");
    }

    private static boolean isEnabled(String name) {
        if (!FactionsPlugin.getInstance().conf().restrictWorlds().isRestrictWorlds()) {
            return true;
        }
        return FactionsPlugin.getInstance().conf().restrictWorlds().isWhitelist() == FactionsPlugin.getInstance().conf().restrictWorlds().getWorldList().contains(name);
    }

    public static boolean isEnabled(World world) {
        return isEnabled(world.getName());
    }

    public static boolean isEnabled(CommandSender sender) {
        if (sender instanceof Player) {
            return isEnabled(((Player) sender).getWorld().getName());
        }
        return true;
    }

    public static long encodeChunk(int x, int z) {
        return (long) x & 0xffffffffL | ((long) z & 0xffffffffL) << 32;
    }

    public static int[] decodeChunk(long encoded) {
        return new int[]{(int) encoded, (int) (encoded >> 32)};
    }

    public static long encodeLocation(Location location) {
        return ((long) location.getBlockX() & 0x7FFFFFF) | (((long) location.getBlockZ() & 0x7FFFFFF) << 27) | ((long) location.getBlockY() << 54);
    }

    public static int[] decodeLocation(long encoded) {
        return new int[]{(int) (encoded << 37 >> 37), (int) (encoded >>> 54), (int) ((encoded << 10) >> 37)};
    }

    public static int blockToChunk(int blockVal) {
        return blockVal >> 4;
    }

    public static int blockToRegion(int blockVal) {
        return blockVal >> 9;
    }

    public static int chunkToRegion(int chunkVal) {
        return chunkVal >> 5;
    }

    public static int chunkToBlock(int chunkVal) {
        return chunkVal << 4;
    }

    public static int regionToBlock(int regionVal) {
        return regionVal << 9;
    }

    public static int regionToChunk(int regionVal) {
        return regionVal << 5;
    }
}
