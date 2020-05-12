package com.massivecraft.factions.data.json;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FactionsPlugin;
import com.massivecraft.factions.data.MemoryFPlayer;
import com.massivecraft.factions.data.MemoryFPlayers;
import com.massivecraft.factions.util.DiscUtil;
import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import org.bukkit.Bukkit;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Map;
import java.util.UUID;
import java.util.function.IntConsumer;

public class JSONFPlayers extends MemoryFPlayers {

    public Gson getGson() {
        return FactionsPlugin.getInstance().getGson();
    }

    private static final Path PLAYERS_PATH = FactionsPlugin.getInstance().getDataFolder().toPath().resolve("data").resolve("players.json");

    public void convertFrom(MemoryFPlayers old, BooleanConsumer finish) {
        this.fPlayers.putAll(Maps.transformValues(old.getFPlayers(), faction -> new JSONFPlayer((MemoryFPlayer) faction)));
        forceSave(finish);
        instance = this;
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
            //move into folder if outside of folder
            Path possibleData = FactionsPlugin.getInstance().getDataFolder().toPath().resolve("players.json");
            if (Files.notExists(possibleData)) {
                return new Object2ObjectOpenHashMap<>(0);
            }
            try {
                Files.move(possibleData, PLAYERS_PATH, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return new Object2ObjectOpenHashMap<>(0);
        }
        Object2ObjectMap<UUID, JSONFPlayer> read = DiscUtil.read(PLAYERS_PATH, FactionsPlugin.getInstance().getGson(), new TypeToken<Object2ObjectOpenHashMap<UUID, JSONFPlayer>>(){}.getType());
        return read;
    }

    @Override
    public FPlayer generateFPlayer(UUID id) {
        return new JSONFPlayer(id);
    }
}