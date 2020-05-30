package com.massivecraft.factions.util;

import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

public final class BlockVisualizer {

    private final Object2ObjectMap<UUID, ObjectSet<Location>> trackedLocations = new Object2ObjectOpenHashMap<>();

    public Set<Location> getPlayerLocations(UUID uuid) {
        return this.trackedLocations.computeIfAbsent(uuid, id -> new ObjectOpenHashSet<>());
    }

    public Set<Location> getPlayerLocations(Player player) {
        return getPlayerLocations(player.getUniqueId());
    }

    @SuppressWarnings("deprecation")
    public void addLocation(Player player, Location location, Material material) {
        if (getPlayerLocations(player).add(location)) {
            player.sendBlockChange(location, material, (byte) 0);
        }
    }

    public void addLocations(Player player, Material material, Location... locations) {
        for (Location location : locations) {
            addLocation(player, location, material);
        }
    }

    @SuppressWarnings("deprecation")
    public void addLocations(Player player, Map<Location, Material> locations) {
        Set<Location> tracked = getPlayerLocations(player);
        for (Entry<Location, Material> entry : locations.entrySet()) {
            if (tracked.add(entry.getKey())) {
                player.sendBlockChange(entry.getKey(), entry.getValue(), (byte) 0);
            }
        }
    }

    @SuppressWarnings("deprecation")
    public void addLocations(Player player, Collection<Location> locations, Material material) {
        Set<Location> tracked = getPlayerLocations(player);
        for (Location location : tracked) {
            if (tracked.add(location)) {
                player.sendBlockChange(location, material, (byte) 0);
            }
        }
    }

    @SuppressWarnings("deprecation")
    public void addBlocks(Player player, Collection<Block> blocks, Material material) {
        Set<Location> tracked = getPlayerLocations(player);
        for (Block block : blocks) {
            Location location = block.getLocation();
            if (tracked.add(location)) {
                player.sendBlockChange(location, material, (byte) 0);
            }
        }
    }

    @SuppressWarnings("deprecation")
    public void clear(Player player, boolean deep) {
        Set<Location> locations = this.trackedLocations.remove(player.getUniqueId());
        if (locations == null) {
            return;
        }
        if (deep) {
            for (Location location : locations) {
                Block block = location.getWorld().getBlockAt(location);
                player.sendBlockChange(location, block.getType(), block.getData());
            }
        }
    }
}
