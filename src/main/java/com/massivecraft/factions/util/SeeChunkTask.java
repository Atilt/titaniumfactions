package com.massivecraft.factions.util;

import com.massivecraft.factions.Board;
import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FactionsPlugin;
import com.massivecraft.factions.util.material.FactionMaterial;
import com.massivecraft.factions.util.particle.ParticleColor;
import me.lucko.helper.bucket.Bucket;
import me.lucko.helper.bucket.factory.BucketFactory;
import me.lucko.helper.bucket.partitioning.PartitioningStrategies;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public final class SeeChunkTask extends BukkitRunnable implements AutoCloseable {

    private static SeeChunkTask instance;

    private final Bucket<FPlayer> players = BucketFactory.newHashSetBucket(18, PartitioningStrategies.lowestSize());

    private boolean useColor;
    private Object effect;

    private int task = -1;

    public static SeeChunkTask get() {
        if (instance == null) {
            synchronized (SeeChunkTask.class) {
                if (instance == null) {
                    instance = new SeeChunkTask();
                }
            }
        }
        return instance;
    }

    private SeeChunkTask() {}

    public boolean isStarted() {
        return this.task != -1;
    }

    public void start() {
        this.effect = FactionsPlugin.getInstance().getParticleProvider().effectFromString(FactionsPlugin.getInstance().conf().commands().seeChunk().getParticleName());
        this.useColor = FactionsPlugin.getInstance().conf().commands().seeChunk().isRelationalColor();
        this.task = this.runTaskTimer(FactionsPlugin.getInstance(), 1L, 1L).getTaskId();
    }

    public boolean track(FPlayer fPlayer) {
        return this.players.add(fPlayer);
    }

    public boolean untrack(FPlayer fPlayer, boolean deep) {
        boolean removed = this.players.remove(fPlayer);
        if (removed) {
            VisualizeUtil.clear(fPlayer.getPlayer(), deep);
        }
        return removed;
    }

    @Override
    public void run() {
        for (FPlayer fPlayer : this.players.asCycle().next()) {
            if (!fPlayer.isSeeingChunk()) {
                continue;
            }
            showPillars(Bukkit.getPlayer(fPlayer.getId()), fPlayer, this.effect, useColor);
        }
    }

    @Override
    public void close() {
        if (this.task != -1) {
            Bukkit.getScheduler().cancelTask(this.task);
        }
    }

    public void showPillars(Player me, FPlayer fme, Object effect, boolean useColor) {
        boolean hasEffect = effect != null;
        
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
            if (!useColor) {
                FactionsPlugin.getInstance().getParticleProvider().playerSpawn(me, effect, 1, corner1, corner2, corner3, corner4);
                continue;
            }
            FactionsPlugin.getInstance().getParticleProvider().playerSpawn(me, effect, ParticleColor.fromChatColor(Board.getInstance().getFactionAt(fme.getLastStoodAt()).getRelationTo(fme).getColor()), corner1, corner2, corner3, corner4);
        }
    }
}
