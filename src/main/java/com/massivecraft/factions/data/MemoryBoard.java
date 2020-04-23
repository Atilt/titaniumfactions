package com.massivecraft.factions.data;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.massivecraft.factions.Board;
import com.massivecraft.factions.FLocation;
import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.Factions;
import com.massivecraft.factions.FactionsPlugin;
import com.massivecraft.factions.integration.LWC;
import com.massivecraft.factions.perms.Relation;
import com.massivecraft.factions.util.AsciiCompass;
import com.massivecraft.factions.util.TL;
import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import mkremins.fanciful.FancyMessage;
import org.bukkit.ChatColor;
import org.bukkit.World;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;


public abstract class MemoryBoard extends Board {

    //this is what's being used to determine each chunk's ownership.
    public static class MemoryBoardMap extends Object2ObjectOpenHashMap<FLocation, Integer> {
        private static final long serialVersionUID = -6689617828610585368L;


        //better implementation. setIdAt = 0.06%
        //this is what's being used to save in json
        //Multimap<FactionId, Chunk> aka Map<FactionId, Set<Chunk>>
        //
        private final Multimap<Integer, FLocation> factionToLandMap = HashMultimap.create();

        @Override
        public Integer put(FLocation floc, Integer factionId) {
            Integer previousValue = super.put(floc, factionId);
            if (previousValue != null) { //if there was a previous value, then remove it from the multimap.
                factionToLandMap.remove(previousValue, floc);
            }

            factionToLandMap.put(factionId, floc);
            return previousValue;
        }

        @Override
        public Integer remove(Object key) {
            Integer result = super.remove(key);
            if (result != null) {
                FLocation floc = (FLocation) key;
                factionToLandMap.remove(result, floc);
            }

            return result;
        }

        @Override
        public void clear() {
            super.clear();
            factionToLandMap.clear();
        }

        @Deprecated
        public int getOwnedLandCount(String factionId) {
            return getOwnedLandCount(Integer.parseInt(factionId));
        }

        public int getOwnedLandCount(int factionId) {
            return factionToLandMap.get(factionId).size();
        }

        @Deprecated
        public void removeFaction(String factionId) {
            removeFactionRaw(Integer.parseInt(factionId));
        }

        public void removeFactionRaw(int factionId) {
            Collection<FLocation> fLocations = factionToLandMap.removeAll(factionId);
            for (FPlayer fPlayer : FPlayers.getInstance().getOnlinePlayers()) {
                if (fLocations.contains(fPlayer.getLastStoodAt())) {
                    if (FactionsPlugin.getInstance().conf().commands().fly().isEnable() && !fPlayer.isAdminBypassing() && fPlayer.isFlying()) {
                        fPlayer.setFlying(false);
                    }
                    if (fPlayer.isWarmingUp()) {
                        fPlayer.clearWarmup();
                        fPlayer.msg(TL.WARMUPS_CANCELLED);
                    }
                }
            }
            for (FLocation floc : fLocations) {
                super.remove(floc);
            }
        }
    }

    private static final char[] mapKeyChrs = "\\/#$%=&^ABCDEFGHJKLMNOPQRSTUVWXYZ1234567890abcdeghjmnopqrsuvwxyz?".toCharArray();

    public MemoryBoardMap flocationIds = new MemoryBoardMap();
    public static final int NO_ID = 0;

    @Deprecated
    public String getIdAt(FLocation flocation) {
        return Integer.toString(getIdAtRaw(flocation));
    }

    public int getIdAtRaw(FLocation flocation) {
        return flocationIds.getOrDefault(flocation, NO_ID);
    }

    @Override
    public Faction getFactionAt(FLocation flocation) {
        return Factions.getInstance().getFactionById(getIdAtRaw(flocation));
    }

    @Deprecated
    @Override
    public void setIdAt(String id, FLocation flocation) {
        setIdAt(Integer.parseInt(id), flocation);
    }

    @Override
    public void setIdAt(int id, FLocation flocation) {
        clearOwnershipAt(flocation);

        if (id == NO_ID) {
            removeAt(flocation);
        }

        flocationIds.put(flocation, id);
    }

    public void setFactionAt(Faction faction, FLocation flocation) {
        setIdAt(faction.getIdRaw(), flocation);
    }

    public void removeAt(FLocation flocation) {
        Faction faction = getFactionAt(flocation);
        faction.getWarps().values().removeIf(lazyLocation -> flocation.isInChunk(lazyLocation.getLocation()));

        for (FPlayer fPlayer : faction.getFPlayersWhereOnline(true)) {
            if (!fPlayer.getLastStoodAt().equals(flocation)) {
                continue;
            }
            if (!fPlayer.isAdminBypassing() && fPlayer.isFlying()) {
                fPlayer.setFlying(false);
            }
            if (fPlayer.isWarmingUp()) {
                fPlayer.clearWarmup();
                fPlayer.msg(TL.WARMUPS_CANCELLED);
            }
        }
        clearOwnershipAt(flocation);
        flocationIds.remove(flocation);
    }

