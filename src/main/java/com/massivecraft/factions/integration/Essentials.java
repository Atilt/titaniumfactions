package com.massivecraft.factions.integration;

import com.earth2me.essentials.Trade;
import com.massivecraft.factions.FactionsPlugin;
import net.ess3.api.IEssentials;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.plugin.Plugin;

import java.math.BigDecimal;

public class Essentials {

    private static IEssentials essentials;

    public static IEssentials setup() {
        Plugin ess = Bukkit.getPluginManager().getPlugin("Essentials");
        if (ess != null) {
            essentials = (IEssentials) ess;
            return essentials;
        }

        return null;
    }

    // return false if feature is disabled or Essentials isn't available
    public static boolean handleTeleport(Player player, Location loc) {
        if (!FactionsPlugin.getInstance().conf().factions().homes().isTeleportCommandEssentialsIntegration() || essentials == null) {
            return false;
        }
        try {
            essentials.getUser(player).getTeleport().teleport(loc, new Trade(BigDecimal.valueOf(FactionsPlugin.getInstance().conf().economy().getCostHome()), essentials), PlayerTeleportEvent.TeleportCause.PLUGIN);
        } catch (Exception e) {
            player.sendMessage(ChatColor.RED.toString() + e.getMessage());
        }
        return true;
    }

    public static boolean isVanished(Player player) {
        return essentials != null && player != null && essentials.getUser(player).isVanished();
    }

    public static boolean isOverBalCap(double amount) {
        if (essentials == null) {
            return false;
        }

        return amount > essentials.getSettings().getMaxMoney().doubleValue();
    }

    public static Plugin getEssentials() {
        return essentials;
    }
}
