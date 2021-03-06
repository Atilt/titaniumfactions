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
import com.massivecraft.factions.math.FastMath;
import com.massivecraft.factions.perms.Permissible;
import com.massivecraft.factions.perms.PermissibleAction;
import com.massivecraft.factions.perms.Relation;
import com.massivecraft.factions.perms.Role;
import com.massivecraft.factions.struct.BanInfo;
import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.util.FastUUID;
import com.massivecraft.factions.util.LazyLocation;
import com.massivecraft.factions.util.MiscUtil;
import com.massivecraft.factions.util.RelationUtil;
import com.massivecraft.factions.util.TL;
import com.massivecraft.factions.util.TextUtil;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2BooleanOpenHashMap;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

public abstract class MemoryFaction implements Faction, EconomyParticipator {
    protected int id = -10;
    protected transient UUID econId;
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
    protected Map<FLocation, Set<UUID>> claimOwnership = new HashMap<>();
    protected transient Set<FPlayer> fplayers = new HashSet<>();
    protected Set<UUID> invites = new HashSet<>();
    protected Map<UUID, List<String>> announcements = new HashMap<>();
    protected final Map<String, LazyLocation> warps = new HashMap<>();
    protected final Map<String, String> warpPasswords = new HashMap<>();
    private long lastDeath;
    protected int maxVaults;
    protected Role defaultRole;
    protected final Map<Permissible, Map<PermissibleAction, Boolean>> permissions = new HashMap<>();
    protected final Map<Permissible, Map<PermissibleAction, Boolean>> permissionsOffline = new HashMap<>();
    protected final Set<BanInfo> bans = new HashSet<>();
    protected double dtr;
    protected long lastDTRUpdateTime;
    protected long frozenDTRUntilTime;
    protected int tntBank;

    @Override
    public Map<UUID, List<String>> getAnnouncements() {
        return this.announcements;
    }

    @Override
    public void addAnnouncement(FPlayer fPlayer, String msg) {
        announcements.computeIfAbsent(fPlayer.getId(), id -> new ArrayList<>()).add(msg);
    }

    @Override
    public boolean hasUnreadAnnouncements(FPlayer fPlayer) {
        return this.announcements.containsKey(fPlayer.getId());
    }

    @Deprecated
    @Override
    public void sendUnreadAnnouncements(FPlayer fPlayer) {
        Collection<String> messages = announcements.remove(fPlayer.getId());
        if (messages == null) {
            return;
        }
        fPlayer.msg(TL.FACTIONS_ANNOUNCEMENT_TOP);
        Player player = fPlayer.getPlayer();
        for (String s : messages) {
            fPlayer.sendMessage(player, s);
        }
        fPlayer.msg(TL.FACTIONS_ANNOUNCEMENT_BOTTOM);
    }

    @Override
    public void sendUnreadAnnouncements(Player player) {
        Collection<String> messages = announcements.remove(player.getUniqueId());
        if (messages == null) {
            return;
        }
        player.sendMessage(TL.FACTIONS_ANNOUNCEMENT_TOP.toString());
        for (String s : messages) {
            player.sendMessage(s);
        }
        player.sendMessage(TL.FACTIONS_ANNOUNCEMENT_BOTTOM.toString());
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
        this.bans.add(new BanInfo(banner.getId(), target.getId(), Instant.now().toEpochMilli()));
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
            setFoundedDate(Instant.now().toEpochMilli());
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

        sendMessage(TextUtil.parseTags("<b>Your faction home has been un-set since it is no longer in your territory."));
        this.home = null;
    }

    @Override
    public UUID getAccountId() {
        if (this.econId == null) {
            String attempt = "faction-" + this.getId();
            this.econId = UUID.nameUUIDFromBytes(attempt.substring(0, Math.min(32, attempt.length())).getBytes());
        }
        // We need to override the default money given to players.
        if (!Econ.hasAccount(this.econId)) {
            Econ.createAccount(this.econId);
            Econ.setBalance(this.econId, 0);
        }
        return this.econId;
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
        if (accessMap == null) {
            return permInfo.defaultAllowed();
        }
        Boolean access = accessMap.get(permissibleAction);
        return access == null ? permInfo.defaultAllowed() : access;
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

        permissionsMap.computeIfAbsent(permissible, pa -> new Object2BooleanOpenHashMap<>()).put(permissibleAction, value);
        return true;
    }

