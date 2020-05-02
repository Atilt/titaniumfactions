package com.massivecraft.factions.util;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.FactionsPlugin;
import com.massivecraft.factions.perms.Relation;
import com.massivecraft.factions.struct.Permission;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class FlightUtil {

    private static FlightUtil instance;

    private EnemiesTask enemiesTask;

    private FlightUtil() {
        double enemyCheck = FactionsPlugin.getInstance().conf().commands().fly().getRadiusCheck() * 20;
        if (enemyCheck > 0) {
            enemiesTask = new EnemiesTask();
            enemiesTask.runTaskTimer(FactionsPlugin.getInstance(), 0, (long) enemyCheck);
        }

        double spawnRate = FactionsPlugin.getInstance().conf().commands().fly().particles().getSpawnRate() * 20;
        if (spawnRate > 0) {
            new ParticleTrailsTask().runTaskTimer(FactionsPlugin.getInstance(), 0, (long) spawnRate);
        }
    }

    public static FlightUtil getInstance() {
        if (instance == null) {
            synchronized (FlightUtil.class) {
                if (instance == null) {
                    instance = new FlightUtil();
                }
            }
        }
        return instance;
    }

    public boolean enemiesNearby(FPlayer target, int radius) {
        if (this.enemiesTask == null) {
            return false;
        } else {
            return this.enemiesTask.enemiesNearby(target, radius);
        }
    }

    public static class EnemiesTask extends BukkitRunnable {

        @Override
        public void run() {
            for (FPlayer pilot : FPlayers.getInstance()) {
                if (pilot.isFlying() && !pilot.isAdminBypassing()) {
                    if (enemiesNearby(pilot, FactionsPlugin.getInstance().conf().commands().fly().getEnemyRadius())) {
                        pilot.msg(TL.COMMAND_FLY_ENEMY_DISABLE);
                        pilot.setFlying(false);
                        if (pilot.isAutoFlying()) {
                            pilot.setAutoFlying(false);
                        }
                    }
                }
            }
        }

        public boolean enemiesNearby(FPlayer target, int radius) {
            if (!FactionsPlugin.getInstance().worldUtil().isEnabled(target.getPlayer().getWorld())) {
                return false;
            }
            List<Entity> nearbyEntities = target.getPlayer().getNearbyEntities(radius, radius, radius);
            for (Entity entity : nearbyEntities) {
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

    public static class ParticleTrailsTask extends BukkitRunnable {

        private final int amount;
        private final float speed;

        private ParticleTrailsTask() {
            this.amount = FactionsPlugin.getInstance().conf().commands().fly().particles().getAmount();
            this.speed = (float) FactionsPlugin.getInstance().conf().commands().fly().particles().getSpeed();
        }

        @Override
        public void run() {
            for (FPlayer pilot : FPlayers.getInstance()) {
                if (!pilot.isFlying()) {
                    continue;
                }
                Player player = Bukkit.getPlayer(pilot.getId());
                if (pilot.getFlyTrailsEffect() != null && Permission.FLY_TRAILS.has(player) && pilot.getFlyTrailsState()) {
                    FactionsPlugin.getInstance().getParticleProvider().spawn(FactionsPlugin.getInstance().getParticleProvider().effectFromString(pilot.getFlyTrailsEffect()), player.getLocation(), amount, speed, 0, 0, 0);
                }
            }
        }
    }
}
