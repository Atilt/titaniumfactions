package com.massivecraft.factions.integration;

import com.griefcraft.lwc.LWCPlugin;
import com.griefcraft.model.Protection;
import com.massivecraft.factions.FLocation;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.FactionsPlugin;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.plugin.Plugin;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class LWC {
    private static com.griefcraft.lwc.LWC lwc;

    public static void setup() {
        Plugin test = Bukkit.getPluginManager().getPlugin("LWC");
        if (!(test instanceof LWCPlugin) || !test.isEnabled()) return;

        lwc = ((LWCPlugin) test).getLWC();
        FactionsPlugin.getInstance().getPluginLogger().info("Successfully hooked into LWC!" + (FactionsPlugin.getInstance().conf().lwc().isEnabled() ? "" : " Integration is currently disabled (\"lwc.integration\")."));
    }

    public static boolean getEnabled() {
        return lwc != null && FactionsPlugin.getInstance().conf().lwc().isEnabled();
    }

    public static Plugin getLWC() {
        return lwc == null ? null : lwc.getPlugin();
    }

    public static void clearOtherLocks(FLocation flocation, Faction faction) {
        Protection protection;
        for (Block block : findBlocks(flocation)) {
            if ((protection = lwc.findProtection(block)) != null) {
                if (!faction.getFPlayers().contains(FPlayers.getInstance().getByOfflinePlayer(Bukkit.getOfflinePlayer(protection.getOwner())))) {
                    protection.remove();
                }
            }
        }
    }

    public static void clearAllLocks(FLocation flocation) {
        Protection protection;
        for (Block block : findBlocks(flocation)) {
            if ((protection = lwc.findProtection(block)) != null) {
                protection.remove();
            }
        }
    }

    private static Set<Block> findBlocks(FLocation flocation) {
        World world = Bukkit.getWorld(flocation.getWorldName());
        if (world == null) {
            return Collections.emptySet(); // world not loaded or something? cancel out to prevent error
        }
        BlockState[] blockStates = world.getChunkAt(flocation.getX(), flocation.getZ()).getTileEntities();
        Set<Block> blocks = new HashSet<>(blockStates.length);
        for (BlockState tileEntity : world.getChunkAt(flocation.getX(), flocation.getZ()).getTileEntities()) {
            if (!lwc.isProtectable(tileEntity)) {
                continue;
            }
            blocks.add(tileEntity.getBlock());
        }
        return blocks;
    }
}