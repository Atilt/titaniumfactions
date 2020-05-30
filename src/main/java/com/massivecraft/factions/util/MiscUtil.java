package com.massivecraft.factions.util;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FactionsPlugin;
import it.unimi.dsi.fastutil.chars.CharOpenHashSet;
import it.unimi.dsi.fastutil.chars.CharSet;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

public final class MiscUtil {

    public static EntityType creatureTypeFromEntity(Entity entity) {
        if (!(entity instanceof Creature)) {
            return null;
        }
        return EntityType.fromName(entity.getClass().getSimpleName().substring(5));
    }

    // Inclusive range
    public static long[] range(long start, long end) {
        long[] values = new long[(int) Math.abs(end - start) + 1];

        if (end < start) {
            long oldstart = start;
            start = end;
            end = oldstart;
        }

        for (long i = start; i <= end; i++) {
            values[(int) (i - start)] = i;
        }

        return values;
    }

    private static final Pattern ALPHANUMERIC = Pattern.compile("[^A-Za-z0-9]");
    private static final CharSet SUBSTANCE_CHARS = new CharOpenHashSet(Arrays.asList('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'));

    public static String getComparisonString(String str) {
        return ALPHANUMERIC.matcher(str).replaceAll("");
    }

    public static List<String> validateTag(String str) {
        ObjectList<String> errors = new ObjectArrayList<>();
        String match = str.toLowerCase();

        for (String blacklistItem : FactionsPlugin.getInstance().conf().factions().other().getNameBlacklist()) {
            if (match.toLowerCase().contains(blacklistItem.toLowerCase())) {
                errors.add(TextUtil.parse(TL.GENERIC_FACTIONTAG_BLACKLIST.toString()));
                break;
            }
        }

        if (getComparisonString(str).length() < FactionsPlugin.getInstance().conf().factions().other().getTagLengthMin()) {
            errors.add(TL.GENERIC_FACTIONTAG_TOOSHORT.format(Integer.toString(FactionsPlugin.getInstance().conf().factions().other().getTagLengthMin())));
        }

        if (str.length() > FactionsPlugin.getInstance().conf().factions().other().getTagLengthMax()) {
            errors.add(TL.GENERIC_FACTIONTAG_TOOLONG.format(Integer.toString(FactionsPlugin.getInstance().conf().factions().other().getTagLengthMax())));
        }

        for (char c : str.toCharArray()) {
            if (!SUBSTANCE_CHARS.contains(c)) {
                errors.add(TL.GENERIC_FACTIONTAG_ALPHANUMERIC.format(Character.toString(c)));
            }
        }

        return errors;
    }

    public static List<FPlayer> rankOrder(Collection<FPlayer> players) {
        ObjectList<FPlayer> ret = new ObjectArrayList<>(players);
        Collections.sort(ret);
        return ret;
    }
}
