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
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import org.bukkit.Bukkit;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.IntConsumer;

public class JSONFactions extends MemoryFactions {
    public Gson getGson() {
        return FactionsPlugin.getInstance().getGson();
    }

    private final File file;

    public File getFile() {
        return file;
    }

    // -------------------------------------------- //
    // CONSTRUCTORS
    // -------------------------------------------- //

    public JSONFactions() {
        this.file = new File(FactionsPlugin.getInstance().getDataFolder(), "data/factions.json");
        this.nextId = 1;
    }

    public void forceSave(BooleanConsumer finish) {
        forceSave(true, finish);
    }

    public void forceSave(boolean sync, BooleanConsumer finish) {
        final Map<String, JSONFaction> entitiesThatShouldBeSaved = new HashMap<>();
        for (Faction entity : this.factions.values()) {
            entitiesThatShouldBeSaved.put(entity.getId(), (JSONFaction) entity);
        }

        saveCore(file, entitiesThatShouldBeSaved, sync, finish);
    }

    private boolean saveCore(File target, Map<String, JSONFaction> entities, boolean sync, BooleanConsumer finish) {
        return DiscUtil.writeCatch(target, FactionsPlugin.getInstance().getGson().toJson(entities), sync, finish);
    }

/*    public int load(BooleanConsumer finish) {
        Map<String, JSONFaction> factions = this.loadCore(finish);
        if (factions == null) {
            return 0;
        }
        this.factions.putAll(factions);

        super.load();
        return factions.size();
    }*/

    @Override
    public void load(IntConsumer loaded) {
        Bukkit.getScheduler().runTaskAsynchronously(FactionsPlugin.getInstance(), () -> {
            Map<String, JSONFaction> factions = this.loadCore(null);
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

    private Map<String, JSONFaction> loadCore(BooleanConsumer finish) {
        if (!this.file.exists()) {
            return new HashMap<>(0);
        }
        String content = DiscUtil.readCatch(this.file);
        if (content == null) {
            return null;
        }

        Map<String, JSONFaction> data = FactionsPlugin.getInstance().getGson().fromJson(content, new TypeToken<Map<String, JSONFaction>>(){}.getType());

        this.nextId = 1;
        saveCore(this.file, data, true, finish); // Update the flatfile
        return data;
    }

    private Set<String> whichKeysNeedMigration(Set<String> keys) {
        ObjectSet<String> list = new ObjectOpenHashSet<>();
        for (String value : keys) {
            if (!value.matches("[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}")) {
                // Not a valid UUID..
                if (value.matches("[a-zA-Z0-9_]{2,16}")) {
                    // Valid playername, we'll mark this as one for conversion
                    // to UUID
                    list.add(value);
                }
            }
        }
        return list;
    }

    // -------------------------------------------- //
    // ID MANAGEMENT
    // -------------------------------------------- //

    public String getNextId() {
        while (!isIdFree(this.nextId)) {
            this.nextId++;
        }
        return Integer.toString(this.nextId);
    }

    public boolean isIdFree(String id) {
        return !this.factions.containsKey(id);
    }

    public boolean isIdFree(int id) {
        return this.isIdFree(Integer.toString(id));
    }

    protected synchronized void updateNextIdForId(int id) {
        if (this.nextId < id) {
            this.nextId = id + 1;
        }
    }

    protected void updateNextIdForId(String id) {
        try {
            int idAsInt = Integer.parseInt(id);
            this.updateNextIdForId(idAsInt);
        } catch (NumberFormatException ignored) {}
    }

    @Override
    public Faction generateFactionObject() {
        String id = getNextId();
        Faction faction = new JSONFaction(id);
        updateNextIdForId(id);
        return faction;
    }

    @Override
    public Faction generateFactionObject(String id) {
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
