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
import org.bukkit.Bukkit;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.IntConsumer;

public class JSONFPlayers extends MemoryFPlayers {

    public Gson getGson() {
        return FactionsPlugin.getInstance().getGson();
    }

    public void setGson(Gson gson) {}

    private File file;

    public JSONFPlayers() {
        file = new File(FactionsPlugin.getInstance().getDataFolder(), "data/players.json");
    }

    public void convertFrom(MemoryFPlayers old, BooleanConsumer finish) {
        this.fPlayers.putAll(Maps.transformValues(old.getFPlayers(), faction -> new JSONFPlayer((MemoryFPlayer) faction)));
        forceSave(finish);
        FPlayers.instance = this;
    }

    public void forceSave(BooleanConsumer finish) {
        forceSave(true, finish);
    }

    public void forceSave(boolean sync, BooleanConsumer finish) {
        Map<UUID, JSONFPlayer> entitiesThatShouldBeSaved = new HashMap<>();
        for (FPlayer entity : this.fPlayers.values()) {
            if (((MemoryFPlayer) entity).shouldBeSaved()) {
                entitiesThatShouldBeSaved.put(entity.getId(), (JSONFPlayer) entity);
            }
        }

        saveCore(file, entitiesThatShouldBeSaved, sync, finish);
    }

    private boolean saveCore(File target, Map<UUID, JSONFPlayer> data, boolean sync, BooleanConsumer finish) {
        return DiscUtil.writeCatch(target, FactionsPlugin.getInstance().getGson().toJson(data), sync, finish);
    }

    @Override
    public void load(IntConsumer loaded) {
        Bukkit.getScheduler().runTaskAsynchronously(FactionsPlugin.getInstance(), () -> {
            Map<UUID, JSONFPlayer> fplayers = this.loadCore(null);
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

    private Map<UUID, JSONFPlayer> loadCore(BooleanConsumer finish) {
        if (!this.file.exists()) {
            return new HashMap<>(0);
        }

        String content = DiscUtil.readCatch(this.file);
        if (content == null) {
            return null;
        }

        return FactionsPlugin.getInstance().getGson().fromJson(content, new TypeToken<Map<UUID, JSONFPlayer>>(){}.getType());
    }

    @Override
    public FPlayer generateFPlayer(UUID id) {
        return new JSONFPlayer(id);
    }
}