    @Deprecated
    public Set<FLocation> getAllClaims(String factionId) {
        return getAllClaims(Integer.parseInt(factionId));
    }

    @Override
    public Set<FLocation> getAllClaims(int factionId) {
        ObjectSet<FLocation> locs = new ObjectOpenHashSet<>();
        for (Entry<FLocation, Integer> entry : flocationIds.entrySet()) {
            if (entry.getValue() == factionId) {
                locs.add(entry.getKey());
            }
        }
        return locs;
    }

    public Set<FLocation> getAllClaims(Faction faction) {
        return getAllClaims(faction.getId());
    }

    // not to be confused with claims, ownership referring to further member-specific ownership of a claim
    public void clearOwnershipAt(FLocation flocation) {
        Faction faction = getFactionAt(flocation);
        if (faction != null && faction.isNormal()) {
            faction.clearClaimOwnership(flocation);
        }
    }

    public void unclaimAll(String factionId) {
        unclaimAll(Integer.parseInt(factionId));
    }

    @Override
    public void unclaimAll(int factionId) {
        Faction faction = Factions.getInstance().getFactionById(factionId);
        if (faction != null && faction.isNormal()) {
            faction.clearAllClaimOwnership();
            faction.clearWarps();
        }
        cleanRaw(factionId);
    }

    public void unclaimAllInWorld(String factionId, World world) {
        unclaimAllInWorld(Integer.parseInt(factionId), world);
    }

    @Override
    public void unclaimAllInWorld(int factionId, World world) {
        for (FLocation loc : getAllClaims(factionId)) {
            if (loc.getWorldName().equals(world.getName())) {
                removeAt(loc);
            }
        }
    }

    @Deprecated
    public void clean(String factionId) {
        cleanRaw(Integer.parseInt(factionId));
    }

    public void cleanRaw(int factionId) {
        if (LWC.getEnabled() && FactionsPlugin.getInstance().conf().lwc().isResetLocksOnUnclaim()) {
            for (Entry<FLocation, Integer> entry : flocationIds.entrySet()) {
                if (entry.getValue() == factionId) {
                    LWC.clearAllLocks(entry.getKey());
                }
            }
        }

        flocationIds.removeFactionRaw(factionId);
    }

    // Is this coord NOT completely surrounded by coords claimed by the same faction?
    // Simpler: Is there any nearby coord with a faction other than the faction here?
    public boolean isBorderLocation(FLocation flocation) {
        Faction faction = getFactionAt(flocation);
        return faction != getFactionAt(flocation.getRelative(1, 0)) || faction != getFactionAt(flocation.getRelative(-1, 0)) || faction != getFactionAt(flocation.getRelative(0, 1)) || faction != getFactionAt(flocation.getRelative(0, -1));
    }

    // Is this coord connected to any coord claimed by the specified faction?
    public boolean isConnectedLocation(FLocation flocation, Faction faction) {
        return faction == getFactionAt(flocation.getRelative(1, 0)) || faction == getFactionAt(flocation.getRelative(-1, 0)) || faction == getFactionAt(flocation.getRelative(0, 1)) || faction == getFactionAt(flocation.getRelative(0, -1));
    }

