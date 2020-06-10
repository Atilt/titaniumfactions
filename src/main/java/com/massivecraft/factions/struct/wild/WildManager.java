package com.massivecraft.factions.struct.wild;

import com.massivecraft.factions.FactionsPlugin;
import com.massivecraft.factions.Trackable;
import com.massivecraft.factions.cooldown.Cooldown;
import com.massivecraft.factions.cooldown.WildCooldown;
import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.IntConsumer;

public final class WildManager implements Trackable<Player> {

    private final Map<String, WildWorld> worlds = new HashMap<String, WildWorld>(){{
        put("example_world", new WildWorld(10000, 10000, 10000, 10000));
        put("example_world_2", new WildWorld(20000, 20000, 20000, 20000));
    }};

    private long teleportCooldown = 60;
    private long teleportDelay = 5;

    private final transient Map<UUID, Cooldown> cooldowns = new HashMap<>();
    private final transient Object2IntMap<UUID> delays = new Object2IntOpenHashMap<>();

    @Override
    public boolean track(Player player) {
        return false;
    }

    @Override
    public boolean untrack(Player player) {
        int task = this.purgeDelay(player.getUniqueId());
        if (task != -1) {
            Bukkit.getScheduler().cancelTask(task);
        }
        this.cooldowns.remove(player.getUniqueId());
        return true;
    }

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
                    this.delays.defaultReturnValue(-1);
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

    public boolean hasDelay(UUID uuid) {
        return this.delays.containsKey(uuid);
    }

    public Cooldown getCooldown(UUID uuid) {
        return this.cooldowns.getOrDefault(uuid, Cooldown.EMPTY);
    }

    public void markCooldown(UUID uuid) {
        this.cooldowns.put(uuid, new WildCooldown(Instant.now(), Duration.ofSeconds(this.teleportCooldown)));
    }

    public void markDelay(UUID uuid, int task) {
        this.delays.put(uuid, task);
    }

    public int purgeDelay(UUID uuid) {
        return this.delays.removeInt(uuid);
    }
}