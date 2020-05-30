package com.massivecraft.factions.util;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.FactionsPlugin;
import com.massivecraft.factions.perms.Relation;
import com.massivecraft.factions.struct.Permission;
import me.lucko.helper.bucket.Bucket;
import me.lucko.helper.bucket.factory.BucketFactory;
import me.lucko.helper.bucket.partitioning.PartitioningStrategies;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public final class FlightTask {

    private static FlightTask instance;

    public static FlightTask get() {
        if (instance == null) {
            synchronized (FlightTask.class) {
                if (instance == null) {
                    instance = new FlightTask();
                }
            }
        }
        return instance;
    }

    private FlightTask() {}

    public void start() {
        double enemyCheck = FactionsPlugin.getInstance().conf().commands().fly().getRadiusCheck() * 20;
        if (enemyCheck > 0) {
            EnemiesTask.get().start();
        }

        double spawnRate = FactionsPlugin.getInstance().conf().commands().fly().particles().getSpawnRate() * 20;
        if (spawnRate > 0) {
            ParticleTrailsTask.get().start();
        }
    }

    public boolean track(FPlayer fPlayer) {
        return EnemiesTask.get().track(fPlayer) && ParticleTrailsTask.get().track(fPlayer);
    }

    public boolean untrack(FPlayer fPlayer) {
        return EnemiesTask.get().untrack(fPlayer) && ParticleTrailsTask.get().untrack(fPlayer);
    }

    public void wipe() {
        EnemiesTask.get().wipe();
        ParticleTrailsTask.get().wipe();
    }

    public boolean enemiesNearby(FPlayer target, int radius) {
        return !EnemiesTask.get().isClosed() && EnemiesTask.get().enemiesNearby(target, radius);
    }

    private static class EnemiesTask extends BukkitRunnable implements AutoCloseable {

        private static EnemiesTask instance;

        private int task = -1;

        private final Bucket<FPlayer> players = BucketFactory.newHashSetBucket(20, PartitioningStrategies.lowestSize());

        public static EnemiesTask get() {
            if (instance == null) {
                synchronized (EnemiesTask.class) {
                    if (instance == null) {
                        instance = new EnemiesTask();
                    }
                }
            }
            return instance;
        }

        public boolean track(FPlayer fPlayer) {
            return this.players.add(fPlayer);
        }

        public boolean untrack(FPlayer fPlayer) {
            return this.players.remove(fPlayer);
        }

        public void wipe() {
            this.players.clear();
        }

        public void start() {
            this.task = this.runTaskTimer(FactionsPlugin.getInstance(), 1L, 1L).getTaskId();
        }

        @Override
        public void close() {
            if (this.task != -1) {
                Bukkit.getScheduler().cancelTask(this.task);
                this.task = -1;
            }
        }

        public boolean isClosed() {
            return this.task == -1;
        }

        @Override
        public void run() {
            for (FPlayer pilot : this.players.asCycle().next()) {
                if (!pilot.isFlying() || pilot.isAdminBypassing()) {
                    continue;
                }
                if (enemiesNearby(pilot, FactionsPlugin.getInstance().conf().commands().fly().getEnemyRadius())) {
                    pilot.msg(TL.COMMAND_FLY_ENEMY_DISABLE);
                    pilot.setFlying(false);
                    if (pilot.isAutoFlying()) {
                        pilot.setAutoFlying(false);
                    }
                }
            }
        }

        public boolean enemiesNearby(FPlayer target, int radius) {
            if (!WorldUtil.isEnabled(target.getPlayer().getWorld())) {
                return false;
            }
            for (Entity entity : target.getPlayer().getNearbyEntities(radius, radius, radius)) {
                if (entity.getType() != EntityType.PLAYER) {
                    continue;
                }
                FPlayer playerNearby = FPlayers.getInstance().getByPlayer((Player) entity);
                if (playerNearby.isAdminBypassing() || playerNearby.isVanished()) {
                    continue;
                }
                if (playerNearby.getRelationTo(target) == Relation.ENEMY) {
                    return true;
                }
            }
            return false;
        }
    }

    private static class ParticleTrailsTask extends BukkitRunnable implements AutoCloseable {

        private static ParticleTrailsTask instance;

        private final int amount;
        private final float speed;

        private int task = -1;

        private final Bucket<FPlayer> players = BucketFactory.newHashSetBucket(20, PartitioningStrategies.lowestSize());

        public static ParticleTrailsTask get() {
            if (instance == null) {
                synchronized (ParticleTrailsTask.class) {
                    if (instance == null) {
                        instance = new ParticleTrailsTask();
                    }
                }
            }
            return instance;
        }

        private ParticleTrailsTask() {
            this.amount = FactionsPlugin.getInstance().conf().commands().fly().particles().getAmount();
            this.speed = (float) FactionsPlugin.getInstance().conf().commands().fly().particles().getSpeed();
        }

        public boolean track(FPlayer fPlayer) {
            return this.players.add(fPlayer);
        }

        public boolean untrack(FPlayer fPlayer) {
            return this.players.remove(fPlayer);
        }

        public void wipe() {
            this.players.clear();
        }

        public void start() {
            this.task = this.runTaskTimer(FactionsPlugin.getInstance(), 1L, 1L).getTaskId();
        }

        @Override
        public void close() {
            if (!this.isClosed()) {
                Bukkit.getScheduler().cancelTask(this.task);
                this.task = -1;
            }
        }

        public boolean isClosed() {
            return this.task == -1;
        }

        @Override
        public void run() {
            if (FPlayers.getInstance().onlineSize() == 1) {
                return;
            }
            for (FPlayer pilot : this.players.asCycle().next()) {
                if (!pilot.isFlying()) {
                    continue;
                }
                Player player = pilot.getPlayer();
                if (pilot.getFlyTrailsEffect() != null && Permission.FLY_TRAILS.has(player) && pilot.getFlyTrailsState()) {
                    FactionsPlugin.getInstance().getParticleProvider().spawn(FactionsPlugin.getInstance().getParticleProvider().effectFromString(pilot.getFlyTrailsEffect()), player.getLocation(), this.amount, this.speed, 0, 0, 0);
                }
            }
        }
    }
}