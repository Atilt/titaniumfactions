package com.massivecraft.factions.tag;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.FactionsPlugin;
import com.massivecraft.factions.integration.Econ;
import com.massivecraft.factions.landraidcontrol.DTRControl;
import com.massivecraft.factions.landraidcontrol.PowerControl;
import com.massivecraft.factions.math.FastMath;
import com.massivecraft.factions.perms.Relation;
import com.massivecraft.factions.util.TL;
import com.massivecraft.factions.util.TextUtil;
import org.apache.commons.lang.time.DurationFormatUtils;

import java.time.Instant;
import java.util.function.BiFunction;
import java.util.function.Function;

public enum FactionTag implements Tag {
    HOME_X("x", (fac) -> fac.hasHome() ? Integer.toString(FastMath.floor(fac.getHome().getX())) : Tag.isMinimalShow() ? null : "{ig}"),
    HOME_Y("y", (fac) -> fac.hasHome() ? Integer.toString(FastMath.floor(fac.getHome().getY())) : Tag.isMinimalShow() ? null : "{ig}"),
    HOME_Z("z", (fac) -> fac.hasHome() ? Integer.toString(FastMath.floor(fac.getHome().getZ())) : Tag.isMinimalShow() ? null : "{ig}"),
    CHUNKS("chunks", (fac) -> Integer.toString(fac.getLandRounded())),
    WARPS("warps", (fac) -> Integer.toString(fac.getWarps().size())),
    HEADER("header", (fac, fp) -> TextUtil.titleize(fac.getTag(fp))),
    POWER("power", (fac) -> Integer.toString(fac.getPowerRounded())),
    MAX_POWER("maxPower", (fac) -> Integer.toString(fac.getPowerMaxRounded())),
    POWER_BOOST("power-boost", (fac) -> {
        double powerBoost = fac.getPowerBoost();
        return (powerBoost == 0.0) ? "" : (powerBoost > 0.0 ? TL.COMMAND_SHOW_BONUS.toString() : TL.COMMAND_SHOW_PENALTY.toString() + powerBoost + ")");
    }),
    LEADER("leader", (fac) -> {
        FPlayer fAdmin = fac.getFPlayerAdmin();
        return fAdmin == null ? "Server" : fAdmin.getName().substring(0, fAdmin.getName().length() > 14 ? 13 : fAdmin.getName().length());
    }),
    JOINING("joining", (fac) -> (fac.getOpen() ? TL.COMMAND_SHOW_UNINVITED.toString() : TL.COMMAND_SHOW_INVITATION.toString())),
    FACTION("faction", (Function<Faction, String>) Faction::getTag),
    FACTION_RELATION_COLOR("faction-relation-color", (fac, fp) -> fp == null ? "" : fp.getColorTo(fac).toString()),
    HOME_WORLD("world", (fac) -> fac.hasHome() ? fac.getHome().getWorld().getName() : Tag.isMinimalShow() ? null : "{ig}"),
    RAIDABLE("raidable", (fac) -> {
        boolean raid = FactionsPlugin.getInstance().getLandRaidControl().isRaidable(fac);
        return raid ? TL.RAIDABLE_TRUE.toString() : TL.RAIDABLE_FALSE.toString();
    }),
    DTR("dtr", (fac) -> {
        if (FactionsPlugin.getInstance().getLandRaidControl() instanceof PowerControl) {
            int dtr = fac.getLandRounded() >= fac.getPowerRounded() ? 0 : FastMath.ceil(((double) (fac.getPowerRounded() - fac.getLandRounded())) / FactionsPlugin.getInstance().conf().factions().landRaidControl().power().getLossPerDeath());
            return TL.COMMAND_SHOW_DEATHS_TIL_RAIDABLE.format(Integer.toString(dtr));
        } else {
            return DTRControl.round(fac.getDTR());
        }
    }),
    MAX_DTR("max-dtr", (fac) -> {
        if (FactionsPlugin.getInstance().getLandRaidControl() instanceof DTRControl) {
            return DTRControl.round(((DTRControl) FactionsPlugin.getInstance().getLandRaidControl()).getMaxDTR(fac));
        }
        return Tag.isMinimalShow() ? null : "{ig}";
    }),
    DTR_FROZEN("dtr-frozen-status", (fac -> TL.DTR_FROZEN_STATUS_MESSAGE.format(fac.isFrozenDTR() ? TL.DTR_FROZEN_STATUS_TRUE.toString() : TL.DTR_FROZEN_STATUS_FALSE.toString()))),
    DTR_FROZEN_TIME("dtr-frozen-time", (fac -> TL.DTR_FROZEN_TIME_MESSAGE.format(fac.isFrozenDTR() ?
            DurationFormatUtils.formatDuration(fac.getFrozenDTRUntilTime() - Instant.now().toEpochMilli(), FactionsPlugin.getInstance().conf().factions().landRaidControl().dtr().getFreezeTimeFormat()) :
            TL.DTR_FROZEN_TIME_NOTFROZEN.toString()))),
    MAX_CHUNKS("max-chunks", (fac -> Integer.toString(FactionsPlugin.getInstance().getLandRaidControl().getLandLimit(fac)))),
    PEACEFUL("peaceful", (fac) -> fac.isPeaceful() ? FactionsPlugin.getInstance().conf().colors().relations().getPeaceful() + TL.COMMAND_SHOW_PEACEFUL.toString() : ""),
    PERMANENT("permanent", (fac) -> fac.isPermanent() ? "permanent" : "{notPermanent}"), // no braces needed
    LAND_VALUE("land-value", (fac) -> Econ.shouldBeUsed() ? Econ.moneyString(Econ.calculateTotalLandValue(fac.getLandRounded())) : Tag.isMinimalShow() ? null : TL.ECON_OFF.format("value")),
    DESCRIPTION("description", Faction::getDescription),
    CREATE_DATE("create-date", (fac) -> TL.formatDate(fac.getFoundedDate())),
    LAND_REFUND("land-refund", (fac) -> Econ.shouldBeUsed() ? Econ.moneyString(Econ.calculateTotalLandRefund(fac.getLandRounded())) : Tag.isMinimalShow() ? null : TL.ECON_OFF.format("refund")),
    BANK_BALANCE("faction-balance", (fac) -> {
        if (Econ.shouldBeUsed()) {
            return FactionsPlugin.getInstance().conf().economy().isBankEnabled() ? Econ.moneyString(Econ.getBalance(fac.getAccountId())) : Tag.isMinimalShow() ? null : TL.ECON_OFF.format("balance");
        }
        return Tag.isMinimalShow() ? null : TL.ECON_OFF.format("balance");
    }),
    TNT_BALANCE("tnt-balance", (fac) -> {
        if (FactionsPlugin.getInstance().conf().commands().tnt().isEnable()) {
            return Integer.toString(fac.getTNTBank());
        }
        return Tag.isMinimalShow() ? null : "";
    }),
    TNT_MAX("tnt-max-balance", (fac) -> {
        if (FactionsPlugin.getInstance().conf().commands().tnt().isEnable()) {
            return Integer.toString(FactionsPlugin.getInstance().conf().commands().tnt().getMaxStorage());
        }
        return Tag.isMinimalShow() ? null : "";
    }),
    ALLIES_COUNT("allies", (fac) -> Integer.toString(fac.getRelationCount(Relation.ALLY))),
    ENEMIES_COUNT("enemies", (fac) -> Integer.toString(fac.getRelationCount(Relation.ENEMY))),
    TRUCES_COUNT("truces", (fac) -> Integer.toString(fac.getRelationCount(Relation.TRUCE))),
    ONLINE_COUNT("online", (fac, fp) -> {
        if (fp != null && fp.isOnline()) {
            return Integer.toString(fac.getFPlayersWhereOnline(true, fp).size());
        } else {
            return Integer.toString(fac.getTotalOnline());
        }
    }),
    OFFLINE_COUNT("offline", (fac, fp) -> {
        if (fp != null && fp.isOnline()) {
            return Integer.toString(fac.getSize() - fac.getFPlayersWhereOnline(true, fp).size());
        } else {
            // Only console should ever get here.
            return Integer.toString(fac.getFPlayersWhereOnline(false).size());
        }
    }),
    FACTION_SIZE("members", (fac) -> Integer.toString(fac.getSize())),
    FACTION_KILLS("faction-kills", (fac) -> Integer.toString(fac.getKills())),
    FACTION_DEATHS("faction-deaths", (fac) -> Integer.toString(fac.getDeaths())),
    FACTION_BANCOUNT("faction-bancount", (fac) -> Integer.toString(fac.getBannedPlayers().size())),
    ;

