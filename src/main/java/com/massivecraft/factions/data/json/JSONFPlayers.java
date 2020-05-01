package com.massivecraft.factions.data.json;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.FactionsPlugin;
import com.massivecraft.factions.data.MemoryFPlayer;
import com.massivecraft.factions.data.MemoryFPlayers;
import com.massivecraft.factions.util.DiscUtil;
import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import org.bukkit.Bukkit;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.UUID;
import java.util.function.IntConsumer;

public class JSONFPlayers extends MemoryFPlayers {

    public Gson getGson() {
        return FactionsPlugin.getInstance().getGson();
    }

    public void setGson(Gson gson) {}

    private static final Path PLAYERS_PATH = FactionsPlugin.getInstance().getDataFolder().toPath().resolve("data").resolve("players.json");

    public void convertFrom(MemoryFPlayers old, BooleanConsumer finish) {
        this.fPlayers.putAll(Maps.transformValues(old.getFPlayers(), faction -> new JSONFPlayer((MemoryFPlayer) faction)));
        forceSave(finish);
        FPlayers.instance = this;
    }

    public void forceSave(BooleanConsumer finish) {
        forceSave(true, finish);
    }

    public void forceSave(boolean sync, BooleanConsumer finish) {
        Object2ObjectMap<UUID, FPlayer> entitiesThatShouldBeSaved = new Object2ObjectOpenHashMap<>(this.fPlayers.size());
        for (FPlayer entity : this.fPlayers.values()) {
            if (((MemoryFPlayer) entity).shouldBeSaved()) {
                entitiesThatShouldBeSaved.put(entity.getId(), entity);
            }
        }
        DiscUtil.write(PLAYERS_PATH, FactionsPlugin.getInstance().getGson(), entitiesThatShouldBeSaved, sync, finish);
    }

    @Override
    public void load(IntConsumer loaded) {
        Bukkit.getScheduler().runTaskAsynchronously(FactionsPlugin.getInstance(), () -> {
            Map<UUID, JSONFPlayer> fplayers = this.loadCore();
            int amount = fplayers == null ? 0 : fplayers.size();
            Bukkit.getScheduler().runTask(FactionsPlugin.getInstance(), () -> {
                this.fPlayers.clear();
                if (amount > 0) {
                    this.fPlayers.putAll(fplayers);
                }
                loaded.accept(amount);
            });
        });
    }

    private Object2ObjectMap<UUID, JSONFPlayer> loadCore() {
        if (Files.notExists(PLAYERS_PATH)) {
            return new Object2ObjectOpenHashMap<>(0);
        }
        return DiscUtil.read(PLAYERS_PATH, FactionsPlugin.getInstance().getGson(), new TypeToken<Object2ObjectMap<UUID, JSONFPlayer>>(){}.getType());
    }

    @Override
    public FPlayer generateFPlayer(UUID id) {
        return new JSONFPlayer(id);
    }
}