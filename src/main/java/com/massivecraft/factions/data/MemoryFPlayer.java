package com.massivecraft.factions.data;

import com.massivecraft.factions.*;
import com.massivecraft.factions.event.FPlayerLeaveEvent;
import com.massivecraft.factions.event.LandClaimEvent;
import com.massivecraft.factions.iface.EconomyParticipator;
import com.massivecraft.factions.iface.RelationParticipator;
import com.massivecraft.factions.integration.Econ;
import com.massivecraft.factions.integration.Essentials;
import com.massivecraft.factions.integration.LWC;
import com.massivecraft.factions.landraidcontrol.DTRControl;
import com.massivecraft.factions.landraidcontrol.PowerControl;
import com.massivecraft.factions.meta.scoreboards.FastBoard;
import com.massivecraft.factions.meta.scoreboards.SidebarProvider;
import com.massivecraft.factions.meta.scoreboards.SidebarTextProvider;
import com.massivecraft.factions.meta.scoreboards.sidebar.InfoSidebar;
import com.massivecraft.factions.perms.PermissibleAction;
import com.massivecraft.factions.perms.Relation;
import com.massivecraft.factions.perms.Role;
import com.massivecraft.factions.struct.ChatMode;
import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.tag.Tag;
import com.massivecraft.factions.util.*;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import net.kyori.text.TextComponent;
import net.kyori.text.adapter.bukkit.TextAdapter;
import org.bukkit.*;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;


/**
 * Logged in players always have exactly one FPlayer instance. Logged out players may or may not have an FPlayer
 * instance. They will always have one if they are part of a faction. This is because only players with a faction are
 * saved to disk (in order to not waste disk space).
 * <p/>
 * The FPlayer is linked to a minecraft player using the player name.
 * <p/>
 * The same instance is always returned for the same player. This means you can use the == operator. No .equals method
 * necessary.
 */

public abstract class MemoryFPlayer implements FPlayer {

    protected int factionId;
    protected Role role;
    protected String title;
    protected double power;
    protected double powerBoost;
    protected long lastPowerUpdateTime;
    protected long lastLoginTime;
    protected ChatMode chatMode;
    protected boolean ignoreAllianceChat = false;
    protected UUID id;
    protected String name;
    protected boolean monitorJoins;
    protected boolean spyingChat = false;
    protected boolean showScoreboard = true;
    protected WarmUpUtil.Warmup warmup;
    protected int warmupTask;
    protected boolean isAdminBypassing = false;
    protected int kills, deaths;
    protected boolean willAutoLeave = true;
    protected int mapHeight = 8; // default to old value
    protected boolean isFlying = false;
    protected boolean isAutoFlying = false;
    protected boolean flyTrailsState = false;
    protected String flyTrailsEffect = null;

    protected boolean seeingChunk = false;

    private transient SidebarTextProvider provider;
    private transient FastBoard scoreboard;
    protected transient FLocation lastStoodAt = FLocation.empty(); // Where did this player stand the last time we checked?
    protected transient boolean mapAutoUpdating;
    protected transient Faction autoClaimFor;
    protected transient boolean autoSafeZoneEnabled;
    protected transient boolean autoWarZoneEnabled;
    protected transient boolean loginPvpDisabled;
    protected transient long lastFrostwalkerMessage;
    protected transient boolean shouldTakeFallDamage = true;

    public void login() {
        this.kills = getPlayer().getStatistic(Statistic.PLAYER_KILLS);
        this.deaths = getPlayer().getStatistic(Statistic.DEATHS);
    }

    public void logout() {
        this.kills = getPlayer().getStatistic(Statistic.PLAYER_KILLS);
        this.deaths = getPlayer().getStatistic(Statistic.DEATHS);
    }

    public Faction getFaction() {
        if (this.factionId == -10) {
            this.factionId = MemoryBoard.NO_ID;
        }
        return Factions.getInstance().getFactionById(this.factionId);
    }

    @Deprecated
    @Override
    public String getFactionId() {
        return Integer.toString(this.factionId);
    }

    @Override
    public int getFactionIdRaw() {
        return this.factionId;
    }

    public boolean hasFaction() {
        return factionId != -10 && factionId != MemoryBoard.NO_ID;
    }

    public void setFaction(Faction faction) {
        Faction oldFaction = this.getFaction();
        if (oldFaction != null) {
            oldFaction.removeFPlayer(this);
        }
        faction.addFPlayer(this);
        this.factionId = faction.getIdRaw();
    }

    public void setMonitorJoins(boolean monitor) {
        this.monitorJoins = monitor;
    }

    public boolean isMonitoringJoins() {
        return this.monitorJoins;
    }

