package com.massivecraft.factions.util.material;

import com.google.gson.reflect.TypeToken;
import com.massivecraft.factions.FactionsPlugin;
import me.lucko.helper.reflect.MinecraftVersions;
import org.bukkit.Material;

import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.Map;

public class MaterialDb {

    /*

    This utility has no concept of block metadata, converts if necessary 1.13
    material names to < 1.12 materials, or keeps 1.13 materials.

    Useful as we don't really need extra metadata for stuff like territory block breaking checking.

        "ACACIA_BOAT": {
            "material": "ACACIA_BOAT",
            "legacy": "BOAT_ACACIA"
        }

     */

    private static MaterialDb instance;

    public boolean legacy = true;
    public MaterialProvider provider;

    private MaterialDb() {
    }

    public Material get(String name) {
        return provider.resolve(name);
    }

    public static void load() {
        instance = new MaterialDb();
        if (instance.legacy = FactionsPlugin.getInstance().getMCVersion().isBefore(MinecraftVersions.v1_13)) { // Before 1.13
            FactionsPlugin.getInstance().getLogger().info("Using legacy support for materials");
        }

        InputStreamReader reader = new InputStreamReader(FactionsPlugin.getInstance().getResource("materials.json"));
        Type typeToken = new TypeToken<Map<String, MaterialProvider.MaterialData>>(){}.getType();
        Map<String, MaterialProvider.MaterialData> materialData = FactionsPlugin.getInstance().getGson().fromJson(reader, typeToken);
        FactionsPlugin.getInstance().getLogger().info("Loaded " + materialData.keySet().size() + " material mappings.");
        instance.provider = new MaterialProvider(materialData);
    }

    public static MaterialDb getInstance() {
        return instance;
    }

}
