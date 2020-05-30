package com.massivecraft.factions.data.json.adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.massivecraft.factions.util.material.FactionMaterial;

import java.io.IOException;

public final class FactionMaterialTypeAdapter extends TypeAdapter<FactionMaterial> {

    @Override
    public void write(JsonWriter out, FactionMaterial value) throws IOException {
        out.value(value.name());
    }

    @Override
    public FactionMaterial read(JsonReader in) throws IOException {
        return FactionMaterial.from(in.nextString());
    }

}
