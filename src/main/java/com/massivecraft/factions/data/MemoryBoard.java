package com.massivecraft.factions.data;

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
import com.massivecraft.factions.util.TextUtil;
import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.ObjectList;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import it.unimi.dsi.fastutil.objects.ObjectSets;
import net.kyori.text.TextComponent;
import net.kyori.text.event.ClickEvent;
import net.kyori.text.event.HoverEvent;
import net.kyori.text.format.TextColor;
import org.bukkit.World;

import java.util.List;
import java.util.Set;


public abstract class MemoryBoard extends Board {

    private static final int NONE = -82102;

    //this is what's being used to determine each chunk's ownership.
    public static class MemoryBoardMap extends Object2IntOpenHashMap<FLocation> {
        private static final long serialVersionUID = -6689617828610585368L;


        //better implementation. setIdAt = 0.06%
        //this is what's being used to save in json
        //Multimap<FactionId, Chunk> aka Map<FactionId, Set<Chunk>>
        //
        private final Int2ObjectMap<ObjectSet<FLocation>> factionToLandMap = new Int2ObjectOpenHashMap<>();

        @Override
        public int put(FLocation fLocation, int factionId) {
            if (factionId == NONE) {
                throw new IllegalArgumentException("illegal faction set");
            }
            int previousValue = super.put(fLocation, factionId);
            if (previousValue != NONE) { //if there was a previous value, then remove it from the multimap.
                ObjectSet<FLocation> fLocations = factionToLandMap.remove(previousValue);
                if (fLocations != null) {
                    fLocations.remove(fLocation);
                }
            }
            factionToLandMap.computeIfAbsent(factionId, id -> new ObjectOpenHashSet<>()).add(fLocation);
            return previousValue;
        }

        @Override
        public int defaultReturnValue() {
            return NONE;
        }

        @Override
        public int removeInt(Object key) {
            int result = super.removeInt(key);
            if (result != NONE) {
                ObjectSet<FLocation> locations = this.factionToLandMap.get(result);
                locations.remove(key);
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
            return factionToLandMap.getOrDefault(factionId, ObjectSets.emptySet()).size();
        }

        @Deprecated
        public void removeFaction(String factionId) {
            removeFaction(Integer.parseInt(factionId));
        }

        public void removeFaction(int factionId) {
            ObjectSet<FLocation> fLocations = factionToLandMap.remove(factionId);
            if (fLocations == null) {
                return;
            }
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
            for (FLocation fLocation : fLocations) {
                super.removeInt(fLocation);
            }
        }
    }

    private static final transient char[] MAP_CHARS = "\\/#$%=&^ABCDEFGHJKLMNOPQRSTUVWXYZ1234567890abcdeghjmnopqrsuvwxyz?".toCharArray();

    public MemoryBoardMap flocationIds = new MemoryBoardMap();
    public static final int NO_ID = 0;

    @Deprecated
    public String getIdAt(FLocation flocation) {
        return Integer.toString(getIdRawAt(flocation));
    }


    @Override
    public int getIdRawAt(FLocation flocation) {
        return flocationIds.getOrDefault(flocation, NO_ID);
    }

    @Override
    public Faction getFactionAt(FLocation flocation) {
        return Factions.getInstance().getFactionById(getIdRawAt(flocation));
    }

    @Deprecated
    @Override
    public void setIdAt(String id, FLocation flocation) {
        setIdAt(Integer.parseInt(id), flocation);
    }

    @Override
    public void setIdAt(int id, FLocation flocation) {
        Faction faction = clearOwnershipAt(flocation);
        if (id == NO_ID && faction != null) {
            removeAt(faction, flocation);
        }
        flocationIds.put(flocation, id);
    }

    public void setFactionAt(Faction faction, FLocation flocation) {
        setIdAt(faction.getIdRaw(), flocation);
    }

    public void removeAt(Faction faction, FLocation flocation) {
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
        flocationIds.removeInt(flocation);
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
        clearOwnershipAt(faction, flocation);
        flocationIds.removeInt(flocation);
    }

    @Deprecated
    public Set<FLocation> getAllClaims(String factionId) {
        return getAllClaims(Integer.parseInt(factionId));
    }

    @Override
    public Set<FLocation> getAllClaims(int factionId) {
        return this.flocationIds.factionToLandMap.getOrDefault(factionId, ObjectSets.emptySet());
    }

    @Override
    public Set<FLocation> getAllClaims(Faction faction) {
        return getAllClaims(faction.getIdRaw());
    }

    // not to be confused with claims, ownership referring to further member-specific ownership of a claim
    private Faction clearOwnershipAt(Faction faction, FLocation flocation) {
        if (faction != null && faction.isNormal()) {
            faction.clearClaimOwnership(flocation);
            return faction;
        }
        return null;
    }

    @Override
    public Faction clearOwnershipAt(FLocation flocation) {
        return clearOwnershipAt(getFactionAt(flocation), flocation);
    }

    @Override
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
        clean(factionId);
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
        clean(Integer.parseInt(factionId));
    }

