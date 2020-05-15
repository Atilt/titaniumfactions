package com.massivecraft.factions.listeners;

import com.massivecraft.factions.Board;
import com.massivecraft.factions.FLocation;
import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.FactionsPlugin;
import com.massivecraft.factions.config.file.MainConfig;
import com.massivecraft.factions.perms.PermissibleAction;
import com.massivecraft.factions.perms.Relation;
import com.massivecraft.factions.util.MiscUtil;
import com.massivecraft.factions.util.TL;
import com.massivecraft.factions.util.WorldUtil;
import com.massivecraft.factions.util.material.MaterialDb;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.entity.Wither;
import org.bukkit.entity.minecart.ExplosiveMinecart;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityCombustByEntityEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.hanging.HangingBreakEvent;
import org.bukkit.event.hanging.HangingBreakEvent.RemoveCause;
import org.bukkit.event.hanging.HangingPlaceEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.projectiles.ProjectileSource;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;


public class FactionsEntityListener extends AbstractListener {
    
    @EventHandler(priority = EventPriority.NORMAL)
    public void onEntityDeath(EntityDeathEvent event) {
        if (!WorldUtil.isEnabled(event.getEntity().getWorld())) {
            return;
        }

        Entity entity = event.getEntity();
        if (entity.getType() == EntityType.PLAYER) {
            FactionsPlugin.getInstance().getLandRaidControl().onDeath((Player) entity);
        }
    }

    /**
     * Who can I hurt? I can never hurt members or allies. I can always hurt enemies. I can hurt neutrals as long as
     * they are outside their own territory.
     */
    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onEntityDamage(EntityDamageEvent event) {
        if (!WorldUtil.isEnabled(event.getEntity().getWorld())) {
            return;
        }

        if (event instanceof EntityDamageByEntityEvent) {
            EntityDamageByEntityEvent sub = (EntityDamageByEntityEvent) event;
            if (!this.canDamagerHurtDamagee(sub, true)) {
                event.setCancelled(true);
                return;
            }
            // event is not cancelled by factions

            Entity damagee = sub.getEntity();
            Entity damager = sub.getDamager();

            if (damagee.getType() == EntityType.PLAYER) {
                cancelFStuckTeleport((Player) damagee);
                cancelFFly((Player) damagee);
            }
            if (damager.getType() == EntityType.PLAYER) {
                cancelFStuckTeleport((Player) damager);
                cancelFFly((Player) damager);
            }
        } else if (FactionsPlugin.getInstance().conf().factions().protection().isSafeZonePreventAllDamageToPlayers() && isPlayerInSafeZone(event.getEntity())) {
            // Players can not take any damage in a Safe Zone
            event.setCancelled(true);
        } else if (event.getCause() == EntityDamageEvent.DamageCause.FALL && event.getEntity().getType() == EntityType.PLAYER) {
            Player player = (Player) event.getEntity();
            FPlayer fPlayer = FPlayers.getInstance().getByPlayer(player);
            if (fPlayer != null && !fPlayer.shouldTakeFallDamage()) {
                event.setCancelled(true); // Falling after /f fly
            }
        }

        // entity took generic damage?
        Entity entity = event.getEntity();
        if (entity.getType() == EntityType.PLAYER) {
            Player player = (Player) entity;
            FPlayer me = FPlayers.getInstance().getByPlayer(player);
            cancelFStuckTeleport(player);
            if (FactionsPlugin.getInstance().conf().commands().fly().isDisableOnGenericDamage()) {
                cancelFFly(player);
            }
            if (me.isWarmingUp()) {
                me.clearWarmup();
                me.msg(TL.WARMUPS_CANCELLED);
            }
        }
    }

    private void cancelFFly(Player player) {
        if (player == null) {
            return;
        }

        FPlayer fPlayer = FPlayers.getInstance().getByPlayer(player);
        if (fPlayer.isFlying()) {
            fPlayer.setFlying(false, true);
            if (fPlayer.isAutoFlying()) {
                fPlayer.setAutoFlying(false);
            }
        }
    }

