package com.massivecraft.factions.util;

import com.massivecraft.factions.FactionsPlugin;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class WorldUtil {

    private WorldUtil() {
        throw new UnsupportedOperationException("This class cannot be instantiated");
    }

    private static boolean isEnabled(String name) {
        if (!FactionsPlugin.getInstance().conf().restrictWorlds().isRestrictWorlds()) {
            return true;
        }
        return FactionsPlugin.getInstance().conf().restrictWorlds().isWhitelist() == FactionsPlugin.getInstance().conf().restrictWorlds().getWorldList().contains(name);
    }

    public static boolean isEnabled(World world) {
        return isEnabled(world.getName());
    }

    public static boolean isEnabled(CommandSender sender) {
        if (sender instanceof Player) {
            return isEnabled(((Player) sender).getWorld().getName());
        }
        return true;
    }
}