    public void clean(int factionId) {
        if (LWC.getEnabled() && FactionsPlugin.getInstance().conf().lwc().isResetLocksOnUnclaim()) {
            for (FLocation fLocation : flocationIds.factionToLandMap.getOrDefault(factionId, ObjectSets.emptySet())) {
                LWC.clearAllLocks(fLocation);
            }
        }

        flocationIds.removeFaction(factionId);
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
                Faction other = getFactionAt(flocation.getRelative(x, z));
                if (other.isNormal() && other != faction) {
                    return true;
                }
            }
        }
        return false;
    }

    public void clean() {
        ObjectIterator<Object2IntMap.Entry<FLocation>> iter = flocationIds.object2IntEntrySet().fastIterator();
        while (iter.hasNext()) {
            Object2IntMap.Entry<FLocation> entry = iter.next();
            if (!Factions.getInstance().isValidFactionId(entry.getIntValue())) {
                if (LWC.getEnabled() && FactionsPlugin.getInstance().conf().lwc().isResetLocksOnUnclaim()) {
                    LWC.clearAllLocks(entry.getKey());
                }
                FactionsPlugin.getInstance().getPluginLogger().info(" == Claims: Removed" + entry.getIntValue() + " from " + entry.getKey());
                iter.remove();
            }
        }
    }

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
        int ret = 0;
        for (Object2IntMap.Entry<FLocation> entry : flocationIds.object2IntEntrySet()) {
            if (entry.getIntValue() == faction.getIdRaw() && entry.getKey().getWorldName().equals(worldName)) {
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
    // H x W
    //17 and 49
    //or 17 and 46
    public List<TextComponent> getMap(FPlayer fplayer, FLocation flocation, double degrees) {
        ObjectList<TextComponent> lines = new ObjectArrayList<>(3);

        lines.add(TextComponent.of(TextUtil.titleize("(" + flocation.getCoordString() + ") " + this.getFactionAt(flocation).getTag(fplayer))));

        List<TextComponent> compass = AsciiCompass.get((float) degrees);

        int height = fplayer.getMapHeight();
        int width = FactionsPlugin.getInstance().conf().map().getWidth();

        int fixedWidth = width - 2;

        FLocation start = flocation.getRelative(-width / 2, -height / 2);

        if (FactionsPlugin.getInstance().conf().map().isShowFactionKey()) {
            height--;
        }
        Object2ObjectMap<String, String> fList = new Object2ObjectOpenHashMap<>();
        int charIdx = 0;
        for (int y = 0; y < height; y++) {
            TextComponent.Builder row = TextComponent.builder();
            for (int x = 0; x < (y < 3 ? fixedWidth : width); x++) {
                if (y < 3 && x == 0) {
                    row.append(compass.get(y));
                    continue;
                }
                //append the line like "-" or whatever
                FLocation relative = start.getRelative(x, y);
                if (relative.equals(flocation)) {
                    row.append(TextComponent.of("+").color(TextColor.AQUA).hoverEvent(HoverEvent.showText(TL.CLAIM_YOUAREHERE.toComponent())));
                    continue;
                }
                Faction found = this.getFactionAt(relative);
                if (found.isWilderness()) {
                    row.append(TextComponent.of("-").color(TextUtil.kyoriColor(FactionsPlugin.getInstance().conf().colors().factions().getWilderness())).hoverEvent(HoverEvent.showText(TL.WILDERNESS.toComponent())));
                    continue;
                }
                if (found.isSafeZone()) {
                    row.append(TextComponent.of("+").color(TextUtil.kyoriColor(FactionsPlugin.getInstance().conf().colors().factions().getSafezone())).hoverEvent(HoverEvent.showText(TL.SAFEZONE.toComponent())));
                    continue;
                }
                if (found.isWarZone()) {
                    row.append(TextComponent.of("+").color(TextUtil.kyoriColor(FactionsPlugin.getInstance().conf().colors().factions().getWarzone())).hoverEvent(HoverEvent.showText(TL.WARZONE.toComponent())));
                    continue;
                }
                Relation relation = fplayer.getRelationTo(found);
                if (fplayer.getFactionIdRaw() == found.getIdRaw() || relation.isAtLeast(Relation.ALLY) || (FactionsPlugin.getInstance().conf().map().isShowNeutralFactionsOnMap() && relation == Relation.NEUTRAL) || (FactionsPlugin.getInstance().conf().map().isShowEnemyFactions() && relation == Relation.ENEMY) || (FactionsPlugin.getInstance().conf().map().isShowTruceFactions() && relation == Relation.TRUCE)) {
                    int incremented = charIdx++;
                    TextColor relationColor = TextUtil.kyoriColor(relation.getColor());
                    row.append(TextComponent.of(fList.computeIfAbsent(found.getTag(), c -> String.valueOf(MAP_CHARS[(incremented) % MAP_CHARS.length]))).color(relationColor).hoverEvent(HoverEvent.showText(TextComponent.of(found.getTag()).color(relationColor))).clickEvent(ClickEvent.runCommand("/f show " + found.getTag())));
                    continue;
                }
                row.append(TextComponent.of("-").color(TextColor.GRAY).hoverEvent(HoverEvent.showText(TextComponent.of(found.getTag()).color(TextColor.GRAY))).clickEvent(ClickEvent.runCommand("/f show " + found.getTag())));
            }
            lines.add(row.build());

            if (FactionsPlugin.getInstance().conf().map().isShowFactionKey() && y == height - 1) {
                TextComponent.Builder fRow = TextComponent.builder();
                for (Object2ObjectMap.Entry<String, String> entry : fList.object2ObjectEntrySet()) {
                    fRow.append(TextComponent.builder().content(entry.getValue() + ": " + entry.getKey() + " ").color(TextUtil.kyoriColor(fplayer.getRelationTo(Factions.getInstance().getByTag(entry.getKey())).getColor())).build());
                }
                lines.add(fRow.build());
            }
        }
        return lines;
    }

    public abstract void convertFrom(MemoryBoard old, BooleanConsumer finish);
}