    /**
     * Checks if there is another faction within a given radius other than Wilderness. Used for HCF feature that
     * requires a 'buffer' between factions.
     *
     * @param flocation - center location.
     * @param faction   - faction checking for.
     * @param radius    - chunk radius to check.
     * @return true if another Faction is within the radius, otherwise false.
     */
    public boolean hasFactionWithin(FLocation flocation, Faction faction, int radius) {
        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {
                if (x == 0 && z == 0) {
                    continue;
                }

                FLocation relative = flocation.getRelative(x, z);
                Faction other = getFactionAt(relative);

                if (other.isNormal() && other != faction) {
                    return true;
                }
            }
        }
        return false;
    }


    //----------------------------------------------//
    // Cleaner. Remove orphaned foreign keys
    //----------------------------------------------//

    public void clean() {
        Iterator<Entry<FLocation, Integer>> iter = flocationIds.entrySet().iterator();
        while (iter.hasNext()) {
            Entry<FLocation, Integer> entry = iter.next();
            if (!Factions.getInstance().isValidFactionId(entry.getValue())) {
                if (LWC.getEnabled() && FactionsPlugin.getInstance().conf().lwc().isResetLocksOnUnclaim()) {
                    LWC.clearAllLocks(entry.getKey());
                }
                FactionsPlugin.getInstance().log("Board cleaner removed " + entry.getValue() + " from " + entry.getKey());
                iter.remove();
            }
        }
    }

    //----------------------------------------------//
    // Coord count
    //----------------------------------------------//

    @Deprecated
    @Override
    public int getFactionCoordCount(String factionId) {
        return getFactionCoordCount(Integer.parseInt(factionId));
    }

    @Override
    public int getFactionCoordCount(int factionId) {
        return flocationIds.getOwnedLandCount(factionId);
    }

    @Override
    public int getFactionCoordCount(Faction faction) {
        return getFactionCoordCount(faction.getIdRaw());
    }

    public int getFactionCoordCountInWorld(Faction faction, String worldName) {
        int factionId = faction.getIdRaw();
        int ret = 0;
        for (Entry<FLocation, Integer> entry : flocationIds.entrySet()) {
            if (entry.getValue() == factionId && entry.getKey().getWorldName().equals(worldName)) {
                ret++;
            }
        }
        return ret;
    }

    //----------------------------------------------//
    // Map generation
    //----------------------------------------------//

    /**
     * The map is relative to a coord and a faction north is in the direction of decreasing x east is in the direction
     * of decreasing z
     */
    public List<FancyMessage> getMap(FPlayer fplayer, FLocation flocation, double inDegrees) {
        Faction faction = fplayer.getFaction();
        ObjectList<FancyMessage> ret = new ObjectArrayList<>();
        Faction factionLoc = getFactionAt(flocation);
        ret.add(new FancyMessage(FactionsPlugin.getInstance().txt().titleize("(" + flocation.getCoordString() + ") " + factionLoc.getTag(fplayer))));

        // Get the compass
        List<String> asciiCompass = AsciiCompass.getAsciiCompass(inDegrees, ChatColor.RED, FactionsPlugin.getInstance().txt().parse("<a>"));

        int halfWidth = FactionsPlugin.getInstance().conf().map().getWidth() / 2;
        // Use player's value for height
        int halfHeight = fplayer.getMapHeight() / 2;
        FLocation topLeft = flocation.getRelative(-halfWidth, -halfHeight);
        int width = halfWidth * 2 + 1;
        int height = halfHeight * 2 + 1;

        if (FactionsPlugin.getInstance().conf().map().isShowFactionKey()) {
            height--;
        }

        Object2ObjectMap<String, Character> fList = new Object2ObjectOpenHashMap<>();
        int chrIdx = 0;

        // For each row
        for (int dz = 0; dz < height; dz++) {
            // Draw and add that row
            FancyMessage row = new FancyMessage("");

            if (dz < 3) {
                row.then(asciiCompass.get(dz));
            }
            for (int dx = (dz < 3 ? 6 : 3); dx < width; dx++) {
                if (dx == halfWidth && dz == halfHeight) {
                    row.then("+").color(ChatColor.AQUA);
                    if (false) {
                        row.tooltip(TL.CLAIM_YOUAREHERE.toString());
                    }
                } else {
                    FLocation flocationHere = topLeft.getRelative(dx, dz);
                    Faction factionHere = getFactionAt(flocationHere);
                    Relation relation = fplayer.getRelationTo(factionHere);
                    if (factionHere.isWilderness()) {
                        row.then("-").color(FactionsPlugin.getInstance().conf().colors().factions().getWilderness());
                    } else if (factionHere.isSafeZone()) {
                        row.then("+").color(FactionsPlugin.getInstance().conf().colors().factions().getSafezone());
                    } else if (factionHere.isWarZone()) {
                        row.then("+").color(FactionsPlugin.getInstance().conf().colors().factions().getWarzone());
                    } else if (factionHere == faction || factionHere == factionLoc || relation.isAtLeast(Relation.ALLY) ||
                            (FactionsPlugin.getInstance().conf().map().isShowNeutralFactionsOnMap() && relation.equals(Relation.NEUTRAL)) ||
                            (FactionsPlugin.getInstance().conf().map().isShowEnemyFactions() && relation.equals(Relation.ENEMY)) ||
                            FactionsPlugin.getInstance().conf().map().isShowTruceFactions() && relation.equals(Relation.TRUCE)) {
                        if (!fList.containsKey(factionHere.getTag())) {
                            fList.put(factionHere.getTag(), mapKeyChrs[Math.min(chrIdx++, mapKeyChrs.length - 1)]);
                        }
                        char tag = fList.get(factionHere.getTag());
                        row.then(String.valueOf(tag)).color(factionHere.getColorTo(faction));
                    } else {
                        row.then("-").color(ChatColor.GRAY);
                    }
                }
            }
            ret.add(row);
        }

        // Add the faction key
        if (FactionsPlugin.getInstance().conf().map().isShowFactionKey()) {
            FancyMessage fRow = new FancyMessage("");
            for (String key : fList.keySet()) {
                Relation relation = fplayer.getRelationTo(Factions.getInstance().getByTag(key));
                fRow.then(String.format("%s: %s ", fList.get(key), key)).color(relation.getColor());
            }
            ret.add(fRow);
        }

        return ret;
    }

    public abstract void convertFrom(MemoryBoard old, BooleanConsumer finish);
}
