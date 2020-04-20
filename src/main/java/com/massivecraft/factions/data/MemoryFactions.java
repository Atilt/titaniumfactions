package com.massivecraft.factions.data;

import com.massivecraft.factions.Faction;
import com.massivecraft.factions.Factions;
import com.massivecraft.factions.util.MiscUtil;
import com.massivecraft.factions.util.TL;
import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public abstract class MemoryFactions extends Factions {
    public final Map<String, Faction> factions = new ConcurrentHashMap<>();
    public int nextId = 1;

    public int load() {
        // Make sure the default neutral factions exists

        Faction wilderness = factions.computeIfAbsent("0", s -> generateFactionObject("0"));
        wilderness.setTag(TL.WILDERNESS.toString());
        wilderness.setDescription(TL.WILDERNESS_DESCRIPTION.toString());

        Faction safezone = factions.computeIfAbsent("-1", s -> generateFactionObject("-1"));
        safezone.setTag(TL.SAFEZONE.toString().replace(" ", ""));
        safezone.setDescription(TL.SAFEZONE_DESCRIPTION.toString());

        Faction warzone = factions.computeIfAbsent("-2", s -> generateFactionObject("-2"));
        warzone.setTag(TL.WARZONE.toString().replace(" ", ""));
        warzone.setDescription(TL.WARZONE_DESCRIPTION.toString());

        return 0;
    }

    public Faction getFactionById(String id) {
        return factions.get(id);
    }

    public abstract Faction generateFactionObject(String string);

    public Faction getByTag(String str) {
        String compStr = MiscUtil.getComparisonString(str);
        for (Faction faction : factions.values()) {
            if (faction.getComparisonTag().equals(compStr)) {
                return faction;
            }
        }
        return null;
    }

    public Faction getBestTagMatch(String start) {
        int best = 0;
        start = start.toLowerCase();
        int minlength = start.length();
        Faction bestMatch = null;
        for (Faction faction : factions.values()) {
            String candidate = faction.getTag();
            candidate = ChatColor.stripColor(candidate);
            if (candidate.length() < minlength) {
                continue;
            }
            if (!candidate.toLowerCase().startsWith(start)) {
                continue;
            }

            // The closer to zero the better
            int lendiff = candidate.length() - minlength;
            if (lendiff == 0) {
                return faction;
            }
            if (lendiff < best || best == 0) {
                best = lendiff;
                bestMatch = faction;
            }
        }

        return bestMatch;
    }

    public boolean isTagTaken(String str) {
        return this.getByTag(str) != null;
    }

    public boolean isValidFactionId(String id) {
        return factions.containsKey(id);
    }

    public Faction createFaction() {
        Faction faction = generateFactionObject();
        factions.put(faction.getId(), faction);
        return faction;
    }

    public Set<String> getFactionTags() {
        Set<String> tags = new HashSet<>();
        for (Faction faction : factions.values()) {
            tags.add(faction.getTag());
        }
        return tags;
    }

    public abstract Faction generateFactionObject();

    public void removeFaction(String id) {
        factions.remove(id).remove();
    }

    @Override
    public ArrayList<Faction> getAllFactions() {
        return new ArrayList<>(factions.values());
    }

    @Override
    public Faction getNone() {
        return factions.get("0");
    }

    @Override
    public Faction getWilderness() {
        return factions.get("0");
    }

    @Override
    public Faction getSafeZone() {
        return factions.get("-1");
    }

    @Override
    public Faction getWarZone() {
        return factions.get("-2");
    }

    public abstract void convertFrom(MemoryFactions old, BooleanConsumer finish);
}
