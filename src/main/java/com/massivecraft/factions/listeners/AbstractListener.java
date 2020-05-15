package com.massivecraft.factions.listeners;

import com.massivecraft.factions.Board;
import com.massivecraft.factions.FLocation;
import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.FactionsPlugin;
import com.massivecraft.factions.perms.PermissibleAction;
import com.massivecraft.factions.perms.Relation;
import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.util.TL;
import com.massivecraft.factions.util.TextUtil;
import com.massivecraft.factions.util.material.MaterialDb;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;


public abstract class AbstractListener implements Listener {

    public boolean playerCanInteractHere(Player player, Location location) {
        if (FactionsPlugin.getInstance().conf().factions().protection().getPlayersWhoBypassAllProtection().contains(player.getName())) {
            return true;
        }

        FPlayer me = FPlayers.getInstance().getByPlayer(player);
        if (me.isAdminBypassing()) {
            return true;
        }

        FLocation loc = FLocation.wrap(location);
        Faction otherFaction = Board.getInstance().getFactionAt(loc);

        if (FactionsPlugin.getInstance().getLandRaidControl().isRaidable(otherFaction)) {
            return true;
        }

        if (otherFaction.isWilderness()) {
            if (!FactionsPlugin.getInstance().conf().factions().protection().isWildernessDenyUsage() || FactionsPlugin.getInstance().conf().factions().protection().getWorldsNoWildernessProtection().contains(location.getWorld().getName())) {
                return true; // This is not faction territory. Use whatever you like here.
            }
            me.msg(TL.PLAYER_USE_WILDERNESS, "this");
            return false;
        } else if (otherFaction.isSafeZone()) {
            if (!FactionsPlugin.getInstance().conf().factions().protection().isSafeZoneDenyUsage() || Permission.MANAGE_SAFE_ZONE.has(player)) {
                return true;
            }
            me.msg(TL.PLAYER_USE_SAFEZONE, "this");
            return false;
        } else if (otherFaction.isWarZone()) {
            if (!FactionsPlugin.getInstance().conf().factions().protection().isWarZoneDenyUsage() || Permission.MANAGE_WAR_ZONE.has(player)) {
                return true;
            }
            me.msg(TL.PLAYER_USE_WARZONE, "this");

            return false;
        }

        boolean access = otherFaction.hasAccess(me, PermissibleAction.ITEM);

        // Cancel if we are not in our own territory
        if (!access) {
            me.msg(TL.PLAYER_USE_TERRITORY, "this", otherFaction.getTag(me.getFaction()));
            return false;
        }

        // Also cancel if player doesn't have ownership rights for this claim
        if (FactionsPlugin.getInstance().conf().factions().ownedArea().isEnabled() && FactionsPlugin.getInstance().conf().factions().ownedArea().isDenyUsage() && !otherFaction.playerHasOwnershipRights(me, loc)) {
            me.msg(TL.PLAYER_USE_OWNED, "this", otherFaction.getOwnerListString(loc));
            return false;
        }

        return true;
    }

