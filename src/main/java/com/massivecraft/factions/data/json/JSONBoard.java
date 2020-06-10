package com.massivecraft.factions.data.json;

import com.google.gson.reflect.TypeToken;
import com.massivecraft.factions.FLocation;
import com.massivecraft.factions.FactionsPlugin;
import com.massivecraft.factions.data.MemoryBoard;
import com.massivecraft.factions.data.json.adapters.MapFLocToStringSetTypeAdapter;
import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntRBTreeMap;
import org.bukkit.Bukkit;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.function.IntConsumer;
import java.util.logging.Level;


public class JSONBoard extends MemoryBoard {

    private static final transient Path BOARD_PATH = FactionsPlugin.getInstance().getDataFolder().toPath().resolve("data").resolve("board.json");

    public Map<String, Object2IntMap<String>> dumpAsSaveFormat() {
        Map<String, Object2IntMap<String>> worldCoordIds = new HashMap<>(flocationIds.size());

        for (Object2IntMap.Entry<FLocation> entry : flocationIds.object2IntEntrySet()) {
            worldCoordIds.computeIfAbsent(entry.getKey().getWorldName(), s -> new Object2IntRBTreeMap<>()).put(entry.getKey().getCoordString(), entry.getIntValue());
        }

        return worldCoordIds;
    }

    public void forceSave(BooleanConsumer finish) {
        forceSave(true, finish);
    }

    public void forceSave(boolean sync, BooleanConsumer finish) {
        FactionsPlugin.getInstance().getIOController().write(BOARD_PATH, FactionsPlugin.getInstance().getGson(), dumpAsSaveFormat(), sync, finish);
    }

    @Override
    public void load(IntConsumer loaded) {
        if (Files.notExists(BOARD_PATH)) {
            loaded.accept(0);
            return;
        }
        Bukkit.getScheduler().runTaskAsynchronously(FactionsPlugin.getInstance(), () -> {
            try {
                Map<String, Object2IntMap<String>> worldCoordIds = FactionsPlugin.getInstance().getIOController().read(BOARD_PATH, FactionsPlugin.getInstance().getGson(), new TypeToken<Map<String, Object2IntOpenHashMap<String>>>(){}.getType());

                Bukkit.getScheduler().runTask(FactionsPlugin.getInstance(), () -> {
                    if (worldCoordIds != null) {
                        flocationIds.clear();
                        for (Map.Entry<String, Object2IntMap<String>> world : worldCoordIds.entrySet()) {
                            for (Object2IntMap.Entry<String> coordinates : world.getValue().object2IntEntrySet()) {
                                String[] coords = MapFLocToStringSetTypeAdapter.COORDINATE_PATTERN.split(coordinates.getKey().trim());
                                flocationIds.put(FLocation.wrap(world.getKey(), Integer.parseInt(coords[0]), Integer.parseInt(coords[1])), coordinates.getIntValue());
                            }
                        }
                    }
                    loaded.accept(this.flocationIds.size());
                });
            } catch (Exception e) {
                FactionsPlugin.getInstance().getPluginLogger().log(Level.SEVERE, "Failed to load the board from disk.", e);
                loaded.accept(0);
            }
        });
    }

    @Override
    public void convertFrom(MemoryBoard old, BooleanConsumer finish) {
        this.flocationIds = old.flocationIds;
        forceSave(finish);
        instance = this;
    }
}
