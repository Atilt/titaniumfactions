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
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.function.IntConsumer;
import java.util.logging.Level;


public class JSONBoard extends MemoryBoard {

    private static final transient File FILE = new File(FactionsPlugin.getInstance().getDataFolder(), "data/board.json");

    //Map<World, <CompactCoord, FactionId>>
    public Map<String, Map<String, String>> dumpAsSaveFormat() {
        Map<String, Map<String, String>> worldCoordIds = new HashMap<>(flocationIds.size());

        for (Entry<FLocation, String> entry : flocationIds.entrySet()) {
            worldCoordIds.computeIfAbsent(entry.getKey().getWorldName(), s -> new TreeMap<>()).put(entry.getKey().getCoordString(), entry.getValue());
        }

        return worldCoordIds;
    }

    //world, //
    public void loadFromSaveFormat(Map<String, Map<String, String>> worldCoordIds) {
        flocationIds.clear();

        for (Entry<String, Map<String, String>> entry : worldCoordIds.entrySet()) {
            for (Entry<String, String> entry2 : entry.getValue().entrySet()) {
                String[] coords = entry2.getKey().trim().split("[,\\s]+");
                flocationIds.put(FLocation.wrap(entry.getKey(), Integer.parseInt(coords[0]), Integer.parseInt(coords[1])), entry2.getValue());
            }
        }
    }

    public void forceSave(BooleanConsumer finish) {
        forceSave(true, finish);
    }

    public void forceSave(boolean sync, BooleanConsumer finish) {
        DiscUtil.writeCatch(FILE, FactionsPlugin.getInstance().getGson().toJson(dumpAsSaveFormat()), sync, finish);
    }

    @Override
    public void load(IntConsumer loaded) {
        if (!FILE.exists()) {
            FactionsPlugin.getInstance().getLogger().info("No board to load from disk. Creating new file.");
            forceSave(null);
            loaded.accept(0);
            return;
        }
        Bukkit.getScheduler().runTaskAsynchronously(FactionsPlugin.getInstance(), () -> {
            try {
                Map<String, Map<String, String>> worldCoordIds = FactionsPlugin.getInstance().getGson().fromJson(DiscUtil.read(FILE), new TypeToken<Map<String, Map<String, String>>>(){}.getType());

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
