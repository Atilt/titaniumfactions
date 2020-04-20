package com.massivecraft.factions.data.json;

import com.google.gson.reflect.TypeToken;
import com.massivecraft.factions.Board;
import com.massivecraft.factions.FLocation;
import com.massivecraft.factions.FactionsPlugin;
import com.massivecraft.factions.data.MemoryBoard;
import com.massivecraft.factions.util.DiscUtil;
import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import org.bukkit.Bukkit;

import java.io.File;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.function.IntConsumer;
import java.util.logging.Level;


public class JSONBoard extends MemoryBoard {
    private static transient File file = new File(FactionsPlugin.getInstance().getDataFolder(), "data/board.json");

    // -------------------------------------------- //
    // Persistance
    // -------------------------------------------- //

    public Map<String, Map<String, String>> dumpAsSaveFormat() {
        Map<String, Map<String, String>> worldCoordIds = new HashMap<>();

        String worldName, coords;
        String id;

        for (Entry<FLocation, String> entry : flocationIds.entrySet()) {
            worldName = entry.getKey().getWorldName();
            coords = entry.getKey().getCoordString();
            id = entry.getValue();
            if (!worldCoordIds.containsKey(worldName)) {
                worldCoordIds.put(worldName, new TreeMap<>());
            }

            worldCoordIds.get(worldName).put(coords, id);
        }

        return worldCoordIds;
    }

    public void loadFromSaveFormat(Map<String, Map<String, String>> worldCoordIds) {
        flocationIds.clear();

        String worldName;
        String[] coords;
        int x, z;
        String factionId;

        for (Entry<String, Map<String, String>> entry : worldCoordIds.entrySet()) {
            worldName = entry.getKey();
            for (Entry<String, String> entry2 : entry.getValue().entrySet()) {
                coords = entry2.getKey().trim().split("[,\\s]+");
                x = Integer.parseInt(coords[0]);
                z = Integer.parseInt(coords[1]);
                factionId = entry2.getValue();
                flocationIds.put(new FLocation(worldName, x, z), factionId);
            }
        }
    }

    public void forceSave(BooleanConsumer finish) {
        forceSave(true, finish);
    }

    public void forceSave(boolean sync, BooleanConsumer finish) {
        DiscUtil.writeCatch(file, FactionsPlugin.getInstance().getGson().toJson(dumpAsSaveFormat()), sync, finish);
    }

    public int load(BooleanConsumer finish) {
        if (!file.exists()) {
            FactionsPlugin.getInstance().getLogger().info("No board to load from disk. Creating new file.");
            forceSave(finish);
            return 0;
        }

        try {
            Type type = new TypeToken<Map<String, Map<String, String>>>() {
            }.getType();
            Map<String, Map<String, String>> worldCoordIds = FactionsPlugin.getInstance().getGson().fromJson(DiscUtil.read(file), type);
            loadFromSaveFormat(worldCoordIds);
        } catch (Exception e) {
            FactionsPlugin.getInstance().getLogger().log(Level.SEVERE, "Failed to load the board from disk.", e);
            return 0;
        }

        return flocationIds.size();
    }

    @Override
    public void load(IntConsumer loaded) {
        if (!file.exists()) {
            FactionsPlugin.getInstance().getLogger().info("No board to load from disk. Creating new file.");
            forceSave(null);
            loaded.accept(0);
            return;
        }
        Bukkit.getScheduler().runTaskAsynchronously(FactionsPlugin.getInstance(), () -> {
            try {
                Map<String, Map<String, String>> worldCoordIds = FactionsPlugin.getInstance().getGson().fromJson(DiscUtil.read(file), new TypeToken<Map<String, Map<String, String>>>(){}.getType());

                Bukkit.getScheduler().runTask(FactionsPlugin.getInstance(), () -> {
                    loadFromSaveFormat(worldCoordIds);
                    loaded.accept(this.flocationIds.size());
                });
            } catch (Exception e) {
                FactionsPlugin.getInstance().getLogger().log(Level.SEVERE, "Failed to load the board from disk.", e);
                loaded.accept(0);
            }
        });
    }

    @Override
    public void convertFrom(MemoryBoard old, BooleanConsumer finish) {
        this.flocationIds = old.flocationIds;
        forceSave(finish);
        Board.instance = this;
    }
}
