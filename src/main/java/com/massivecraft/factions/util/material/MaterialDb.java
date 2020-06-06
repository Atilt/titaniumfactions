package com.massivecraft.factions.util.material;

import com.google.gson.reflect.TypeToken;
import com.massivecraft.factions.FactionsPlugin;
import me.lucko.helper.reflect.MinecraftVersions;
import org.bukkit.Material;

import java.io.InputStreamReader;
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

    private boolean legacy = true;
    private MaterialProvider provider;

    private MaterialDb() {}

    public static MaterialDb get() {
        if (instance == null) {
            synchronized (MaterialDb.class) {
                if (instance == null) {
                    instance = new MaterialDb();
                }
            }
        }
        return instance;
    }

    public boolean isLegacy() {
        return this.legacy;
    }

    public MaterialProvider getProvider() {
        return this.provider;
    }

    public Material get(String name) {
        return this.provider.resolve(name);
    }

    public int load() {
        this.legacy = FactionsPlugin.getInstance().getMCVersion().isBefore(MinecraftVersions.v1_13);
        InputStreamReader reader = new InputStreamReader(FactionsPlugin.getInstance().getResource("materials.json"));
        Map<String, MaterialProvider.MaterialData> materialData = FactionsPlugin.getInstance().getGson().fromJson(reader, new TypeToken<Map<String, MaterialProvider.MaterialData>>(){}.getType());
        this.provider = new MaterialProvider(materialData);
        return materialData.size();
    }
}
