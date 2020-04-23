package com.massivecraft.factions.data.json.adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.massivecraft.factions.util.FastUUID;

import java.io.IOException;
import java.util.UUID;

public final class UUIDTypeAdapter extends TypeAdapter<UUID> {

    @Override
    public void write(JsonWriter jsonWriter, UUID uuid) throws IOException {
        jsonWriter.value(FastUUID.toString(uuid));
    }

    @Override
    public UUID read(JsonReader jsonReader) throws IOException {
        return FastUUID.parseUUID(jsonReader.nextString());
    }
}
