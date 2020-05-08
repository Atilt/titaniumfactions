package com.massivecraft.factions.data;

import com.massivecraft.factions.Board;
import com.massivecraft.factions.FLocation;
import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.Factions;
import com.massivecraft.factions.FactionsPlugin;
import com.massivecraft.factions.config.file.DefaultPermissionsConfig;
import com.massivecraft.factions.iface.EconomyParticipator;
import com.massivecraft.factions.iface.RelationParticipator;
import com.massivecraft.factions.integration.Econ;
import com.massivecraft.factions.integration.LWC;
import com.massivecraft.factions.landraidcontrol.DTRControl;
import com.massivecraft.factions.landraidcontrol.LandRaidControl;
import com.massivecraft.factions.perms.Permissible;
import com.massivecraft.factions.perms.PermissibleAction;
import com.massivecraft.factions.perms.Relation;
import com.massivecraft.factions.perms.Role;
import com.massivecraft.factions.struct.BanInfo;
import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.util.LazyLocation;
import com.massivecraft.factions.util.MiscUtil;
import com.massivecraft.factions.util.RelationUtil;
import com.massivecraft.factions.util.TL;
import com.massivecraft.factions.util.TextUtil;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import it.unimi.dsi.fastutil.objects.ObjectLists;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import it.unimi.dsi.fastutil.objects.ObjectSets;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

public abstract class MemoryFaction implements Faction, EconomyParticipator {
    protected int id = -10;
    protected boolean peacefulExplosionsEnabled;
    protected boolean permanent;
    protected String tag;
    protected String description;
    protected boolean open;
    protected boolean peaceful;
    protected Integer permanentPower;
    protected LazyLocation home;
    protected long foundedDate;
    protected transient long lastPlayerLoggedOffTime;
    protected double powerBoost;
    protected Map<Integer, Relation> relationWish = new Int2ObjectOpenHashMap<>();
    protected Map<FLocation, Set<UUID>> claimOwnership = new Object2ObjectOpenHashMap<>();
    protected transient ObjectSet<FPlayer> fplayers = new ObjectOpenHashSet<>();
    protected Set<UUID> invites = new ObjectOpenHashSet<>();
    protected Map<UUID, List<String>> announcements = new Object2ObjectOpenHashMap<>();
    protected Map<String, LazyLocation> warps = new Object2ObjectOpenHashMap<>();
    protected Map<String, String> warpPasswords = new Object2ObjectOpenHashMap<>();
    private long lastDeath;
    protected int maxVaults;
    protected Role defaultRole;
    protected Map<Permissible, Map<PermissibleAction, Boolean>> permissions = new Object2ObjectOpenHashMap<>();
    protected Map<Permissible, Map<PermissibleAction, Boolean>> permissionsOffline = new Object2ObjectOpenHashMap<>();
    protected Set<BanInfo> bans = new ObjectOpenHashSet<>();
    protected double dtr;
    protected long lastDTRUpdateTime;
    protected long frozenDTRUntilTime;
    protected int tntBank;

    @Override
    public Map<UUID, List<String>> getAnnouncements() {
        return this.announcements;
    }

    public void addAnnouncement(FPlayer fPlayer, String msg) {
        announcements.computeIfAbsent(fPlayer.getId(), id -> new ObjectArrayList<>()).add(msg);
    }

    public void sendUnreadAnnouncements(FPlayer fPlayer) {
        Collection<String> messages = announcements.remove(fPlayer.getId());
        if (messages == null) {
            return;
        }
        fPlayer.msg(TL.FACTIONS_ANNOUNCEMENT_TOP);
        for (String s : messages) {
            fPlayer.sendMessage(s);
        }
        fPlayer.msg(TL.FACTIONS_ANNOUNCEMENT_BOTTOM);
    }

    public void removeAnnouncements(FPlayer fPlayer) {
        announcements.remove(fPlayer.getId());
    }

    public Map<String, LazyLocation> getWarps() {
        return this.warps;
    }

    public LazyLocation getWarp(String name) {
        return this.warps.get(name);
    }

    public void setWarp(String name, LazyLocation loc) {
        this.warps.put(name, loc);
    }

    public boolean isWarp(String name) {
        return this.warps.containsKey(name);
    }

    public boolean removeWarp(String name) {
        warpPasswords.remove(name); // remove password no matter what.
        return warps.remove(name) != null;
    }

    public boolean isWarpPassword(String warp, String password) {
        return hasWarpPassword(warp) && warpPasswords.get(warp.toLowerCase()).equals(password);
    }

    public boolean hasWarpPassword(String warp) {
        return warpPasswords.containsKey(warp.toLowerCase());
    }

