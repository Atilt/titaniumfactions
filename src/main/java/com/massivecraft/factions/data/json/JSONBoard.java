package com.massivecraft.factions.data.json;

import com.google.gson.reflect.TypeToken;
import com.massivecraft.factions.Board;
import com.massivecraft.factions.FLocation;
import com.massivecraft.factions.FactionsPlugin;
import com.massivecraft.factions.data.MemoryBoard;
import com.massivecraft.factions.data.json.adapters.MapFLocToStringSetTypeAdapter;
import com.massivecraft.factions.util.DiscUtil;
import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntRBTreeMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import org.bukkit.Bukkit;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Map;
import java.util.function.IntConsumer;
import java.util.logging.Level;


public class JSONBoard extends MemoryBoard {

    private static final transient Path BOARD_PATH = FactionsPlugin.getInstance().getDataFolder().toPath().resolve("data").resolve("board.json");

    //Map<World, Map<CompactCoord, FactionId>>
    public Map<String, Object2IntMap<String>> dumpAsSaveFormat() {
        Object2ObjectMap<String, Object2IntMap<String>> worldCoordIds = new Object2ObjectOpenHashMap<>(flocationIds.size());

        for (Object2IntMap.Entry<FLocation> entry : flocationIds.object2IntEntrySet()) {
            worldCoordIds.computeIfAbsent(entry.getKey().getWorldName(), s -> new Object2IntRBTreeMap<>()).put(entry.getKey().getCoordString(), entry.getIntValue());
        }

        return worldCoordIds;
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
            //move into folder if outside of folder
            Path possibleData = FactionsPlugin.getInstance().getDataFolder().toPath().resolve("board.json");
            if (Files.notExists(possibleData)) {
                forceSave(result -> loaded.accept(0));
                return;
            }
            try {
                Files.move(possibleData, BOARD_PATH, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Bukkit.getScheduler().runTaskAsynchronously(FactionsPlugin.getInstance(), () -> {
            try {
                Object2ObjectMap<String, Object2IntMap<String>> worldCoordIds = DiscUtil.read(BOARD_PATH, FactionsPlugin.getInstance().getGson(), new TypeToken<Object2ObjectOpenHashMap<String, Object2IntOpenHashMap<String>>>(){}.getType());

                Bukkit.getScheduler().runTask(FactionsPlugin.getInstance(), () -> {
                    if (worldCoordIds != null) {
                        flocationIds.clear();
                        for (Object2ObjectMap.Entry<String, Object2IntMap<String>> world : worldCoordIds.object2ObjectEntrySet()) {
                            for (Object2IntMap.Entry<String> coordinates : world.getValue().object2IntEntrySet()) {
                                String[] coords = MapFLocToStringSetTypeAdapter.COORDINATE_PATTERN.split(coordinates.getKey().trim());
                                flocationIds.put(FLocation.wrap(world.getKey(), Integer.parseInt(coords[0]), Integer.parseInt(coords[1])), coordinates.getIntValue());
                            }
                        }
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