    @Override
    public void checkPerms() {
        if (this.permissions == null || this.permissions.isEmpty()) {
            this.resetPerms();
        }
    }

    @Override
    public void resetPerms() {
        // FactionsPlugin.getInstance().getPluginLogger().info(Level.WARNING, "Resetting permissions for Faction: " + tag);

        this.resetPerms(this.permissions, FactionsPlugin.getInstance().getConfigManager().getPermissionsConfig().getPermissions(), true);
        this.resetPerms(this.permissionsOffline, FactionsPlugin.getInstance().getConfigManager().getOfflinePermissionsConfig().getPermissions(), false);
    }

    private void resetPerms(Map<Permissible, Map<PermissibleAction, Boolean>> permissions, DefaultPermissionsConfig.Permissions defaults, boolean online) {
        permissions.clear();

        for (Relation relation : Relation.VALUES) {
            if (relation != Relation.MEMBER) {
                permissions.put(relation, new Object2BooleanOpenHashMap<>());
            }
        }
        if (online) {
            for (Role role : Role.VALUES) {
                if (role != Role.ADMIN) {
                    permissions.put(role, new Object2BooleanOpenHashMap<>());
                }
            }
        }

        for (Map.Entry<Permissible, Map<PermissibleAction, Boolean>> entry : permissions.entrySet()) {
            for (PermissibleAction permissibleAction : PermissibleAction.VALUES) {
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

    @Override
    public Map<Permissible, Map<PermissibleAction, Boolean>> getPermissions() {
        return Collections.unmodifiableMap(permissions);
    }

    public Map<Permissible, Map<PermissibleAction, Boolean>> getOfflinePermissions() {
        return Collections.unmodifiableMap(permissionsOffline);
    }

    @Override
    public Role getDefaultRole() {
        return this.defaultRole;
    }

    @Override
    public void setDefaultRole(Role role) {
        this.defaultRole = role;
    }

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
        this.foundedDate = Instant.now().toEpochMilli();
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
        fplayers = new HashSet<>();
        invites = old.invites;
        announcements = old.announcements;
        this.defaultRole = old.defaultRole;
        this.dtr = old.dtr;

        resetPerms(); // Reset on new Faction so it has default values.
    }

    @Override
    public boolean noPvPInTerritory() {
        return isSafeZone() || (peaceful && FactionsPlugin.getInstance().conf().factions().specialCase().isPeacefulTerritoryDisablePVP());
    }

    @Override
    public boolean noMonstersInTerritory() {
        return isSafeZone() ||
                (peaceful && FactionsPlugin.getInstance().conf().factions().specialCase().isPeacefulTerritoryDisableMonsters()) ||
                (isWarZone() && FactionsPlugin.getInstance().conf().factions().protection().isWarZonePreventMonsterSpawns());
    }

    @Override
    public boolean isNormal() {
        return !(this.isWilderness() || this.isSafeZone() || this.isWarZone());
    }

    @Override
    public boolean isNone() {
        return this.id == 0;
    }

    @Override
    public boolean isWilderness() {
        return this.id == 0;
    }

    @Override
    public boolean isSafeZone() {
        return this.id == -1;
    }

    @Override
    public boolean isWarZone() {
        return this.id == -2;
    }

    @Override
    public boolean isPlayerFreeType() {
        return this.isSafeZone() || this.isWarZone();
    }

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

    @Override
    public Relation getRelationWish(Faction otherFaction) {
        return this.relationWish.getOrDefault(otherFaction.getIdRaw(), Relation.fromString(FactionsPlugin.getInstance().conf().factions().other().getDefaultRelation()));
    }

    @Override
    public void setRelationWish(Faction otherFaction, Relation relation) {
        if (relation == Relation.NEUTRAL) {
            this.relationWish.remove(otherFaction.getIdRaw());
            return;
        }
        this.relationWish.put(otherFaction.getIdRaw(), relation);
    }

    @Override
    public int getRelationCount(Relation relation) {
        int count = 0;
        for (Faction faction : Factions.getInstance().getAllFactions()) {
            if (faction.getRelationTo(this) == relation) {
                count++;
            }
        }
        return count;
    }

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
        this.lastDTRUpdateTime = Instant.now().toEpochMilli();
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
        return Instant.now().toEpochMilli() < this.frozenDTRUntilTime;
    }


    @Override
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

    @Override
    public double getPowerMax() {
        if (this.hasPermanentPower()) {
            return this.getPermanentPower();
        }
        double ret = 0;
        for (FPlayer fplayer : this.fplayers) {
            ret += fplayer.getPowerMax();
        }
        if (FactionsPlugin.getInstance().conf().factions().landRaidControl().power().getFactionMax() > 0 && ret > FactionsPlugin.getInstance().conf().factions().landRaidControl().power().getFactionMax()) {
            ret = FactionsPlugin.getInstance().conf().factions().landRaidControl().power().getFactionMax();
        }
        return ret + this.powerBoost;
    }

    @Override
    public int getPowerRounded() {
        return FastMath.round(this.getPower());
    }

    @Override
    public int getPowerMaxRounded() {
        return FastMath.round(this.getPowerMax());
    }

    @Override
    public boolean hasLandInflation() {
        return FactionsPlugin.getInstance().getLandRaidControl().hasLandInflation(this);
    }

    @Override
    public Integer getPermanentPower() {
        return this.permanentPower;
    }

    @Override
    public void setPermanentPower(Integer permanentPower) {
        this.permanentPower = permanentPower;
    }

    @Override
    public boolean hasPermanentPower() {
        return this.permanentPower != null;
    }

    @Override
    public double getPowerBoost() {
        return this.powerBoost;
    }

    @Override
    public void setPowerBoost(double powerBoost) {
        this.powerBoost = powerBoost;
    }

    @Override
    public boolean isPowerFrozen() {
        int freezeSeconds = FactionsPlugin.getInstance().conf().factions().landRaidControl().power().getPowerFreeze();
        return freezeSeconds != 0 && Instant.now().toEpochMilli() - lastDeath < freezeSeconds * 1000;
    }

    @Override
    public int getLandRounded() {
        return Board.getInstance().getFactionCoordCount(this);
    }

    @Override
    public int getLandRoundedInWorld(String worldName) {
        return Board.getInstance().getFactionCoordCountInWorld(this, worldName);
    }

    @Override
    public int getTNTBank() {
        return this.tntBank;
    }

    @Override
    public void setTNTBank(int amount) {
        this.tntBank = amount;
    }

    @Override
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

    @Override
    public boolean addFPlayer(FPlayer fplayer) {
        return !this.isPlayerFreeType() && fplayers.add(fplayer);
    }

    @Override
    public boolean removeFPlayer(FPlayer fplayer) {
        return !this.isPlayerFreeType() && fplayers.remove(fplayer);
    }

    @Override
    public int getSize() {
        return this.fplayers.size();
    }

    @Override
    public Set<FPlayer> getFPlayers() {
        return Collections.unmodifiableSet(this.fplayers);
    }

    @Override
    public Set<FPlayer> getFPlayersWhereOnline(boolean online) {
        if (!this.isNormal()) {
            return Collections.emptySet();
        }
        Set<FPlayer> ret = new HashSet<>();
        if (online) {
            for (FPlayer onlinePlayer : FPlayers.getInstance().getOnlinePlayers()) {
                if (onlinePlayer.getFaction() == this) {
                    ret.add(onlinePlayer);
                }
            }
            return ret;
        }
        for (FPlayer fplayer : this.fplayers) {
            if (!fplayer.isOnline()) {
                ret.add(fplayer);
            }
        }
        return ret;
    }

    @Override
    public Set<FPlayer> getFPlayersWhereOnline(boolean online, FPlayer viewer) {
        if (!this.isNormal()) {
            return Collections.emptySet();
        }
        Set<FPlayer> ret = new HashSet<>();
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

    @Override
    public int getTotalFPlayersWhereOnline(boolean online, FPlayer viewer) {
        if (!this.isNormal()) {
            return 0;
        }
        if (!online) {
            return this.getSize() - this.getTotalOnline();
        }
        int count = 0;
        for (FPlayer viewed : FPlayers.getInstance().getOnlinePlayers()) {
            if (viewed.getFaction() != this) {
                continue;
            }
            Player viewerPlayer = viewer.getPlayer();
            if (viewerPlayer != null && viewerPlayer.canSee(viewed.getPlayer())) {
                count++;
            }
        }
        return count;
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
            return Collections.emptyList();
        }
        List<FPlayer> ret = new ArrayList<>();

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

    @Override
    public List<Player> getOnlinePlayers() {
        if (this.isPlayerFreeType()) {
            return Collections.emptyList();
        }
        List<Player> ret = new ArrayList<>();

        for (FPlayer onlinePlayer : FPlayers.getInstance().getOnlinePlayers()) {
            if (onlinePlayer.getFaction() != this) {
                continue;
            }
            ret.add(onlinePlayer.getPlayer());
        }
        return ret;
    }

    @Override
    public int getTotalOnline() {
        int online = 0;
        for (FPlayer onlinePlayer : FPlayers.getInstance().getOnlinePlayers()) {
            if (onlinePlayer.getFaction() != this) {
                continue;
            }
            online++;
        }
        return online;
    }

    // slightly faster check than getOnlinePlayers() if you just want to see if
    // there are any players online
    @Override
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
        return FactionsPlugin.getInstance().conf().factions().other().getConsiderFactionsReallyOfflineAfterXMinutes() > 0 && Instant.now().toEpochMilli() < lastPlayerLoggedOffTime + (FactionsPlugin.getInstance().conf().factions().other().getConsiderFactionsReallyOfflineAfterXMinutes() * 60000);
    }

    public void memberLoggedOff() {
        if (this.isNormal()) {
            lastPlayerLoggedOffTime = Instant.now().toEpochMilli();
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
                FactionsPlugin.getInstance().getPluginLogger().info("== Factions: " + this.getTag() + " (" + this.getId() + ") has been disbanded since it has no members left.");
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
            if (oldLeader == null) {
                return;
            }
            this.sendMessage(TextUtil.parseTags(oldLeader.getName() + "<i> has been removed. " + replacements.get(0).getName() + "<i> has been promoted as the new faction admin."));
        }
    }

    @Override
    public void msg(String msg) {
        this.sendMessage(msg);
    }

    @Override
    public void msg(TL translation) {
        this.sendMessage(translation.toString());
    }

    @Override
    public void msg(TL translation, TL toAppend) {
        this.sendMessage(translation.format(toAppend.toString()));
    }

    @Override
    public void msg(TL translation, String... args) {
        this.sendMessage(translation.format(args));
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
        return claimOwnership.size();
    }

    public boolean doesLocationHaveOwnersSet(FLocation loc) {
        if (claimOwnership.isEmpty()) {
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
        claimOwnership.computeIfAbsent(loc, fLocation -> new HashSet<>()).add(player.getId());
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

        StringBuilder ownerList = new StringBuilder(ownerData.size() * 2);

        for (UUID anOwnerData : ownerData) {
            if (ownerList.length() > 0) {
                ownerList.append(", ");
            }
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(anOwnerData);
            ownerList.append(offlinePlayer.getName() == null ? FastUUID.toString(anOwnerData) : offlinePlayer.getName());
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
        ((MemoryBoard) Board.getInstance()).clean(this.id);

        for (FPlayer fPlayer : fplayers) {
            fPlayer.resetFactionData(false);
        }
    }

    public Set<FLocation> getAllClaims() {
        return Board.getInstance().getAllClaims(this);
    }

    @Override
    public double getMoney() {
        return Econ.getBalance(this.getAccountId());
    }

    @Override
    public double getTotalMoney() {
        double money = this.getMoney();
        for (FPlayer fplayer : this.fplayers) {
            money += Econ.getBalance(fplayer.getAccountId());
        }
        return money;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MemoryFaction that = (MemoryFaction) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return id;
    }
}