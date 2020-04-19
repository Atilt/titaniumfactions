package com.massivecraft.factions.util;

import com.massivecraft.factions.Board;
import com.massivecraft.factions.FLocation;
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
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

@SuppressWarnings("unchecked")
public class SeeChunkUtil extends BukkitRunnable {

    private ObjectSet<UUID> playersSeeingChunks = new ObjectOpenHashSet<>();
    private boolean useColor;
    private Object effect;

    public SeeChunkUtil() {
        String effectName = FactionsPlugin.getInstance().conf().commands().seeChunk().getParticleName();
        this.effect = FactionsPlugin.getInstance().getParticleProvider().effectFromString(effectName);
        this.useColor = FactionsPlugin.getInstance().conf().commands().seeChunk().isRelationalColor();

        FactionsPlugin.getInstance().getLogger().info(FactionsPlugin.getInstance().txt().parse("Using %s as the ParticleEffect for /f sc", FactionsPlugin.getInstance().getParticleProvider().effectName(effect)));
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
        }
    }

    public void updatePlayerInfo(UUID uuid, boolean toggle) {
        if (toggle) {
            playersSeeingChunks.add(uuid);
        } else {
            playersSeeingChunks.remove(uuid);
        }
    }

    public static void showPillars(Player me, FPlayer fme, Object effect, boolean useColor) {
        World world = me.getWorld();
        FLocation flocation = new FLocation(me);
        int chunkX = (int) flocation.getX();
        int chunkZ = (int) flocation.getZ();

        ParticleColor color = null;
        if (useColor) {
            ChatColor chatColor = Board.getInstance().getFactionAt(flocation).getRelationTo(fme).getColor();
            color = ParticleColor.fromChatColor(chatColor);
        }

        int blockX = chunkX << 4;
        int blockZ = chunkZ << 4;

        showPillar(me, world, blockX, blockZ, effect, color);

        showPillar(me, world, blockX + 16, blockZ, effect, color);

        showPillar(me, world, blockX, blockZ + 16, effect, color);

        showPillar(me, world, blockX + 16, blockZ + 16, effect, color);
    }

    public static void showPillar(Player player, World world, int blockX, int blockZ, Object effect, ParticleColor color) {
        // Lets start at the player's Y spot -30 to optimize
        for (int blockY = player.getLocation().getBlockY() - 30; blockY < player.getLocation().getBlockY() + 30; blockY++) {
            if (world.getBlockTypeIdAt(blockX, blockY, blockZ) == 0) {
                continue;
            }
            Location loc = new Location(world, blockX, blockY, blockZ);

            if (effect != null) {
                if (color == null) {
                    FactionsPlugin.getInstance().getParticleProvider().playerSpawn(player, effect, loc, 1);
                } else {
                    FactionsPlugin.getInstance().getParticleProvider().playerSpawn(player, effect, loc, color);
                }
            } else {
                Material mat = blockY % 5 == 0 ? FactionMaterial.from("REDSTONE_LAMP").get() : FactionMaterial.from("GLASS_PANE").get();
                VisualizeUtil.addLocation(player, loc, mat);
            }
        }
    }
}
