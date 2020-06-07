package com.massivecraft.factions.config.transition;

import com.google.common.base.Charsets;
import com.google.common.collect.Maps;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.massivecraft.factions.FLocation;
import com.massivecraft.factions.FactionsPlugin;
import com.massivecraft.factions.config.Loader;
import com.massivecraft.factions.config.transition.oldclass.v0.*;
import com.massivecraft.factions.config.transition.oldclass.v1.OldMainConfigV1;
import com.massivecraft.factions.config.transition.oldclass.v1.TransitionConfigV1;
import com.massivecraft.factions.data.json.adapters.*;
import com.massivecraft.factions.perms.Role;
import com.massivecraft.factions.util.LazyLocation;
import com.massivecraft.factions.util.material.FactionMaterial;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import org.bukkit.Material;

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;

public class Transitioner {
    
    private Gson gsonV0;
    private static final JsonParser PARSER = new JsonParser();

    public static void transition() {
        Transitioner transitioner = new Transitioner();
        transitioner.migrateV0();

        Path confPath = FactionsPlugin.getInstance().getDataFolder().toPath().resolve("config").resolve("main.conf");
        if (!confPath.toFile().exists()) {
            return;
        }

        HoconConfigurationLoader loader = Loader.getLoader("main");
        try {
            CommentedConfigurationNode rootNode = loader.load();
            CommentedConfigurationNode versionNode = rootNode.getNode("aVeryFriendlyFactionsConfig").getNode("version");

            if (versionNode.isVirtual()) {
                transitioner.migrateV1(loader);
                rootNode = loader.load();
                versionNode = rootNode.getNode("aVeryFriendlyFactionsConfig").getNode("version");
                if (versionNode.isVirtual()) {
                    return; // Failure!
                }
            }
            int version = rootNode.getNode("aVeryFriendlyFactionsConfig").getNode("version").getInt();
            if (version < 3) {
                transitioner.migrateV2(rootNode);
            }

            loader.save(rootNode);
        } catch (IOException e) {
            FactionsPlugin.getInstance().getPluginLogger().log(Level.SEVERE, "Failed to save configuration migration! Data may be lost, requiring restoration from backups.", e);
        }
    }

    private void migrateV0() {
        Path pluginFolder = FactionsPlugin.getInstance().getDataFolder().toPath();
        Path configFolder = pluginFolder.resolve("config");
        if (configFolder.toFile().exists()) {
            // Found existing config, so nothing to do here.
            return;
        }
        Path oldConf = pluginFolder.resolve("conf.json");
        if (!oldConf.toFile().exists()) {
            // No config, no conversion!
            return;
        }
        Path oldConfigFolder = pluginFolder.resolve("oldConfig");
        if (Files.exists(oldConfigFolder)) {
            // Found existing oldConfig, implying it was already upgraded once
            FactionsPlugin.getInstance().getPluginLogger().warning("Found no 'config' folder, but an 'oldConfig' exists. Not attempting conversion.");
            return;
        }
        FactionsPlugin.getInstance().getPluginLogger().info("Found no 'config' folder. Starting configuration transition...");
        this.buildV0Gson();
        try {
            OldConfV0 conf = this.gsonV0.fromJson(new String(Files.readAllBytes(oldConf), StandardCharsets.UTF_8), OldConfV0.class);
            TransitionConfigV0 newConfig = new TransitionConfigV0(conf);
            Loader.loadAndSave("main", newConfig);
            Files.createDirectory(oldConfigFolder);
            Path dataFolder = pluginFolder.resolve("data");
            if (Files.notExists(dataFolder)) {
                Files.createDirectory(dataFolder);
            }
            Path newPlayers = dataFolder.resolve("players.json");
            Files.move(pluginFolder.resolve("players.json"), newPlayers);
            Files.move(pluginFolder.resolve("board.json"), dataFolder.resolve("board.json"));

            FactionsPlugin.getInstance().getIOController().write(newPlayers, this.gsonV0, transitionRoles(PARSER.parse(Files.newBufferedReader(newPlayers, Charsets.UTF_8))), true, null);

            Path oldFactions = pluginFolder.resolve("factions.json");
            Map<String, OldMemoryFactionV0> data = FactionsPlugin.getInstance().getIOController().read(oldFactions, this.gsonV0, new TypeToken<Map<String, OldMemoryFactionV0>>(){}.getType());
            Map<String, NewMemoryFaction> newData = Maps.transformValues(data, NewMemoryFaction::new);

            Files.move(oldFactions, oldConfigFolder.resolve("factions.json"));
            Files.move(oldConf, oldConfigFolder.resolve("conf.json"));

            FactionsPlugin.getInstance().getIOController().write(dataFolder.resolve("factions.json"), this.gsonV0, newData, true, null);
            FactionsPlugin.getInstance().getPluginLogger().info("Transition complete!");
        } catch (IOException | IllegalAccessException exception) {
            FactionsPlugin.getInstance().getPluginLogger().log(Level.SEVERE, "Could not convert old conf.json", exception);
        }
    }

