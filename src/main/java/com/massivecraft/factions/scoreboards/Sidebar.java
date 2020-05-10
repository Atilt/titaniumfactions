package com.massivecraft.factions.scoreboards;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FactionsPlugin;
import com.massivecraft.factions.scoreboards.sidebar.DefaultSidebar;
import me.lucko.helper.bucket.Bucket;
import me.lucko.helper.bucket.factory.BucketFactory;
import me.lucko.helper.bucket.partitioning.PartitioningStrategies;
import org.bukkit.Bukkit;

public class Sidebar implements AutoCloseable {

    private static Sidebar instance;

    private int task = -1;
    private final Bucket<FPlayer> players = BucketFactory.newHashSetBucket(23, PartitioningStrategies.lowestSize());

    public static final DefaultSidebar DEFAULT_SIDEBAR = new DefaultSidebar();

    Sidebar() {}

    public static Sidebar get() {
        if (instance == null) {
            synchronized (Sidebar.class) {
                if (instance == null) {
                    instance = new Sidebar();
                }
            }
        }
        return instance;
    }

    public void start() {
        this.task = Bukkit.getScheduler().runTaskTimer(FactionsPlugin.getInstance(), () -> {
            for (FPlayer fPlayer : players.asCycle().next()) {
                FastBoard fastBoard = fPlayer.getScoreboard();
                if (fastBoard == null) {
                    continue;
                }
                fastBoard.updateTitle(fPlayer);
                fastBoard.updateLines(fPlayer);
            }
        }, 1L, 1L).getTaskId();
    }

    public boolean track(FPlayer fPlayer) {
        return this.players.add(fPlayer);
    }

    public boolean untrack(FPlayer fPlayer) {
        return this.players.remove(fPlayer);
    }

    @Override
    public void close() {
        if (this.task != -1) {
            Bukkit.getScheduler().cancelTask(this.task);
        }
    }
}