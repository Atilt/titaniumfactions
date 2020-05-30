package com.massivecraft.factions.util;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.FactionsPlugin;
import com.massivecraft.factions.perms.Role;
import me.lucko.helper.bucket.Bucket;
import me.lucko.helper.bucket.factory.BucketFactory;
import me.lucko.helper.bucket.partitioning.PartitioningStrategies;
import org.bukkit.Bukkit;

import java.time.Instant;
import java.util.Iterator;

public final class AutoLeaveTask implements Runnable, AutoCloseable {

    private int id = -1;

    private InactivityScan inactivityScan;

    @Override
    public void run() {
        if (this.inactivityScan != null && !this.inactivityScan.isClosed()) {
            return;
        }
        this.inactivityScan = new InactivityScan();
        this.inactivityScan.start();
    }

    public void start() {
        long interval = (long) (20 * 60 * FactionsPlugin.getInstance().conf().factions().other().getAutoLeaveRoutineRunsEveryXMinutes());
        this.id = Bukkit.getScheduler().runTaskTimer(FactionsPlugin.getInstance(), this, interval, interval).getTaskId();
    }

    @Override
    public void close() {
        if (this.id != -1) {
            Bukkit.getScheduler().cancelTask(this.id);
        }
        if (this.inactivityScan != null) {
            this.inactivityScan.close();
        }
    }

    static class InactivityScan implements Runnable, AutoCloseable {

        private int id = -1;

        private boolean ready;
        private boolean closed;
        private final Bucket<FPlayer> players = BucketFactory.newHashSetBucket(20, PartitioningStrategies.lowestSize());

        private final long toleranceMillis = (long) FactionsPlugin.getInstance().conf().factions().other().getAutoLeaveAfterDaysOfInactivity() * 24 * 60 * 60 * 1000;
        private int runs = 0;

        @Override
        public void run() {
            if (FactionsPlugin.getInstance().conf().factions().other().getAutoLeaveAfterDaysOfInactivity() <= 0.0 || FactionsPlugin.getInstance().conf().factions().other().getAutoLeaveRoutineMaxMillisecondsPerTick() <= 0.0) {
                this.close();
                return;
            }

            if (!this.ready) {
                return;
            }
            // this is set so it only does one iteration at a time, no matter how frequently the timer fires
            this.ready = false;
            // and this is tracked to keep one iteration from dragging on too long and possibly choking the system if there are a very large number of players to go through
            long loopStartTime = Instant.now().toEpochMilli();

            //a partition
            Iterator<FPlayer> selected = this.players.asCycle().next().iterator();

            while (selected.hasNext()) {
                FPlayer fplayer = selected.next();
                if (!fplayer.hasFaction()) {
                    continue;
                }
                long now = Instant.now().toEpochMilli();

                // if this iteration has been running for maximum time, stop to take a breather until next tick
                if (now > loopStartTime + FactionsPlugin.getInstance().conf().factions().other().getAutoLeaveRoutineMaxMillisecondsPerTick()) {
                    this.ready = true;
                    return;
                }
                this.runs++;

                // Check if they should be exempt from this.
                if (!fplayer.willAutoLeave()) {
                    continue;
                }

                if (fplayer.isOffline() && now - fplayer.getLastLoginTime() > toleranceMillis) {
                    if (FactionsPlugin.getInstance().conf().logging().isFactionLeave() || FactionsPlugin.getInstance().conf().logging().isFactionKick()) {
                        FactionsPlugin.getInstance().getPluginLogger().info("== Players: " + fplayer.getName() + " was auto-removed due to inactivity.");
                    }

                    // if player is faction admin, sort out the faction since he's going away
                    if (fplayer.getRole() == Role.ADMIN) {
                        Faction faction = fplayer.getFaction();
                        if (faction != null) {
                            faction.promoteNewLeader();
                        }
                    }

                    fplayer.leave(false);
                    selected.remove();  // go ahead and remove this list's link to the FPlayer object
                    if (FactionsPlugin.getInstance().conf().factions().other().isAutoLeaveDeleteFPlayerData()) {
                        fplayer.remove();
                    }
                }
            }
            if (this.runs >= this.players.getPartitionCount()) {
                this.close();
            }
        }

        public void start() {
            this.players.addAll(FPlayers.getInstance().getAllFPlayers());
            this.id = Bukkit.getScheduler().runTaskTimer(FactionsPlugin.getInstance(), this, 1L, 1L).getTaskId();
        }

        public boolean isClosed() {
            return this.closed;
        }

        @Override
        public void close() {
            if (!this.isClosed() && this.id != -1) {
                this.ready = false;
                this.closed = true;
                Bukkit.getScheduler().cancelTask(this.id);
                this.runs = 0;
            }
        }
    }
}
