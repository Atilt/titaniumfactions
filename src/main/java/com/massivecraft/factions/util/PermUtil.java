package com.massivecraft.factions.util;

import com.massivecraft.factions.FactionsPlugin;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;


public final class PermUtil {

    private final Object2ObjectMap<String, String> permissionDescriptions = new Object2ObjectOpenHashMap<>();

    protected FactionsPlugin plugin;

    public PermUtil(FactionsPlugin plugin) {
        this.plugin = plugin;
        this.setup();
    }

    public String getForbiddenMessage(String perm) {
        return plugin.txt().parse(TL.GENERIC_NOPERMISSION.toString(), getPermissionDescription(perm));
    }

    /**
     * This method hooks into all permission plugins we are supporting
     */
    public final void setup() {
        for (Permission permission : plugin.getDescription().getPermissions()) {
            //p.log("\""+permission.getName()+"\" = \""+permission.getDescription()+"\"");
            this.permissionDescriptions.put(permission.getName(), permission.getDescription());
        }
    }

    public String getPermissionDescription(String perm) {
        return permissionDescriptions.getOrDefault(perm, TL.GENERIC_DOTHAT.toString());
    }

    public boolean has(CommandSender me, String perm, boolean informSenderIfNot) {
        if (me == null) {
            return false; // What? How?
        }
        if (me.hasPermission(perm)) {
            return true;
        } else if (informSenderIfNot) {
            me.sendMessage(this.getForbiddenMessage(perm));
        }
        return false;
    }
}
