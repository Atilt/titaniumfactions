package com.massivecraft.factions.data.json.adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.massivecraft.factions.util.LazyLocation;

import java.io.IOException;


public class MyLocationTypeAdapter extends TypeAdapter<LazyLocation> {

    @Override
    public LazyLocation read(JsonReader jsonReader) throws IOException {
        LazyLocation.Builder builder = new LazyLocation.Builder();
        jsonReader.beginObject();
        while (jsonReader.hasNext()) {
            String key = jsonReader.nextName();
            switch (key) {
                case "world":
                    builder.worldName(jsonReader.nextString());
                    break;
                case "x":
                    builder.withX(jsonReader.nextDouble());
                    break;
                case "y":
                    builder.withY(jsonReader.nextDouble());
                    break;
                case "z":
                    builder.withZ(jsonReader.nextDouble());
                    break;
                case "yaw":
                    builder.yaw((float) jsonReader.nextDouble());
                    break;
                case "pitch":
                    builder.pitch((float) jsonReader.nextDouble());
            }
        }
        jsonReader.endObject();
        return builder.build();
    }

    @Override
    public void write(JsonWriter jsonWriter, LazyLocation lazyLocation) throws IOException {
        jsonWriter.beginObject()
            .name("world").value(lazyLocation.getWorldName())
            .name("x").value(lazyLocation.getX())
            .name("y").value(lazyLocation.getY())
            .name("z").value(lazyLocation.getZ())
            .name("yaw").value(lazyLocation.getYaw())
            .name("pitch").value(lazyLocation.getPitch())
        .endObject();
    }
}