    public boolean canPlayerUseBlock(Player player, Material material, Location location, boolean justCheck) {
        if (FactionsPlugin.getInstance().conf().factions().protection().getPlayersWhoBypassAllProtection().contains(player.getName())) {
            return true;
        }

        FPlayer me = FPlayers.getInstance().getByPlayer(player);
        if (me.isAdminBypassing()) {
            return true;
        }

        FLocation loc = FLocation.wrap(location);
        Faction otherFaction = Board.getInstance().getFactionAt(loc);

        // no door/chest/whatever protection in wilderness, war zones, or safe zones
        if (!otherFaction.isNormal()) {
            if (material == Material.ITEM_FRAME || material == Material.ARMOR_STAND) {
                return playerCanInteractHere(player, location);
            }
            return true;
        }

        if (FactionsPlugin.getInstance().getLandRaidControl().isRaidable(otherFaction)) {
            return true;
        }

        PermissibleAction action = null;

        switch (material) {
            case LEVER:
                action = PermissibleAction.LEVER;
                break;
            case STONE_BUTTON:
            case BIRCH_BUTTON:
            case ACACIA_BUTTON:
            case DARK_OAK_BUTTON:
            case JUNGLE_BUTTON:
            case OAK_BUTTON:
            case SPRUCE_BUTTON:
                action = PermissibleAction.BUTTON;
                break;
            case DARK_OAK_DOOR:
            case ACACIA_DOOR:
            case BIRCH_DOOR:
            case IRON_DOOR:
            case JUNGLE_DOOR:
            case SPRUCE_DOOR:
            case ACACIA_TRAPDOOR:
            case OAK_DOOR:
            case BIRCH_TRAPDOOR:
            case DARK_OAK_TRAPDOOR:
            case IRON_TRAPDOOR:
            case JUNGLE_TRAPDOOR:
            case OAK_TRAPDOOR:
            case SPRUCE_TRAPDOOR:
                action = PermissibleAction.DOOR;
                break;
            case CHEST:
            case ENDER_CHEST:
            case TRAPPED_CHEST:
            case BARREL:
            case FURNACE:
            case DROPPER:
            case DISPENSER:
            case HOPPER:
            case BLAST_FURNACE:
            case CAULDRON:
            case CAMPFIRE:
            case BREWING_STAND:
            case CARTOGRAPHY_TABLE:
            case GRINDSTONE:
            case SMOKER:
            case STONECUTTER:
            case LECTERN:
            case ITEM_FRAME:
            case JUKEBOX:
            case ARMOR_STAND:
            case REPEATER:
            case ENCHANTING_TABLE:
            case FARMLAND:
            case BEACON:
            case ANVIL:
            case CHIPPED_ANVIL:
            case DAMAGED_ANVIL:
            case FLOWER_POT:
            case BEE_NEST:
                action = PermissibleAction.CONTAINER;
                break;
            default:
                // Check for doors that might have diff material name in old version.
                if (MaterialDb.get().getProvider().isGate(material)) {
                    action = PermissibleAction.DOOR;
                }
                if (MaterialDb.get().getProvider().isShulkerBox(material) || MaterialDb.get().getProvider().isPottedFlower(material)) {
                    action = PermissibleAction.CONTAINER;
                }
                if (MaterialDb.get().getProvider().isPressurePlate(material)) {
                    action = PermissibleAction.PLATE;
                }
                if (MaterialDb.get().getProvider().isSign(material)) {
                    action = PermissibleAction.ITEM;
                }
                break;
        }

        if (action == null) {
            return true;
        }

        // F PERM check runs through before other checks.
        if (!otherFaction.hasAccess(me, action)) {
            if (action != PermissibleAction.PLATE) {
                me.msg(TL.GENERIC_NOPERMISSION, action);
            }
            return false;
        }

        // Dupe fix.
        Faction myFaction = me.getFaction();
        Relation rel = myFaction.getRelationTo(otherFaction);
        if (FactionsPlugin.getInstance().conf().exploits().doPreventDuping() &&
                (!rel.isMember() || !otherFaction.playerHasOwnershipRights(me, loc))) {
            Material mainHand = player.getItemInHand().getType();

            // Check if material is at risk for dupe in either hand.
            if (MaterialDb.get().getProvider().isExploitable(mainHand)) {
                return false;
            }
        }

        // Also cancel if player doesn't have ownership rights for this claim
        if (FactionsPlugin.getInstance().conf().factions().ownedArea().isEnabled() && FactionsPlugin.getInstance().conf().factions().ownedArea().isProtectMaterials() && !otherFaction.playerHasOwnershipRights(me, loc)) {
            if (!justCheck) {
                me.msg(TL.PLAYER_USE_OWNED, TextUtil.getMaterialName(material), otherFaction.getOwnerListString(loc));
            }

            return false;
        }

        return true;
    }
}
