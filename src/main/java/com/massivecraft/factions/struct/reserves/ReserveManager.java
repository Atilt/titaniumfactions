package com.massivecraft.factions.struct.reserves;

import com.massivecraft.factions.FactionsPlugin;
import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import org.bukkit.Bukkit;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.IntConsumer;

public final class ReserveManager {

    private final Map<UUID, Reserve> reserves = new HashMap<>();

    public Reserve getReserve(UUID uuid) {
        return this.reserves.get(uuid);
    }

    public UUID getReserveOwner(String tag) {
        for (Map.Entry<UUID, Reserve> entry : this.reserves.entrySet()) {
            if (entry.getValue().getTag().toLowerCase().equals(tag.toLowerCase())) {
                return entry.getKey();
            }
        }
        return null;
    }

    public Reserve reserve(UUID uuid, Reserve reserve) {
        return this.reserves.put(uuid, reserve);
    }

    public Reserve unreserve(UUID uuid) {
        return this.reserves.remove(uuid);
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
            ReserveManager reserveManager = FactionsPlugin.getInstance().getIOController().read(path, FactionsPlugin.getInstance().getGson(), ReserveManager.class);
            boolean data = reserveManager != null;
            Bukkit.getScheduler().runTask(FactionsPlugin.getInstance(), () -> {
                if (data) {
                    this.reserves.clear();
                    this.reserves.putAll(reserveManager.reserves);
                }
                result.accept(this.reserves.size());
            });
        });
    }
}