    public void cancelFStuckTeleport(Player player) {
        if (player == null) {
            return;
        }
        if (FactionsPlugin.getInstance().getStuckMap().remove(player.getUniqueId()) != null) {
            FPlayers.getInstance().getByPlayer(player).msg(TL.COMMAND_STUCK_CANCELLED);
        }
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onEntityExplode(EntityExplodeEvent event) {
        this.handleExplosion(event.getLocation(), event.getEntity(), event, event.blockList());
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onBlockExplode(BlockExplodeEvent event) {
        this.handleExplosion(event.getBlock().getLocation(), null, event, event.blockList());
    }
    
    public static final BlockFace[] FACES = new BlockFace[]{
            BlockFace.NORTH,
            BlockFace.SOUTH,
            BlockFace.EAST,
            BlockFace.WEST,
            BlockFace.UP,
            BlockFace.DOWN
    };

    private void handleExplosion(Location loc, Entity boomer, Cancellable event, List<Block> blockList) {
        if (!WorldUtil.isEnabled(loc.getWorld())) {
            return;
        }

        if (explosionDisallowed(boomer, loc)) {
            event.setCancelled(true);
            return;
        }

        blockList.removeIf(block -> explosionDisallowed(boomer, block.getLocation()));

        if ((boomer.getType() == EntityType.PRIMED_TNT|| boomer.getType() == EntityType.MINECART_TNT) && FactionsPlugin.getInstance().conf().exploits().isTntWaterlog()) {
            // TNT in water/lava doesn't normally destroy any surrounding blocks, which is usually desired behavior, but...
            // this change below provides workaround for waterwalling providing perfect protection,
            // and makes cheap (non-obsidian) TNT cannons require minor maintenance between shots
            Block center = loc.getBlock();
            if (center.isLiquid()) {
                // a single surrounding block in all 6 directions is broken if the material is weak enough
                for (BlockFace target : FACES) {
                    Block relative = center.getRelative(target);
                    if (explosionDisallowed(boomer, relative.getLocation()) || MaterialDb.get().getProvider().isExplosiveResistant(relative.getType())) {
                        continue;
                    }
                    relative.breakNaturally();
                }
            }
        }
    }

    private boolean explosionDisallowed(Entity boomer, Location location) {
        Faction faction = Board.getInstance().getFactionAt(FLocation.wrap(location));
        boolean online = faction.hasPlayersOnline();
        if (faction.noExplosionsInTerritory() || (faction.isPeaceful() && FactionsPlugin.getInstance().conf().factions().specialCase().isPeacefulTerritoryDisableBoom())) {
            // faction is peaceful and has explosions set to disabled
            return true;
        }
        MainConfig.Factions.Protection protection = FactionsPlugin.getInstance().conf().factions().protection();
        if (boomer instanceof Creeper && ((faction.isWilderness() && protection.isWildernessBlockCreepers() && !protection.getWorldsNoWildernessProtection().contains(location.getWorld().getName())) ||
                (faction.isNormal() && (online ? protection.isTerritoryBlockCreepers() : protection.isTerritoryBlockCreepersWhenOffline())) ||
                (faction.isWarZone() && protection.isWarZoneBlockCreepers()) ||
                faction.isSafeZone())) {
            // creeper which needs prevention
            return true;
        } else if (
                (boomer instanceof Fireball || boomer instanceof Wither) && (faction.isWilderness() && protection.isWildernessBlockFireballs() && !protection.getWorldsNoWildernessProtection().contains(location.getWorld().getName()) || faction.isNormal() && (online ? protection.isTerritoryBlockFireballs() : protection.isTerritoryBlockFireballsWhenOffline()) || faction.isWarZone() && protection.isWarZoneBlockFireballs() || faction.isSafeZone())) {
            // ghast fireball which needs prevention
            // it's a bit crude just using fireball protection for Wither boss too, but I'd rather not add in a whole new set of xxxBlockWitherExplosion or whatever
            return true;
        } else if ((boomer instanceof TNTPrimed || boomer instanceof ExplosiveMinecart) && ((faction.isWilderness() && protection.isWildernessBlockTNT() && !protection.getWorldsNoWildernessProtection().contains(location.getWorld().getName())) ||
                (faction.isNormal() && (online ? protection.isTerritoryBlockTNT() : protection.isTerritoryBlockTNTWhenOffline())) ||
                (faction.isWarZone() && protection.isWarZoneBlockTNT()) ||
                (faction.isSafeZone() && protection.isSafeZoneBlockTNT()))) {
            // TNT which needs prevention
            return true;
        } else return ((faction.isWilderness() && protection.isWildernessBlockOtherExplosions() && !protection.getWorldsNoWildernessProtection().contains(location.getWorld().getName())) ||
                (faction.isNormal() && (online ? protection.isTerritoryBlockOtherExplosions() : protection.isTerritoryBlockOtherExplosionsWhenOffline())) ||
                (faction.isWarZone() && protection.isWarZoneBlockOtherExplosions()) ||
                (faction.isSafeZone() && protection.isSafeZoneBlockOtherExplosions()));
    }

    // mainly for flaming arrows; don't want allies or people in safe zones to be ignited even after damage event is cancelled
    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onEntityCombustByEntity(EntityCombustByEntityEvent event) {
        if (!WorldUtil.isEnabled(event.getEntity().getWorld())) {
            return;
        }

        EntityDamageByEntityEvent sub = new EntityDamageByEntityEvent(event.getCombuster(), event.getEntity(), EntityDamageEvent.DamageCause.FIRE, 0d);
        if (!this.canDamagerHurtDamagee(sub, false)) {
            event.setCancelled(true);
        }
    }

    private static final ObjectSet<PotionEffectType> NEGATIVE_POTION_EFFECTS = new ObjectOpenHashSet<>(Arrays.asList(PotionEffectType.BLINDNESS, PotionEffectType.CONFUSION, PotionEffectType.HARM, PotionEffectType.HUNGER, PotionEffectType.POISON, PotionEffectType.SLOW, PotionEffectType.SLOW_DIGGING, PotionEffectType.WEAKNESS, PotionEffectType.WITHER));

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onPotionSplashEvent(PotionSplashEvent event) {
        if (!WorldUtil.isEnabled(event.getEntity().getWorld())) {
            return;
        }

        // see if the potion has a harmful effect
        boolean badjuju = false;
        for (PotionEffect effect : event.getPotion().getEffects()) {
            if (NEGATIVE_POTION_EFFECTS.contains(effect.getType())) {
                badjuju = true;
                break;
            }
        }
        if (!badjuju) {
            return;
        }

        ProjectileSource thrower = event.getPotion().getShooter();
        if (!(thrower instanceof Entity)) {
            return;
        }

        if (((Entity) thrower).getType() == EntityType.PLAYER) {
            FPlayer fPlayer = FPlayers.getInstance().getByPlayer((Player) thrower);
            if (fPlayer.getFaction().isPeaceful()) {
                event.setCancelled(true);
                return;
            }
        }

        // scan through affected entities to make sure they're all valid targets
        for (LivingEntity target : event.getAffectedEntities()) {
            EntityDamageByEntityEvent sub = new EntityDamageByEntityEvent((Entity) thrower, target, EntityDamageEvent.DamageCause.CUSTOM, 0);
            if (!this.canDamagerHurtDamagee(sub, true)) {
                event.setIntensity(target, 0.0);  // affected entity list doesn't accept modification (so no iter.remove()), but this works
            }
        }
    }

    public boolean isPlayerInSafeZone(Entity damagee) {
        return damagee.getType() == EntityType.PLAYER && Board.getInstance().getFactionAt(FLocation.wrap(damagee.getLocation())).isSafeZone();
    }

    public boolean canDamagerHurtDamagee(EntityDamageByEntityEvent sub) {
        return canDamagerHurtDamagee(sub, true);
    }

    public boolean canDamagerHurtDamagee(EntityDamageByEntityEvent sub, boolean notify) {
        Entity damager = sub.getDamager();
        Entity damagee = sub.getEntity();

        // for damage caused by projectiles, getDamager() returns the projectile... what we need to know is the source
        if (damager instanceof Projectile) {
            Projectile projectile = (Projectile) damager;

            if (!(projectile.getShooter() instanceof Entity)) {
                return true;
            }

            damager = (Entity) projectile.getShooter();
        }

        if (damager.getType() == EntityType.PLAYER) {
            Player player = (Player) damager;
            Material material = null;
            switch (sub.getEntity().getType()) {
                case ITEM_FRAME:
                    material = Material.ITEM_FRAME;
                    break;
                case ARMOR_STAND:
                    material = Material.ARMOR_STAND;
                    break;
            }
            if (material != null && !canPlayerUseBlock(player, material, damagee.getLocation(), false)) {
                return false;
            }
        }

        if (damagee.getType() != EntityType.PLAYER) {
            return true;
        }

        FPlayer defender = FPlayers.getInstance().getByPlayer((Player) damagee);

        if (defender == null || defender.getPlayer() == null) {
            return true;
        }

        if (damager == damagee) {  // ender pearl usage and other self-inflicted damage
            return true;
        }

        FLocation defenderLoc = FLocation.wrap(defender.getPlayer().getLocation());
        Faction defLocFaction = Board.getInstance().getFactionAt(defenderLoc);

        // Players can not take attack damage in a SafeZone, or possibly peaceful territory
        if (defLocFaction.noPvPInTerritory()) {
            if (damager.getType() == EntityType.PLAYER) {
                if (notify) {
                    FPlayer attacker = FPlayers.getInstance().getByPlayer((Player) damager);
                    attacker.msg(TL.PLAYER_CANTHURT, (defLocFaction.isSafeZone() ? TL.REGION_SAFEZONE.toString() : TL.REGION_PEACEFUL.toString()));
                }
                return false;
            }
            return !defLocFaction.noMonstersInTerritory();
        }

        if (damager.getType() != EntityType.PLAYER) {
            return true;
        }

        FPlayer attacker = FPlayers.getInstance().getByPlayer((Player) damager);

        if (attacker == null || attacker.getPlayer() == null) {
            return true;
        }

        if (FactionsPlugin.getInstance().conf().factions().protection().getPlayersWhoBypassAllProtection().contains(attacker.getName())) {
            return true;
        }

        if (attacker.hasLoginPvpDisabled()) {
            if (notify) {
                attacker.msg(TL.PLAYER_PVP_LOGIN, FactionsPlugin.getInstance().conf().factions().pvp().getNoPVPDamageToOthersForXSecondsAfterLogin());
            }
            return false;
        }

        Faction locFaction = Board.getInstance().getFactionAt(FLocation.wrap(attacker));

        // so we know from above that the defender isn't in a safezone... what about the attacker, sneaky dog that he might be?
        if (locFaction.noPvPInTerritory()) {
            if (notify) {
                attacker.msg(TL.PLAYER_CANTHURT, (locFaction.isSafeZone() ? TL.REGION_SAFEZONE.toString() : TL.REGION_PEACEFUL.toString()));
            }
            return false;
        }

        if (locFaction.isWarZone() && FactionsPlugin.getInstance().conf().factions().protection().isWarZoneFriendlyFire()) {
            return true;
        }

        if (FactionsPlugin.getInstance().conf().factions().pvp().getWorldsIgnorePvP().contains(defenderLoc.getWorld().getName())) {
            return true;
        }

        Faction defendFaction = defender.getFaction();
        Faction attackFaction = attacker.getFaction();

        if (attackFaction.isWilderness() && FactionsPlugin.getInstance().conf().factions().pvp().isDisablePVPForFactionlessPlayers()) {
            if (notify) {
                attacker.msg(TL.PLAYER_PVP_REQUIREFACTION);
            }
            return false;
        } else if (defendFaction.isWilderness()) {
            if (defLocFaction == attackFaction && FactionsPlugin.getInstance().conf().factions().pvp().isEnablePVPAgainstFactionlessInAttackersLand()) {
                // Allow PVP vs. Factionless in attacker's faction territory
                return true;
            } else if (FactionsPlugin.getInstance().conf().factions().pvp().isDisablePVPForFactionlessPlayers()) {
                if (notify) {
                    attacker.msg(TL.PLAYER_PVP_FACTIONLESS);
                }
                return false;
            }
        }

        if (!defLocFaction.isWarZone() || FactionsPlugin.getInstance().conf().factions().pvp().isDisablePeacefulPVPInWarzone()) {
            if (defendFaction.isPeaceful()) {
                if (notify) {
                    attacker.msg(TL.PLAYER_PVP_PEACEFUL);
                }
                return false;
            } else if (attackFaction.isPeaceful()) {
                if (notify) {
                    attacker.msg(TL.PLAYER_PVP_PEACEFUL);
                }
                return false;
            }
        }

        Relation relation = defendFaction.getRelationTo(attackFaction);

        // You can not hurt neutral factions
        if (FactionsPlugin.getInstance().conf().factions().pvp().isDisablePVPBetweenNeutralFactions() && relation.isNeutral()) {
            if (notify) {
                attacker.msg(TL.PLAYER_PVP_NEUTRAL);
            }
            return false;
        }

        // Players without faction may be hurt anywhere
        if (!defender.hasFaction()) {
            return true;
        }

        // You can never hurt faction members or allies
        if (relation.isMember() || relation.isAlly() || relation.isTruce()) {
            if (notify) {
                attacker.msg(TL.PLAYER_PVP_CANTHURT, defender.describeTo(attacker));
            }
            return false;
        }

        boolean ownTerritory = defender.isInOwnTerritory();

        // You can not hurt neutrals in their own territory.
        if (ownTerritory && relation.isNeutral()) {
            if (notify) {
                attacker.msg(TL.PLAYER_PVP_NEUTRALFAIL, defender.describeTo(attacker));
                defender.msg(TL.PLAYER_PVP_TRIED, attacker.describeTo(defender, true));
            }
            return false;
        }

        // Damage will be dealt. However check if the damage should be reduced.
        /*
        if (damage > 0.0 && ownTerritory && Conf.territoryShieldFactor > 0) {
            double newDamage = Math.ceil(damage * (1D - Conf.territoryShieldFactor));
            sub.setDamage(newDamage);

            // Send message
            if (notify) {
                String perc = MessageFormat.format("{0,number,#%}", (Conf.territoryShieldFactor)); // TODO does this display correctly??
                defender.msg("<i>Enemy damage reduced by <rose>%s<i>.", perc);
            }
        } */

        return true;
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        if (!WorldUtil.isEnabled(event.getEntity().getWorld())) {
            return;
        }
        if (FactionsPlugin.getInstance().getSafeZoneNerfedCreatureTypes().contains(event.getEntityType()) && Board.getInstance().getFactionAt(FLocation.wrap(event.getLocation())).noMonstersInTerritory()){
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onEntityTarget(EntityTargetEvent event) {
        if (!WorldUtil.isEnabled(event.getEntity().getWorld())) {
            return;
        }

        // if there is a target
        Entity target = event.getTarget();
        if (target == null) {
            return;
        }

        // We are interested in blocking targeting for certain mobs":
        if (!FactionsPlugin.getInstance().getSafeZoneNerfedCreatureTypes().contains(MiscUtil.creatureTypeFromEntity(event.getEntity()))) {
            return;
        }

        // in case "the target is in a safe zone.
        if (Board.getInstance().getFactionAt(FLocation.wrap(target.getLocation())).noMonstersInTerritory()) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onPaintingBreak(HangingBreakEvent event) {
        if (!WorldUtil.isEnabled(event.getEntity().getWorld())) {
            return;
        }

        if (event.getCause() == RemoveCause.EXPLOSION) {
            Location loc = event.getEntity().getLocation();
            Faction faction = Board.getInstance().getFactionAt(FLocation.wrap(loc));
            if (faction.noExplosionsInTerritory()) {
                // faction is peaceful and has explosions set to disabled
                event.setCancelled(true);
                return;
            }

            boolean online = faction.hasPlayersOnline();

            if ((faction.isWilderness() && !FactionsPlugin.getInstance().conf().factions().protection().getWorldsNoWildernessProtection().contains(loc.getWorld().getName()) && (FactionsPlugin.getInstance().conf().factions().protection().isWildernessBlockCreepers() || FactionsPlugin.getInstance().conf().factions().protection().isWildernessBlockFireballs() || FactionsPlugin.getInstance().conf().factions().protection().isWildernessBlockTNT())) ||
                    (faction.isNormal() && (online ? (FactionsPlugin.getInstance().conf().factions().protection().isTerritoryBlockCreepers() || FactionsPlugin.getInstance().conf().factions().protection().isTerritoryBlockFireballs() || FactionsPlugin.getInstance().conf().factions().protection().isTerritoryBlockTNT()) : (FactionsPlugin.getInstance().conf().factions().protection().isTerritoryBlockCreepersWhenOffline() || FactionsPlugin.getInstance().conf().factions().protection().isTerritoryBlockFireballsWhenOffline() || FactionsPlugin.getInstance().conf().factions().protection().isTerritoryBlockTNTWhenOffline()))) ||
                    (faction.isWarZone() && (FactionsPlugin.getInstance().conf().factions().protection().isWarZoneBlockCreepers() || FactionsPlugin.getInstance().conf().factions().protection().isWarZoneBlockFireballs() || FactionsPlugin.getInstance().conf().factions().protection().isWarZoneBlockTNT())) ||
                    faction.isSafeZone()) {
                // explosion which needs prevention
                event.setCancelled(true);
            }
        }

        if (!(event instanceof HangingBreakByEntityEvent)) {
            return;
        }

        Entity breaker = ((HangingBreakByEntityEvent) event).getRemover();
        if (breaker instanceof Projectile) {
            ProjectileSource source = ((Projectile) breaker).getShooter();
            if (!(source instanceof Player)) {
                return;
            }
            breaker = (Player) source;
        }
        if (breaker != null && breaker.getType() == EntityType.PLAYER && !FactionsBlockListener.playerCanBuildDestroyBlock((Player) breaker, event.getEntity().getLocation(), PermissibleAction.DESTROY, false)) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onPaintingPlace(HangingPlaceEvent event) {
        if (event.getPlayer() == null || !WorldUtil.isEnabled(event.getPlayer().getWorld())) {
            return;
        }

        if (!FactionsBlockListener.playerCanBuildDestroyBlock(event.getPlayer(), event.getBlock().getRelative(event.getBlockFace()).getLocation(), PermissibleAction.BUILD, false)) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onEntityChangeBlock(EntityChangeBlockEvent event) {
        if (!WorldUtil.isEnabled(event.getEntity().getWorld())) {
            return;
        }

        Entity entity = event.getEntity();

        // for now, only interested in Enderman and Wither boss tomfoolery
        if (entity.getType() == EntityType.ENDERMAN) {
            if (stopEndermanBlockManipulation(event.getBlock().getLocation())) {
                event.setCancelled(true);
            }
        } else if (entity.getType() == EntityType.WITHER) {
            Faction faction = Board.getInstance().getFactionAt(FLocation.wrap(event.getBlock().getLocation()));
            // it's a bit crude just using fireball protection, but I'd rather not add in a whole new set of xxxBlockWitherExplosion or whatever
            if ((faction.isWilderness() && FactionsPlugin.getInstance().conf().factions().protection().isWildernessBlockFireballs() && !FactionsPlugin.getInstance().conf().factions().protection().getWorldsNoWildernessProtection().contains(event.getBlock().getWorld().getName())) ||
                    (faction.isNormal() && (faction.hasPlayersOnline() ? FactionsPlugin.getInstance().conf().factions().protection().isTerritoryBlockFireballs() : FactionsPlugin.getInstance().conf().factions().protection().isTerritoryBlockFireballsWhenOffline())) ||
                    (faction.isWarZone() && FactionsPlugin.getInstance().conf().factions().protection().isWarZoneBlockFireballs()) ||
                    faction.isSafeZone()) {
                event.setCancelled(true);
            }
        }
    }

    private boolean stopEndermanBlockManipulation(Location loc) {
        if (loc == null) {
            return false;
        }
        // quick check to see if all Enderman deny options are enabled; if so, no need to check location
        if (FactionsPlugin.getInstance().conf().factions().protection().isWildernessDenyEndermanBlocks() &&
                FactionsPlugin.getInstance().conf().factions().protection().isTerritoryDenyEndermanBlocks() &&
                FactionsPlugin.getInstance().conf().factions().protection().isTerritoryDenyEndermanBlocksWhenOffline() &&
                FactionsPlugin.getInstance().conf().factions().protection().isSafeZoneDenyEndermanBlocks() &&
                FactionsPlugin.getInstance().conf().factions().protection().isWarZoneDenyEndermanBlocks()) {
            return true;
        }

        Faction claimFaction = Board.getInstance().getFactionAt(FLocation.wrap(loc));

        if (claimFaction.isWilderness()) {
            return FactionsPlugin.getInstance().conf().factions().protection().isWildernessDenyEndermanBlocks();
        } else if (claimFaction.isNormal()) {
            return claimFaction.hasPlayersOnline() ? FactionsPlugin.getInstance().conf().factions().protection().isTerritoryDenyEndermanBlocks() : FactionsPlugin.getInstance().conf().factions().protection().isTerritoryDenyEndermanBlocksWhenOffline();
        } else if (claimFaction.isSafeZone()) {
            return FactionsPlugin.getInstance().conf().factions().protection().isSafeZoneDenyEndermanBlocks();
        } else if (claimFaction.isWarZone()) {
            return FactionsPlugin.getInstance().conf().factions().protection().isWarZoneDenyEndermanBlocks();
        }

        return false;
    }
}
