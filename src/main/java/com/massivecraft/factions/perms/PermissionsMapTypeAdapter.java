package com.massivecraft.factions.perms;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.massivecraft.factions.FactionsPlugin;
import com.massivecraft.factions.util.TL;
import it.unimi.dsi.fastutil.objects.Object2BooleanMap;
import it.unimi.dsi.fastutil.objects.Object2BooleanOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.logging.Level;

public class PermissionsMapTypeAdapter implements JsonDeserializer<Map<Permissible, Map<PermissibleAction, Boolean>>> {

    @Override
    public Map<Permissible, Map<PermissibleAction, Boolean>> deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {

        try {
            JsonObject obj = json.getAsJsonObject();
            if (obj == null) {
                return null;
            }

            Map<Permissible, Map<PermissibleAction, Boolean>> permissionsMap = new Object2ObjectOpenHashMap<>();

            // Top level is Relation
            for (Map.Entry<String, JsonElement> entry : obj.entrySet()) {
                Permissible permissible = getPermissible(entry.getKey());

                if (permissible == null) {
                    continue;
                }

                // Second level is the map between action -> access
                Object2BooleanMap<PermissibleAction> accessMap = new Object2BooleanOpenHashMap<>();
                for (Map.Entry<String, JsonElement> entry2 : entry.getValue().getAsJsonObject().entrySet()) {
                    PermissibleAction permissibleAction = PermissibleAction.fromString(entry2.getKey());
                    boolean bool;
                    try {
                        bool = entry2.getValue().getAsBoolean();
                    } catch (Exception e) {
                        continue;
                    }
                    accessMap.put(permissibleAction, bool);
                }
                permissionsMap.put(permissible, accessMap);
            }

            return permissionsMap;

        } catch (Exception ex) {
            FactionsPlugin.getInstance().getLogger().log(Level.SEVERE, "Error encountered while deserializing a PermissionsMap.", ex);
            return null;
        }
    }

    private Permissible getPermissible(String name) {
        String upper = name.toUpperCase();
        if (name.equals(upper)) {
            Role possibleRole = Role.fromString(upper);
            if (possibleRole == null) {
                return Relation.fromString(upper);
            }
            return possibleRole;
        }
        if (name.equals(TL.ROLE_RECRUIT.toString())) {
            return Role.RECRUIT;
        }
        if (name.equals(TL.ROLE_NORMAL.toString())) {
            return Role.NORMAL;
        }
        if (name.equals(TL.ROLE_MODERATOR.toString())) {
            return Role.MODERATOR;
        }
        if (name.equals("member")) {
            return null;
        }
        return Relation.fromString(name);
    }
}
