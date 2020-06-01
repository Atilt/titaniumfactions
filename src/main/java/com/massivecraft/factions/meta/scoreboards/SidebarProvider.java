package com.massivecraft.factions.meta.scoreboards;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.FactionsPlugin;
import com.massivecraft.factions.meta.scoreboards.sidebar.DefaultSidebar;
import me.lucko.helper.bucket.Bucket;
import me.lucko.helper.bucket.factory.BucketFactory;
import me.lucko.helper.bucket.partitioning.PartitioningStrategies;
import org.bukkit.Bukkit;

public final class SidebarProvider implements AutoCloseable {

    private static SidebarProvider instance;

    private int task = -1;
    private final Bucket<FPlayer> players = BucketFactory.newHashSetBucket(23, PartitioningStrategies.lowestSize());

    public static final DefaultSidebar DEFAULT_SIDEBAR = new DefaultSidebar();

    SidebarProvider() {}

    public static SidebarProvider get() {
        if (instance == null) {
            synchronized (SidebarProvider.class) {
                if (instance == null) {
                    instance = new SidebarProvider();
                }
            }
        }
        return instance;
    }

    public void start() {
        this.task = Bukkit.getScheduler().runTaskTimer(FactionsPlugin.getInstance(), () -> {
            if (!FactionsPlugin.getInstance().conf().scoreboard().constant().isEnabled()) {
                return;
            }

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

    public void trackAll() {
        boolean enabled = FactionsPlugin.getInstance().conf().scoreboard().info().isEnabled();
        for (FPlayer onlinePlayer : FPlayers.getInstance().getOnlinePlayers()) {
            if (onlinePlayer.showScoreboard()) {
                onlinePlayer.setShowScoreboard(false);
                if (enabled) {
                    onlinePlayer.setShowScoreboard(true);
                }
            }
        }
    }

    public boolean untrack(FPlayer fPlayer) {
        return this.players.remove(fPlayer);
    }

    @Override
    public void close() {
        if (this.task != -1) {
            Bukkit.getScheduler().cancelTask(this.task);
            for (FPlayer fPlayer : this.players) {
                if (fPlayer.getScoreboard() == null) {
                    continue;
                }
                fPlayer.getScoreboard().delete();
            }
        }
    }
}