    public Role getRole() {
        // Hack to fix null roles..
        if (role == null) {
            this.role = Role.NORMAL;
        }

        return this.role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public double getPowerBoost() {
        return this.powerBoost;
    }

    public void setPowerBoost(double powerBoost) {
        this.powerBoost = powerBoost;
    }

    public boolean willAutoLeave() {
        return this.willAutoLeave;
    }

    public void setAutoLeave(boolean willLeave) {
        this.willAutoLeave = willLeave;
    }

    public long getLastFrostwalkerMessage() {
        return this.lastFrostwalkerMessage;
    }

    public void setLastFrostwalkerMessage() {
        this.lastFrostwalkerMessage = Instant.now().toEpochMilli();
    }

    public Faction getAutoClaimFor() {
        return autoClaimFor;
    }

    public void setAutoClaimFor(Faction faction) {
        this.autoClaimFor = faction;
        if (this.autoClaimFor != null) {
            // TODO: merge these into same autoclaim
            this.autoSafeZoneEnabled = false;
            this.autoWarZoneEnabled = false;
        }
    }

    public boolean isAutoSafeClaimEnabled() {
        return autoSafeZoneEnabled;
    }

    public void setIsAutoSafeClaimEnabled(boolean enabled) {
        this.autoSafeZoneEnabled = enabled;
        if (enabled) {
            this.autoClaimFor = null;
            this.autoWarZoneEnabled = false;
        }
    }

    public boolean isAutoWarClaimEnabled() {
        return autoWarZoneEnabled;
    }

    public void setIsAutoWarClaimEnabled(boolean enabled) {
        this.autoWarZoneEnabled = enabled;
        if (enabled) {
            this.autoClaimFor = null;
            this.autoSafeZoneEnabled = false;
        }
    }

    @Override
    public boolean isAdminBypassing() {
        return this.isAdminBypassing;
    }

    public boolean isVanished() {
        Player player = this.getPlayer();
        if (Essentials.isVanished(player)) {
            return true;
        }
        if (player != null) {
            for (MetadataValue metadataValue : player.getMetadata("vanished")) {
                if (metadataValue != null && metadataValue.asBoolean()) {
                    return true;
                }
            }
        }
        return false;
    }

    public void setIsAdminBypassing(boolean val) {
        this.isAdminBypassing = val;
    }

    public void setChatMode(ChatMode chatMode) {
        this.chatMode = chatMode;
    }

    public ChatMode getChatMode() {
        if (this.chatMode == null || this.factionId == -10 || this.factionId == MemoryBoard.NO_ID || !FactionsPlugin.getInstance().conf().factions().chat().isFactionOnlyChat()) {
            this.chatMode = ChatMode.PUBLIC;
        }
        return chatMode;
    }

    public void setIgnoreAllianceChat(boolean ignore) {
        this.ignoreAllianceChat = ignore;
    }

    public boolean isIgnoreAllianceChat() {
        return ignoreAllianceChat;
    }

    public void setSpyingChat(boolean chatSpying) {
        this.spyingChat = chatSpying;
    }

    public boolean isSpyingChat() {
        return spyingChat;
    }

    public UUID getAccountId() {
        return this.getId();
    }

    public MemoryFPlayer() {
    }

    public MemoryFPlayer(UUID id) {
        this.id = id;
        this.resetFactionData(false);
        this.power = FactionsPlugin.getInstance().conf().factions().landRaidControl().power().getPlayerStarting();
        this.lastPowerUpdateTime = Instant.now().toEpochMilli();
        this.lastLoginTime = Instant.now().toEpochMilli();
        this.mapAutoUpdating = false;
        this.autoClaimFor = null;
        this.autoSafeZoneEnabled = false;
        this.autoWarZoneEnabled = false;
        this.loginPvpDisabled = FactionsPlugin.getInstance().conf().factions().pvp().getNoPVPDamageToOthersForXSecondsAfterLogin() > 0;
        this.powerBoost = 0.0;
        this.kills = 0;
        this.deaths = 0;
        this.mapHeight = FactionsPlugin.getInstance().conf().map().getHeight();

        if (FactionsPlugin.getInstance().conf().factions().other().getNewPlayerStartingFactionID() != 0) {
            try {
                this.factionId = FactionsPlugin.getInstance().conf().factions().other().getNewPlayerStartingFactionID();
                if (!Factions.getInstance().isValidFactionId(this.factionId)) {
                    this.factionId = 0;
                }
            } catch (NumberFormatException exception) {
                this.factionId = 0;
            }
        }
    }

    public MemoryFPlayer(MemoryFPlayer other) {
        this.factionId = other.factionId;
        this.id = other.id;
        this.power = other.power;
        this.lastLoginTime = other.lastLoginTime;
        this.mapAutoUpdating = other.mapAutoUpdating;
        this.autoClaimFor = other.autoClaimFor;
        this.autoSafeZoneEnabled = other.autoSafeZoneEnabled;
        this.autoWarZoneEnabled = other.autoWarZoneEnabled;
        this.loginPvpDisabled = other.loginPvpDisabled;
        this.powerBoost = other.powerBoost;
        this.role = other.role;
        this.title = other.title;
        this.chatMode = other.chatMode;
        this.spyingChat = other.spyingChat;
        this.lastStoodAt = other.lastStoodAt;
        this.isAdminBypassing = other.isAdminBypassing;
        this.kills = other.kills;
        this.deaths = other.deaths;
        this.mapHeight = other.mapHeight;
    }

    public void resetFactionData(boolean doSpoutUpdate) {
        // clean up any territory ownership in old faction, if there is one
        if (factionId != -10 && Factions.getInstance().isValidFactionId(this.getFactionIdRaw())) {
            Faction currentFaction = this.getFaction();
            currentFaction.removeFPlayer(this);
            if (currentFaction.isNormal()) {
                currentFaction.clearClaimOwnership(this);
            }
        }
        this.factionId = 0; // The default neutral faction
        this.chatMode = ChatMode.PUBLIC;
        this.role = Role.NORMAL;
        this.title = "";
        this.autoClaimFor = null;
    }

    public void resetFactionData() {
        this.resetFactionData(true);
    }

    public long getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(long lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
        if (FactionsPlugin.getInstance().conf().factions().pvp().getNoPVPDamageToOthersForXSecondsAfterLogin() > 0) {
            this.loginPvpDisabled = true;
        }
    }

    public boolean isMapAutoUpdating() {
        return mapAutoUpdating;
    }

    public void setMapAutoUpdating(boolean mapAutoUpdating) {
        this.mapAutoUpdating = mapAutoUpdating;
    }

    public boolean hasLoginPvpDisabled() {
        if (!loginPvpDisabled) {
            return false;
        }
        if (this.lastLoginTime + (FactionsPlugin.getInstance().conf().factions().pvp().getNoPVPDamageToOthersForXSecondsAfterLogin() * 1000) < Instant.now().toEpochMilli()) {
            this.loginPvpDisabled = false;
            return false;
        }
        return true;
    }

    public FLocation getLastStoodAt() {
        return this.lastStoodAt;
    }

    public void setLastStoodAt(FLocation flocation) {
        this.lastStoodAt = flocation;
    }

    public String getTitle() {
        return this.hasFaction() ? title : TL.NOFACTION_PREFIX.toString();
    }

    public void setTitle(CommandSender sender, String title) {
        this.title = sender.hasPermission(Permission.TITLE_COLOR.node) ? TextUtil.parseColorBukkit(title) : title;
    }

    public String getName() {
        if (this.name == null) {
            OfflinePlayer offline = Bukkit.getOfflinePlayer(this.id);
            this.name = offline.getName() != null ? offline.getName() : FastUUID.toString(this.id);
        }
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTag() {
        return this.hasFaction() ? this.getFaction().getTag() : "";
    }

    // Base concatenations:

    public String getNameAndSomething(String something) {
        StringBuilder builder = new StringBuilder(this.role.getPrefix());
        if (something != null && !something.isEmpty()) {
            builder.append(something).append(" ");
        }
        return builder.append(this.getName()).toString();
    }

    public String getNameAndTitle() {
        return this.getNameAndSomething(this.getTitle());
    }

    public String getNameAndTag() {
        return this.getNameAndSomething(this.getTag());
    }

    // Colored concatenations:
    // These are used in information messages

    public String getNameAndTitle(Faction faction) {
        return this.getColorTo(faction) + this.getNameAndTitle();
    }

    public String getNameAndTitle(MemoryFPlayer fplayer) {
        return this.getColorTo(fplayer) + this.getNameAndTitle();
    }

    // Chat Tag:
    // These are injected into the format of global chat messages.

    public String getChatTag() {
        return this.hasFaction() ? String.format(FactionsPlugin.getInstance().conf().factions().chat().getTagFormat(), this.getRole().getPrefix() + this.getTag()) : TL.NOFACTION_PREFIX.toString();
    }

    // Colored Chat Tag
    public String getChatTag(Faction faction) {
        return this.hasFaction() ? this.getRelationTo(faction).getColor() + getChatTag() : TL.NOFACTION_PREFIX.toString();
    }

    @Override
    public String getChatTag(FPlayer fplayer) {
        return this.hasFaction() ? this.getRelationTo(fplayer).getColor() + getChatTag() : TL.NOFACTION_PREFIX.toString();
    }

    public int getKills() {
        return isOnline() ? getPlayer().getStatistic(Statistic.PLAYER_KILLS) : this.kills;
    }

    public int getDeaths() {
        return isOnline() ? getPlayer().getStatistic(Statistic.DEATHS) : this.deaths;

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

    public Relation getRelationToLocation() {
        return Board.getInstance().getFactionAt(FLocation.wrap(this)).getRelationTo(this);
    }

    @Override
    public ChatColor getColorTo(RelationParticipator rp) {
        return RelationUtil.getColorOfThatToMe(this, rp);
    }

    //----------------------------------------------//
    // Health
    //----------------------------------------------//
    public void heal(int amnt) {
        Player player = this.getPlayer();
        if (player != null) {
            player.setHealth(player.getHealth() + amnt);
        }
    }


    //----------------------------------------------//
    // Power
    //----------------------------------------------//
    public double getPower() {
        this.updatePower();
        return this.power;
    }

    public void alterPower(double delta) {
        this.power = Math.max(this.getPowerMin(), Math.min(this.getPowerMax(), this.power += delta));
    }

    public double getPowerMax() {
        return FactionsPlugin.getInstance().conf().factions().landRaidControl().power().getPlayerMax() + this.powerBoost;
    }

    public double getPowerMin() {
        return FactionsPlugin.getInstance().conf().factions().landRaidControl().power().getPlayerMin() + this.powerBoost;
    }

    public int getPowerRounded() {
        return FastMath.round(this.getPower());
    }

    public int getPowerMaxRounded() {
        return FastMath.round(this.getPowerMax());
    }

    public int getPowerMinRounded() {
        return FastMath.round(this.getPowerMin());
    }

    public void updatePower() {
        Player player = getPlayer();
        if (player == null) {
            losePowerFromBeingOffline();
            if (!FactionsPlugin.getInstance().conf().factions().landRaidControl().power().isRegenOffline()) {
                return;
            }
        } else {
            if (hasFaction() && getFaction().isPowerFrozen()) {
                return; // Don't let power regen if faction power is frozen.
            }
        }
        long now = Instant.now().toEpochMilli();
        this.lastPowerUpdateTime = now;

        if (player != null && player.isDead()) {
            return;  // don't let dead players regain power until they respawn
        }

        this.alterPower((now - this.lastPowerUpdateTime) * FactionsPlugin.getInstance().conf().factions().landRaidControl().power().getPowerPerMinute() / 60000);
    }

    public void losePowerFromBeingOffline() {
        long now = Instant.now().toEpochMilli();
        if (FactionsPlugin.getInstance().conf().factions().landRaidControl().power().getOfflineLossPerDay() > 0.0 && this.power > FactionsPlugin.getInstance().conf().factions().landRaidControl().power().getOfflineLossLimit()) {
            long millisPassed = now - this.lastPowerUpdateTime;

            double loss = millisPassed * FactionsPlugin.getInstance().conf().factions().landRaidControl().power().getOfflineLossPerDay() / (24 * 60 * 60 * 1000);
            if (this.power - loss < FactionsPlugin.getInstance().conf().factions().landRaidControl().power().getOfflineLossLimit()) {
                loss = this.power;
            }
            this.alterPower(-loss);
        }
        this.lastPowerUpdateTime = now;
    }

    public void onDeath() {
        if (hasFaction()) {
            getFaction().setLastDeath(Instant.now().toEpochMilli());
        }
    }

    public boolean isInOwnTerritory() {
        return Board.getInstance().getFactionAt(FLocation.wrap(this)) == this.getFaction();
    }

    public boolean isInOthersTerritory() {
        Faction factionHere = Board.getInstance().getFactionAt(FLocation.wrap(this));
        return factionHere != null && factionHere.isNormal() && factionHere != this.getFaction();
    }

    public boolean isInAllyTerritory() {
        return Board.getInstance().getFactionAt(FLocation.wrap(this)).getRelationTo(this).isAlly();
    }

    public boolean isInNeutralTerritory() {
        return Board.getInstance().getFactionAt(FLocation.wrap(this)).getRelationTo(this).isNeutral();
    }

    public boolean isInEnemyTerritory() {
        return Board.getInstance().getFactionAt(FLocation.wrap(this)).getRelationTo(this).isEnemy();
    }

    @Override
    public void sendFactionHereMessage(Faction from) {
        Player player = getPlayer();
        if (player == null) {
            return;
        }
        sendFactionHereMessage(from, player);
    }

    @Override
    public void sendFactionHereMessage(Faction from, Player player) {
        Faction toShow = Board.getInstance().getFactionAt(getLastStoodAt());

        if (FactionsPlugin.getInstance().conf().factions().enterTitles().isEnabled()) {
            int in = FactionsPlugin.getInstance().conf().factions().enterTitles().getFadeIn();
            int stay = FactionsPlugin.getInstance().conf().factions().enterTitles().getStay();
            int out = FactionsPlugin.getInstance().conf().factions().enterTitles().getFadeOut();

            String title = Tag.parsePlain(toShow, this, FactionsPlugin.getInstance().conf().factions().enterTitles().getTitle());
            String sub = TextUtil.parse(Tag.parsePlain(toShow, this, FactionsPlugin.getInstance().conf().factions().enterTitles().getSubtitle()));

            // We send null instead of empty because Spigot won't touch the title if it's null, but clears if empty.
            // We're just trying to be as unintrusive as possible.
            TitleAPI.send(player, title, sub, in, stay, out);
        }

        if (canSeeBoardFor(toShow)) {
            if (this.provider == SidebarProvider.DEFAULT_SIDEBAR) {
                this.setTextProvider(new InfoSidebar(toShow));
            } else {
                ((InfoSidebar) this.provider).setFaction(toShow);
            }
        } else if (this.provider != SidebarProvider.DEFAULT_SIDEBAR) {
            this.defaultTextProvider();
        }
        if (FactionsPlugin.getInstance().conf().factions().enterTitles().isAlsoShowChat()) {
            this.sendMessage(TextUtil.parse(TL.FACTION_LEAVE.format(from.getTag(this), toShow.getTag(this))));
        }
    }


    /**
     * Check if the scoreboard should be shown. Simple method to be used by above method.
     *
     * @param toShow Faction to be shown.
     * @return true if should show, otherwise false.
     */
    public boolean canSeeBoardFor(Faction toShow) {
        return this.showScoreboard &&
                toShow.isNormal() &&
                FactionsPlugin.getInstance().conf().scoreboard().info().isEnabled();
    }

    @Override
    public FastBoard getScoreboard() {
        return this.scoreboard;
    }

    @Override
    public SidebarTextProvider getScoreboardTextProvider() {
        return this.provider;
    }

    @Override
    public void setTextProvider(SidebarTextProvider provider) {
        this.provider = provider;
    }

    @Override
    public void defaultTextProvider() {
        this.provider = SidebarProvider.DEFAULT_SIDEBAR;
    }

    @Override
    public boolean showScoreboard() {
        return this.showScoreboard;
    }

    @Override
    public void setShowScoreboard(boolean show) {
        this.showScoreboard = show;
        if (show && SidebarProvider.get().track(this)) {
            this.scoreboard = new FastBoard(Bukkit.getPlayer(this.id));
            return;
        }
        if (SidebarProvider.get().untrack(this)) {
            this.scoreboard.delete();
            this.scoreboard = null;
        }
    }

    public void leave(boolean makePay) {
        Faction myFaction = this.getFaction();
        boolean econMakePay = makePay && Econ.shouldBeUsed() && !this.isAdminBypassing();

        if (myFaction == null) {
            resetFactionData();
            return;
        }

        boolean perm = myFaction.isPermanent();

        if (!perm && this.getRole() == Role.ADMIN) {
            if (myFaction.getSize() > 1 && myFaction.getTotalWhereRole(Role.ADMIN) == 1) {
                msg(TL.LEAVE_PASSADMIN);
                return;
            }
        }

        if (makePay && !FactionsPlugin.getInstance().getLandRaidControl().canLeaveFaction(this)) {
            return;
        }

        // if economy is enabled and they're not on the bypass list, make sure they can pay
        if (econMakePay && !Econ.hasAtLeast(this, FactionsPlugin.getInstance().conf().economy().getCostLeave(), TL.LEAVE_TOLEAVE.toString())) {
            return;
        }

        FPlayerLeaveEvent leaveEvent = new FPlayerLeaveEvent(this, myFaction, FPlayerLeaveEvent.PlayerLeaveReason.LEAVE);
        Bukkit.getPluginManager().callEvent(leaveEvent);
        if (leaveEvent.isCancelled()) {
            return;
        }

        // then make 'em pay (if applicable)
        if (econMakePay && !Econ.modifyMoney(this, -FactionsPlugin.getInstance().conf().economy().getCostLeave(), TL.LEAVE_TOLEAVE.toString(), TL.LEAVE_FORLEAVE.toString())) {
            return;
        }

        // Am I the last one in the faction?
        if (myFaction.getSize() == 1) {
            // Transfer all money
            if (Econ.shouldBeUsed()) {
                Econ.transferMoney(this, myFaction, this, Econ.getBalance(myFaction.getAccountId()));
            }
        }

        if (myFaction.isNormal()) {
            for (FPlayer fplayer : myFaction.getFPlayersWhereOnline(true)) {
                fplayer.msg(TL.LEAVE_LEFT, this.describeTo(fplayer, true), myFaction.describeTo(fplayer));
            }

            if (FactionsPlugin.getInstance().conf().logging().isFactionLeave()) {
                FactionsPlugin.getInstance().getPluginLogger().info(TL.LEAVE_LEFT.format(this.getName(), myFaction.getTag()));
            }
        }

        myFaction.removeAnnouncements(this);
        this.resetFactionData();
        setFlying(false, false);

        if (myFaction.isNormal() && !perm && myFaction.getFPlayers().isEmpty()) {
            // Remove this faction
            for (FPlayer fplayer : FPlayers.getInstance().getOnlinePlayers()) {
                fplayer.msg(TL.LEAVE_DISBANDED, myFaction.describeTo(fplayer, true));
            }

            Factions.getInstance().removeFaction(myFaction.getIdRaw());
            if (FactionsPlugin.getInstance().conf().logging().isFactionDisband()) {
                FactionsPlugin.getInstance().getPluginLogger().info(TL.LEAVE_DISBANDEDLOG.format(myFaction.getTag(), Integer.toString(myFaction.getIdRaw()), this.getName()));
            }
        }
    }

    public boolean canClaimForFaction(Faction forFaction) {
        return this.isAdminBypassing() || !forFaction.isWilderness() && (forFaction == this.getFaction() && this.getFaction().hasAccess(this, PermissibleAction.TERRITORY)) || (forFaction.isSafeZone() && Permission.MANAGE_SAFE_ZONE.has(getPlayer())) || (forFaction.isWarZone() && Permission.MANAGE_WAR_ZONE.has(getPlayer()));
    }

    // Not used
    public boolean canClaimForFactionAtLocation(Faction forFaction, Location location, boolean notifyFailure) {
        return canClaimForFactionAtLocation(forFaction, FLocation.wrap(location), notifyFailure);
    }

    public boolean canClaimForFactionAtLocation(Faction forFaction, FLocation flocation, boolean notifyFailure) {
        FactionsPlugin plugin = FactionsPlugin.getInstance();
        String denyReason = null;
        Faction myFaction = getFaction();
        Faction currentFaction = Board.getInstance().getFactionAt(flocation);
        int ownedLand = forFaction.getLandRounded();
        int factionBuffer = plugin.conf().factions().claims().getBufferZone();
        int worldBuffer = plugin.conf().worldBorder().getBuffer();

        if (plugin.conf().worldGuard().isChecking() && plugin.getWorldguard() != null && plugin.getWorldguard().checkForRegionsInChunk(flocation.getChunk())) {
            // Checks for WorldGuard regions in the chunk attempting to be claimed
            denyReason = TextUtil.parse(TL.CLAIM_PROTECTED.toString());
        } else if (plugin.conf().factions().claims().getWorldsNoClaiming().contains(flocation.getWorldName())) {
            // Cannot claim in this world
            denyReason = TextUtil.parse(TL.CLAIM_DISABLED.toString());
        } else if (this.isAdminBypassing()) {
            // Admin bypass
            return true;
        } else if (forFaction.isSafeZone() && Permission.MANAGE_SAFE_ZONE.has(getPlayer())) {
            // Safezone and can claim for such
            return true;
        } else if (forFaction.isWarZone() && Permission.MANAGE_WAR_ZONE.has(getPlayer())) {
            // Warzone and can claim for such
            return true;
        } else if (!forFaction.hasAccess(this, PermissibleAction.TERRITORY)) {
            // Lacking perms to territory claim
            denyReason = TL.CLAIM_CANTCLAIM.format(forFaction.describeTo(this));
        } else if (forFaction == currentFaction) {
            // Already owned by this faction, nitwit
            denyReason = TL.CLAIM_ALREADYOWN.format(forFaction.describeTo(this, true));
        } else if (forFaction.getSize() < plugin.conf().factions().claims().getRequireMinFactionMembers()) {
            // Need more members in order to claim land
            denyReason = TL.CLAIM_MEMBERS.format(Integer.toString(plugin.conf().factions().claims().getRequireMinFactionMembers()));
        } else if (currentFaction.isSafeZone()) {
            // Cannot claim safezone
            denyReason = TextUtil.parse(TL.CLAIM_SAFEZONE.toString());
        } else if (currentFaction.isWarZone()) {
            // Cannot claim warzone
            denyReason = TextUtil.parse(TL.CLAIM_WARZONE.toString());
        } else if (plugin.getLandRaidControl() instanceof PowerControl && ownedLand >= forFaction.getPowerRounded()) {
            // Already own at least as much land as power
            denyReason = TextUtil.parse(TL.CLAIM_POWER.toString());
        } else if (plugin.getLandRaidControl() instanceof DTRControl && ownedLand >= plugin.getLandRaidControl().getLandLimit(forFaction)) {
            // Already own at least as much land as land limit (DTR)
            denyReason = TextUtil.parse(TL.CLAIM_DTR_LAND.toString());
        } else if (plugin.conf().factions().claims().getLandsMax() != 0 && ownedLand >= plugin.conf().factions().claims().getLandsMax() && forFaction.isNormal()) {
            // Land limit reached
            denyReason = TextUtil.parse(TL.CLAIM_LIMIT.toString());
        } else if (currentFaction.getRelationTo(forFaction) == Relation.ALLY) {
            // // Can't claim ally
            denyReason = TextUtil.parse(TL.CLAIM_ALLY.toString());
        } else if (plugin.conf().factions().claims().isMustBeConnected() && !this.isAdminBypassing() && myFaction.getLandRoundedInWorld(flocation.getWorldName()) > 0 && !Board.getInstance().isConnectedLocation(flocation, myFaction) && (!plugin.conf().factions().claims().isCanBeUnconnectedIfOwnedByOtherFaction() || !currentFaction.isNormal())) {
            // Must be contiguous/connected
            if (plugin.conf().factions().claims().isCanBeUnconnectedIfOwnedByOtherFaction()) {
                denyReason = TextUtil.parse(TL.CLAIM_CONTIGIOUS.toString());
            } else {
                denyReason = TextUtil.parse(TL.CLAIM_FACTIONCONTIGUOUS.toString());
            }
        } else if (!(currentFaction.isNormal() && plugin.conf().factions().claims().isAllowOverClaimAndIgnoringBuffer() && currentFaction.hasLandInflation()) && factionBuffer > 0 && Board.getInstance().hasFactionWithin(flocation, myFaction, factionBuffer)) {
            // Too close to buffer
            denyReason = TextUtil.parse(TL.CLAIM_TOOCLOSETOOTHERFACTION.format(Integer.toString(factionBuffer)));
        } else if (flocation.isOutsideWorldBorder(worldBuffer)) {
            // Border buffer
            if (worldBuffer > 0) {
                denyReason = TextUtil.parse(TL.CLAIM_OUTSIDEBORDERBUFFER.format(Integer.toString(worldBuffer)));
            } else {
                denyReason = TextUtil.parse(TL.CLAIM_OUTSIDEWORLDBORDER.toString());
            }
        } else if (currentFaction.isNormal()) {
            if (myFaction.isPeaceful()) {
                // Cannot claim as peaceful
                denyReason = TL.CLAIM_PEACEFUL.format(currentFaction.getTag(this));
            } else if (currentFaction.isPeaceful()) {
                // Cannot claim from peaceful
                denyReason = TL.CLAIM_PEACEFULTARGET.format(currentFaction.getTag(this));
            } else if (!currentFaction.hasLandInflation()) {
                // Cannot claim other faction (perhaps based on power/land ratio)
                // TODO more messages WARN current faction most importantly
                denyReason = TL.CLAIM_THISISSPARTA.format(currentFaction.getTag(this));
            } else if (currentFaction.hasLandInflation() && !plugin.conf().factions().claims().isAllowOverClaim()) {
                // deny over claim when it normally would be allowed.
                denyReason = TextUtil.parse(TL.CLAIM_OVERCLAIM_DISABLED.toString());
            } else if (!Board.getInstance().isBorderLocation(flocation)) {
                denyReason = TextUtil.parse(TL.CLAIM_BORDER.toString());
            }
        }
        // TODO: Add more else if statements.

        if (notifyFailure && denyReason != null) {
            sendMessage(denyReason);
        }
        return denyReason == null;
    }

    public void canClaimForFactionAtLocationCompact(World world, Faction forFaction, FLocation flocation, MultiClaim multiClaim) {
        FactionsPlugin plugin = FactionsPlugin.getInstance();

        Faction myFaction = getFaction();
        int ownedLand = forFaction.getLandRounded();

        int factionBuffer = plugin.conf().factions().claims().getBufferZone();
        int worldBuffer = plugin.conf().worldBorder().getBuffer();

        Faction currentFaction = Board.getInstance().getFactionAt(flocation);

        if (plugin.conf().worldGuard().isChecking() && plugin.getWorldguard() != null && plugin.getWorldguard().checkForRegionsInChunk(flocation.getChunk())) {
            // Checks for WorldGuard regions in the chunk attempting to be claimed
            multiClaim.appendFailure(TL.CLAIM_PROTECTED.toString());
        } else if (plugin.conf().factions().claims().getWorldsNoClaiming().contains(flocation.getWorldName())) {
            // Cannot claim in this world
            multiClaim.appendFailure(TL.CLAIM_DISABLED.toString());
        } /*else if (this.isAdminBypassing()) {
            // Admin bypass
            return true;
        } else if (forFaction.isSafeZone() && Permission.MANAGE_SAFE_ZONE.has(getPlayer())) {
            // Safezone and can claim for such
            return true;
        } else if (forFaction.isWarZone() && Permission.MANAGE_WAR_ZONE.has(getPlayer())) {
            // Warzone and can claim for such
            return true;
        }*/ else if (!forFaction.hasAccess(this, PermissibleAction.TERRITORY)) {
            // Lacking perms to territory claim
            multiClaim.appendFailure(TL.CLAIM_CANTCLAIM.format(forFaction.describeTo(this)));
        } else if (forFaction == currentFaction) {
            // Already owned by this faction, nitwit
            multiClaim.appendFailure(TL.CLAIM_ALREADYOWN.format(forFaction.describeTo(this, true)));
        } else if (forFaction.getSize() < plugin.conf().factions().claims().getRequireMinFactionMembers()) {
            // Need more members in order to claim land
            multiClaim.appendFailure(TL.CLAIM_MEMBERS.format(Integer.toString(plugin.conf().factions().claims().getRequireMinFactionMembers())));
        } else if (currentFaction.isSafeZone()) {
            // Cannot claim safezone
            multiClaim.appendFailure(TextUtil.parse(TL.CLAIM_SAFEZONE.toString()));
        } else if (currentFaction.isWarZone()) {
            // Cannot claim warzone
            multiClaim.appendFailure(TextUtil.parse(TL.CLAIM_WARZONE.toString()));
        } else if (plugin.getLandRaidControl() instanceof PowerControl && ownedLand >= forFaction.getPowerRounded()) {
            // Already own at least as much land as power
            multiClaim.appendFailure(TextUtil.parse(TL.CLAIM_POWER.toString()));
        } else if (plugin.getLandRaidControl() instanceof DTRControl && ownedLand >= plugin.getLandRaidControl().getLandLimit(forFaction)) {
            // Already own at least as much land as land limit (DTR)
            multiClaim.appendFailure(TextUtil.parse(TL.CLAIM_DTR_LAND.toString()));
        } else if (plugin.conf().factions().claims().getLandsMax() != 0 && ownedLand >= plugin.conf().factions().claims().getLandsMax() && forFaction.isNormal()) {
            // Land limit reached
            multiClaim.appendFailure(TextUtil.parse(TL.CLAIM_LIMIT.toString()));
        } else if (currentFaction.getRelationTo(forFaction) == Relation.ALLY) {
            // // Can't claim ally
            multiClaim.appendFailure(TextUtil.parse(TL.CLAIM_ALLY.toString()));
        } else if (plugin.conf().factions().claims().isMustBeConnected() && !this.isAdminBypassing() && myFaction.getLandRoundedInWorld(flocation.getWorldName()) > 0 && !Board.getInstance().isConnectedLocation(flocation, myFaction) && (!plugin.conf().factions().claims().isCanBeUnconnectedIfOwnedByOtherFaction() || !currentFaction.isNormal())) {
            // Must be contiguous/connected
            if (plugin.conf().factions().claims().isCanBeUnconnectedIfOwnedByOtherFaction()) {
                multiClaim.appendFailure(TextUtil.parse(TL.CLAIM_CONTIGIOUS.toString()));
            } else {
                multiClaim.appendFailure(TextUtil.parse(TL.CLAIM_FACTIONCONTIGUOUS.toString()));
            }
        } else if (!(currentFaction.isNormal() && plugin.conf().factions().claims().isAllowOverClaimAndIgnoringBuffer() && currentFaction.hasLandInflation()) && factionBuffer > 0 && Board.getInstance().hasFactionWithin(flocation, myFaction, factionBuffer)) {
            // Too close to buffer
            multiClaim.appendFailure(TextUtil.parse(TL.CLAIM_TOOCLOSETOOTHERFACTION.format(Integer.toString(factionBuffer))));
        } else if (flocation.isOutsideWorldBorder(world, worldBuffer)) {
            // Border buffer
            if (worldBuffer > 0) {
                multiClaim.appendFailure(TextUtil.parse(TL.CLAIM_OUTSIDEBORDERBUFFER.format(Integer.toString(worldBuffer))));
            } else {
                multiClaim.appendFailure(TextUtil.parse(TL.CLAIM_OUTSIDEWORLDBORDER.toString()));
            }
        } else if (currentFaction.isNormal()) {
            if (myFaction.isPeaceful()) {
                // Cannot claim as peaceful
                multiClaim.appendFailure(TL.CLAIM_PEACEFUL.format(currentFaction.getTag(this)));
            } else if (currentFaction.isPeaceful()) {
                // Cannot claim from peaceful
                multiClaim.appendFailure(TL.CLAIM_PEACEFULTARGET.format(currentFaction.getTag(this)));
            } else if (!currentFaction.hasLandInflation()) {
                // Cannot claim other faction (perhaps based on power/land ratio)
                // TODO more messages WARN current faction most importantly
                multiClaim.appendFailure(TL.CLAIM_THISISSPARTA.format(currentFaction.getTag(this)));
            } else if (currentFaction.hasLandInflation() && !plugin.conf().factions().claims().isAllowOverClaim()) {
                // deny over claim when it normally would be allowed.
                multiClaim.appendFailure(TextUtil.parse(TL.CLAIM_OVERCLAIM_DISABLED.toString()));
            } else if (!Board.getInstance().isBorderLocation(flocation)) {
                multiClaim.appendFailure(TextUtil.parse(TL.CLAIM_BORDER.toString()));
            }
        }
    }

    public boolean attemptClaim(Faction forFaction, Location location, boolean notifyFailure) {
        return attemptClaim(forFaction, FLocation.wrap(location), notifyFailure);
    }

    @Override
    public void attemptClaimCompact(World world, Faction forFaction, FLocation flocation, MultiClaim multiClaim) {
        // notifyFailure is false if called by auto-claim; no need to notify on every failure for it
        // return value is false on failure, true on success

        int ownedLand = forFaction.getLandRounded();

        this.canClaimForFactionAtLocationCompact(world, forFaction, flocation, multiClaim);

        Faction currentFaction = Board.getInstance().getFactionAt(flocation);

        // if economy is enabled and they're not on the bypass list, make sure they can pay
        boolean mustPay = Econ.shouldBeUsed() && !this.isAdminBypassing() && !forFaction.isSafeZone() && !forFaction.isWarZone();
        double cost = 0.0;
        EconomyParticipator payee = null;
        if (mustPay) {
            cost = Econ.calculateClaimCost(ownedLand, currentFaction.isNormal());

            if (FactionsPlugin.getInstance().conf().economy().getClaimUnconnectedFee() != 0.0 && forFaction.getLandRoundedInWorld(flocation.getWorldName()) > 0 && !Board.getInstance().isConnectedLocation(flocation, forFaction)) {
                cost += FactionsPlugin.getInstance().conf().economy().getClaimUnconnectedFee();
            }

            if (FactionsPlugin.getInstance().conf().economy().isBankEnabled() && FactionsPlugin.getInstance().conf().economy().isBankFactionPaysLandCosts() && this.hasFaction() && this.getFaction().hasAccess(this, PermissibleAction.ECONOMY)) {
                payee = this.getFaction();
            } else {
                payee = this;
            }

            if (!Econ.hasAtLeast(payee, cost)) {
                multiClaim.appendFailure(TL.CLAIM_CLICK_TO_CLAIM.toString());
                return;
            }
        }

        LandClaimEvent claimEvent = new LandClaimEvent(flocation, forFaction, this);
        Bukkit.getPluginManager().callEvent(claimEvent);
        if (claimEvent.isCancelled()) {
            return;
        }

        // then make 'em pay (if applicable)
        if (mustPay && !Econ.modifyMoney(payee, -cost)) {
            multiClaim.appendFailure(TL.ECON_LOST_FAILURE.format(payee.describeTo(payee, true), Econ.moneyString(-cost), TL.CLAIM_TOCLAIM.toString()));
            return;
        }

        // Was an over claim
        if (mustPay && currentFaction.isNormal() && currentFaction.hasLandInflation()) {
            // Give them money for over claiming.
            Econ.modifyMoney(payee, FactionsPlugin.getInstance().conf().economy().getOverclaimRewardMultiplier());
        }

        if (LWC.getEnabled() && forFaction.isNormal() && FactionsPlugin.getInstance().conf().lwc().isResetLocksOnCapture()) {
            LWC.clearOtherLocks(flocation, this.getFaction());
        }

        // announce success
/*        ObjectSet<FPlayer> informTheseFPlayers = new ObjectOpenHashSet<>();
        informTheseFPlayers.add(this);
        informTheseFPlayers.addAll(forFaction.getFPlayersWhereOnline(true));
        for (FPlayer fp : informTheseFPlayers) {
            fp.msg(TL.CLAIM_CLAIMED, this.describeTo(fp, true), forFaction.describeTo(fp), currentFaction.describeTo(fp));
        }*/
        if (this.getFaction().isNormal() && Board.getInstance().getFactionAt(flocation) == forFaction) {
            return;
        }

        multiClaim.appendSuccess(TL.CLAIM_CLAIMED.format(this.describeTo(this, true), forFaction.describeTo(this), currentFaction.describeTo(this)));
        Board.getInstance().setFactionAt(forFaction, flocation);

        //temporary disable logging.
/*        if (FactionsPlugin.getInstance().conf().logging().isLandClaims()) {
            FactionsPlugin.getInstance().getPluginLogger().info(TL.CLAIM_CLAIMEDLOG.toString(), this.getName(), flocation.getCoordString(), forFaction.getTag());
        }*/
    }

    public boolean attemptClaim(Faction forFaction, FLocation flocation, boolean notifyFailure) {
        // notifyFailure is false if called by auto-claim; no need to notify on every failure for it
        // return value is false on failure, true on success

        int ownedLand = forFaction.getLandRounded();

        if (!this.canClaimForFactionAtLocation(forFaction, flocation, notifyFailure)) {
            return false;
        }
        Faction currentFaction = Board.getInstance().getFactionAt(flocation);

        // if economy is enabled and they're not on the bypass list, make sure they can pay
        boolean mustPay = Econ.shouldBeUsed() && !this.isAdminBypassing() && !forFaction.isSafeZone() && !forFaction.isWarZone();
        double cost = 0.0;
        EconomyParticipator payee = null;
        if (mustPay) {
            cost = Econ.calculateClaimCost(ownedLand, currentFaction.isNormal());

            if (FactionsPlugin.getInstance().conf().economy().getClaimUnconnectedFee() != 0.0 && forFaction.getLandRoundedInWorld(flocation.getWorldName()) > 0 && !Board.getInstance().isConnectedLocation(flocation, forFaction)) {
                cost += FactionsPlugin.getInstance().conf().economy().getClaimUnconnectedFee();
            }

            if (FactionsPlugin.getInstance().conf().economy().isBankEnabled() && FactionsPlugin.getInstance().conf().economy().isBankFactionPaysLandCosts() && this.hasFaction() && this.getFaction().hasAccess(this, PermissibleAction.ECONOMY)) {
                payee = this.getFaction();
            } else {
                payee = this;
            }

            if (!Econ.hasAtLeast(payee, cost, TL.CLAIM_TOCLAIM.toString())) {
                return false;
            }
        }

        LandClaimEvent claimEvent = new LandClaimEvent(flocation, forFaction, this);
        Bukkit.getPluginManager().callEvent(claimEvent);
        if (claimEvent.isCancelled()) {
            return false;
        }

        // then make 'em pay (if applicable)
        if (mustPay && !Econ.modifyMoney(payee, -cost, TL.CLAIM_TOCLAIM.toString(), TL.CLAIM_FORCLAIM.toString())) {
            return false;
        }

        // Was an over claim
        if (mustPay && currentFaction.isNormal() && currentFaction.hasLandInflation()) {
            // Give them money for over claiming.
            Econ.modifyMoney(payee, FactionsPlugin.getInstance().conf().economy().getOverclaimRewardMultiplier(), TL.CLAIM_TOOVERCLAIM.toString(), TL.CLAIM_FOROVERCLAIM.toString());
        }

        if (LWC.getEnabled() && forFaction.isNormal() && FactionsPlugin.getInstance().conf().lwc().isResetLocksOnCapture()) {
            LWC.clearOtherLocks(flocation, this.getFaction());
        }

        // announce success
        ObjectSet<FPlayer> informTheseFPlayers = new ObjectOpenHashSet<>();
        informTheseFPlayers.add(this);
        informTheseFPlayers.addAll(forFaction.getFPlayersWhereOnline(true));
        for (FPlayer fp : informTheseFPlayers) {
            fp.msg(TL.CLAIM_CLAIMED, this.describeTo(fp, true), forFaction.describeTo(fp), currentFaction.describeTo(fp));
        }

        Board.getInstance().setFactionAt(forFaction, flocation);

        //temporary disable logging.
/*        if (FactionsPlugin.getInstance().conf().logging().isLandClaims()) {
            FactionsPlugin.getInstance().getPluginLogger().info(TL.CLAIM_CLAIMEDLOG.toString(), this.getName(), flocation.getCoordString(), forFaction.getTag());
        }*/

        return true;
    }

    public boolean shouldBeSaved() {
        // TODO DTR
        return this.hasFaction() || (this.getPowerRounded() != this.getPowerMaxRounded() && this.getPowerRounded() != FastMath.round(FactionsPlugin.getInstance().conf().factions().landRaidControl().power().getPlayerStarting()));
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

    public Player getPlayer() {
        return this.scoreboard != null ? this.scoreboard.getPlayer() : Bukkit.getPlayer(this.getId());
    }

    public boolean isOnline() {
        return FPlayers.getInstance().isOnline(this) && WorldUtil.isEnabled(this.getLastStoodAt().getWorld());
    }

    // make sure target player should be able to detect that this player is online
    public boolean isOnlineAndVisibleTo(Player player) {
        Player target = this.getPlayer();
        return target != null && player.canSee(target) && WorldUtil.isEnabled(player.getWorld());
    }

    public boolean isOffline() {
        return !isOnline();
    }

    public boolean isFlying() {
        return isFlying;
    }

    public void setFlying(boolean fly) {
        setFlying(fly, false);
    }

    public void setFlying(boolean fly, boolean damage) {
        Player player = getPlayer();
        if (player != null) {
            player.setAllowFlight(fly);
            player.setFlying(fly);
        }

        if (!damage) {
            msg(TL.COMMAND_FLY_CHANGE, fly ? "enabled" : "disabled");
        } else {
            msg(TL.COMMAND_FLY_DAMAGE);
        }

        // If leaving fly mode, don't let them take fall damage for x seconds.
        if (!fly) {
            int cooldown = FactionsPlugin.getInstance().conf().commands().fly().getFallDamageCooldown();

            // If the value is 0 or lower, make them take fall damage.
            // Otherwise, start a timer and have this cancel after a few seconds.
            // Short task so we're just doing it in method. Not clean but eh.
            if (cooldown > 0) {
                setTakeFallDamage(false);
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        setTakeFallDamage(true);
                    }
                }.runTaskLater(FactionsPlugin.getInstance(), cooldown * 20L);
            }
        }

        isFlying = fly;
    }

    public boolean isAutoFlying() {
        return isAutoFlying;
    }

    public void setAutoFlying(boolean autoFly) {
        msg(TL.COMMAND_FLY_AUTO, autoFly ? "enabled" : "disabled");
        this.isAutoFlying = autoFly;
    }

    public boolean canFlyAtLocation() {
        return canFlyAtLocation(lastStoodAt);
    }

    public boolean canFlyAtLocation(FLocation location) {
        Faction faction = Board.getInstance().getFactionAt(location);
        if (faction.isWilderness()) {
            return Permission.FLY_WILDERNESS.has(getPlayer());
        } else if (faction.isSafeZone()) {
            return Permission.FLY_SAFEZONE.has(getPlayer());
        } else if (faction.isWarZone()) {
            return Permission.FLY_WARZONE.has(getPlayer());
        }

        // admin bypass (ops) can fly.
        if (isAdminBypassing) {
            return true;
        }

        return faction.hasAccess(this, PermissibleAction.FLY);
    }

    public boolean shouldTakeFallDamage() {
        return this.shouldTakeFallDamage;
    }

    public void setTakeFallDamage(boolean fallDamage) {
        this.shouldTakeFallDamage = fallDamage;
    }

    public boolean isSeeingChunk() {
        return seeingChunk;
    }

    public void setSeeingChunk(boolean seeingChunk) {
        this.seeingChunk = seeingChunk;
        if (this.seeingChunk) {
            SeeChunkTask.get().track(this);
        } else {
            SeeChunkTask.get().untrack(this, true);
        }
    }

    public boolean getFlyTrailsState() {
        return flyTrailsState;
    }

    public void setFlyTrailsState(boolean state) {
        flyTrailsState = state;
        msg(TL.COMMAND_FLYTRAILS_CHANGE, state ? "enabled" : "disabled");
    }

    public String getFlyTrailsEffect() {
        return flyTrailsEffect;
    }

    public void setFlyTrailsEffect(String effect) {
        flyTrailsEffect = effect;
        msg(TL.COMMAND_FLYTRAILS_PARTICLE_CHANGE, effect);
    }

    private static final transient Pattern MESSAGE_LINES = Pattern.compile("/n/");

    public void sendMessage(String msg) {
        if (msg.contains("{null}")) {
            return; // user wants this message to not send
        }
        Player player = this.getPlayer();
        if (player == null) {
            return;
        }
        for (String s : MESSAGE_LINES.split(msg)) {
            player.sendMessage(s);
        }
    }

    public void sendMessage(List<String> msgs) {
        Player player = this.getPlayer();
        if (player != null) {
            for (String msg : msgs) {
                player.sendMessage(msg);
            }
        }
    }

    public void sendFancyMessage(TextComponent message) {
        Player player = getPlayer();
        if (player != null) {
            TextAdapter.sendMessage(player, message);
        }
    }

    public void sendFancyMessage(List<TextComponent> messages) {
        Player player = getPlayer();
        if (player != null) {
            for (TextComponent msg : messages) {
                TextAdapter.sendMessage(player, msg);
            }
        }
    }

    public int getMapHeight() {
        if (this.mapHeight < 1) {
            this.mapHeight = FactionsPlugin.getInstance().conf().map().getHeight();
        }

        return this.mapHeight;
    }

    public void setMapHeight(int height) {
        this.mapHeight = Math.min(height, (FactionsPlugin.getInstance().conf().map().getHeight() * 2));
    }

    public String getNameAndTitle(FPlayer fplayer) {
        return this.getColorTo(fplayer) + this.getNameAndTitle();
    }

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public void setId(UUID id) {
        this.id = id;
    }

    @Override
    public void clearWarmup() {
        if (warmup != null) {
            Bukkit.getScheduler().cancelTask(warmupTask);
            this.stopWarmup();
        }
    }

    @Override
    public void stopWarmup() {
        warmup = null;
    }

    @Override
    public boolean isWarmingUp() {
        return warmup != null;
    }

    @Override
    public WarmUpUtil.Warmup getWarmupType() {
        return warmup;
    }

    @Override
    public void addWarmup(WarmUpUtil.Warmup warmup, int taskId) {
        if (this.warmup != null) {
            this.clearWarmup();
        }
        this.warmup = warmup;
        this.warmupTask = taskId;
    }

    @Override
    public int compareTo(FPlayer o) {
        return Integer.compare(o.getRole().getValue(), this.role.getValue());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MemoryFPlayer that = (MemoryFPlayer) o;

        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
