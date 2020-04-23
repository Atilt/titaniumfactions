package com.massivecraft.factions;

import com.massivecraft.factions.data.json.JSONBoard;
import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import mkremins.fanciful.FancyMessage;
import org.bukkit.World;

import java.util.List;
import java.util.Set;
import java.util.function.IntConsumer;


public abstract class Board {
    protected static Board instance = getBoardImpl();

    //----------------------------------------------//
    // Get and Set
    //----------------------------------------------//
    public abstract String getIdAt(FLocation flocation);

    private static Board getBoardImpl() {
        return new JSONBoard(); // TODO switch on configuration backend
    }

    public static Board getInstance() {
        return instance;
    }

    public abstract Faction getFactionAt(FLocation flocation);

    @Deprecated
    public abstract void setIdAt(String id, FLocation flocation);

    public abstract void setIdAt(int id, FLocation fLocation);

    public abstract void setFactionAt(Faction faction, FLocation flocation);

    public abstract void removeAt(FLocation flocation);

    @Deprecated
    public abstract Set<FLocation> getAllClaims(String factionId);

    public abstract Set<FLocation> getAllClaims(int factionId);

    public abstract Set<FLocation> getAllClaims(Faction faction);

    // not to be confused with claims, ownership referring to further member-specific ownership of a claim
    public abstract void clearOwnershipAt(FLocation flocation);

    @Deprecated
    public abstract void unclaimAll(String factionId);

    public abstract void unclaimAll(int factionId);

    @Deprecated
    public abstract void unclaimAllInWorld(String factionId, World world);

    public abstract void unclaimAllInWorld(int factionId, World world);

    // Is this coord NOT completely surrounded by coords claimed by the same faction?
    // Simpler: Is there any nearby coord with a faction other than the faction here?
    public abstract boolean isBorderLocation(FLocation flocation);

    // Is this coord connected to any coord claimed by the specified faction?
    public abstract boolean isConnectedLocation(FLocation flocation, Faction faction);

    public abstract boolean hasFactionWithin(FLocation flocation, Faction faction, int radius);

    public abstract void clean();

    @Deprecated
    public abstract int getFactionCoordCount(String factionId);

    public abstract int getFactionCoordCount(int factionId);

    public abstract int getFactionCoordCount(Faction faction);

    public abstract int getFactionCoordCountInWorld(Faction faction, String worldName);

    //----------------------------------------------//
    // Map generation
    //----------------------------------------------//

    /**
     * The map is relative to a coord and a faction north is in the direction of decreasing x east is in the direction
     * of decreasing z
     */
    public abstract List<FancyMessage> getMap(FPlayer fPlayer, FLocation flocation, double inDegrees);

    public abstract void forceSave(BooleanConsumer finish);

    public abstract void forceSave(boolean sync, BooleanConsumer finish);

    public abstract void load(IntConsumer loaded);
}