    public JsonObject transitionRoles(JsonElement element) {
        JsonObject object = element.getAsJsonObject();
        for (Map.Entry<String, JsonElement> entry : object.entrySet()) {
            JsonObject data = entry.getValue().getAsJsonObject();
            JsonElement role = data.get("role");
            if (role == null) {
                continue;
            }
            data.remove("role");
            data.addProperty("role", Role.fromString(role.getAsString(), Role.NORMAL).name());
        }
        return object;
    }

    private void buildV0Gson() {
        Type mapFLocToStringSetType = new TypeToken<Map<FLocation, Set<String>>>() {
        }.getType();

        Type accessTypeAdatper = new TypeToken<Map<OldPermissableV0, Map<OldPermissableActionV0, OldAccessV0>>>() {
        }.getType();

        Type factionMaterialType = new TypeToken<FactionMaterial>() {
        }.getType();

        Type materialType = new TypeToken<Material>() {
        }.getType();

        this.gsonV0 = new GsonBuilder()
                .setPrettyPrinting()
                .disableHtmlEscaping()
                .enableComplexMapKeySerialization()
                .excludeFieldsWithModifiers(Modifier.TRANSIENT, Modifier.VOLATILE)
                .registerTypeAdapter(factionMaterialType, new FactionMaterialTypeAdapter())
                .registerTypeAdapter(materialType, new MaterialTypeAdapter())
                .registerTypeAdapter(accessTypeAdatper, new OldPermissionsMapTypeAdapterV0())
                .registerTypeAdapter(LazyLocation.class, new MyLocationTypeAdapter())
                .registerTypeAdapter(mapFLocToStringSetType, new MapFLocToStringSetTypeAdapter())
                .registerTypeAdapter(UUID.class, new UUIDTypeAdapter())
                .registerTypeAdapterFactory(EnumTypeTypeAdapter.ENUM_FACTORY).create();
    }

    private void migrateV1(HoconConfigurationLoader loader) {
        Path pluginFolder = FactionsPlugin.getInstance().getDataFolder().toPath();
        Path configPath = pluginFolder.resolve("config.yml");
        Path oldConfigFolder = pluginFolder.resolve("oldConfig");
        if (!configPath.toFile().exists()) {
            FactionsPlugin.getInstance().getPluginLogger().warning("Found a main.conf from before 0.5.4 but no config.yml was found! Might lose some config information!");
            return;
        }
        try {
            OldMainConfigV1 oldConf = new OldMainConfigV1();
            Loader.load(loader, oldConf);
            TransitionConfigV1 newConf = new TransitionConfigV1();
            Loader.load(loader, newConf);
            newConf.update(oldConf, FactionsPlugin.getInstance().getConfig());
            Loader.loadAndSave(loader, newConf);
            if (Files.notExists(oldConfigFolder)) {
                Files.createDirectories(oldConfigFolder);
            }
            Files.move(configPath, oldConfigFolder.resolve("config.yml"));
        } catch (Exception e) {
            FactionsPlugin.getInstance().getPluginLogger().log(Level.SEVERE, "Could not migrate configuration", e);
        }
    }

    private void migrateV2(CommentedConfigurationNode node) {
        node.getNode("factions").getNode("enterTitles").getNode("title").setValue("");
        node.getNode("factions").getNode("enterTitles").getNode("subtitle").setValue("{faction-relation-color}{faction}");
        node.getNode("aVeryFriendlyFactionsConfig").getNode("version").setValue(3);
        node.getNode("scoreboard").getNode("constant").getNode("factionlessTitle").setValue(node.getNode("scoreboard").getNode("constant").getNode("title").getString());

        FactionsPlugin.getInstance().getPluginLogger().info("Detected a config from before 0.5.7");
        FactionsPlugin.getInstance().getPluginLogger().info("  Setting default enterTitles settings based on old style. Visit main.conf to edit.");
        FactionsPlugin.getInstance().getPluginLogger().info("  Setting default constant scoreboard factionlessTitle settings based on normal title. Visit main.conf to edit.");
    }
}
