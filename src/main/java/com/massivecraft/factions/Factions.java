package com.massivecraft.factions;

import com.massivecraft.factions.data.json.JSONFactions;
import it.unimi.dsi.fastutil.booleans.BooleanConsumer;

import java.util.List;
import java.util.Set;
import java.util.function.IntConsumer;

public abstract class Factions {
    protected static Factions instance = getFactionsImpl();

    @Deprecated
    public abstract Faction getFactionById(String id);

    public abstract Faction getFactionById(int id);

    public abstract Faction getByTag(String str);

    public abstract Faction getBestTagMatch(String start);

    public abstract boolean isTagTaken(String str);

    @Deprecated
    public abstract boolean isValidFactionId(String id);

    public abstract boolean isValidFactionId(int id);

    public abstract Faction createFaction();

    @Deprecated
    public abstract void removeFaction(String id);

    public abstract void removeFaction(int id);

    public abstract Set<String> getFactionTags();

    public abstract List<Faction> getAllFactions();

    public abstract List<Faction> getAllNormalFactions();

    @Deprecated
    public abstract Faction getNone();

    public abstract Faction getWilderness();

    public abstract Faction getSafeZone();

    public abstract Faction getWarZone();

    public abstract void forceSave(BooleanConsumer finish);

    public abstract void forceSave(boolean sync, BooleanConsumer finish);

    public static Factions getInstance() {
        return instance;
    }

    private static Factions getFactionsImpl() {
        // TODO switch on configuration backend
        return new JSONFactions();
    }

    public abstract void load(IntConsumer loaded);
}
