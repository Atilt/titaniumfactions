package com.massivecraft.factions.data.json;

import com.google.common.collect.Maps;
import com.google.gson.reflect.TypeToken;
import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FactionsPlugin;
import com.massivecraft.factions.data.MemoryFPlayer;
import com.massivecraft.factions.data.MemoryFPlayers;
import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import org.bukkit.Bukkit;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.IntConsumer;

public class JSONFPlayers extends MemoryFPlayers {

    private static final Path PLAYERS_PATH = FactionsPlugin.getInstance().getDataFolder().toPath().resolve("data").resolve("players.json");

    public void convertFrom(MemoryFPlayers old, BooleanConsumer finish) {
        this.fPlayers.putAll(Maps.transformValues(old.getFPlayers(), faction -> new JSONFPlayer((MemoryFPlayer) faction)));
        forceSave(finish);
        instance = this;
    }

    @Override
    public void forceSave(BooleanConsumer finish) {
        forceSave(true, finish);
    }

    @Override
    public void forceSave(boolean sync, BooleanConsumer finish) {
        Map<UUID, FPlayer> entitiesThatShouldBeSaved = new HashMap<>(this.fPlayers.size());
        for (FPlayer entity : this.fPlayers.values()) {
            if (((MemoryFPlayer) entity).shouldBeSaved()) {
                entitiesThatShouldBeSaved.put(entity.getId(), entity);
            }
        }
        FactionsPlugin.getInstance().getIOController().write(PLAYERS_PATH, FactionsPlugin.getInstance().getGson(), entitiesThatShouldBeSaved, sync, finish);
    }

    @Override
    public void load(IntConsumer loaded) {
        if (Files.notExists(PLAYERS_PATH)) {
            loaded.accept(0);
            return;
        }
        Bukkit.getScheduler().runTaskAsynchronously(FactionsPlugin.getInstance(), () -> {
            Map<UUID, JSONFPlayer> data = FactionsPlugin.getInstance().getIOController().read(PLAYERS_PATH, FactionsPlugin.getInstance().getGson(), new TypeToken<Map<UUID, JSONFPlayer>>(){}.getType());
            int amount = data == null ? 0 : data.size();
            Bukkit.getScheduler().runTask(FactionsPlugin.getInstance(), () -> {
                if (amount > 0) {
                    this.fPlayers.putAll(data);
                }
                loaded.accept(amount);
            });
        });
    }

    @Override
    public FPlayer generateFPlayer(UUID id) {
        return new JSONFPlayer(id);
    }
}