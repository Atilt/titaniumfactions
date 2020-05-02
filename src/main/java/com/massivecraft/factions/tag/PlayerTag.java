package com.massivecraft.factions.tag;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.FactionsPlugin;
import com.massivecraft.factions.integration.Econ;
import com.massivecraft.factions.util.TL;
import com.massivecraft.factions.util.TextUtil;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.function.Function;

public enum PlayerTag implements Tag {
    GROUP("group", (fp) -> {
        if (fp.isOnline()) {
            return FactionsPlugin.getInstance().getPrimaryGroup(fp.getPlayer());
        } else {
            return "";
        }
    }),
    LAST_SEEN("lastSeen", (fp) -> {
        String humanized = DurationFormatUtils.formatDurationWords(System.currentTimeMillis() - fp.getLastLoginTime(), true, true) + TL.COMMAND_STATUS_AGOSUFFIX;
        return fp.isOnline() ? ChatColor.GREEN + TL.COMMAND_STATUS_ONLINE.toString() : (System.currentTimeMillis() - fp.getLastLoginTime() < 432000000 ? ChatColor.YELLOW + humanized : ChatColor.RED + humanized);
    }),
    PLAYER_BALANCE("balance", (fp) -> Econ.isSetup() ? Econ.getFriendlyBalance(fp) : (Tag.isMinimalShow() ? null : TL.ECON_OFF.format("balance"))),
    PLAYER_POWER("player-power", (fp) -> Integer.toString(fp.getPowerRounded())),
    PLAYER_MAXPOWER("player-maxpower", (fp) -> Integer.toString(fp.getPowerMaxRounded())),
    PLAYER_KILLS("player-kills", (fp) -> Integer.toString(fp.getKills())),
    PLAYER_DEATHS("player-deaths", (fp) -> Integer.toString(fp.getDeaths())),
    PLAYER_NAME("name", FPlayer::getName),
    TOTAL_ONLINE_VISIBLE("total-online-visible", (fp) -> {
        if (fp == null) {
            return Integer.toString(FPlayers.getInstance().size());
        }
        int count = 0;
        Player me = fp.getPlayer();
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (me.canSee(player)) {
                count++;
            }
        }
        return Integer.toString(count);
    }),
    ;

    private final String tag;
    private final Function<FPlayer, String> function;

    public static String parse(String text, FPlayer player) {
        for (PlayerTag tag : VALUES) {
            text = tag.replace(text, player);
        }
        return text;
    }

    PlayerTag(String tag, Function<FPlayer, String> function) {
        this.tag = '{' + tag + '}';
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

    public String replace(String text, FPlayer player) {
        if (!this.foundInString(text)) {
            return text;
        }
        String result = this.function.apply(player);
        if (result == null) {
            return null;
        }
        return TextUtil.replace(text, this.tag, result);
    }

    public static final PlayerTag[] VALUES = PlayerTag.values();
}
