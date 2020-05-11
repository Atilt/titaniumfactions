package com.massivecraft.factions.integration;

import com.massivecraft.factions.Board;
import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.Factions;
import com.massivecraft.factions.FactionsPlugin;
import com.massivecraft.factions.landraidcontrol.DTRControl;
import com.massivecraft.factions.perms.Relation;
import com.massivecraft.factions.tag.FactionTag;
import com.massivecraft.factions.tag.Tag;
import com.massivecraft.factions.util.TL;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.clip.placeholderapi.expansion.Relational;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class ClipPlaceholderAPIManager extends PlaceholderExpansion implements Relational {

    // Identifier for this expansion
    @Override
    public String getIdentifier() {
        return "factionsuuid";
    }

    @Override
    public String getAuthor() {
        return "drtshock";
    }

    // Since we are registering this expansion from the dependency, this can be null
    @Override
    public String getPlugin() {
        return null;
    }

    // Return the plugin version since this expansion is bundled with the dependency
    @Override
    public String getVersion() {
        return FactionsPlugin.getInstance().getDescription().getVersion();
    }

    @Override
    public boolean persist() {
        return true;
    }

    // Relational placeholders
    @Override
    public String onPlaceholderRequest(Player p1, Player p2, String placeholder) {
        if (p1 == null || p2 == null || placeholder == null) {
            return "";
        }

        FPlayer fp1 = FPlayers.getInstance().getByPlayer(p1);
        FPlayer fp2 = FPlayers.getInstance().getByPlayer(p2);
        if (fp1 == null || fp2 == null) {
            return "";
        }

        switch (placeholder) {
            case "relation":
                String relationName = fp1.getRelationTo(fp2).nicename;
                return relationName != null ? relationName : "";
            case "relation_color":
                ChatColor color = fp1.getColorTo(fp2);
                return color != null ? color.toString() : "";
        }

        return null;
    }

    @Override
    public String onPlaceholderRequest(Player player, String placeholder) {
        if (player == null || placeholder == null) {
            return "";
        }

        FPlayer fPlayer = FPlayers.getInstance().getByPlayer(player);
        Faction faction = fPlayer.getFaction();
        boolean territory = false;
        if (placeholder.contains("faction_territory")) {
            faction = Board.getInstance().getFactionAt(fPlayer.getLastStoodAt());
            placeholder = placeholder.replace("_territory", "");
            territory = true;
        }
        switch (placeholder) {
            // First list player stuff
            case "player_name":
                return fPlayer.getName();
            case "player_lastseen":
                String humanized = DurationFormatUtils.formatDurationWords(System.currentTimeMillis() - fPlayer.getLastLoginTime(), true, true) + TL.COMMAND_STATUS_AGOSUFFIX;
                return fPlayer.isOnline() ? ChatColor.GREEN + TL.COMMAND_STATUS_ONLINE.toString() : (System.currentTimeMillis() - fPlayer.getLastLoginTime() < 432000000 ? ChatColor.YELLOW + humanized : ChatColor.RED + humanized);
            case "player_group":
                return FactionsPlugin.getInstance().getPrimaryGroup(Bukkit.getOfflinePlayer(fPlayer.getId()));
            case "player_balance":
                return Econ.isSetup() ? Econ.getFriendlyBalance(fPlayer) : TL.ECON_OFF.format("balance");
            case "player_power":
                return String.valueOf(fPlayer.getPowerRounded());
            case "player_maxpower":
                return String.valueOf(fPlayer.getPowerMaxRounded());
            case "player_kills":
                return String.valueOf(fPlayer.getKills());
            case "player_deaths":
                return String.valueOf(fPlayer.getDeaths());
            case "player_role":
                return fPlayer.hasFaction() ? fPlayer.getRole().getPrefix() : "";
            case "player_role_name":
                return fPlayer.hasFaction() ? fPlayer.getRole().getTranslation().toString() : TL.PLACEHOLDER_ROLE_NAME.toString();
            // Then Faction stuff
            case "faction_name":
                return (fPlayer.hasFaction() || territory) ? faction.getTag() : TL.NOFACTION_PREFIX.toString();
            case "faction_name_custom":
                return (fPlayer.hasFaction() || territory) ? Tag.parsePlain(fPlayer, TL.PLACEHOLDER_CUSTOM_FACTION.toString()) : "";
            case "faction_only_space":
                return (fPlayer.hasFaction() || territory) ? " " : "";
            case "faction_power":
                return String.valueOf(faction.getPowerRounded());
            case "faction_powermax":
                return String.valueOf(faction.getPowerMaxRounded());
            case "faction_dtr":
                return (fPlayer.hasFaction() || territory) ? DTRControl.round(faction.getDTR()) : "";
            case "faction_dtrmax":
                if ((fPlayer.hasFaction() || territory) && FactionsPlugin.getInstance().getLandRaidControl() instanceof DTRControl) {
                    return DTRControl.round(((DTRControl) FactionsPlugin.getInstance().getLandRaidControl()).getMaxDTR(faction));
                }
                return "";
            case "faction_dtr_frozen":
                if ((fPlayer.hasFaction() || territory) && FactionsPlugin.getInstance().getLandRaidControl() instanceof DTRControl) {
                    return FactionTag.DTR_FROZEN.replace(FactionTag.DTR_FROZEN.getTag(), faction);
                }
                return "";
            case "faction_dtr_frozen_time":
                if ((fPlayer.hasFaction() || territory) && FactionsPlugin.getInstance().getLandRaidControl() instanceof DTRControl) {
                    return FactionTag.DTR_FROZEN_TIME.replace(FactionTag.DTR_FROZEN_TIME.getTag(), faction);
                }
                return "";
            case "faction_maxclaims":
                return (fPlayer.hasFaction() || territory) ? String.valueOf(FactionsPlugin.getInstance().getLandRaidControl().getLandLimit(faction)) : "";
            case "faction_description":
                return faction.getDescription();
            case "faction_claims":
                return Integer.toString(faction.getAllClaims().size());
            case "faction_founded":
                return TL.sdf.format(faction.getFoundedDate());
            case "faction_joining":
                return (faction.getOpen() ? TL.COMMAND_SHOW_UNINVITED.toString() : TL.COMMAND_SHOW_INVITATION.toString());
            case "faction_peaceful":
                return faction.isPeaceful() ? FactionsPlugin.getInstance().conf().colors().relations().getNeutral() + TL.COMMAND_SHOW_PEACEFUL.toString() : "";
            case "faction_powerboost":
                double powerBoost = faction.getPowerBoost();
                return (powerBoost == 0.0) ? "" : (powerBoost > 0.0 ? TL.COMMAND_SHOW_BONUS.toString() : TL.COMMAND_SHOW_PENALTY.toString()) + powerBoost + ")";
            case "faction_leader":
                FPlayer fAdmin = faction.getFPlayerAdmin();
                return fAdmin == null ? "Server" : fAdmin.getName().substring(0, fAdmin.getName().length() > 14 ? 13 : fAdmin.getName().length());
            case "faction_warps":
                return Integer.toString(faction.getWarps().size());
            case "faction_raidable":
                boolean raid = FactionsPlugin.getInstance().getLandRaidControl().isRaidable(faction);
                return raid ? TL.RAIDABLE_TRUE.toString() : TL.RAIDABLE_FALSE.toString();
            case "faction_home_world":
                return faction.hasHome() ? faction.getHome().getWorld().getName() : "";
            case "faction_home_x":
                return faction.hasHome() ? String.valueOf(faction.getHome().getBlockX()) : "";
            case "faction_home_y":
                return faction.hasHome() ? String.valueOf(faction.getHome().getBlockY()) : "";
            case "faction_home_z":
                return faction.hasHome() ? String.valueOf(faction.getHome().getBlockZ()) : "";
            case "faction_land_value":
                return Econ.shouldBeUsed() ? Econ.moneyString(Econ.calculateTotalLandValue(faction.getLandRounded())) : TL.ECON_OFF.format("value");
            case "faction_land_refund":
                return Econ.shouldBeUsed() ? Econ.moneyString(Econ.calculateTotalLandRefund(faction.getLandRounded())) : TL.ECON_OFF.format("refund");
            case "faction_bank_balance":
                return Econ.shouldBeUsed() ? Econ.moneyString(Econ.getBalance(faction.getAccountId())) : TL.ECON_OFF.format("balance");
            case "faction_tnt_balance":
                return FactionTag.TNT_BALANCE.replace(FactionTag.TNT_BALANCE.getTag(), faction);
            case "faction_tnt_max_balance":
                return FactionTag.TNT_MAX.replace(FactionTag.TNT_MAX.getTag(), faction);
            case "faction_allies":
                return String.valueOf(faction.getRelationCount(Relation.ALLY));
            case "faction_allies_players":
                return String.valueOf(this.countOn(faction, Relation.ALLY, null, fPlayer));
            case "faction_allies_players_online":
                return String.valueOf(this.countOn(faction, Relation.ALLY, true, fPlayer));
            case "faction_allies_players_offline":
                return String.valueOf(this.countOn(faction, Relation.ALLY, false, fPlayer));
            case "faction_enemies":
                return String.valueOf(faction.getRelationCount(Relation.ENEMY));
            case "faction_enemies_players":
                return String.valueOf(this.countOn(faction, Relation.ENEMY, null, fPlayer));
            case "faction_enemies_players_online":
                return String.valueOf(this.countOn(faction, Relation.ENEMY, true, fPlayer));
            case "faction_enemies_players_offline":
                return String.valueOf(this.countOn(faction, Relation.ENEMY, false, fPlayer));
            case "faction_truces":
                return String.valueOf(faction.getRelationCount(Relation.TRUCE));
            case "faction_truces_players":
                return String.valueOf(this.countOn(faction, Relation.TRUCE, null, fPlayer));
            case "faction_truces_players_online":
                return String.valueOf(this.countOn(faction, Relation.TRUCE, true, fPlayer));
            case "faction_truces_players_offline":
                return String.valueOf(this.countOn(faction, Relation.TRUCE, false, fPlayer));
            case "faction_online":
                return String.valueOf(faction.getOnlinePlayers().size());
            case "faction_offline":
                return String.valueOf(faction.getFPlayers().size() - faction.getOnlinePlayers().size());
            case "faction_size":
                return String.valueOf(faction.getFPlayers().size());
            case "faction_kills":
                return String.valueOf(faction.getKills());
            case "faction_deaths":
                return String.valueOf(faction.getDeaths());
            case "faction_maxvaults":
                return String.valueOf(faction.getMaxVaults());
            case "faction_relation_color":
                return fPlayer.getColorTo(faction).toString();
        }

        return null;
    }

    private int countOn(Faction f, Relation relation, Boolean status, FPlayer player) {
        int count = 0;
        for (Faction faction : Factions.getInstance().getAllFactions()) {
            if (faction.getRelationTo(f) == relation) {
                if (status == null) {
                    count += faction.getFPlayers().size();
                } else {
                    count += faction.getFPlayersWhereOnline(status, player).size();
                }
            }
        }
        return count;
    }
}
