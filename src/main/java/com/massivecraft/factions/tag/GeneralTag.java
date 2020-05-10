package com.massivecraft.factions.tag;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.FactionsPlugin;
import com.massivecraft.factions.perms.Relation;
import com.massivecraft.factions.util.TL;
import com.massivecraft.factions.util.TextUtil;

import java.util.function.Supplier;

public enum GeneralTag implements Tag {
    MAX_WARPS("max-warps", () -> Integer.toString(FactionsPlugin.getInstance().conf().commands().warp().getMaxWarps())),
    MAX_ALLIES("max-allies", () -> getRelation(Relation.ALLY)),
    MAX_ENEMIES("max-enemies", () -> getRelation(Relation.ENEMY)),
    MAX_TRUCES("max-truces", () -> getRelation(Relation.TRUCE)),
    FACTIONLESS("factionless", () -> {
        int count = 0;
        for (FPlayer onlinePlayer : FPlayers.getInstance().getOnlinePlayers()) {
            if (!onlinePlayer.hasFaction()) {
                count++;
            }
        }
        return Integer.toString(count);
    }),
    FACTIONLESS_TOTAL("factionless-total", () -> {
        int count = 0;
        for (FPlayer fPlayer : FPlayers.getInstance().getAllFPlayers()) {
            if (!fPlayer.hasFaction()) {
                count++;
            }
        }
        return Integer.toString(count);
    }),
    TOTAL_ONLINE("total-online", () -> Integer.toString(FPlayers.getInstance().size())),
    ;

    private final String tag;
    private final Supplier<String> supplier;

    private static String getRelation(Relation relation) {
        if (FactionsPlugin.getInstance().conf().factions().maxRelations().isEnabled()) {
            return Integer.toString(relation.getMax());
        }
        return TL.GENERIC_INFINITY.toString();
    }

    public static String parse(String text) {
        for (GeneralTag tag : GeneralTag.values()) {
            text = tag.replace(text);
        }
        return text;
    }

    GeneralTag(String tag, Supplier<String> supplier) {
        this.tag = '{' + tag + '}';
        this.supplier = supplier;
    }

    @Override
    public String getTag() {
        return this.tag;
    }

    @Override
    public boolean foundInString(String test) {
        return test != null && test.contains(this.tag);
    }

    public String replace(String text) {
        if (!this.foundInString(text)) {
            return text;
        }
        String result = this.supplier.get();
        return result == null ? null : TextUtil.replace(text, this.tag, result);
    }
}
