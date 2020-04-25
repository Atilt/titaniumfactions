package com.massivecraft.factions.data;

import com.massivecraft.factions.Faction;
import com.massivecraft.factions.Factions;
import com.massivecraft.factions.util.MiscUtil;
import com.massivecraft.factions.util.TL;
import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.libs.it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

import java.util.List;
import java.util.Set;

public abstract class MemoryFactions extends Factions {

    public final Int2ObjectOpenHashMap<Faction> factions = new Int2ObjectOpenHashMap<>();
    public int nextId = 1;

    public int load() {
        // Make sure the default neutral factions exists

        Faction wilderness = factions.computeIfAbsent(0, id -> generateFactionObject("0"));
        wilderness.setTag(TL.WILDERNESS.toString());
        wilderness.setDescription(TL.WILDERNESS_DESCRIPTION.toString());

        Faction safezone = factions.computeIfAbsent(-1, s -> generateFactionObject("-1"));
        safezone.setTag(TL.SAFEZONE.toString().replace(" ", ""));
        safezone.setDescription(TL.SAFEZONE_DESCRIPTION.toString());

        Faction warzone = factions.computeIfAbsent(-2, s -> generateFactionObject("-2"));
        warzone.setTag(TL.WARZONE.toString().replace(" ", ""));
        warzone.setDescription(TL.WARZONE_DESCRIPTION.toString());

        return 0;
    }

    @Deprecated
    @Override
    public Faction getFactionById(String id) {
        return getFactionById(Integer.parseInt(id));
    }

    @Override
    public Faction getFactionById(int id) {
        return factions.get(id);
    }

    @Deprecated
    public abstract Faction generateFactionObject(String string);

    public abstract Faction generateFactionObject(int id);

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
            String candidate = ChatColor.stripColor(faction.getTag());
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

    @Deprecated
    @Override
    public boolean isValidFactionId(String id) {
        return isValidFactionId(Integer.parseInt(id));
    }

    @Override
    public boolean isValidFactionId(int id) {
        return this.factions.containsKey(id);
    }

    public Faction createFaction() {
        Faction faction = generateFactionObject();
        factions.put(faction.getIdRaw(), faction);
        return faction;
    }

    public Set<String> getFactionTags() {
        ObjectSet<String> tags = new ObjectOpenHashSet<>(factions.size());
        for (Faction faction : factions.values()) {
            tags.add(faction.getTag());
        }
        return tags;
    }

    public abstract Faction generateFactionObject();

    @Deprecated
    @Override
    public void removeFaction(String id) {
        removeFaction(Integer.parseInt(id));
    }

    @Override
    public void removeFaction(int id) {
        factions.remove(id).remove();
    }

    @Override
    public List<Faction> getAllFactions() {
        return new ObjectArrayList<>(factions.values());
    }

    @Override
    public Faction getNone() {
        return factions.get(0);
    }

    @Override
    public Faction getWilderness() {
        return factions.get(0);
    }

    @Override
    public Faction getSafeZone() {
        return factions.get(-1);
    }

    @Override
    public Faction getWarZone() {
        return factions.get(-2);
    }

    public abstract void convertFrom(MemoryFactions old, BooleanConsumer finish);
}
