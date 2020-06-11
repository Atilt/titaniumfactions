package com.massivecraft.factions.config.transition.oldclass.v0;

import com.google.common.collect.Sets;
import org.bukkit.ChatColor;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class OldConfV0 {
    public final List<String> baseCommandAliases = new ArrayList<>();

    // Colors
    public final ChatColor colorMember = ChatColor.GREEN;
    public final ChatColor colorAlly = ChatColor.LIGHT_PURPLE;
    public final ChatColor colorTruce = ChatColor.DARK_PURPLE;
    public final ChatColor colorNeutral = ChatColor.WHITE;
    public final ChatColor colorEnemy = ChatColor.RED;

    public final ChatColor colorPeaceful = ChatColor.GOLD;
    public final ChatColor colorWilderness = ChatColor.GRAY;
    public final ChatColor colorSafezone = ChatColor.GOLD;
    public final ChatColor colorWar = ChatColor.DARK_RED;

    // Power
    public final double powerPlayerMax = 10.0;
    public final double powerPlayerMin = -10.0;
    public final double powerPlayerStarting = 0.0;
    public final double powerPerMinute = 0.2; // Default health rate... it takes 5 min to heal one power
    public final double powerPerDeath = 4.0; // A death makes you lose 4 power
    public final boolean powerRegenOffline = false;  // does player power regenerate even while they're offline?
    public final double powerOfflineLossPerDay = 0.0;  // players will lose this much power per day offline
    public final double powerOfflineLossLimit = 0.0;  // players will no longer lose power from being offline once their power drops to this amount or less
    public final double powerFactionMax = 0.0;  // if greater than 0, the cap on how much power a faction can have (additional power from players beyond that will act as a "buffer" of sorts)

    public final String prefixAdmin = "***";
    public final String prefixColeader = "**";
    public final String prefixMod = "*";
    public final String prefixNormal = "+";
    public final String prefixRecruit = "-";

    public final boolean allowMultipleColeaders = false;

    public final int factionTagLengthMin = 3;
    public final int factionTagLengthMax = 10;
    public final boolean factionTagForceUpperCase = false;

    public final boolean newFactionsDefaultOpen = false;

    // when faction membership hits this limit, players will no longer be able to join using /f join; default is 0, no limit
    public final int factionMemberLimit = 0;

    // what faction ID to start new players in when they first join the server; default is 0, "no faction"
    public final String newPlayerStartingFactionID = "0";

    public final boolean showMapFactionKey = true;
    public final boolean showNeutralFactionsOnMap = true;
    public final boolean showEnemyFactionsOnMap = true;
    public final boolean showTruceFactionsOnMap = true;

    // Disallow joining/leaving/kicking while power is negative
    public final boolean canLeaveWithNegativePower = true;

    // Configuration for faction-only chat
    public final boolean factionOnlyChat = true;
    // Configuration on the Faction tag in chat messages.
    public final boolean chatTagEnabled = true;
    public final transient boolean chatTagHandledByAnotherPlugin = false;
    public final boolean chatTagRelationColored = true;
    public final String chatTagReplaceString = "[FACTION]";
    public final String chatTagInsertAfterString = "";
    public final String chatTagInsertBeforeString = "";
    public final int chatTagInsertIndex = 0;
    public final boolean chatTagPadBefore = false;
    public final boolean chatTagPadAfter = true;
    public final String chatTagFormat = "[]" + ChatColor.WHITE;
    public final boolean alwaysShowChatTag = true;
    public final String factionChatFormat = "[]:" + ChatColor.WHITE + " []";
    public final String allianceChatFormat = ChatColor.LIGHT_PURPLE + "[]:" + ChatColor.WHITE + " []";
    public final String truceChatFormat = ChatColor.DARK_PURPLE + "[]:" + ChatColor.WHITE + " []";
    public final String modChatFormat = ChatColor.RED + "[]:" + ChatColor.WHITE + " []";

    public final boolean broadcastDescriptionChanges = false;
    public final boolean broadcastTagChanges = false;

    public final double saveToFileEveryXMinutes = 30.0;

    public final double autoLeaveAfterDaysOfInactivity = 10.0;
    public final double autoLeaveRoutineRunsEveryXMinutes = 5.0;
    public final int autoLeaveRoutineMaxMillisecondsPerTick = 5;  // 1 server tick is roughly 50ms, so default max 10% of a tick
    public final boolean removePlayerDataWhenBanned = true;
    public final boolean autoLeaveDeleteFPlayerData = true; // Let them just remove player from Faction.

    public final boolean worldGuardChecking = false;
    public final boolean worldGuardBuildPriority = false;

    // server logging options
    public final boolean logFactionCreate = true;
    public final boolean logFactionDisband = true;
    public final boolean logFactionJoin = true;
    public final boolean logFactionKick = true;
    public final boolean logFactionLeave = true;
    public final boolean logLandClaims = true;
    public final boolean logLandUnclaims = true;
    public final boolean logMoneyTransactions = true;
    public final boolean logPlayerCommands = true;

    // prevent some potential exploits
    public final boolean handleExploitObsidianGenerators = true;
    public final boolean handleExploitEnderPearlClipping = true;
    public final boolean handleExploitInteractionSpam = true;
    public final boolean handleExploitTNTWaterlog = false;
    public final boolean handleExploitLiquidFlow = false;

    public final boolean homesEnabled = true;
    public final boolean homesMustBeInClaimedTerritory = true;
    public final boolean homesTeleportToOnDeath = true;
    public final boolean homesRespawnFromNoPowerLossWorlds = true;
    public final boolean homesTeleportCommandEnabled = true;
    public final boolean homesTeleportCommandEssentialsIntegration = true;
    public final boolean homesTeleportCommandSmokeEffectEnabled = true;
    public final float homesTeleportCommandSmokeEffectThickness = 3f;
    public final boolean homesTeleportAllowedFromEnemyTerritory = true;
    public final boolean homesTeleportAllowedFromDifferentWorld = true;
    public final double homesTeleportAllowedEnemyDistance = 32.0;
    public final boolean homesTeleportIgnoreEnemiesIfInOwnTerritory = true;

    public final boolean disablePVPBetweenNeutralFactions = false;
    public final boolean disablePVPForFactionlessPlayers = false;
    public final boolean enablePVPAgainstFactionlessInAttackersLand = false;

    public final int noPVPDamageToOthersForXSecondsAfterLogin = 3;

    public final boolean peacefulTerritoryDisablePVP = true;
    public final boolean peacefulTerritoryDisableMonsters = false;
    public final boolean peacefulTerritoryDisableBoom = false;
    public final boolean peacefulMembersDisablePowerLoss = true;

    public final boolean permanentFactionsDisableLeaderPromotion = false;

    public final boolean claimsMustBeConnected = false;
    public final boolean claimsCanBeUnconnectedIfOwnedByOtherFaction = true;
    public final int claimsRequireMinFactionMembers = 1;
    public final int claimedLandsMax = 0;
    public final int lineClaimLimit = 5;

    // if someone is doing a radius claim and the process fails to claim land this many times in a row, it will exit
    public final int radiusClaimFailureLimit = 9;

    public final double considerFactionsReallyOfflineAfterXMinutes = 0.0;

    public final int actionDeniedPainAmount = 1;

    // commands which will be prevented if the player is a member of a permanent faction
    public final Set<String> permanentFactionMemberDenyCommands = new LinkedHashSet<>();

    // commands which will be prevented when in claimed territory of another faction
    public final Set<String> territoryNeutralDenyCommands = new LinkedHashSet<>();
    public final Set<String> territoryEnemyDenyCommands = new LinkedHashSet<>();
    public final Set<String> territoryAllyDenyCommands = new LinkedHashSet<>();
    public final Set<String> warzoneDenyCommands = new LinkedHashSet<>();
    public final Set<String> wildernessDenyCommands = new LinkedHashSet<>();

    // IGNORED STARTS HERE LOL
    public boolean defaultFlyPermEnemy = false;
    public boolean defaultFlyPermNeutral = false;
    public boolean defaultFlyPermTruce = false;
    public boolean defaultFlyPermAlly = true;
    public boolean defaultFlyPermMember = true;

    public boolean territoryDenyBuild = true;
    public boolean territoryDenyBuildWhenOffline = true;
    public boolean territoryPainBuild = false;
    public boolean territoryPainBuildWhenOffline = false;
    public boolean territoryDenyUseage = true;
    public boolean territoryEnemyDenyBuild = true;
    public boolean territoryEnemyDenyBuildWhenOffline = true;
    public boolean territoryEnemyPainBuild = false;
    public boolean territoryEnemyPainBuildWhenOffline = false;
    public boolean territoryEnemyDenyUseage = true;
    public boolean territoryEnemyProtectMaterials = true;
    public boolean territoryAllyDenyBuild = true;
    public boolean territoryAllyDenyBuildWhenOffline = true;
    public boolean territoryAllyPainBuild = false;
    public boolean territoryAllyPainBuildWhenOffline = false;
    public boolean territoryAllyDenyUseage = true;
    public boolean territoryAllyProtectMaterials = true;
    public boolean territoryTruceDenyBuild = true;
    public boolean territoryTruceDenyBuildWhenOffline = true;
    public boolean territoryTrucePainBuild = false;
    public boolean territoryTrucePainBuildWhenOffline = false;
    public boolean territoryTruceDenyUseage = true;
    public boolean territoryTruceProtectMaterials = true;
    // IGNORED ENDS HERE LOL
    public final boolean territoryBlockCreepers = false;
    public final boolean territoryBlockCreepersWhenOffline = false;
    public final boolean territoryBlockFireballs = false;
    public final boolean territoryBlockFireballsWhenOffline = false;
    public final boolean territoryBlockTNT = false;
    public final boolean territoryBlockTNTWhenOffline = false;
    public final boolean territoryDenyEndermanBlocks = true;
    public final boolean territoryDenyEndermanBlocksWhenOffline = true;

    public final boolean safeZoneDenyBuild = true;
    public final boolean safeZoneDenyUseage = true;
    public final boolean safeZoneBlockTNT = true;
    public final boolean safeZonePreventAllDamageToPlayers = false;
    public final boolean safeZoneDenyEndermanBlocks = true;

    public final boolean warZoneDenyBuild = true;
    public final boolean warZoneDenyUseage = true;
    public final boolean warZoneBlockCreepers = false;
    public final boolean warZoneBlockFireballs = false;
    public final boolean warZoneBlockTNT = true;
    public final boolean warZonePowerLoss = true;
    public final boolean warZoneFriendlyFire = false;
    public final boolean warZoneDenyEndermanBlocks = true;

    public final boolean wildernessDenyBuild = false;
    public final boolean wildernessDenyUseage = false;
    public final boolean wildernessBlockCreepers = false;
    public final boolean wildernessBlockFireballs = false;
    public final boolean wildernessBlockTNT = false;
    public final boolean wildernessPowerLoss = true;
    public final boolean wildernessDenyEndermanBlocks = false;

    // for claimed areas where further faction-member ownership can be defined
    public final boolean ownedAreasEnabled = true;
    public final int ownedAreasLimitPerFaction = 0;
    public final boolean ownedAreasModeratorsCanSet = false;
    public final boolean ownedAreaModeratorsBypass = true;
    public final boolean ownedAreaDenyBuild = true;
    public final boolean ownedAreaPainBuild = false;
    public final boolean ownedAreaProtectMaterials = true;
    public final boolean ownedAreaDenyUseage = true;

    public final boolean ownedMessageOnBorder = true;
    public final boolean ownedMessageInsideTerritory = true;
    public final boolean ownedMessageByChunk = false;

    public final boolean pistonProtectionThroughDenyBuild = true;

    public final Set<Material> territoryProtectedMaterials = Sets.newHashSet();
    public final Set<Material> territoryDenyUseageMaterials = EnumSet.noneOf(Material.class);
    public final Set<Material> territoryProtectedMaterialsWhenOffline = EnumSet.noneOf(Material.class);
    public final Set<Material> territoryDenyUseageMaterialsWhenOffline = EnumSet.noneOf(Material.class);

    // Economy settings
    public final boolean econEnabled = false;
    public final String econUniverseAccount = "";
    public final double econCostClaimWilderness = 30.0;
    public final double econCostClaimFromFactionBonus = 30.0;
    public final double econOverclaimRewardMultiplier = 0.0;
    public final double econClaimAdditionalMultiplier = 0.5;
    public final double econClaimRefundMultiplier = 0.7;
    public final double econClaimUnconnectedFee = 0.0;
    public final double econCostCreate = 100.0;
    public final double econCostOwner = 15.0;
    public final double econCostSethome = 30.0;
    public final double econCostJoin = 0.0;
    public final double econCostLeave = 0.0;
    public final double econCostKick = 0.0;
    public final double econCostInvite = 0.0;
    public final double econCostHome = 0.0;
    public final double econCostTag = 0.0;
    public final double econCostDesc = 0.0;
    public final double econCostTitle = 0.0;
    public final double econCostList = 0.0;
    public final double econCostMap = 0.0;
    public final double econCostPower = 0.0;
    public final double econCostShow = 0.0;
    public final double econCostStuck = 0.0;
    public final double econCostOpen = 0.0;
    public final double econCostAlly = 0.0;
    public final double econCostTruce = 0.0;
    public final double econCostEnemy = 0.0;
    public final double econCostNeutral = 0.0;
    public final double econCostNoBoom = 0.0;


    // -------------------------------------------- //
    // INTEGRATION: DYNMAP
    // -------------------------------------------- //

    // Should the dynmap intagration be used?
    public boolean dynmapUse = false;

    // Name of the Factions layer
    public String dynmapLayerName = "Factions";

    // Should the layer be visible per default
    public boolean dynmapLayerVisible = true;

    // Ordering priority in layer menu (low goes before high - default is 0)
    public int dynmapLayerPriority = 2;

    // (optional) set minimum zoom level before layer is visible (0 = default, always visible)
    public int dynmapLayerMinimumZoom = 0;

    // Format for popup - substitute values for macros
    public String dynmapDescription =
            "<div class=\"infowindow\">\n"
                    + "<span style=\"font-weight: bold; font-size: 150%;\">%name%</span><br>\n"
                    + "<span style=\"font-style: italic; font-size: 110%;\">%description%</span><br>"
                    + "<br>\n"
                    + "<span style=\"font-weight: bold;\">Leader:</span> %players.leader%<br>\n"
                    + "<span style=\"font-weight: bold;\">Admins:</span> %players.admins.count%<br>\n"
                    + "<span style=\"font-weight: bold;\">Moderators:</span> %players.moderators.count%<br>\n"
                    + "<span style=\"font-weight: bold;\">Members:</span> %players.normals.count%<br>\n"
                    + "<span style=\"font-weight: bold;\">TOTAL:</span> %players.count%<br>\n"
                    + "</br>\n"
                    + "<span style=\"font-weight: bold;\">Bank:</span> %money%<br>\n"
                    + "<br>\n"
                    + "</div>";

    // Enable the %money% macro. Only do this if you know your economy manager is thread-safe.
    public boolean dynmapDescriptionMoney = false;

    // Allow players in faction to see one another on Dynmap (only relevant if Dynmap has 'player-info-protected' enabled)
    public boolean dynmapVisibilityByFaction = true;

    // Optional setting to limit which regions to show.
    // If empty all regions are shown.
    // Specify Faction either by name or UUID.
    // To show all regions on a given world, add 'world:<worldname>' to the list.
    public Set<String> dynmapVisibleFactions = new HashSet<>();

    // Optional setting to hide specific Factions.
    // Specify Faction either by name or UUID.
    // To hide all regions on a given world, add 'world:<worldname>' to the list.
    public Set<String> dynmapHiddenFactions = new HashSet<>();

    // Region Style
    public final transient String DYNMAP_STYLE_LINE_COLOR = "#00FF00";
    public final transient double DYNMAP_STYLE_LINE_OPACITY = 0.8D;
    public final transient int DYNMAP_STYLE_LINE_WEIGHT = 3;
    public final transient String DYNMAP_STYLE_FILL_COLOR = "#00FF00";
    public final transient double DYNMAP_STYLE_FILL_OPACITY = 0.35D;
    public final transient String DYNMAP_STYLE_HOME_MARKER = "greenflag";
    public final transient boolean DYNMAP_STYLE_BOOST = false;

    /*public DynmapStyle dynmapDefaultStyle = new DynmapStyle()
            .setStrokeColor(DYNMAP_STYLE_LINE_COLOR)
            .setLineOpacity(DYNMAP_STYLE_LINE_OPACITY)
            .setLineWeight(DYNMAP_STYLE_LINE_WEIGHT)
            .setFillColor(DYNMAP_STYLE_FILL_COLOR)
            .setFillOpacity(DYNMAP_STYLE_FILL_OPACITY)
            .setHomeMarker(DYNMAP_STYLE_HOME_MARKER)
            .setBoost(DYNMAP_STYLE_BOOST);

    // Optional per Faction style overrides. Any defined replace those in dynmapDefaultStyle.
    // Specify Faction either by name or UUID.
    public Map<String, DynmapStyle> dynmapFactionStyles = ImmutableMap.of(
            "SafeZone", new DynmapStyle().setStrokeColor("#FF00FF").setFillColor("#FF00FF").setBoost(false),
            "WarZone", new DynmapStyle().setStrokeColor("#FF0000").setFillColor("#FF0000").setBoost(false)
    );
*/

    //Faction banks, to pay for land claiming and other costs instead of individuals paying for them
    public final boolean bankEnabled = true;
    public final boolean bankMembersCanWithdraw = false; //Have to be at least moderator to withdraw or pay money to another faction
    public final boolean bankFactionPaysCosts = true; //The faction pays for faction command costs, such as sethome
    public final boolean bankFactionPaysLandCosts = true; //The faction pays for land claiming costs.

    // mainly for other plugins/mods that use a fake player to take actions, which shouldn't be subject to our protections
    public final Set<String> playersWhoBypassAllProtection = new LinkedHashSet<>();

    public final Set<String> worldsNoClaiming = new LinkedHashSet<>();
    public final Set<String> worldsNoPowerLoss = new LinkedHashSet<>();
    public final Set<String> worldsIgnorePvP = new LinkedHashSet<>();
    public final Set<String> worldsNoWildernessProtection = new LinkedHashSet<>();

    // faction-<factionId>
    public final String vaultPrefix = "faction-[]";
    public final int defaultMaxVaults = 0;

    // Taller and wider for "bigger f map"
    public final int mapHeight = 17;
    public final int mapWidth = 49;
}
