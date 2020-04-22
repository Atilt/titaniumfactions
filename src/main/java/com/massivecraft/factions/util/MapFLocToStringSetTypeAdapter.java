package com.massivecraft.factions.util;

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

import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;


public class MapFLocToStringSetTypeAdapter implements JsonDeserializer<Map<FLocation, Set<String>>>, JsonSerializer<Map<FLocation, Set<String>>> {

    private static final Pattern COORDINATE_PATTERN = Pattern.compile("[,\\s]+");

    @Override
    public Map<FLocation, Set<String>> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject obj = json.getAsJsonObject();
        if (obj == null) {
            return null;
        }
        Map<FLocation, Set<String>> locationMap = new ConcurrentHashMap<>();

        for (Entry<String, JsonElement> entry : obj.entrySet()) {
            String worldName = entry.getKey();
            for (Entry<String, JsonElement> entry2 : entry.getValue().getAsJsonObject().entrySet()) {

                String[] coords = COORDINATE_PATTERN.split(entry2.getKey().trim());
                int x = Integer.parseInt(coords[0]);
                int z = Integer.parseInt(coords[1]);

                JsonArray array = entry2.getValue().getAsJsonArray();
                Set<String> nameSet = new HashSet<>(array.size());

                for (JsonElement jsonElement : array) {
                    nameSet.add(jsonElement.getAsString());
                }

                locationMap.put(new FLocation(worldName, x, z), nameSet);
            }
        }
        return locationMap;
    }

    @Override
    public JsonElement serialize(Map<FLocation, Set<String>> src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject obj = new JsonObject();
        if (src != null) {
            FLocation loc;
            String locWorld;
            Set<String> nameSet;
            Iterator<String> iter;
            JsonArray nameArray;
            JsonPrimitive nameElement;

            for (Entry<FLocation, Set<String>> entry : src.entrySet()) {
                loc = entry.getKey();
                locWorld = loc.getWorldName();
                nameSet = entry.getValue();

                if (nameSet == null || nameSet.isEmpty()) {
                    continue;
                }

                nameArray = new JsonArray();
                iter = nameSet.iterator();
                while (iter.hasNext()) {
                    nameElement = new JsonPrimitive(iter.next());
                    nameArray.add(nameElement);
                }

                if (!obj.has(locWorld)) {
                    obj.add(locWorld, new JsonObject());
                }

                obj.get(locWorld).getAsJsonObject().add(loc.getCoordString(), nameArray);
            }
        }
        return obj;
    }
}
