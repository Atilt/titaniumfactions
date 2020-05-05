package com.massivecraft.factions.util;

import com.massivecraft.factions.FactionsPlugin;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class WorldUtil {
    private ObjectSet<String> worlds;
    private boolean check;
    private boolean whitelist;

    public WorldUtil(FactionsPlugin plugin) {
        check = plugin.conf().restrictWorlds().isRestrictWorlds();
        if (!check) {
            return;
        }
        worlds = new ObjectOpenHashSet<>(plugin.conf().restrictWorlds().getWorldList());
        whitelist = plugin.conf().restrictWorlds().isWhitelist();
    }

    private boolean isEnabled(String name) {
        if (!check) {
            return true;
        }
        return whitelist == worlds.contains(name);
    }

    public boolean isEnabled(World world) {
        return isEnabled(world.getName());
    }

    public boolean isEnabled(CommandSender sender) {
        if (sender instanceof Player) {
            return isEnabled(((Player) sender).getWorld().getName());
        }
        return true;
    }
}
