package com.massivecraft.factions.struct.wild;

import com.massivecraft.factions.FactionsPlugin;
import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.function.IntConsumer;

public final class WildManager {

    private final Map<String, WildWorld> worlds = new HashMap<String, WildWorld>(){{
        put("example_world", new WildWorld(10000, 10000, 10000, 10000));
        put("example_world_2", new WildWorld(20000, 20000, 20000, 20000));
    }};

    private long teleportCooldown = 60;
    private long teleportDelay = 5;

    public long getTeleportCooldown() {
        return teleportCooldown;
    }

    public long getTeleportDelay() {
        return teleportDelay;
    }

    public void serialize(Path path, BooleanConsumer result) {
        FactionsPlugin.getInstance().getIOController().write(path, FactionsPlugin.getInstance().getGson(), this, true, result);
    }

    public void deserialize(Path path, IntConsumer result) {
        if (Files.notExists(path)) {
            this.serialize(path, null);
            result.accept(0);
            return;
        }
        Bukkit.getScheduler().runTaskAsynchronously(FactionsPlugin.getInstance(), () -> {
            WildManager wildManager = FactionsPlugin.getInstance().getIOController().read(path, FactionsPlugin.getInstance().getGson(), WildManager.class);
            boolean data = wildManager != null;
            Bukkit.getScheduler().runTask(FactionsPlugin.getInstance(), () -> {
                if (data) {
                    this.worlds.clear();
                    this.worlds.putAll(wildManager.worlds);
                    this.teleportCooldown = wildManager.teleportCooldown;
                    this.teleportDelay = wildManager.teleportDelay;
                }
                result.accept(this.worlds.size());
            });
        });
    }

    public boolean isEnabled() {
        return !this.worlds.isEmpty();
    }

    public boolean hasSupport(World world) {
        return this.worlds.containsKey(world.getName());
    }

    public WildWorld getWildWorld(World world) {
        return this.worlds.get(world.getName());
    }
}