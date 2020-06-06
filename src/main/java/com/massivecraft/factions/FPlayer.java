package com.massivecraft.factions;

import com.massivecraft.factions.iface.EconomyParticipator;
import com.massivecraft.factions.meta.scoreboards.FastBoard;
import com.massivecraft.factions.meta.scoreboards.SidebarTextProvider;
import com.massivecraft.factions.perms.Relation;
import com.massivecraft.factions.perms.Role;
import com.massivecraft.factions.struct.ChatMode;
import com.massivecraft.factions.util.MultiClaim;
import com.massivecraft.factions.util.WarmUpUtil;
import net.kyori.text.TextComponent;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;


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

public interface FPlayer extends EconomyParticipator, Comparable<FPlayer> {

    void login();

    void logout();

    Faction getFaction();

    String getFactionId();

    int getFactionIdRaw();

    boolean hasFaction();

    void setFaction(Faction faction);

    boolean willAutoLeave();

    void setAutoLeave(boolean autoLeave);

    long getLastFrostwalkerMessage();

    void setLastFrostwalkerMessage();

    void setMonitorJoins(boolean monitor);

    boolean isMonitoringJoins();

    Role getRole();

    void setRole(Role role);

    boolean shouldTakeFallDamage();

    void setTakeFallDamage(boolean fallDamage);

    double getPowerBoost();

    void setPowerBoost(double powerBoost);

    Faction getAutoClaimFor();

    void setAutoClaimFor(Faction faction);

    boolean isAutoSafeClaimEnabled();

    void setIsAutoSafeClaimEnabled(boolean enabled);

    boolean isAutoWarClaimEnabled();

    void setIsAutoWarClaimEnabled(boolean enabled);

    boolean isAdminBypassing();

    boolean isVanished(Player viewer);

    void setIsAdminBypassing(boolean val);

    void setChatMode(ChatMode chatMode);

    ChatMode getChatMode();

    void setIgnoreAllianceChat(boolean ignore);

    boolean isIgnoreAllianceChat();

    void setSpyingChat(boolean chatSpying);

    boolean isSpyingChat();

    FastBoard getScoreboard();

    SidebarTextProvider getScoreboardTextProvider();

    void setTextProvider(SidebarTextProvider provider);

    void defaultTextProvider();

    boolean showScoreboard();

    void setShowScoreboard(boolean show);

    void resetFactionData(boolean doSpoutUpdate);

    void resetFactionData();

    long getLastLoginTime();

    void setLastLoginTime(long lastLoginTime);

    boolean isMapAutoUpdating();

    void setMapAutoUpdating(boolean mapAutoUpdating);

    boolean hasLoginPvpDisabled();

    FLocation getLastStoodAt();

    void setLastStoodAt(FLocation flocation);

    String getTitle();

    void setTitle(CommandSender sender, String title);

    String getName();

    void setName(String name);

    String getTag();

    // Base concatenations:

    String getNameAndSomething(String something);

    String getNameAndTitle();

    String getNameAndTag();

    // Colored concatenations:
    // These are used in information messages

    String getNameAndTitle(Faction faction);

    String getNameAndTitle(FPlayer fplayer);

    // Chat Tag:
    // These are injected into the format of global chat messages.

    String getChatTag();

    // Colored Chat Tag
    String getChatTag(Faction faction);

    String getChatTag(FPlayer fplayer);

    int getKills();

    int getDeaths();


    // -------------------------------
    // Relation and relation colors
    // -------------------------------

    Relation getRelationToLocation();

    //----------------------------------------------//
    // Health
    //----------------------------------------------//
    void heal(double amnt);


    //----------------------------------------------//
    // Power
    //----------------------------------------------//
    double getPower();

    void alterPower(double delta);

    double getPowerMax();

    double getPowerMin();

    int getPowerRounded();

    int getPowerMaxRounded();

    int getPowerMinRounded();

    void updatePower();

    void losePowerFromBeingOffline();

    void onDeath();

    //----------------------------------------------//
    // Territory
    //----------------------------------------------//
    boolean isInOwnTerritory();

    boolean isInOthersTerritory();

    boolean isInAllyTerritory();

    boolean isInNeutralTerritory();

    boolean isInEnemyTerritory();

    void sendFactionHereMessage(Faction from);

    void sendFactionHereMessage(Faction from, Player player);

    // -------------------------------
    // Actions
    // -------------------------------

    void leave(boolean makePay);

    boolean canClaimForFaction(Faction forFaction);

    boolean canClaimForFactionAtLocation(Faction forFaction, Location location, boolean notifyFailure);

    boolean canClaimForFactionAtLocation(Faction forFaction, FLocation location, boolean notifyFailure);

    boolean attemptClaim(Faction forFaction, Location location, boolean notifyFailure);

    boolean attemptClaim(Faction forFaction, FLocation location, boolean notifyFailure);

    void attemptClaimCompact(World world, Faction forFaction, FLocation flocation, MultiClaim multiClaim);

    UUID getId();

    Player getPlayer();

    boolean isOnline();

    void sendMessage(Player player, String message);

    void sendMessage(String message);

    void sendMessage(List<String> messages);

    void sendFancyMessage(TextComponent message);

    void sendFancyMessage(List<TextComponent> message);

    int getMapHeight();

    void setMapHeight(int height);

    boolean isOnlineAndVisibleTo(Player me);

    void remove();

    boolean isOffline();

    void setId(UUID id);

    boolean isFlying();

    void setFlying(boolean fly);

    void setFlying(boolean fly, boolean damage);

    boolean isAutoFlying();

    void setAutoFlying(boolean autoFly);

    boolean canFlyAtLocation();

    boolean canFlyAtLocation(FLocation location);

    boolean isSeeingChunk();

    void setSeeingChunk(boolean seeingChunk);

    boolean getFlyTrailsState();

    void setFlyTrailsState(boolean state);

    String getFlyTrailsEffect();

    void setFlyTrailsEffect(String effect);

    // -------------------------------
    // Warmups
    // -------------------------------

    boolean isWarmingUp();

    WarmUpUtil.Warmup getWarmupType();

    void addWarmup(WarmUpUtil.Warmup warmup, int taskId);

    void stopWarmup();

    void clearWarmup();

}
