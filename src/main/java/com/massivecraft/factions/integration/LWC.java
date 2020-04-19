package com.massivecraft.factions.integration;

import com.griefcraft.lwc.LWCPlugin;
import com.griefcraft.model.Protection;
import com.massivecraft.factions.FLocation;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.FactionsPlugin;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import it.unimi.dsi.fastutil.objects.ObjectSets;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.plugin.Plugin;

import java.util.Set;

public class LWC {
    private static com.griefcraft.lwc.LWC lwc;

    public static void setup() {
        Plugin test = Bukkit.getServer().getPluginManager().getPlugin("LWC");
        if (!(test instanceof LWCPlugin) || !test.isEnabled()) return;

        lwc = ((LWCPlugin) test).getLWC();
        FactionsPlugin.getInstance().log("Successfully hooked into LWC!" + (FactionsPlugin.getInstance().conf().lwc().isEnabled() ? "" : " Integration is currently disabled (\"lwc.integration\")."));
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
                if (!faction.getFPlayers().contains(FPlayers.getInstance().getByOfflinePlayer(Bukkit.getServer().getOfflinePlayer(protection.getOwner())))) {
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
            return ObjectSets.emptySet(); // world not loaded or something? cancel out to prevent error
        }
        ObjectSet<Block> blocks = new ObjectOpenHashSet<>();
        for (BlockState tileEntity : world.getChunkAt(flocation.getX(), flocation.getZ()).getTileEntities()) {
            if (!lwc.isProtectable(tileEntity)) {
                continue;
            }
            blocks.add(tileEntity.getBlock());
        }
        return blocks;
    }
}