    public void setWarpPassword(String warp, String password) {
        warpPasswords.put(warp.toLowerCase(), password);
    }

    public void clearWarps() {
        warps.clear();
    }

    public int getMaxVaults() {
        return this.maxVaults;
    }

    public void setMaxVaults(int value) {
        this.maxVaults = value;
    }

    public Set<UUID> getInvites() {
        return invites;
    }

    public String getId() {
        return Integer.toString(getIdRaw());
    }

    @Override
    public int getIdRaw() {
        return this.id;
    }

    @Override
    public void setId(String id) {
        this.setId(Integer.parseInt(id));
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    public void invite(FPlayer fplayer) {
        this.invites.add(fplayer.getId());
    }

    public void deinvite(FPlayer fplayer) {
        this.invites.remove(fplayer.getId());
    }

    public boolean isInvited(FPlayer fplayer) {
        return this.invites.contains(fplayer.getId());
    }

    public void ban(FPlayer target, FPlayer banner) {
        this.bans.add(new BanInfo(banner.getId(), target.getId(), System.currentTimeMillis()));
    }

    public void unban(FPlayer player) {
        bans.removeIf(banInfo -> banInfo.getBanned().equals(player.getId()));
    }

    public boolean isBanned(FPlayer player) {
        for (BanInfo info : bans) {
            if (info.getBanned().equals(player.getId())) {
                return true;
            }
        }

        return false;
    }

    public Set<BanInfo> getBannedPlayers() {
        return this.bans;
    }

    public boolean getOpen() {
        return open;
    }

    public void setOpen(boolean isOpen) {
        open = isOpen;
    }

    public boolean isPeaceful() {
        return this.peaceful;
    }

    public void setPeaceful(boolean isPeaceful) {
        this.peaceful = isPeaceful;
    }

    public void setPeacefulExplosionsEnabled(boolean val) {
        peacefulExplosionsEnabled = val;
    }

    public boolean getPeacefulExplosionsEnabled() {
        return this.peacefulExplosionsEnabled;
    }

    public boolean noExplosionsInTerritory() {
        return this.peaceful && !peacefulExplosionsEnabled;
    }

    public boolean isPermanent() {
        return permanent || !this.isNormal();
    }

    public void setPermanent(boolean isPermanent) {
        permanent = isPermanent;
    }

    public String getTag() {
        return this.tag;
    }

    public String getTag(String prefix) {
        return prefix + this.tag;
    }

    public String getTag(Faction otherFaction) {
        if (otherFaction == null) {
            return getTag();
        }
        return this.getTag(this.getColorTo(otherFaction).toString());
    }

    public String getTag(FPlayer otherFplayer) {
        if (otherFplayer == null) {
            return getTag();
        }
        return this.getTag(this.getColorTo(otherFplayer).toString());
    }

    public void setTag(String str) {
        this.tag = FactionsPlugin.getInstance().conf().factions().other().isTagForceUpperCase() ? str.toUpperCase() : str;
    }

    public String getComparisonTag() {
        return MiscUtil.getComparisonString(this.tag);
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String value) {
        this.description = value;
    }

    public void setHome(Location home) {
        this.home = new LazyLocation(home);
    }

    public void delHome() {
        this.home = null;
    }

    public boolean hasHome() {
        return this.getHome() != null;
    }

    public Location getHome() {
        confirmValidHome();
        return (this.home != null) ? this.home.getLocation() : null;
    }

    public long getFoundedDate() {
        if (this.foundedDate == 0) {
            setFoundedDate(System.currentTimeMillis());
        }
        return this.foundedDate;
    }

    public void setFoundedDate(long newDate) {
        this.foundedDate = newDate;
    }

    public void confirmValidHome() {
        if (!FactionsPlugin.getInstance().conf().factions().homes().isMustBeInClaimedTerritory() || this.home == null || (this.home.getLocation() != null && Board.getInstance().getFactionAt(FLocation.wrap(this.home.getLocation())) == this)) {
            return;
        }

        msg("<b>Your faction home has been un-set since it is no longer in your territory.");
        this.home = null;
    }

    public UUID getAccountId() {
        String attempt = "faction-" + this.getId();
        UUID aid = UUID.nameUUIDFromBytes(attempt.substring(0, Math.min(32, attempt.length())).getBytes()); //cant use FastUUID because it only accepts real ids

        // We need to override the default money given to players.
        if (!Econ.hasAccount(aid)) {
            Econ.createAccount(aid);
            Econ.setBalance(aid, 0);
        }

        return aid;
    }

    public void setLastDeath(long time) {
        this.lastDeath = time;
    }

    public long getLastDeath() {
        return this.lastDeath;
    }

    public int getKills() {
        int kills = 0;
        for (FPlayer fp : getFPlayers()) {
            kills += fp.getKills();
        }

        return kills;
    }

    public int getDeaths() {
        int deaths = 0;
        for (FPlayer fp : getFPlayers()) {
            deaths += fp.getDeaths();
        }

        return deaths;
    }

    public boolean hasAccess(boolean online, Permissible permissible, PermissibleAction permissibleAction) {
        if (permissible == null || permissibleAction == null) {
            return false; // Fail in a safe way
        }
        if (permissible == Role.ADMIN) {
            return true;
        }

        Map<Permissible, Map<PermissibleAction, Boolean>> permissionsMap = this.getPermissionsMap(online);

        DefaultPermissionsConfig.Permissions.PermissiblePermInfo permInfo = this.getPermInfo(online, permissible, permissibleAction);
        if (permInfo == null) { // Not valid lookup, like a role-only lookup of a relation
            return false;
        }
        if (permInfo.isLocked()) { // Locked, so ignore the faction's setting
            return permInfo.defaultAllowed();
        }

        Map<PermissibleAction, Boolean> accessMap = permissionsMap.get(permissible);
        if (accessMap != null && accessMap.containsKey(permissibleAction)) {
            return accessMap.get(permissibleAction);
        }

        return permInfo.defaultAllowed(); // Fall back on default if something went wrong
    }

    public boolean isLocked(boolean online, Permissible permissible, PermissibleAction permissibleAction) {
        DefaultPermissionsConfig.Permissions.PermissiblePermInfo permInfo = this.getPermInfo(online, permissible, permissibleAction);
        if (permInfo == null) { // Not valid lookup, like a role-only lookup of a relation
            return false;
        }
        return permInfo.isLocked();
    }

    private DefaultPermissionsConfig.Permissions.PermissiblePermInfo getPermInfo(boolean online, Permissible permissible, PermissibleAction permissibleAction) {
        DefaultPermissionsConfig.Permissions defaultPermissions = this.getDefaultPermissions(online);
        if (permissibleAction.isFactionOnly()) {
            if (permissible instanceof Role) {
                return permissibleAction.getFactionOnly(defaultPermissions).get(permissible);
            } else {
                return null; // Can't do things faction only if not in the faction.
            }
        } else {
            return permissibleAction.getFullPerm(defaultPermissions).get(permissible);
        }
    }

    private Map<Permissible, Map<PermissibleAction, Boolean>> getPermissionsMap(boolean online) {
        if (online || !FactionsPlugin.getInstance().conf().factions().other().isSeparateOfflinePerms()) {
            return this.permissions;
        } else {
            return this.permissionsOffline;
        }
    }

    private DefaultPermissionsConfig.Permissions getDefaultPermissions(boolean online) {
        if (online || !FactionsPlugin.getInstance().conf().factions().other().isSeparateOfflinePerms()) {
            return FactionsPlugin.getInstance().getConfigManager().getPermissionsConfig().getPermissions();
        } else {
            return FactionsPlugin.getInstance().getConfigManager().getOfflinePermissionsConfig().getPermissions();
        }
    }

    /**
     * Get the Access of a player. Will use player's Role if they are a faction member. Otherwise, uses their Relation.
     *
     * @param player            player
     * @param permissibleAction permissible
     * @return player's access
     */
    public boolean hasAccess(FPlayer player, PermissibleAction permissibleAction) {
        if (player == null || permissibleAction == null) {
            return false; // Fail in a safe way
        }

        Permissible perm;
        boolean online = true;
        if (player.getFaction() == this) {
            perm = player.getRole();
        } else {
            perm = player.getFaction().getRelationTo(this);
            online = this.hasPlayersOnline();
        }

        return this.hasAccess(online, perm, permissibleAction);
    }

    public boolean setPermission(boolean online, Permissible permissible, PermissibleAction permissibleAction, boolean value) {
        Map<Permissible, Map<PermissibleAction, Boolean>> permissionsMap = this.getPermissionsMap(online);
        DefaultPermissionsConfig.Permissions defaultPermissions = this.getDefaultPermissions(online);

        DefaultPermissionsConfig.Permissions.PermissiblePermInfo permInfo = this.getPermInfo(online, permissible, permissibleAction);
        if (permInfo == null || permInfo.isLocked()) {
            return false; // Locked, can't continue;
        }

        Map<PermissibleAction, Boolean> accessMap = permissionsMap.get(permissible);
        if (accessMap == null) {
            accessMap = new HashMap<>();
        }

        accessMap.put(permissibleAction, value);
        return true;
    }

    public void checkPerms() {
        if (this.permissions == null || this.permissions.isEmpty()) {
            this.resetPerms();
        }
    }

    public void resetPerms() {
        // FactionsPlugin.getInstance().log(Level.WARNING, "Resetting permissions for Faction: " + tag);

        this.resetPerms(this.permissions, FactionsPlugin.getInstance().getConfigManager().getPermissionsConfig().getPermissions(), true);
        this.resetPerms(this.permissionsOffline, FactionsPlugin.getInstance().getConfigManager().getOfflinePermissionsConfig().getPermissions(), false);
    }

    private void resetPerms(Map<Permissible, Map<PermissibleAction, Boolean>> permissions, DefaultPermissionsConfig.Permissions defaults, boolean online) {
        permissions.clear();

        for (Relation relation : Relation.VALUES) {
            if (relation != Relation.MEMBER) {
                permissions.put(relation, new HashMap<>());
            }
        }
        if (online) {
            for (Role role : Role.VALUES) {
                if (role != Role.ADMIN) {
                    permissions.put(role, new HashMap<>());
                }
            }
        }

        for (Map.Entry<Permissible, Map<PermissibleAction, Boolean>> entry : permissions.entrySet()) {
            for (PermissibleAction permissibleAction : PermissibleAction.values()) {
                if (permissibleAction.isFactionOnly()) {
                    if (online && !(entry.getKey() instanceof Relation)) {
                        entry.getValue().put(permissibleAction, permissibleAction.getFactionOnly(defaults).get(entry.getKey()).defaultAllowed());
                    }
                } else {
                    entry.getValue().put(permissibleAction, permissibleAction.getFullPerm(defaults).get(entry.getKey()).defaultAllowed());
                }
            }
        }
    }

    /**
     * Read only map of Permissions.
     *
     * @return map of permissions
     */
    public Map<Permissible, Map<PermissibleAction, Boolean>> getPermissions() {
        return Collections.unmodifiableMap(permissions);
    }

    public Map<Permissible, Map<PermissibleAction, Boolean>> getOfflinePermissions() {
        return Collections.unmodifiableMap(permissionsOffline);
    }

    public Role getDefaultRole() {
        return this.defaultRole;
    }

    public void setDefaultRole(Role role) {
        this.defaultRole = role;
    }

    // -------------------------------------------- //
    // Construct
    // -------------------------------------------- //
    protected MemoryFaction() {
    }

    public MemoryFaction(int id) {
        this.id = id;
        this.open = FactionsPlugin.getInstance().conf().factions().other().isNewFactionsDefaultOpen();
        this.tag = "???";
        this.description = TL.GENERIC_DEFAULTDESCRIPTION.toString();
        this.lastPlayerLoggedOffTime = 0;
        this.peaceful = false;
        this.peacefulExplosionsEnabled = false;
        this.permanent = false;
        this.powerBoost = 0.0;
        this.foundedDate = System.currentTimeMillis();
        this.maxVaults = FactionsPlugin.getInstance().conf().playerVaults().getDefaultMaxVaults();
        this.defaultRole = FactionsPlugin.getInstance().conf().factions().other().getDefaultRole();
        this.dtr = FactionsPlugin.getInstance().conf().factions().landRaidControl().dtr().getStartingDTR();

        resetPerms(); // Reset on new Faction so it has default values.
    }

    public MemoryFaction(MemoryFaction old) {
        id = old.id;
        peacefulExplosionsEnabled = old.peacefulExplosionsEnabled;
        permanent = old.permanent;
        tag = old.tag;
        description = old.description;
        open = old.open;
        foundedDate = old.foundedDate;
        peaceful = old.peaceful;
        permanentPower = old.permanentPower;
        home = old.home;
        lastPlayerLoggedOffTime = old.lastPlayerLoggedOffTime;
        powerBoost = old.powerBoost;
        relationWish = old.relationWish;
        claimOwnership = old.claimOwnership;
        fplayers = new ObjectOpenHashSet<>();
        invites = old.invites;
        announcements = old.announcements;
        this.defaultRole = old.defaultRole;
        this.dtr = old.dtr;

        resetPerms(); // Reset on new Faction so it has default values.
    }

    // -------------------------------------------- //
    // Extra Getters And Setters
    // -------------------------------------------- //
    public boolean noPvPInTerritory() {
        return isSafeZone() || (peaceful && FactionsPlugin.getInstance().conf().factions().specialCase().isPeacefulTerritoryDisablePVP());
    }

    public boolean noMonstersInTerritory() {
        return isSafeZone() ||
                (peaceful && FactionsPlugin.getInstance().conf().factions().specialCase().isPeacefulTerritoryDisableMonsters()) ||
                (isWarZone() && FactionsPlugin.getInstance().conf().factions().protection().isWarZonePreventMonsterSpawns());
    }

    // -------------------------------
    // Understand the type
    // -------------------------------

    public boolean isNormal() {
        return !(this.isWilderness() || this.isSafeZone() || this.isWarZone());
    }

    public boolean isNone() {
        return this.id == 0;
    }

    public boolean isWilderness() {
        return this.id == 0;
    }

    public boolean isSafeZone() {
        return this.id == -1;
    }

    public boolean isWarZone() {
        return this.id == -2;
    }

    public boolean isPlayerFreeType() {
        return this.isSafeZone() || this.isWarZone();
    }

    // -------------------------------
    // Relation and relation colors
    // -------------------------------

    @Override
    public String describeTo(RelationParticipator that, boolean ucfirst) {
        return RelationUtil.describeThatToMe(this, that, ucfirst);
    }

    @Override
    public String describeTo(RelationParticipator that) {
        return RelationUtil.describeThatToMe(this, that);
    }

    @Override
    public Relation getRelationTo(RelationParticipator rp) {
        return RelationUtil.getRelationTo(this, rp);
    }

    @Override
    public Relation getRelationTo(RelationParticipator rp, boolean ignorePeaceful) {
        return RelationUtil.getRelationTo(this, rp, ignorePeaceful);
    }

    @Override
    public ChatColor getColorTo(RelationParticipator rp) {
        return RelationUtil.getColorOfThatToMe(this, rp);
    }

    public Relation getRelationWish(Faction otherFaction) {
        return this.relationWish.getOrDefault(otherFaction.getIdRaw(), Relation.fromString(FactionsPlugin.getInstance().conf().factions().other().getDefaultRelation()));
    }

    public void setRelationWish(Faction otherFaction, Relation relation) {
        if (relation == Relation.NEUTRAL) {
            this.relationWish.remove(otherFaction.getIdRaw());
            return;
        }
        this.relationWish.put(otherFaction.getIdRaw(), relation);
    }

    public int getRelationCount(Relation relation) {
        int count = 0;
        for (Faction faction : Factions.getInstance().getAllFactions()) {
            if (faction.getRelationTo(this) == relation) {
                count++;
            }
        }
        return count;
    }

    // ----------------------------------------------//
    // DTR
    // ----------------------------------------------//

    @Override
    public double getDTR() {
        LandRaidControl lrc = FactionsPlugin.getInstance().getLandRaidControl();
        if (lrc instanceof DTRControl) {
            ((DTRControl) lrc).updateDTR(this);
        }
        return this.dtr;
    }

    @Override
    public double getDTRWithoutUpdate() {
        return this.dtr;
    }

    @Override
    public void setDTR(double dtr) {
        this.dtr = dtr;
        this.lastDTRUpdateTime = System.currentTimeMillis();
    }

    @Override
    public long getLastDTRUpdateTime() {
        return this.lastDTRUpdateTime;
    }

    @Override
    public long getFrozenDTRUntilTime() {
        return this.frozenDTRUntilTime;
    }

    @Override
    public void setFrozenDTR(long time) {
        this.frozenDTRUntilTime = time;
    }

    @Override
    public boolean isFrozenDTR() {
        return System.currentTimeMillis() < this.frozenDTRUntilTime;
    }

    // ----------------------------------------------//
    // Power
    // ----------------------------------------------//
    @Deprecated
    public double getPower() {
        if (this.hasPermanentPower()) {
            return this.getPermanentPower();
        }


        double ret = 0;
        for (FPlayer fplayer : fplayers) {
            ret += fplayer.getPower();
        }
        if (FactionsPlugin.getInstance().conf().factions().landRaidControl().power().getFactionMax() > 0 && ret > FactionsPlugin.getInstance().conf().factions().landRaidControl().power().getFactionMax()) {
            ret = FactionsPlugin.getInstance().conf().factions().landRaidControl().power().getFactionMax();
        }
        return ret + this.powerBoost;
    }

    @Deprecated
    public double getPowerMax() {
        if (this.hasPermanentPower()) {
            return this.getPermanentPower();
        }

        double ret = 0;
        for (FPlayer fplayer : fplayers) {
            ret += fplayer.getPowerMax();
        }
        if (FactionsPlugin.getInstance().conf().factions().landRaidControl().power().getFactionMax() > 0 && ret > FactionsPlugin.getInstance().conf().factions().landRaidControl().power().getFactionMax()) {
            ret = FactionsPlugin.getInstance().conf().factions().landRaidControl().power().getFactionMax();
        }
        return ret + this.powerBoost;
    }

    public int getPowerRounded() {
        return (int) Math.round(this.getPower());
    }

    public int getPowerMaxRounded() {
        return (int) Math.round(this.getPowerMax());
    }

    public boolean hasLandInflation() {
        return FactionsPlugin.getInstance().getLandRaidControl().hasLandInflation(this);
    }

    public Integer getPermanentPower() {
        return this.permanentPower;
    }

    public void setPermanentPower(Integer permanentPower) {
        this.permanentPower = permanentPower;
    }

    public boolean hasPermanentPower() {
        return this.permanentPower != null;
    }

    public double getPowerBoost() {
        return this.powerBoost;
    }

    public void setPowerBoost(double powerBoost) {
        this.powerBoost = powerBoost;
    }

    public boolean isPowerFrozen() {
        int freezeSeconds = FactionsPlugin.getInstance().conf().factions().landRaidControl().power().getPowerFreeze();
        return freezeSeconds != 0 && System.currentTimeMillis() - lastDeath < freezeSeconds * 1000;
    }

    public int getLandRounded() {
        return Board.getInstance().getFactionCoordCount(this);
    }

    public int getLandRoundedInWorld(String worldName) {
        return Board.getInstance().getFactionCoordCountInWorld(this, worldName);
    }

    public int getTNTBank() {
        return this.tntBank;
    }

    public void setTNTBank(int amount) {
        this.tntBank = amount;
    }

    public void refreshFPlayers() {
        fplayers.clear();
        if (this.isPlayerFreeType()) {
            return;
        }

        for (FPlayer fplayer : FPlayers.getInstance().getAllFPlayers()) {
            if (fplayer.getFactionIdRaw() == id) {
                fplayers.add(fplayer);
            }
        }
    }

    public boolean addFPlayer(FPlayer fplayer) {
        return !this.isPlayerFreeType() && fplayers.add(fplayer);
    }

    public boolean removeFPlayer(FPlayer fplayer) {
        return !this.isPlayerFreeType() && fplayers.remove(fplayer);
    }

    public int getSize() {
        return fplayers.size();
    }

    public Set<FPlayer> getFPlayers() {
        return ObjectSets.unmodifiable(this.fplayers);
    }

    public Set<FPlayer> getFPlayersWhereOnline(boolean online) {
        if (!this.isNormal()) {
            return ObjectSets.emptySet();
        }
        ObjectSet<FPlayer> ret = new ObjectOpenHashSet<>();
        if (online) {
            for (FPlayer onlinePlayer : FPlayers.getInstance().getOnlinePlayers()) {
                if (onlinePlayer.getFaction() == this) {
                    ret.add(onlinePlayer);
                }
            }
            return ret;
        }
        for (FPlayer fplayer : fplayers) {
            if (!fplayer.isOnline()) {
                ret.add(fplayer);
            }
        }
        return ret;
    }

    public Set<FPlayer> getFPlayersWhereOnline(boolean online, FPlayer viewer) {
        if (!this.isNormal()) {
            return ObjectSets.emptySet();
        }
        ObjectSet<FPlayer> ret = new ObjectOpenHashSet<>();
        for (FPlayer viewed : FPlayers.getInstance().getOnlinePlayers()) {
            if (viewed.getFaction() != this) {
                continue;
            }
            if (!online) {
                ret.add(viewed);
            } else {
                Player viewerPlayer = viewer.getPlayer();
                if (viewerPlayer != null && viewerPlayer.canSee(viewed.getPlayer())) {
                    ret.add(viewed);
                }
            }
        }
        return ret;
    }

    public FPlayer getFPlayerAdmin() {
        if (!this.isNormal()) {
            return null;
        }

        for (FPlayer fplayer : fplayers) {
            if (fplayer.getRole() == Role.ADMIN) {
                return fplayer;
            }
        }
        return null;
    }

    @Override
    public List<FPlayer> getFPlayersWhereRole(Role role) {
        if (!this.isNormal()) {
            return ObjectLists.emptyList();
        }
        ObjectList<FPlayer> ret = new ObjectArrayList<>();

        for (FPlayer fplayer : this.fplayers) {
            if (fplayer.getRole() == role) {
                ret.add(fplayer);
            }
        }

        return ret;
    }

    @Override
    public int getTotalWhereRole(Role role) {
        if (!this.isNormal()) {
            return 0;
        }
        int amount = 0;
        for (FPlayer fplayer : this.fplayers) {
            if (fplayer.getRole() == role) {
                amount++;
            }
        }
        return amount;
    }

    public List<Player> getOnlinePlayers() {
        if (this.isPlayerFreeType()) {
            return ObjectLists.emptyList();
        }
        ObjectList<Player> ret = new ObjectArrayList<>();

        for (FPlayer onlinePlayer : FPlayers.getInstance().getOnlinePlayers()) {
            if (onlinePlayer.getFaction() != this) {
                continue;
            }
            ret.add(onlinePlayer.getPlayer());
        }
        return ret;
    }

    // slightly faster check than getOnlinePlayers() if you just want to see if
    // there are any players online
    public boolean hasPlayersOnline() {
        // only real factions can have players online, not safe zone / war zone
        if (this.isPlayerFreeType()) {
            return false;
        }
        for (FPlayer onlinePlayer : FPlayers.getInstance().getOnlinePlayers()) {
            if (onlinePlayer.getFaction() == this) {
                return true;
            }
        }

        // even if all players are technically logged off, maybe someone was on
        // recently enough to not consider them officially offline yet
        return FactionsPlugin.getInstance().conf().factions().other().getConsiderFactionsReallyOfflineAfterXMinutes() > 0 && System.currentTimeMillis() < lastPlayerLoggedOffTime + (FactionsPlugin.getInstance().conf().factions().other().getConsiderFactionsReallyOfflineAfterXMinutes() * 60000);
    }

    public void memberLoggedOff() {
        if (this.isNormal()) {
            lastPlayerLoggedOffTime = System.currentTimeMillis();
        }
    }

    // used when current leader is about to be removed from the faction;
    // promotes new leader, or disbands faction if no other members left
    public void promoteNewLeader() {
        if (!this.isNormal()) {
            return;
        }
        if (this.isPermanent() && FactionsPlugin.getInstance().conf().factions().specialCase().isPermanentFactionsDisableLeaderPromotion()) {
            return;
        }

        FPlayer oldLeader = this.getFPlayerAdmin();

        // get list of coleaders, or mods, or list of normal members if there are no moderators
        List<FPlayer> replacements = this.getFPlayersWhereRole(Role.COLEADER);
        if (replacements.isEmpty()) {
            replacements = this.getFPlayersWhereRole(Role.MODERATOR);
        }

        if (replacements.isEmpty()) {
            replacements = this.getFPlayersWhereRole(Role.NORMAL);
        }

        if (replacements.isEmpty()) { // faction admin  is the only  member; one-man  faction
            if (this.isPermanent()) {
                if (oldLeader != null) {
                    oldLeader.setRole(Role.NORMAL);
                }
                return;
            }

            // no members left and faction isn't permanent, so disband it
            if (FactionsPlugin.getInstance().conf().logging().isFactionDisband()) {
                FactionsPlugin.getInstance().log("The faction " + this.getTag() + " (" + this.getId() + ") has been disbanded since it has no members left.");
            }

            for (FPlayer fplayer : FPlayers.getInstance().getOnlinePlayers()) {
                fplayer.msg(TL.LEAVE_DISBANDED, this.getTag(fplayer));
            }

            Factions.getInstance().removeFaction(getIdRaw());
        } else { // promote new faction admin
            if (oldLeader != null) {
                oldLeader.setRole(Role.COLEADER);
            }
            replacements.get(0).setRole(Role.ADMIN);
            //TODO:TL
            this.msg("<i>Faction admin <h>%s<i> has been removed. %s<i> has been promoted as the new faction admin.", oldLeader == null ? "" : oldLeader.getName(), replacements.get(0).getName());
            FactionsPlugin.getInstance().log("Faction " + this.getTag() + " (" + this.getId() + ") admin was removed. Replacement admin: " + replacements.get(0).getName());
        }
    }

    // ----------------------------------------------//
    // Messages
    // ----------------------------------------------//
    public void msg(String message, Object... args) {
        message = TextUtil.parse(message, args);

        for (FPlayer fplayer : this.getFPlayersWhereOnline(true)) {
            fplayer.sendMessage(message);
        }
    }

    public void msg(TL translation, Object... args) {
        msg(translation.toString(), args);
    }

    public void sendMessage(String message) {
        for (FPlayer fplayer : this.getFPlayersWhereOnline(true)) {
            fplayer.sendMessage(message);
        }
    }

    public void sendMessage(List<String> messages) {
        for (FPlayer fplayer : this.getFPlayersWhereOnline(true)) {
            fplayer.sendMessage(messages);
        }
    }

    // ----------------------------------------------//
    // Ownership of specific claims
    // ----------------------------------------------//

    @Override
    public Map<FLocation, Set<UUID>> getClaimOwnership() {
        return claimOwnership;
    }

    public void clearAllClaimOwnership() {
        claimOwnership.clear();
    }

    public void clearClaimOwnership(FLocation loc) {
        if (LWC.getEnabled() && FactionsPlugin.getInstance().conf().lwc().isResetLocksOnUnclaim()) {
            LWC.clearAllLocks(loc);
        }
        claimOwnership.remove(loc);
    }

    public void clearClaimOwnership(FPlayer player) {
        if (id == -10) {
            return;
        }

        for (Entry<FLocation, Set<UUID>> entry : claimOwnership.entrySet()) {
            Collection<UUID> ownerData = entry.getValue();

            if (ownerData == null) {
                continue;
            }
            ownerData.removeIf(s -> s.equals(player.getId()));

            if (ownerData.isEmpty()) {
                if (LWC.getEnabled() && FactionsPlugin.getInstance().conf().lwc().isResetLocksOnUnclaim()) {
                    LWC.clearAllLocks(entry.getKey());
                }
                claimOwnership.remove(entry.getKey());
            }
        }
    }

    public int getCountOfClaimsWithOwners() {
        return claimOwnership.isEmpty() ? 0 : claimOwnership.size();
    }

    public boolean doesLocationHaveOwnersSet(FLocation loc) {
        if (claimOwnership.isEmpty() || !claimOwnership.containsKey(loc)) {
            return false;
        }

        Collection<UUID> ownerData = claimOwnership.get(loc);
        return ownerData != null && !ownerData.isEmpty();
    }

    public boolean isPlayerInOwnerList(FPlayer player, FLocation loc) {
        if (claimOwnership.isEmpty()) {
            return false;
        }
        Collection<UUID> ownerData = claimOwnership.get(loc);
        return ownerData != null && ownerData.contains(player.getId());
    }

    public void setPlayerAsOwner(FPlayer player, FLocation loc) {
        claimOwnership.computeIfAbsent(loc, fLocation -> new ObjectOpenHashSet<>()).add(player.getId());
    }

    public void removePlayerAsOwner(FPlayer player, FLocation loc) {
        Set<UUID> ownerData = claimOwnership.get(loc);
        if (ownerData == null) {
            return;
        }
        ownerData.remove(player.getId());
        claimOwnership.put(loc, ownerData);
    }

    public Collection<UUID> getOwnerList(FLocation loc) {
        return claimOwnership.get(loc);
    }

    public String getOwnerListString(FLocation loc) {
        Collection<UUID> ownerData = claimOwnership.get(loc);
        if (ownerData == null || ownerData.isEmpty()) {
            return "";
        }

        StringBuilder ownerList = new StringBuilder();

        for (UUID anOwnerData : ownerData) {
            if (ownerList.length() > 0) {
                ownerList.append(", ");
            }
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(anOwnerData);
            ownerList.append(offlinePlayer.getName() == null ? "null player" : offlinePlayer.getName());
        }
        return ownerList.toString();
    }

    public boolean playerHasOwnershipRights(FPlayer fplayer, FLocation loc) {
        // in own faction, with sufficient role or permission to bypass
        // ownership?
        if (fplayer.getFaction() == this && (fplayer.getRole().isAtLeast(FactionsPlugin.getInstance().conf().factions().ownedArea().isModeratorsBypass() ? Role.MODERATOR : Role.ADMIN) || Permission.OWNERSHIP_BYPASS.has(fplayer.getPlayer()))) {
            return true;
        }

        // make sure claimOwnership is initialized
        if (claimOwnership.isEmpty()) {
            return true;
        }

        // need to check the ownership list, then
        Collection<UUID> ownerData = claimOwnership.get(loc);

        // if no owner list, owner list is empty, or player is in owner list,
        // they're allowed
        return ownerData == null || ownerData.isEmpty() || ownerData.contains(fplayer.getId());
    }

    // ----------------------------------------------//
    // Persistance and entity management
    // ----------------------------------------------//
    public void remove() {
        if (Econ.shouldBeUsed()) {
            Econ.setBalance(getAccountId(), 0);
        }

        // Clean the board
        ((MemoryBoard) Board.getInstance()).clean(id);

        for (FPlayer fPlayer : fplayers) {
            fPlayer.resetFactionData(false);
        }
    }

    public Set<FLocation> getAllClaims() {
        return Board.getInstance().getAllClaims(this);
    }
}
