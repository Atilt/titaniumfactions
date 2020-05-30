package com.massivecraft.factions.config.transition.oldclass.v0;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.massivecraft.factions.FactionsPlugin;
import com.massivecraft.factions.util.TL;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

public class OldPermissionsMapTypeAdapterV0 implements JsonDeserializer<Map<OldPermissableV0, Map<OldPermissableActionV0, OldAccessV0>>> {

    @Override
    public Map<OldPermissableV0, Map<OldPermissableActionV0, OldAccessV0>> deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {

        try {
            JsonObject obj = json.getAsJsonObject();
            if (obj == null) {
                return null;
            }

            Map<OldPermissableV0, Map<OldPermissableActionV0, OldAccessV0>> permissionsMap = new ConcurrentHashMap<>();

            // Top level is Relation
            for (Map.Entry<String, JsonElement> entry : obj.entrySet()) {
                OldPermissableV0 permissible = getPermissible(entry.getKey());

                if (permissible == null) {
                    continue;
                }

                // Second level is the map between action -> access
                Map<OldPermissableActionV0, OldAccessV0> accessMap = new HashMap<>();
                for (Map.Entry<String, JsonElement> entry2 : entry.getValue().getAsJsonObject().entrySet()) {
                    OldPermissableActionV0 permissableAction = OldPermissableActionV0.fromString(entry2.getKey());
                    if (permissableAction == null) {
                        switch (entry2.getKey()) {
                            case "frostwalk":
                                permissableAction = OldPermissableActionV0.FROST_WALK;
                                break;
                            case "painbuild":
                                permissableAction = OldPermissableActionV0.PAIN_BUILD;
                                break;
                            case "items":
                                permissableAction = OldPermissableActionV0.ITEM;
                                break;
                        }
                    }
                    OldAccessV0 access = OldAccessV0.fromString(entry2.getValue().getAsString());
                    accessMap.put(permissableAction, access);
                }
                permissionsMap.put(permissible, accessMap);
            }

            return permissionsMap;

        } catch (Exception ex) {
            FactionsPlugin.getInstance().getPluginLogger().log(Level.SEVERE, "Error encountered while deserializing a PermissionsMap.", ex);
            return null;
        }
    }

    private OldPermissableV0 getPermissible(String name) {
        String formatted = name.toUpperCase();
        if (name.equals(formatted)) {
            OldRoleV0 oldRole = OldRoleV0.fromString(formatted);
            if (oldRole != null) {
                return oldRole;
            }
            OldRelationV0 oldRelation = OldRelationV0.fromString(formatted);
            if (oldRelation != null) {
                return oldRelation;
            }
        } else {
            if (name.equals(TL.ROLE_RECRUIT.toString())) {
                return OldRoleV0.RECRUIT;
            } else if (name.equals(TL.ROLE_NORMAL.toString())) {
                return OldRoleV0.NORMAL;
            } else if (name.equals(TL.ROLE_MODERATOR.toString())) {
                return OldRoleV0.MODERATOR;
            } else {
                // If it is explicitly member and its old data then it refers to relation member not role, skip it
                if (name.equals("member")) {
                    return null;
                }
            }
        }
        return OldRelationV0.fromString(name);
    }
}
