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
import com.massivecraft.factions.util.UUIDFetcher;
import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;

public class JSONFPlayers extends MemoryFPlayers {
    public Gson getGson() {
        return FactionsPlugin.getInstance().getGson();
    }

    public void setGson(Gson gson) {
        // NOOP
    }

    private File file;

    public JSONFPlayers() {
        if (FactionsPlugin.getInstance().getServerUUID() == null) {
            FactionsPlugin.getInstance().grumpException(new RuntimeException());
        }
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
        final Map<String, JSONFPlayer> entitiesThatShouldBeSaved = new HashMap<>();
        for (FPlayer entity : this.fPlayers.values()) {
            if (((MemoryFPlayer) entity).shouldBeSaved()) {
                entitiesThatShouldBeSaved.put(entity.getId(), (JSONFPlayer) entity);
            }
        }

        saveCore(file, entitiesThatShouldBeSaved, sync, finish);
    }

    private boolean saveCore(File target, Map<String, JSONFPlayer> data, boolean sync, BooleanConsumer finish) {
        return DiscUtil.writeCatch(target, FactionsPlugin.getInstance().getGson().toJson(data), sync, finish);
    }

    public int load(BooleanConsumer finish) {
        Map<String, JSONFPlayer> fplayers = this.loadCore(finish);
        if (fplayers == null) {
            return 0;
        }
        this.fPlayers.clear();
        this.fPlayers.putAll(fplayers);
        return fPlayers.size();
    }

    private Map<String, JSONFPlayer> loadCore(BooleanConsumer finish) {
        if (!this.file.exists()) {
            return new HashMap<>();
        }

        String content = DiscUtil.readCatch(this.file);
        if (content == null) {
            return null;
        }

        Map<String, JSONFPlayer> data = FactionsPlugin.getInstance().getGson().fromJson(content, new TypeToken<Map<String, JSONFPlayer>>() {
        }.getType());
        Set<String> list = new HashSet<>();
        Set<String> invalidList = new HashSet<>();
        for (Entry<String, JSONFPlayer> entry : data.entrySet()) {
            String key = entry.getKey();
            entry.getValue().setId(key);
            if (doesKeyNeedMigration(key)) {
                if (!isKeyInvalid(key)) {
                    list.add(key);
                } else {
                    invalidList.add(key);
                }
            }
        }

        if (list.size() > 0) {
            // We've got some converting to do!
            FactionsPlugin.getInstance().log(Level.INFO, "Factions is now updating players.json");

            // First we'll make a backup, because god forbid anybody heed a
            // warning
            File file = new File(this.file.getParentFile(), "players.json.old");
            try {
                file.createNewFile();
            } catch (IOException e) {
                FactionsPlugin.getInstance().getLogger().log(Level.SEVERE, "Failed to create file players.json.old", e);
            }
            saveCore(file, data, true, finish);
            FactionsPlugin.getInstance().log(Level.INFO, "Backed up your old data at " + file.getAbsolutePath());

            // Start fetching those UUIDs
            FactionsPlugin.getInstance().log(Level.INFO, "Please wait while Factions converts " + list.size() + " old player names to UUID. This may take a while.");
            UUIDFetcher fetcher = new UUIDFetcher(new ArrayList<>(list));
            try {
                Map<String, UUID> response = fetcher.call();
                for (String s : list) {
                    // Are we missing any responses?
                    if (!response.containsKey(s)) {
                        // They don't have a UUID so they should just be removed
                        invalidList.add(s);
                    }
                }
                for (String value : response.keySet()) {
                    // For all the valid responses, let's replace their old
                    // named entry with a UUID key
                    String id = response.get(value).toString();

                    JSONFPlayer player = data.get(value);

                    if (player == null) {
                        // The player never existed here, and shouldn't persist
                        invalidList.add(value);
                        continue;
                    }

                    player.setId(id); // Update the object so it knows

                    data.remove(value); // Out with the old...
                    data.put(id, player); // And in with the new
                }
            } catch (Exception e) {
                FactionsPlugin.getInstance().getLogger().log(Level.SEVERE, "Failed name to UUID conversion", e);
            }
            if (invalidList.size() > 0) {
                for (String name : invalidList) {
                    // Remove all the invalid names we collected
                    data.remove(name);
                }
                FactionsPlugin.getInstance().log(Level.INFO, "While converting we found names that either don't have a UUID or aren't players and removed them from storage.");
                FactionsPlugin.getInstance().log(Level.INFO, "The following names were detected as being invalid: " + StringUtils.join(invalidList, ", "));
            }
            saveCore(this.file, data, true, finish); // Update the
            // flatfile
            FactionsPlugin.getInstance().log(Level.INFO, "Done converting players.json to UUID.");
        }
        return data;
    }

    private boolean doesKeyNeedMigration(String key) {
        if (!key.matches("[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}")) {
            // Not a valid UUID..
            // Valid playername, we'll mark this as one for conversion
            // to UUID
            return key.matches("[a-zA-Z0-9_]{2,16}");
        }
        return false;
    }

    private boolean isKeyInvalid(String key) {
        return !key.matches("[a-zA-Z0-9_]{2,16}");
    }

    @Override
    public FPlayer generateFPlayer(String id) {
        FPlayer player = new JSONFPlayer(id);
        this.fPlayers.put(id, player);
        return player;
    }
}
