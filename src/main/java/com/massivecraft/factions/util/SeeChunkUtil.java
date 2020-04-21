package com.massivecraft.factions.util;

import com.massivecraft.factions.Board;
import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.FactionsPlugin;
import com.massivecraft.factions.util.material.FactionMaterial;
import com.massivecraft.factions.util.particle.ParticleColor;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

@SuppressWarnings("unchecked")
public class SeeChunkUtil extends BukkitRunnable {

    private ObjectSet<UUID> playersSeeingChunks = new ObjectOpenHashSet<>();
    private boolean useColor;
    private Object effect;

    public SeeChunkUtil() {
        this.effect = FactionsPlugin.getInstance().getParticleProvider().effectFromString(FactionsPlugin.getInstance().conf().commands().seeChunk().getParticleName());
        this.useColor = FactionsPlugin.getInstance().conf().commands().seeChunk().isRelationalColor();
    }

    @Override
    public void run() {
        ObjectIterator<UUID> iterator = playersSeeingChunks.iterator();
        while (iterator.hasNext()) {
            UUID playerId = iterator.next();
            Player player = Bukkit.getPlayer(playerId);
            if (player == null) {
                iterator.remove();
                continue;
            }
            FPlayer fme = FPlayers.getInstance().getByPlayer(player);
            showPillars(player, fme, this.effect, useColor);

            //implement this whole class as a bucket ??
        }
    }

    @Deprecated
    public void updatePlayerInfo(UUID uuid, boolean toggle) {
        Player player = Bukkit.getPlayer(uuid);
        if (player == null) {
            return;
        }
        updatePlayerInfo(player, toggle);
    }

    public void updatePlayerInfo(Player player, boolean toggle) {
        if (toggle) {
            playersSeeingChunks.add(player.getUniqueId());
        } else {
            if (playersSeeingChunks.remove(player.getUniqueId())) {
                VisualizeUtil.clear(player);
            }
        }
    }

    public static void showPillars(Player me, FPlayer fme, Object effect, boolean useColor) {
        ParticleColor color = null;
        if (useColor) {
            ChatColor chatColor = Board.getInstance().getFactionAt(fme.getLastStoodAt()).getRelationTo(fme).getColor();
            color = ParticleColor.fromChatColor(chatColor);
        }

        boolean hasEffect = effect != null;
        boolean hasColor = color != null;
        
        int x = fme.getLastStoodAt().getX();
        int y = me.getLocation().getBlockY();
        int z = fme.getLastStoodAt().getZ();

        Location corner1 = new Location(me.getWorld(), x << 4, y, z << 4);
        Location corner2 = new Location(me.getWorld(), ((x << 4) | 15) + 1, y, z << 4);
        Location corner3 = new Location(me.getWorld(), x << 4, y, ((z << 4) | 15) + 1);
        Location corner4 = new Location(me.getWorld(), ((x << 4) | 15) + 1, y, ((z << 4) | 15) + 1);

        for (int height = 0; height < 16; height++) {
            corner1.add(0, height, 0);
            corner2.add(0, height, 0);
            corner3.add(0, height, 0);
            corner4.add(0, height, 0);
            if (!hasEffect) {
                Material mat = height % 2 == 0 ? FactionMaterial.from("REDSTONE_LAMP").get() : FactionMaterial.from("GLASS_PANE").get();
                VisualizeUtil.addLocations(me, mat, corner1, corner2, corner3, corner4);
                continue;
            }
            if (!hasColor) {
                FactionsPlugin.getInstance().getParticleProvider().playerSpawn(me, effect, 1, corner1, corner2, corner3, corner4);
                continue;
            }
            FactionsPlugin.getInstance().getParticleProvider().playerSpawn(me, effect, color, corner1, corner2, corner3, corner4);
        }
    }
}
