package com.massivecraft.factions.data.json;

import com.google.gson.reflect.TypeToken;
import com.massivecraft.factions.Board;
import com.massivecraft.factions.FLocation;
import com.massivecraft.factions.FactionsPlugin;
import com.massivecraft.factions.data.MemoryBoard;
import com.massivecraft.factions.data.json.adapters.MapFLocToStringSetTypeAdapter;
import com.massivecraft.factions.util.DiscUtil;
import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import org.bukkit.Bukkit;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.function.IntConsumer;
import java.util.logging.Level;


public class JSONBoard extends MemoryBoard {

    private static final transient Path BOARD_PATH = FactionsPlugin.getInstance().getDataFolder().toPath().resolve("data/board.json");

    //Map<World, Map<CompactCoord, FactionId>>
    public Map<String, Map<String, Integer>> dumpAsSaveFormat() {
        Map<String, Map<String, Integer>> worldCoordIds = new HashMap<>(flocationIds.size());

        for (Entry<FLocation, Integer> entry : flocationIds.entrySet()) {
            worldCoordIds.computeIfAbsent(entry.getKey().getWorldName(), s -> new TreeMap<>()).put(entry.getKey().getCoordString(), entry.getValue());
        }

        return worldCoordIds;
    }

    //world, //
    public void loadFromSaveFormat(Map<String, Map<String, Integer>> worldCoordIds) {
        flocationIds.clear();

        for (Entry<String, Map<String, Integer>> entry : worldCoordIds.entrySet()) {
            for (Entry<String, Integer> entry2 : entry.getValue().entrySet()) {
                String[] coords = MapFLocToStringSetTypeAdapter.COORDINATE_PATTERN.split(entry2.getKey().trim());
                flocationIds.put(FLocation.wrap(entry.getKey(), Integer.parseInt(coords[0]), Integer.parseInt(coords[1])), entry2.getValue());
            }
        }
    }

    public void forceSave(BooleanConsumer finish) {
        forceSave(true, finish);
    }

    public void forceSave(boolean sync, BooleanConsumer finish) {
        DiscUtil.write(BOARD_PATH, FactionsPlugin.getInstance().getGson(), dumpAsSaveFormat(), sync, finish);
    }

    @Override
    public void load(IntConsumer loaded) {
        if (Files.notExists(BOARD_PATH)) {
            FactionsPlugin.getInstance().getLogger().info("No board to load from disk. Creating new file.");
            forceSave(result -> loaded.accept(0));
            return;
        }
        Bukkit.getScheduler().runTaskAsynchronously(FactionsPlugin.getInstance(), () -> {
            try {
                Map<String, Map<String, Integer>> worldCoordIds = DiscUtil.read(BOARD_PATH, FactionsPlugin.getInstance().getGson(), new TypeToken<Map<String, Map<String, Integer>>>(){}.getType());

                Bukkit.getScheduler().runTask(FactionsPlugin.getInstance(), () -> {
                    if (worldCoordIds != null) {
                        loadFromSaveFormat(worldCoordIds);
                    }
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
