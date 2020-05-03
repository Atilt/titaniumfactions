package com.massivecraft.factions.data.json;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.Factions;
import com.massivecraft.factions.FactionsPlugin;
import com.massivecraft.factions.data.MemoryFaction;
import com.massivecraft.factions.data.MemoryFactions;
import com.massivecraft.factions.util.DiscUtil;
import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import org.bukkit.Bukkit;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.function.IntConsumer;

public class JSONFactions extends MemoryFactions {
    public Gson getGson() {
        return FactionsPlugin.getInstance().getGson();
    }

    private static final Path FACTIONS_PATH = FactionsPlugin.getInstance().getDataFolder().toPath().resolve("data").resolve("factions.json");

    public Path toPath() {
        return FACTIONS_PATH;
    }

    public JSONFactions() {
        this.nextId = 1;
    }

    public void forceSave(BooleanConsumer finish) {
        forceSave(true, finish);
    }

    public void forceSave(boolean sync, BooleanConsumer finish) {
        Int2ObjectMap<JSONFaction> entitiesThatShouldBeSaved = new Int2ObjectOpenHashMap<>(this.factions.size());
        for (Faction entity : this.factions.values()) {
            entitiesThatShouldBeSaved.put(entity.getIdRaw(), (JSONFaction) entity);
        }

        saveCore(entitiesThatShouldBeSaved, sync, finish);
    }

    private boolean saveCore(Map<Integer, JSONFaction> entities, boolean sync, BooleanConsumer finish) {
        DiscUtil.write(FACTIONS_PATH, FactionsPlugin.getInstance().getGson(), entities, sync, finish);
        return true;
    }

    @Override
    public void load(IntConsumer loaded) {
        Bukkit.getScheduler().runTaskAsynchronously(FactionsPlugin.getInstance(), () -> {
            Int2ObjectMap<JSONFaction> factions = this.loadCore(null);
            int amount = factions == null ? 0 : factions.size();
            Bukkit.getScheduler().runTask(FactionsPlugin.getInstance(), () -> {
                this.factions.clear();
                if (amount > 0) {
                    this.factions.putAll(factions);
                }
                super.load();
                loaded.accept(amount);
            });
        });
    }

    private Int2ObjectMap<JSONFaction> loadCore(BooleanConsumer finish) {
        if (Files.notExists(FACTIONS_PATH)) {
            return new Int2ObjectOpenHashMap<>(0);
        }
        Int2ObjectMap<JSONFaction> data = DiscUtil.read(FACTIONS_PATH, FactionsPlugin.getInstance().getGson(), new TypeToken<Int2ObjectOpenHashMap<JSONFaction>>(){}.getType());
        this.nextId = 1;
        saveCore(data, true, finish);
        return data;
    }

    public int getNextId() {
        while (!isIdFree(this.nextId)) {
            this.nextId++;
        }
        return this.nextId;
    }
    public boolean isIdFree(int id) {
        return this.factions.containsKey(id);
    }

    protected void updateNextIdForId(int id) {
        synchronized (JSONFactions.class) {
            if (this.nextId < id) {
                this.nextId = id + 1;
            }
        }
    }

    @Override
    public Faction generateFactionObject() {
        int id = getNextId();
        Faction faction = new JSONFaction(id);
        updateNextIdForId(id);
        return faction;
    }

    @Deprecated
    @Override
    public Faction generateFactionObject(String id) {
        return generateFactionObject(Integer.parseInt(id));
    }

    @Override
    public Faction generateFactionObject(int id) {
        return new JSONFaction(id);
    }

    @Override
    public void convertFrom(MemoryFactions old, BooleanConsumer finish) {
        this.factions.putAll(Maps.transformValues(old.factions, faction -> new JSONFaction((MemoryFaction) faction)));
        this.nextId = old.nextId;
        forceSave(finish);
        Factions.instance = this;
    }
}
