package com.massivecraft.factions.data.json.adapters;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.massivecraft.factions.FLocation;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Pattern;


public class MapFLocToStringSetTypeAdapter implements JsonDeserializer<Map<FLocation, Set<String>>>, JsonSerializer<Map<FLocation, Set<String>>> {

    public static final transient Pattern COORDINATE_PATTERN = Pattern.compile("[,\\s]+");

    @Override
    public Map<FLocation, Set<String>> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject obj = json.getAsJsonObject();
        if (obj == null) {
            return null;
        }
        Map<FLocation, Set<String>> locationMap = new Object2ObjectOpenHashMap<>();

        for (Entry<String, JsonElement> entry : obj.entrySet()) {
            String worldName = entry.getKey();
            for (Entry<String, JsonElement> entry2 : entry.getValue().getAsJsonObject().entrySet()) {

                String[] coords = COORDINATE_PATTERN.split(entry2.getKey().trim());
                int x = Integer.parseInt(coords[0]);
                int z = Integer.parseInt(coords[1]);

                JsonArray array = entry2.getValue().getAsJsonArray();
                Set<String> nameSet = new ObjectOpenHashSet<>(array.size());

                for (JsonElement jsonElement : array) {
                    nameSet.add(jsonElement.getAsString());
                }

                locationMap.put(FLocation.wrap(worldName, x, z), nameSet);
            }
        }
        return locationMap;
    }

    @Override
    public JsonElement serialize(Map<FLocation, Set<String>> src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject obj = new JsonObject();
        if (src != null) {

            for (Entry<FLocation, Set<String>> entry : src.entrySet()) {
                Set<String> nameSet = entry.getValue();

                if (nameSet == null || nameSet.isEmpty()) {
                    continue;
                }

                JsonArray nameArray = new JsonArray();
                for (String s : nameSet) {
                    nameArray.add(new JsonPrimitive(s));
                }
                FLocation loc = entry.getKey();
                String locWorld = loc.getWorldName();

                if (!obj.has(locWorld)) {
                    obj.add(locWorld, new JsonObject());
                }

                obj.get(locWorld).getAsJsonObject().add(loc.getCoordString(), nameArray);
            }
        }
        return obj;
    }
}