    private final String tag;
    private final BiFunction<Faction, FPlayer, String> biFunction;
    private final Function<Faction, String> function;

    public static String parse(String text, Faction faction, FPlayer player) {
        for (FactionTag tag : VALUES) {
            text = tag.replace(text, faction, player);
        }
        return text;
    }

    public static String parse(String text, Faction faction) {
        for (FactionTag tag : VALUES) {
            text = tag.replace(text, faction);
        }
        return text;
    }

    FactionTag(String tag, BiFunction<Faction, FPlayer, String> function) {
        this(tag, null, function);
    }

    FactionTag(String tag, Function<Faction, String> function) {
        this(tag, function, null);
    }

    FactionTag(String tag, Function<Faction, String> function, BiFunction<Faction, FPlayer, String> biFunction) {
        this.tag = tag.equalsIgnoreCase("permanent") ? tag : '{' + tag + '}';
        this.biFunction = biFunction;
        this.function = function;
    }

    @Override
    public String getTag() {
        return this.tag;
    }

    @Override
    public boolean foundInString(String test) {
        return test != null && test.contains(this.tag);
    }

    public String replace(String text, Faction faction, FPlayer player) {
        if (!this.foundInString(text)) {
            return text;
        }
        String result = this.function == null ? this.biFunction.apply(faction, player) : this.function.apply(faction);
        return result == null ? null : TextUtil.replace(text, this.tag, result);
    }

    public String replace(String text, Faction faction) {
        return this.replace(text, faction, null);
    }
    
    public static final FactionTag[] VALUES = FactionTag.values();
}
