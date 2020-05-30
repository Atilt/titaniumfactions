package com.massivecraft.factions.util.material;

import com.google.gson.annotations.SerializedName;
import com.massivecraft.factions.FactionsPlugin;
import me.lucko.helper.reflect.MinecraftVersions;
import org.bukkit.Material;

import java.util.EnumSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class MaterialProvider {

    private static final Set<Material> SIGNS = EnumSet.noneOf(Material.class);
    private static final Set<Material> DOORS = EnumSet.noneOf(Material.class);
    private static final Set<Material> GATES = EnumSet.noneOf(Material.class);
    private static final Set<Material> EXPLOITABLE = EnumSet.noneOf(Material.class);
    private static final Set<Material> PRESSURE_PLATES = EnumSet.noneOf(Material.class);
    private static final Set<Material> MINECARTS = EnumSet.noneOf(Material.class);
    private static final Set<Material> EXPLOSIVE_RESISTANT = EnumSet.noneOf(Material.class);
    private static final Set<Material> SHULKER_BOXES = EnumSet.noneOf(Material.class);
    private static final Set<Material> POTTED_FLOWERS = EnumSet.noneOf(Material.class);
    private static final Set<Material> FENCES = EnumSet.noneOf(Material.class);

    protected Map<String, MaterialData> materialData;

    MaterialProvider(Map<String, MaterialData> materialData) {
        this.materialData = materialData;

        SIGNS.add(resolve("OAK_SIGN"));
        SIGNS.add(resolve("ACACIA_SIGN"));
        SIGNS.add(resolve("BIRCH_SIGN"));
        SIGNS.add(resolve("DARK_OAK_SIGN"));
        SIGNS.add(resolve("JUNGLE_SIGN"));
        SIGNS.add(resolve("SPRUCE_SIGN"));
        SIGNS.add(resolve("OAK_WALL_SIGN"));
        SIGNS.add(resolve("ACACIA_WALL_SIGN"));
        SIGNS.add(resolve("BIRCH_WALL_SIGN"));
        SIGNS.add(resolve("DARK_OAK_WALL_SIGN"));
        SIGNS.add(resolve("JUNGLE_WALL_SIGN"));
        SIGNS.add(resolve("SPRUCE_WALL_SIGN"));

        DOORS.add(resolve("DARK_OAK_DOOR"));
        DOORS.add(resolve("ACACIA_DOOR"));
        DOORS.add(resolve("BIRCH_DOOR"));
        DOORS.add(resolve("JUNGLE_DOOR"));
        DOORS.add(resolve("OAK_DOOR"));
        DOORS.add(resolve("SPRUCE_DOOR"));
        DOORS.add(resolve("IRON_DOOR"));

        GATES.add(resolve("OAK_FENCE_GATE"));
        GATES.add(resolve("DARK_OAK_FENCE_GATE"));
        GATES.add(resolve("ACACIA_FENCE_GATE"));
        GATES.add(resolve("BIRCH_FENCE_GATE"));
        GATES.add(resolve("JUNGLE_FENCE_GATE"));
        GATES.add(resolve("SPRUCE_FENCE_GATE"));

        FENCES.add(resolve("OAK_FENCE"));
        FENCES.add(resolve("DARK_OAK_FENCE"));
        FENCES.add(resolve("ACACIA_FENCE"));
        FENCES.add(resolve("BIRCH_FENCE"));
        FENCES.add(resolve("JUNGLE_FENCE"));
        FENCES.add(resolve("SPRUCE_FENCE"));

        EXPLOITABLE.addAll(SIGNS);
        EXPLOITABLE.addAll(DOORS);
        EXPLOITABLE.add(resolve("CHEST"));
        EXPLOITABLE.add(resolve("TRAPPED_CHEST"));

        PRESSURE_PLATES.add(resolve("HEAVY_WEIGHTED_PRESSURE_PLATE"));
        PRESSURE_PLATES.add(resolve("LIGHT_WEIGHTED_PRESSURE_PLATE"));
        PRESSURE_PLATES.add(resolve("STONE_PRESSURE_PLATE"));
        PRESSURE_PLATES.add(resolve("LIGHT_WEIGHTED_PRESSURE_PLATE"));
        if (FactionsPlugin.getInstance().getMCVersion().isAfterOrEq(MinecraftVersions.v1_13)) {
            PRESSURE_PLATES.add(resolve("ACACIA_PRESSURE_PLATE"));
            PRESSURE_PLATES.add(resolve("BIRCH_PRESSURE_PLATE"));
            PRESSURE_PLATES.add(resolve("DARK_OAK_PRESSURE_PLATE"));
            PRESSURE_PLATES.add(resolve("JUNGLE_PRESSURE_PLATE"));
            PRESSURE_PLATES.add(resolve("OAK_PRESSURE_PLATE"));
            PRESSURE_PLATES.add(resolve("SPRUCE_PRESSURE_PLATE"));
        } else {
            PRESSURE_PLATES.add(Material.valueOf("WOOD_PLATE"));
        }
        MINECARTS.add(Material.MINECART);
        MINECARTS.add(resolve("CHEST_MINECART"));
        MINECARTS.add(resolve("COMMAND_BLOCK_MINECART"));
        MINECARTS.add(resolve("FURNACE_MINECART"));
        MINECARTS.add(resolve("HOPPER_MINECART"));
        MINECARTS.add(resolve("TNT_MINECART"));

        EXPLOSIVE_RESISTANT.add(resolve("WATER"));
        EXPLOSIVE_RESISTANT.add(resolve("STATIONARY_WATER"));
        EXPLOSIVE_RESISTANT.add(resolve("AIR"));
        EXPLOSIVE_RESISTANT.add(resolve("CAVE_AIR"));
        EXPLOSIVE_RESISTANT.add(resolve("VOID_AIR"));
        EXPLOSIVE_RESISTANT.add(resolve("BEDROCK"));
        EXPLOSIVE_RESISTANT.add(resolve("LAVA"));
        EXPLOSIVE_RESISTANT.add(resolve("STATIONARY_LAVA"));
        EXPLOSIVE_RESISTANT.add(resolve("OBSIDIAN"));
        EXPLOSIVE_RESISTANT.add(resolve("WATER"));
        EXPLOSIVE_RESISTANT.add(resolve("END_PORTAL"));
        EXPLOSIVE_RESISTANT.add(resolve("END_PORTAL_FRAME"));
        EXPLOSIVE_RESISTANT.add(resolve("NETHER_PORTAL"));
        EXPLOSIVE_RESISTANT.add(resolve("ENCHANTING_TABLE"));
        EXPLOSIVE_RESISTANT.add(resolve("ANVIL"));
        EXPLOSIVE_RESISTANT.add(resolve("CHIPPED_ANVIL"));
        EXPLOSIVE_RESISTANT.add(resolve("DAMAGED_ANVIL"));
        EXPLOSIVE_RESISTANT.add(resolve("ENDER_CHEST"));

        if (FactionsPlugin.getInstance().getMCVersion().isAfterOrEq(MinecraftVersions.v1_9)) {
            SHULKER_BOXES.add(resolve("SHULKER_BOX"));
            if (FactionsPlugin.getInstance().getMCVersion().isAfterOrEq(MinecraftVersions.v1_11)) {
                SHULKER_BOXES.add(resolve("WHITE_SHULKER_BOX"));
                SHULKER_BOXES.add(resolve("YELLOW_SHULKER_BOX"));
                SHULKER_BOXES.add(resolve("BLACK_SHULKER_BOX"));
                SHULKER_BOXES.add(resolve("BLUE_SHULKER_BOX"));
                SHULKER_BOXES.add(resolve("BROWN_SHULKER_BOX"));
                SHULKER_BOXES.add(resolve("CYAN_SHULKER_BOX"));
                SHULKER_BOXES.add(resolve("GRAY_SHULKER_BOX"));
                SHULKER_BOXES.add(resolve("GREEN_SHULKER_BOX"));
                SHULKER_BOXES.add(resolve("LIGHT_BLUE_SHULKER_BOX"));
                SHULKER_BOXES.add(resolve("LIGHT_GRAY_SHULKER_BOX"));
                SHULKER_BOXES.add(resolve("LIME_SHULKER_BOX"));
                SHULKER_BOXES.add(resolve("MAGENTA_SHULKER_BOX"));
                SHULKER_BOXES.add(resolve("ORANGE_SHULKER_BOX"));
                SHULKER_BOXES.add(resolve("PINK_SHULKER_BOX"));
                SHULKER_BOXES.add(resolve("PURPLE_SHULKER_BOX"));
                SHULKER_BOXES.add(resolve("RED_SHULKER_BOX"));
            }

        }
        POTTED_FLOWERS.add(Material.valueOf("FLOWER_POT"));
        if (FactionsPlugin.getInstance().getMCVersion().isAfterOrEq(MinecraftVersions.v1_13)) {
            POTTED_FLOWERS.add(resolve("POTTED_ACACIA_SAPLING"));
            POTTED_FLOWERS.add(resolve("POTTED_ALLIUM"));
            POTTED_FLOWERS.add(resolve("POTTED_AZURE_BLUET"));
            POTTED_FLOWERS.add(resolve("POTTED_BIRCH_SAPLING"));
            POTTED_FLOWERS.add(resolve("POTTED_BLUE_ORCHID"));
            POTTED_FLOWERS.add(resolve("POTTED_BROWN_MUSHROOM"));
            POTTED_FLOWERS.add(resolve("POTTED_CACTUS"));
            POTTED_FLOWERS.add(resolve("POTTED_DANDELION"));
            POTTED_FLOWERS.add(resolve("POTTED_DARK_OAK_SAPLING"));
            POTTED_FLOWERS.add(resolve("POTTED_DEAD_BUSH"));
            POTTED_FLOWERS.add(resolve("POTTED_FERN"));
            POTTED_FLOWERS.add(resolve("POTTED_JUNGLE_SAPLING"));
            POTTED_FLOWERS.add(resolve("POTTED_OAK_SAPLING"));
            POTTED_FLOWERS.add(resolve("POTTED_ORANGE_TULIP"));
            POTTED_FLOWERS.add(resolve("POTTED_OXEYE_DAISY"));
            POTTED_FLOWERS.add(resolve("POTTED_ACACIA_SAPLING"));
            POTTED_FLOWERS.add(resolve("POTTED_PINK_TULIP"));
            POTTED_FLOWERS.add(resolve("POTTED_POPPY"));
            POTTED_FLOWERS.add(resolve("POTTED_RED_MUSHROOM"));
            POTTED_FLOWERS.add(resolve("POTTED_RED_TULIP"));
            POTTED_FLOWERS.add(resolve("POTTED_WHITE_TULIP"));
        }
    }

    public Material resolve(String name) {
        if (name == null) {
            return Material.AIR;
        }

        MaterialProvider.MaterialData data = materialData.get(name);

        if (data == null) {
            FactionsPlugin.getInstance().getPluginLogger().warning("Material database could not recognize: " + name);
            return Material.AIR;
        }

        Material material = data.get();
        if (material == null) {
            FactionsPlugin.getInstance().getPluginLogger().warning("Material database could not recognize: " + name);
            return Material.AIR;
        }
        return material;
    }

    public boolean isLegacy(String legacy) {
        // If we don't have it as key then it is Legacy or doesn't exist
        return !materialData.containsKey(legacy);
    }

    public String fromLegacy(String legacy) {
        for (MaterialData data : materialData.values()) {
            if (data.legacy != null && data.legacy.equalsIgnoreCase(legacy)) {
                return data.name;
            } else if (data.name.equalsIgnoreCase(legacy)) {
                // If legacy doesn't match but name does return it
                return data.name;
            }
        }
        return null;
    }

    public boolean isSign(Material material) {
        return SIGNS.contains(material);
    }

    public boolean isDoor(Material material) {
        return DOORS.contains(material);
    }

    public boolean isGate(Material material) {
        return GATES.contains(material);
    }

    public boolean isFence(Material material) {
        return FENCES.contains(material);
    }

    public boolean isExploitable(Material material) {
        return EXPLOITABLE.contains(material);
    }
    
    public boolean isPressurePlate(Material material) {
        return PRESSURE_PLATES.contains(material);
    }

    public boolean isMinecart(Material material) {
        return MINECARTS.contains(material);
    }

    public boolean isExplosiveResistant(Material material) {
        return EXPLOSIVE_RESISTANT.contains(material);
    }

    public boolean isShulkerBox(Material material) {
        return SHULKER_BOXES.contains(material);
    }
    
    public boolean isPottedFlower(Material material) {
        return POTTED_FLOWERS.contains(material);
    }

    public class MaterialData {

        @SerializedName("material")
        private String name;
        private String legacy;

        public MaterialData(String name, String legacy) {
            this.name = name;
            this.legacy = legacy;
        }

        public Material get() {
            return Material.matchMaterial(MaterialDb.get().isLegacy() ? this.legacy == null ? this.name : this.legacy : this.name);
        }

        @Override
        public String toString() {
            return "MaterialData{" +
                    "name='" + name + '\'' +
                    ", legacy='" + legacy + '\'' +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            MaterialData that = (MaterialData) o;

            if (!Objects.equals(name, that.name)) return false;
            return Objects.equals(legacy, that.legacy);
        }

        @Override
        public int hashCode() {
            int result = name != null ? name.hashCode() : 0;
            result = 31 * result + (legacy != null ? legacy.hashCode() : 0);
            return result;
        }
    }
}
