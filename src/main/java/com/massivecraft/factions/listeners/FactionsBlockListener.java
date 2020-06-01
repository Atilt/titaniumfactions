package com.massivecraft.factions.listeners;

import com.massivecraft.factions.*;
import com.massivecraft.factions.perms.PermissibleAction;
import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.util.TL;
import com.massivecraft.factions.util.WorldUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;

import java.time.Instant;
import java.util.List;

public class FactionsBlockListener implements Listener {

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent event) {
        if (!WorldUtil.isEnabled(event.getBlock().getWorld())) {
            return;
        }

        if (!event.canBuild()) {
            return;
        }

        // special case for flint&steel, which should only be prevented by DenyUsage list
        if (event.getBlockPlaced().getType() == Material.FIRE) {
            return;
        }

        Faction targetFaction = Board.getInstance().getFactionAt(FLocation.wrap(event.getBlock().getLocation()));
        if (targetFaction.isNormal() && !targetFaction.isPeaceful() && FactionsPlugin.getInstance().conf().factions().specialCase().getIgnoreBuildMaterials().contains(event.getBlock().getType())) {
            return;
        }

        if (!playerCanBuildDestroyBlock(event.getPlayer(), event.getBlock().getLocation(), PermissibleAction.BUILD, false)) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onBlockFromTo(BlockFromToEvent event) {
        if (!WorldUtil.isEnabled(event.getBlock().getWorld())) {
            return;
        }

        if (!FactionsPlugin.getInstance().conf().exploits().isLiquidFlow()) {
            return;
        }
        if (event.getBlock().isLiquid() && event.getToBlock().isEmpty()) {
            Faction from = Board.getInstance().getFactionAt(FLocation.wrap(event.getBlock()));
            Faction to = Board.getInstance().getFactionAt(FLocation.wrap(event.getToBlock()));
            if (from == to) {
                // not concerned with inter-faction events
                return;
            }
            // from faction != to faction
            if (to.isNormal()) {
                if (from.isNormal() && from.getRelationTo(to).isAlly()) {
                    return;
                }
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        if (!WorldUtil.isEnabled(event.getBlock().getWorld())) {
            return;
        }

        if (!playerCanBuildDestroyBlock(event.getPlayer(), event.getBlock().getLocation(), PermissibleAction.DESTROY, false)) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onBlockDamage(BlockDamageEvent event) {
        if (!WorldUtil.isEnabled(event.getBlock().getWorld())) {
            return;
        }

        if (event.getInstaBreak() && !playerCanBuildDestroyBlock(event.getPlayer(), event.getBlock().getLocation(), PermissibleAction.DESTROY, false)) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onBlockPistonExtend(BlockPistonExtendEvent event) {
        if (!WorldUtil.isEnabled(event.getBlock().getWorld())) {
            return;
        }

        if (!FactionsPlugin.getInstance().conf().factions().protection().isPistonProtectionThroughDenyBuild()) {
            return;
        }

        // if the pushed blocks list is empty, no worries
        if (event.getBlocks().isEmpty()) {
            return;
        }

        Faction pistonFaction = Board.getInstance().getFactionAt(FLocation.wrap(event.getBlock()));

        if (!canPistonMoveBlock(pistonFaction, event.getBlocks(), event.getDirection())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onBlockPistonRetract(BlockPistonRetractEvent event) {
        if (!WorldUtil.isEnabled(event.getBlock().getWorld())) {
            return;
        }

        // if not a sticky piston, retraction should be fine
        if (!event.isSticky() || !FactionsPlugin.getInstance().conf().factions().protection().isPistonProtectionThroughDenyBuild()) {
            return;
        }

        // if the retracted blocks list is empty, no worries
        if (event.getBlocks().isEmpty()) {
            return;
        }

        Faction pistonFaction = Board.getInstance().getFactionAt(FLocation.wrap(event.getBlock()));

        if (!canPistonMoveBlock(pistonFaction, event.getBlocks(), null)) {
            event.setCancelled(true);
        }
    }


    private boolean canPistonMoveBlock(Faction pistonFaction, List<Block> blocks, BlockFace direction) {
        if (direction == null) {
            return true;
        }
        String world = blocks.get(0).getWorld().getName();
        for (Block block : blocks) {
            Faction otherFaction = Board.getInstance().getFactionAt(FLocation.wrap(block.getLocation().add(direction.getModX(), direction.getModY(), direction.getModZ())));
            if (pistonFaction == otherFaction) {
                continue;
            }
            // Check if the piston is moving in a faction's territory. This disables pistons entirely in faction territory.
            if (FactionsPlugin.getInstance().conf().factions().other().isDisablePistonsInTerritory() && otherFaction.isNormal()) {
                return false;
            }
            if (otherFaction.isWilderness() && FactionsPlugin.getInstance().conf().factions().protection().isWildernessDenyBuild() && !FactionsPlugin.getInstance().conf().factions().protection().getWorldsNoWildernessProtection().contains(world)) {
                return false;
            } else if (otherFaction.isSafeZone() && FactionsPlugin.getInstance().conf().factions().protection().isSafeZoneDenyBuild()) {
                return false;
            } else if (otherFaction.isWarZone() && FactionsPlugin.getInstance().conf().factions().protection().isWarZoneDenyBuild()) {
                return false;
            }
            if (!otherFaction.hasAccess(otherFaction.hasPlayersOnline(), pistonFaction.getRelationTo(otherFaction), PermissibleAction.BUILD)) {
                return false;
            }
        }
        return true;
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onFrostWalker(EntityBlockFormEvent event) {
        if (!WorldUtil.isEnabled(event.getBlock().getWorld())) {
            return;
        }

        if (event.getEntity() == null || event.getBlock() == null || event.getEntity().getType() != EntityType.PLAYER) {
            return;
        }

        Player player = (Player) event.getEntity();
        Location location = event.getBlock().getLocation();

        // only notify every 10 seconds
        FPlayer fPlayer = FPlayers.getInstance().getByPlayer(player);
        boolean justCheck = fPlayer.getLastFrostwalkerMessage() + 10000 > Instant.now().toEpochMilli();
        if (!justCheck) {
            fPlayer.setLastFrostwalkerMessage();
        }

        // Check if they have build permissions here. If not, block this from happening.
        if (!playerCanBuildDestroyBlock(player, location, PermissibleAction.FROSTWALK, justCheck)) {
            event.setCancelled(true);
        }
    }

    public static boolean playerCanBuildDestroyBlock(Player player, Location location, PermissibleAction permissibleAction, boolean justCheck) {
        if (FactionsPlugin.getInstance().conf().factions().protection().getPlayersWhoBypassAllProtection().contains(player.getName())) {
            return true;
        }

        FPlayer me = FPlayers.getInstance().getById(player.getUniqueId());
        if (me.isAdminBypassing()) {
            return true;
        }

        FLocation loc = FLocation.wrap(location);
        Faction otherFaction = Board.getInstance().getFactionAt(loc);

        if (otherFaction.isWilderness()) {
            if (FactionsPlugin.getInstance().conf().worldGuard().isBuildPriority() && FactionsPlugin.getInstance().getWorldguard() != null && FactionsPlugin.getInstance().getWorldguard().playerCanBuild(player, location)) {
                return true;
            }

            if (!FactionsPlugin.getInstance().conf().factions().protection().isWildernessDenyBuild() || FactionsPlugin.getInstance().conf().factions().protection().getWorldsNoWildernessProtection().contains(location.getWorld().getName())) {
                return true; // This is not faction territory. Use whatever you like here.
            }

            if (!justCheck) {
                me.msg(TL.PERM_DENIED_WILDERNESS, permissibleAction.getShortDescription());
            }

            return false;
        } else if (otherFaction.isSafeZone()) {
            if (FactionsPlugin.getInstance().conf().worldGuard().isBuildPriority() && FactionsPlugin.getInstance().getWorldguard() != null && FactionsPlugin.getInstance().getWorldguard().playerCanBuild(player, location)) {
                return true;
            }

            if (!FactionsPlugin.getInstance().conf().factions().protection().isSafeZoneDenyBuild() || Permission.MANAGE_SAFE_ZONE.has(player)) {
                return true;
            }

            if (!justCheck) {
                me.msg(TL.PERM_DENIED_SAFEZONE, permissibleAction.getShortDescription());
            }

            return false;
        } else if (otherFaction.isWarZone()) {
            if (FactionsPlugin.getInstance().conf().worldGuard().isBuildPriority() && FactionsPlugin.getInstance().getWorldguard() != null && FactionsPlugin.getInstance().getWorldguard().playerCanBuild(player, location)) {
                return true;
            }

            if (!FactionsPlugin.getInstance().conf().factions().protection().isWarZoneDenyBuild() || Permission.MANAGE_WAR_ZONE.has(player)) {
                return true;
            }

            if (!justCheck) {
                me.msg(TL.PERM_DENIED_WARZONE, permissibleAction.getShortDescription());
            }

            return false;
        }
        if (FactionsPlugin.getInstance().getLandRaidControl().isRaidable(otherFaction)) {
            return true;
        }

        Faction myFaction = me.getFaction();
        boolean pain = !justCheck && otherFaction.hasAccess(me, PermissibleAction.PAINBUILD);

        // If the faction hasn't: defined access or denied, fallback to config values
        if (!otherFaction.hasAccess(me, permissibleAction)) {
            if (pain && permissibleAction != PermissibleAction.FROSTWALK) {
                player.damage(FactionsPlugin.getInstance().conf().factions().other().getActionDeniedPainAmount());
                me.msg(TL.PERM_DENIED_PAINTERRITORY, permissibleAction.getShortDescription(), otherFaction.getTag(myFaction));
                return true;
            } else if (!justCheck) {
                me.msg(TL.PERM_DENIED_TERRITORY, permissibleAction.getShortDescription(), otherFaction.getTag(myFaction));
            }
            return false;
        }

        // Also cancel and/or cause pain if player doesn't have ownership rights for this claim
        if (FactionsPlugin.getInstance().conf().factions().ownedArea().isEnabled() && (FactionsPlugin.getInstance().conf().factions().ownedArea().isDenyBuild() || FactionsPlugin.getInstance().conf().factions().ownedArea().isPainBuild()) && !otherFaction.playerHasOwnershipRights(me, loc)) {
            if (pain && FactionsPlugin.getInstance().conf().factions().ownedArea().isPainBuild()) {
                player.damage(FactionsPlugin.getInstance().conf().factions().other().getActionDeniedPainAmount());

                if (!FactionsPlugin.getInstance().conf().factions().ownedArea().isDenyBuild()) {
                    me.msg(TL.PERM_DENIED_PAINOWNED, permissibleAction.getShortDescription(), otherFaction.getOwnerListString(loc));
                }
            }
            if (FactionsPlugin.getInstance().conf().factions().ownedArea().isDenyBuild()) {
                if (!justCheck) {
                    me.msg(TL.PERM_DENIED_OWNED, permissibleAction.getShortDescription(), otherFaction.getOwnerListString(loc));
                }

                return false;
            }
        }

        return true;
    }
}
