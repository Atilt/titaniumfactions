package com.massivecraft.factions.data.json.adapters;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.massivecraft.factions.util.FastUUID;

import java.lang.reflect.Type;
import java.util.UUID;

public final class UUIDTypeAdapter implements JsonDeserializer<UUID>, JsonSerializer<UUID> {

    @Override
    public UUID deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        return FastUUID.parseUUID(jsonElement.getAsString());
    }

    @Override
    public JsonElement serialize(UUID uuid, Type type, JsonSerializationContext jsonSerializationContext) {
        return new JsonPrimitive(FastUUID.toString(uuid));
    }
}
