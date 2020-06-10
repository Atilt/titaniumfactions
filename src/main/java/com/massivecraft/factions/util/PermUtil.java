package com.massivecraft.factions.util;

import com.massivecraft.factions.FactionsPlugin;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;

import java.util.HashMap;
import java.util.Map;


public final class PermUtil {

    private static final Map<String, String> PERMISSION_DESCRIPTIONS = new HashMap<>();

    private PermUtil() {
        throw new UnsupportedOperationException("This class cannot be instantiated");
    }

    public static String getForbiddenMessage(String perm) {
        return TL.GENERIC_NOPERMISSION.format(getPermissionDescription(perm));
    }

    /**
     * This method hooks into all permission plugins we are supporting
     */
    public static void init() {
        for (Permission permission : FactionsPlugin.getInstance().getDescription().getPermissions()) {
            PERMISSION_DESCRIPTIONS.put(permission.getName(), permission.getDescription());
        }
    }

    public static String getPermissionDescription(String perm) {
        return PERMISSION_DESCRIPTIONS.getOrDefault(perm, TL.GENERIC_DOTHAT.toString());
    }

    public static boolean has(CommandSender me, String perm, boolean informSenderIfNot) {
        if (me == null) {
            return false; // What? How?
        }
        if (me.hasPermission(perm)) {
            return true;
        } else if (informSenderIfNot) {
            me.sendMessage(getForbiddenMessage(perm));
        }
        return false;
    }
}
