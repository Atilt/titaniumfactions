package com.massivecraft.factions.util;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FactionsPlugin;
import com.massivecraft.factions.perms.Role;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import org.bukkit.ChatColor;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

public class MiscUtil {

    public static EntityType creatureTypeFromEntity(Entity entity) {
        if (!(entity instanceof Creature)) {
            return null;
        }

        String name = entity.getClass().getSimpleName();
        name = name.substring(5); // Remove "Craft"

        return EntityType.fromName(name);
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

    /// TODO create tag whitelist!!
    public static ObjectSet<String> substanceChars = new ObjectOpenHashSet<>(Arrays.asList("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"));

    public static String getComparisonString(String str) {
        StringBuilder ret = new StringBuilder();

        str = ChatColor.stripColor(str);
        str = str.toLowerCase();

        for (char c : str.toCharArray()) {
            if (substanceChars.contains(String.valueOf(c))) {
                ret.append(c);
            }
        }
        return ret.toString().toLowerCase();
    }

    public static List<String> validateTag(String str) {
        ObjectList<String> errors = new ObjectArrayList<>();

        for (String blacklistItem : FactionsPlugin.getInstance().conf().factions().other().getNameBlacklist()) {
            if (str.toLowerCase().contains(blacklistItem.toLowerCase())) {
                errors.add(FactionsPlugin.getInstance().txt().parse(TL.GENERIC_FACTIONTAG_BLACKLIST.toString()));
                break;
            }
        }

        if (getComparisonString(str).length() < FactionsPlugin.getInstance().conf().factions().other().getTagLengthMin()) {
            errors.add(FactionsPlugin.getInstance().txt().parse(TL.GENERIC_FACTIONTAG_TOOSHORT.toString(), FactionsPlugin.getInstance().conf().factions().other().getTagLengthMin()));
        }

        if (str.length() > FactionsPlugin.getInstance().conf().factions().other().getTagLengthMax()) {
            errors.add(FactionsPlugin.getInstance().txt().parse(TL.GENERIC_FACTIONTAG_TOOLONG.toString(), FactionsPlugin.getInstance().conf().factions().other().getTagLengthMax()));
        }

        for (char c : str.toCharArray()) {
            if (!substanceChars.contains(String.valueOf(c))) {
                errors.add(FactionsPlugin.getInstance().txt().parse(TL.GENERIC_FACTIONTAG_ALPHANUMERIC.toString(), c));
            }
        }

        return errors;
    }

    public static Iterable<FPlayer> rankOrder(Iterable<FPlayer> players) {
        ObjectList<FPlayer> admins = new ObjectArrayList<>();
        ObjectList<FPlayer> coleaders = new ObjectArrayList<>();
        ObjectList<FPlayer> moderators = new ObjectArrayList<>();
        ObjectList<FPlayer> normal = new ObjectArrayList<>();
        ObjectList<FPlayer> recruit = new ObjectArrayList<>();

        for (FPlayer player : players) {

            // Fix for some data being broken when we added the recruit rank.
            if (player.getRole() == null) {
                player.setRole(Role.NORMAL);
                FactionsPlugin.getInstance().log(Level.WARNING, String.format("Player %s had null role. Setting them to normal. This isn't good D:", player.getName()));
            }

            switch (player.getRole()) {
                case ADMIN:
                    admins.add(player);
                    break;

                case COLEADER:
                    coleaders.add(player);
                    break;

                case MODERATOR:
                    moderators.add(player);
                    break;

                case NORMAL:
                    normal.add(player);
                    break;

                case RECRUIT:
                    recruit.add(player);
                    break;
            }
        }

        ObjectList<FPlayer> ret = new ObjectArrayList<>(admins.size() + coleaders.size() + moderators.size() + normal.size() + recruit.size());
        ret.addAll(admins);
        ret.addAll(coleaders);
        ret.addAll(moderators);
        ret.addAll(normal);
        ret.addAll(recruit);
        return ret;
    }
}
