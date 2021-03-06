package com.massivecraft.factions.meta;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public final class BlockVisualizer {

    private final Map<UUID, Set<Location>> trackedLocations = new HashMap<>();

    public Set<Location> getPlayerLocations(UUID uuid) {
        return this.trackedLocations.computeIfAbsent(uuid, id -> new HashSet<>());
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
