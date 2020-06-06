/*
 * Copyright (C) 2013 drtshock
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.massivecraft.factions.util;

import com.massivecraft.factions.FactionsPlugin;
import com.massivecraft.factions.config.Loader;
import net.kyori.text.Component;
import net.kyori.text.TextComponent;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.IOException;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.EnumMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * An enum for requesting strings from the language file. The contents of this enum file may be subject to frequent
 * changes.
 */
public enum TL implements Formattable<TL> {
    /**
     * Translation meta
     */
    _AUTHOR("misc"),
    _RESPONSIBLE("misc"),
    _LANGUAGE("English"),
    _ENCODING("UTF-8"),
    _LOCALE("en_US"),
    _REQUIRESUNICODE("false"),
    _DEFAULT("true"),
    _STATE("complete"), //incomplete, limited, partial, majority, complete

    /**
     * Localised translation meta
     */
    _LOCAL_AUTHOR("misc"),
    _LOCAL_RESPONSIBLE("misc"),
    _LOCAL_LANGUAGE("English"),
    _LOCAL_REGION("US"),
    _LOCAL_STATE("complete"), //And this is the English version. It's not ever going to be not complete.

    /**
     * Command translations
     */
    COMMAND_ADMIN_NOTMEMBER("{0}&e is not a member in your faction."),
    COMMAND_ADMIN_NOTADMIN("&cYou are not the faction admin."),
    COMMAND_ADMIN_TARGETSELF("&cThe target player musn't be yourself."),
    COMMAND_ADMIN_DEMOTES("&eYou have demoted {0}&e from the position of faction admin."),
    COMMAND_ADMIN_DEMOTED("&eYou have been demoted from the position of faction admin by {0}&e."),
    COMMAND_ADMIN_PROMOTES("&eYou have promoted {0}&e to the position of faction admin."),
    COMMAND_ADMIN_PROMOTED("{0}&e gave {1}&e the leadership of {2}&e."),
    COMMAND_ADMIN_DESCRIPTION("Hand over your admin rights"),

    COMMAND_AHOME_DESCRIPTION("Send a player to their f home no matter what."),
    COMMAND_AHOME_NOHOME("{0} doesn't have an f home."),
    COMMAND_AHOME_SUCCESS("$1$s was sent to their f home."),
    COMMAND_AHOME_OFFLINE("{1} is offline."),
    COMMAND_AHOME_TARGET("You were sent to your f home."),

    COMMAND_ANNOUNCE_DESCRIPTION("Announce a message to players in faction."),

    COMMAND_AUTOCLAIM_ENABLED("&eNow auto-claiming land for &d{0}&e."),
    COMMAND_AUTOCLAIM_DISABLED("&eAuto-claiming of land disabled."),
    COMMAND_AUTOCLAIM_REQUIREDRANK("&cYou must be &d{0}&c to claim land."),
    COMMAND_AUTOCLAIM_OTHERFACTION("&cYou can't claim land for &d{0}&c."),
    COMMAND_AUTOCLAIM_DESCRIPTION("Auto-claim land as you walk around"),

    COMMAND_AUTOHELP_HELPFOR("Help for command \""),

    COMMAND_BAN_DESCRIPTION("Ban players from joining your Faction."),
    COMMAND_BAN_TARGET("&cYou were banned from &7{0}"), // banned player perspective
    COMMAND_BAN_BANNED("&e{0} &cbanned &7{1}"),
    COMMAND_BAN_ALREADYBANNED("&c{0} is already banned"),
    COMMAND_BAN_SELF("&cYou may not ban yourself"),
    COMMAND_BAN_INSUFFICIENTRANK("&cYour rank is too low to ban &7{0}"),

    COMMAND_BANLIST_DESCRIPTION("View a Faction's ban list"),
    COMMAND_BANLIST_HEADER("&6There are &c{0}&6 bans for {1}"),
    COMMAND_BANLIST_ENTRY("&7{0}. &c{1} &r- &a{2} &r- &e{3}"),
    COMMAND_BANLIST_NOFACTION("&4You are not in a Faction."),
    COMMAND_BANLIST_INVALID("We couldn't find a Faction by the name {0}"),

    COMMAND_BOOM_PEACEFULONLY("&cThis command is only usable by factions which are specifically designated as peaceful."),
    COMMAND_BOOM_TOTOGGLE("to toggle explosions"),
    COMMAND_BOOM_FORTOGGLE("for toggling explosions"),
    COMMAND_BOOM_ENABLED("{0}&e has {1} explosions in your faction's territory."),
    COMMAND_BOOM_DESCRIPTION("Toggle explosions (peaceful factions only)"),

    COMMAND_BYPASS_ENABLE("&eYou have enabled admin bypass mode. You will be able to build or destroy anywhere."),
    COMMAND_BYPASS_ENABLELOG(" has ENABLED admin bypass mode."),
    COMMAND_BYPASS_DISABLE("&eYou have disabled admin bypass mode."),
    COMMAND_BYPASS_DISABLELOG(" has DISABLED admin bypass mode."),
    COMMAND_BYPASS_DESCRIPTION("Enable admin bypass mode"),

    COMMAND_CHAT_DISABLED("&cThe built in chat channels are disabled on this server."),
    COMMAND_CHAT_INVALIDMODE("&cUnrecognised chat mode. &ePlease enter either 'a','f','m','t' or 'p'"),
    COMMAND_CHAT_INSUFFICIENTRANK("&cYou must be at least moderator to enter this chat."),
    COMMAND_CHAT_DESCRIPTION("Change chat mode"),

    COMMAND_CHAT_MODE_PUBLIC("&ePublic chat mode."),
    COMMAND_CHAT_MODE_ALLIANCE("&eAlliance only chat mode."),
    COMMAND_CHAT_MODE_TRUCE("&eTruce only chat mode."),
    COMMAND_CHAT_MODE_FACTION("&eFaction only chat mode."),
    COMMAND_CHAT_MODE_MOD("&eMod only chat mode."),
    COMMAND_CHAT_MODE_COLEADER("&cColeader only chat mode."),

    COMMAND_CHATSPY_ENABLE("&eYou have enabled chat spying mode."),
    COMMAND_CHATSPY_ENABLELOG(" has ENABLED chat spying mode."),
    COMMAND_CHATSPY_DISABLE("&eYou have disabled chat spying mode."),
    COMMAND_CHATSPY_DISABLELOG(" has DISABLED chat spying mode."),
    COMMAND_CHATSPY_DESCRIPTION("Enable admin chat spy mode"),

    COMMAND_CLAIM_INVALIDRADIUS("&cIf you specify a radius, it must be at least 1."),
    COMMAND_CLAIM_MAXRADIUS("&cIf you specify a radius, it must not be larger than 100."),
    COMMAND_CLAIM_ALREADY_OCCURING("&cPlease wait while your previous claiming finishes."),
    COMMAND_CLAIM_DENIED("&cYou do not have permission to claim in a radius."),
    COMMAND_CLAIM_DESCRIPTION("Claim land from where you are standing"),

    COMMAND_CLAIMFILL_DESCRIPTION("Claim land filling in a gap in claims"),
    COMMAND_CLAIMFILL_ABOVEMAX("&cThe maximum limit for claim fill is {0}."),
    COMMAND_CLAIMFILL_ALREADYCLAIMED("&cCannot claim fill using already claimed land!"),
    COMMAND_CLAIMFILL_TOOFAR("&cThis fill would exceed the maximum distance of {0}"),
    COMMAND_CLAIMFILL_PASTLIMIT("&cThis claim would exceed the limit!"),
    COMMAND_CLAIMFILL_NOTENOUGHLANDLEFT("{0} &cdoes not have enough land left to make {1} claims"),
    COMMAND_CLAIMFILL_TOOMUCHFAIL("&cAborting claim fill after {0} failures"),

    COMMAND_CLAIMLINE_INVALIDRADIUS("&cIf you specify a distance, it must be at least 1."),
    COMMAND_CLAIMLINE_DENIED("&cYou do not have permission to claim in a line."),
    COMMAND_CLAIMLINE_DESCRIPTION("Claim land in a straight line."),
    COMMAND_CLAIMLINE_ABOVEMAX("&cThe maximum limit for claim line is &c{0}&c."),
    COMMAND_CLAIMLINE_NOTVALID("{0}&c is not a cardinal direction. You may use &dnorth&c, &deast&c, &dsouth &cor &dwest&c."),

    COMMAND_COLEADER_CANDIDATES("Players you can promote: "),
    COMMAND_COLEADER_CLICKTOPROMOTE("Click to promote "),
    COMMAND_COLEADER_NOTMEMBER("{0}&c is not a member in your faction."),
    COMMAND_COLEADER_NOTADMIN("&cYou are not the faction admin."),
    COMMAND_COLEADER_SELF("&cThe target player musn't be yourself."),
    COMMAND_COLEADER_TARGETISADMIN("&cThe target player is a faction admin. Demote them first."),
    COMMAND_COLEADER_REVOKES("&eYou have removed coleader status from {0}&e."),
    COMMAND_COLEADER_REVOKED("{0}&e is no longer coleader in your faction."),
    COMMAND_COLEADER_PROMOTES("{0}&e was promoted to coleader in your faction."),
    COMMAND_COLEADER_PROMOTED("&eYou have promoted {0}&e to coleader."),
    COMMAND_COLEADER_DESCRIPTION("Give or revoke coleader rights"),
    COMMAND_COLEADER_ALREADY_COLEADER("The faction already has a coleader. There can only be 1."),

    COMMAND_CONVERT_BACKEND_RUNNING("Already running that backend."),
    COMMAND_CONVERT_BACKEND_INVALID("Invalid backend"),
    COMMAND_CONVERT_DESCRIPTION("Convert the plugin backend"),

    COMMAND_COORDS_MESSAGE("&e{0}'s location: &6{1}&e, &6{2}&e, &6{3}&e in &6{4}&e"),
    COMMAND_COORDS_DESCRIPTION("Broadcast your current position to your faction"),

    COMMAND_CREATE_MUSTLEAVE("&cYou must leave your current faction first."),
    COMMAND_CREATE_INUSE("&cThat tag is already in use."),
    COMMAND_CREATE_TOCREATE("to create a new faction"),
    COMMAND_CREATE_FORCREATE("for creating a new faction"),
    COMMAND_CREATE_ERROR("&cThere was an internal error while trying to create your faction. Please try again."),
    COMMAND_CREATE_CREATED("{0}&e created a new faction {1}"),
    COMMAND_CREATE_YOUSHOULD("&eYou should now: {0}"),
    COMMAND_CREATE_CREATEDLOG(" created a new faction: "),
    COMMAND_CREATE_DESCRIPTION("Create a new faction"),

    COMMAND_DEBUG_ALREADY_RUNNING("Debug is already running..."),
    COMMAND_DEBUG_RUNNING("&eNow running..."),
    COMMAND_DEBUG_COMPLETE("&eDebug generated! Share this URL: {0}"),
    COMMAND_DEBUG_DESCRIPTION("Create a debug paste"),
    COMMAND_DEBUG_DELETIONKEY("&eDeletion key: {0}"),
    COMMAND_DEBUG_FAIL("&eERROR! Could not debug. See console for why."),

    COMMAND_DEINVITE_CANDEINVITE("Players you can deinvite: "),
    COMMAND_DEINVITE_CLICKTODEINVITE("Click to revoke invite for {0}"),
    COMMAND_DEINVITE_ALREADYMEMBER("{0}&e is already a member of {1}"),
    COMMAND_DEINVITE_MIGHTWANT("&eYou might want to: {0}"),
    COMMAND_DEINVITE_REVOKED("{0}&e revoked your invitation to &d{1}&e."),
    COMMAND_DEINVITE_REVOKES("{0}&e revoked {0}'s&e invitation."),
    COMMAND_DEINVITE_DESCRIPTION("Remove a pending invitation"),

    COMMAND_DELFWARP_DELETED("&eDeleted warp &6{0}"),
    COMMAND_DELFWARP_INVALID("&eCouldn't find warp &6{0}"),
    COMMAND_DELFWARP_TODELETE("to delete warp"),
    COMMAND_DELFWARP_FORDELETE("for deleting warp"),
    COMMAND_DELFWARP_DESCRIPTION("Delete a faction warp"),

    COMMAND_DELHOME_DEL("{0}&e unset the home for your faction."),
    COMMAND_DELHOME_DESCRIPTION("Unset the faction home"),
    COMMAND_DELHOME_TOSET("to unset the faction home"),
    COMMAND_DELHOME_FORSET("for unsetting the faction home"),

    COMMAND_DESCRIPTION_CHANGES("You have changed the description for &d{0}&e to:"),
    COMMAND_DESCRIPTION_CHANGED("&eThe faction {0}&e changed their description to:"),
    COMMAND_DESCRIPTION_TOCHANGE("to change faction description"),
    COMMAND_DESCRIPTION_FORCHANGE("for changing faction description"),
    COMMAND_DESCRIPTION_DESCRIPTION("Change the faction description"),

    COMMAND_DISBAND_IMMUTABLE("&eYou cannot disband the Wilderness, safe zone, or war zone."),
    COMMAND_DISBAND_MARKEDPERMANENT("&eThis faction is designated as permanent, so you cannot disband it."),
    COMMAND_DISBAND_BROADCAST_YOURS("&d{0}&e disbanded your faction."),
    COMMAND_DISBAND_BROADCAST_NOTYOURS("&d{0}&e disbanded the faction {1}."),
    COMMAND_DISBAND_HOLDINGS("&eYou have been given the disbanded faction's bank, totaling {0}."),
    COMMAND_DISBAND_DESCRIPTION("Disband a faction"),

    COMMAND_DTR_TOSHOW("to show faction DTR info"),
    COMMAND_DTR_FORSHOW("for showing faction DTR info"),
    COMMAND_DTR_DTR("{0}&6 - DTR / Max DTR: &e{1} / {2}"),
    COMMAND_DTR_DESCRIPTION("Show faction DTR info"),
    COMMAND_DTR_MODIFY_DESCRIPTION("Modify faction DTR"),
    COMMAND_DTR_MODIFY_DONE("&eSet DTR for {0}&e to {1}"),

    COMMAND_FLY_DESCRIPTION("Enter or leave Faction flight mode"),
    COMMAND_FLY_CHANGE("&eFaction flight &d{0}"),
    COMMAND_FLY_DAMAGE("&eFaction flight &ddisabled&e due to entering combat"),
    COMMAND_FLY_AUTO("&eFaction auto flight &d{0}"),
    COMMAND_FLY_NO_ACCESS("&cCannot fly in territory of {0}"),
    COMMAND_FLY_ENEMY_NEARBY("&cCannot enable fly, enemy nearby"),
    COMMAND_FLY_ENEMY_DISABLE("&cEnemy nearby, disabling fly"),

    COMMAND_FLYTRAILS_DESCRIPTION("Enabled or change fly trails"),
    COMMAND_FLYTRAILS_PARTICLE_INVALID("&cInvalid particle effect"),
    COMMAND_FLYTRAILS_PARTICLE_PERMS("&cInsufficient permission to use &d{0}"),
    COMMAND_FLYTRAILS_PARTICLE_CHANGE("&eFaction flight trail effect set to &d{0}"),
    COMMAND_FLYTRAILS_CHANGE("&eFaction flight trail &d{0}"),

    COMMAND_FWARP_CLICKTOWARP("Click to warp!"),
    COMMAND_FWARP_COMMANDFORMAT("&e/f warp <warpname> [password]"),
    COMMAND_FWARP_WARPED("&eWarped to &6{0}"),
    COMMAND_FWARP_INVALID_WARP("&eCouldn't find warp &6{0}"),
    COMMAND_FWARP_TOWARP("to warp"),
    COMMAND_FWARP_FORWARPING("for warping"),
    COMMAND_FWARP_WARPS("Warps: "),
    COMMAND_FWARP_DESCRIPTION("Teleport to a faction warp"),
    COMMAND_FWARP_INVALID_PASSWORD("&4Invalid password!"),
    COMMAND_FWARP_PASSWORD_REQUIRED("&aPlease enter warp password. This will not be visible to anyone else"),
    COMMAND_FWARP_PASSWORD_CANCEL("&cWarp canceled"),
    COMMAND_FWARP_NOACCESS("&cYou do not have permission to use {0} &cwarps"),

    COMMAND_WARPOTHER_COMMANDFORMAT("&e/f warpother <faction> <warpname> [password]"),

    COMMAND_HELP_404("&cThis page does not exist"),
    COMMAND_HELP_NEXTCREATE("&eLearn how to create a faction on the next page."),
    COMMAND_HELP_INVITATIONS("command.help.invitations", "&eYou might want to close it and use invitations:"),
    COMMAND_HELP_HOME("&eAnd don't forget to set your home:"),
    COMMAND_HELP_BANK_1("&eYour faction has a bank which is used to pay for certain"),
    COMMAND_HELP_BANK_2("&ethings, so it will need to have money deposited into it."),
    COMMAND_HELP_BANK_3("&eTo learn more, use the money command."),
    COMMAND_HELP_PLAYERTITLES("&ePlayer titles are just for fun. No rules connected to them."),
    COMMAND_HELP_OWNERSHIP_1("&eClaimed land with ownership set is further protected so"),
    COMMAND_HELP_OWNERSHIP_2("&ethat only the owner(s), faction admin, and possibly the"),
    COMMAND_HELP_OWNERSHIP_3("&efaction moderators have full access."),
    COMMAND_HELP_RELATIONS_1("&eSet the relation you WISH to have with another faction."),
    COMMAND_HELP_RELATIONS_2("&eYour default relation with other factions will be neutral."),
    COMMAND_HELP_RELATIONS_3("&eIf BOTH factions choose \"ally\" you will be allies."),
    COMMAND_HELP_RELATIONS_4("&eIf ONE faction chooses \"enemy\" you will be enemies."),
    COMMAND_HELP_RELATIONS_5("&eYou can never hurt members or allies."),
    COMMAND_HELP_RELATIONS_6("&eYou can not hurt neutrals in their own territory."),
    COMMAND_HELP_RELATIONS_7("&eYou can always hurt enemies and players without faction."),
    COMMAND_HELP_RELATIONS_8(""),
    COMMAND_HELP_RELATIONS_9("&eDamage from enemies is reduced in your own territory."),
    COMMAND_HELP_RELATIONS_10("&eWhen you die you lose power. It is restored over time."),
    COMMAND_HELP_RELATIONS_11("&eThe power of a faction is the sum of all member power."),
    COMMAND_HELP_RELATIONS_12("&eThe power of a faction determines how much land it can hold."),
    COMMAND_HELP_RELATIONS_13("&eYou can claim land from factions with too little power."),
    COMMAND_HELP_PERMISSIONS_1("&eOnly faction members can build and destroy in their own"),
    COMMAND_HELP_PERMISSIONS_2("&eterritory. Usage of the following items is also restricted:"),
    COMMAND_HELP_PERMISSIONS_3("&eDoor, Chest, Furnace, Dispenser, Diode."),
    COMMAND_HELP_PERMISSIONS_4(""),
    COMMAND_HELP_PERMISSIONS_5("&eMake sure to put pressure plates in front of doors for your"),
    COMMAND_HELP_PERMISSIONS_6("&eguest visitors. Otherwise they can't get through. You can"),
    COMMAND_HELP_PERMISSIONS_7("&ealso use this to create member only areas."),
    COMMAND_HELP_PERMISSIONS_8("&eAs dispensers are protected, you can create traps without"),
    COMMAND_HELP_PERMISSIONS_9("&eworrying about those arrows getting stolen."),
    COMMAND_HELP_ADMIN_1("&b/f claim safezone &eclaim land for the safe zone"),
    COMMAND_HELP_ADMIN_2("&b/f claim warzone &eclaim land for the war zone"),
    COMMAND_HELP_ADMIN_3("&b/f autoclaim [safezone|warzone] &etake a guess"),
    COMMAND_HELP_MOAR_1("Finally some commands for the server admins:"),
    COMMAND_HELP_MOAR_2("&eMore commands for server admins:"),
    COMMAND_HELP_MOAR_3("&eEven more commands for server admins:"),
    COMMAND_HELP_DESCRIPTION("Display a help page"),

    COMMAND_HOME_DENIED("&cSorry, you cannot teleport to the home of {0}"),
    COMMAND_HOME_DISABLED("&cSorry, Faction homes are disabled on this server."),
    COMMAND_HOME_TELEPORTDISABLED("&cSorry, the ability to teleport to Faction homes is disabled on this server."),
    COMMAND_HOME_NOHOME("&cYour faction does not have a home. "),
    COMMAND_HOME_INENEMY("&cYou cannot teleport to your faction home while in the territory of an enemy faction."),
    COMMAND_HOME_WRONGWORLD("&cYou cannot teleport to your faction home while in a different world."),
    COMMAND_HOME_ENEMYNEAR("&cYou cannot teleport to your faction home while an enemy is within {0} blocks of you."),
    COMMAND_HOME_TOTELEPORT("to teleport to your faction home"),
    COMMAND_HOME_FORTELEPORT("for teleporting to your faction home"),
    COMMAND_HOME_DESCRIPTION("Teleport to the faction home"),

    COMMAND_INVITE_TOINVITE("to invite someone"),
    COMMAND_INVITE_FORINVITE("for inviting someone"),
    COMMAND_INVITE_CLICKTOJOIN("Click to join!"),
    COMMAND_INVITE_INVITEDYOU(" has invited you to join "),
    COMMAND_INVITE_INVITED("{0}&e invited {0}&e to your faction."),
    COMMAND_INVITE_ALREADYMEMBER("{0}&e is already a member of {1}"),
    COMMAND_INVITE_DESCRIPTION("Invite a player to your faction"),
    COMMAND_INVITE_BANNED("&7{0} &cis banned from your Faction. Not sending an invite."),

    COMMAND_JOIN_CANNOTFORCE("&cYou do not have permission to move other players into a faction."),
    COMMAND_JOIN_SYSTEMFACTION("&cPlayers may only join normal factions. This is a system faction."),
    COMMAND_JOIN_ALREADYMEMBER("&c{0} {1} already a member of {2}"),
    COMMAND_JOIN_ATLIMIT(" &c!&f The faction {0} is at the limit of {1} members, so {2} cannot currently join."),
    COMMAND_JOIN_INOTHERFACTION("&c{0} must leave {1} current faction first."),
    COMMAND_JOIN_NEGATIVEPOWER("&c{0} cannot join a faction with a negative power level."),
    COMMAND_JOIN_REQUIRESINVITATION("&eThis faction requires invitation."),
    COMMAND_JOIN_ATTEMPTEDJOIN("{0}&e tried to join your faction."),
    COMMAND_JOIN_TOJOIN("to join a faction"),
    COMMAND_JOIN_FORJOIN("for joining a faction"),
    COMMAND_JOIN_SUCCESS("&e{0} successfully joined {1}."),
    COMMAND_JOIN_MOVED("&e{0} moved you into the faction {1}."),
    COMMAND_JOIN_JOINED("&e{0} joined your faction."),
    COMMAND_JOIN_JOINEDLOG("{0} joined the faction {1}."),
    COMMAND_JOIN_MOVEDLOG("{0} moved the player {1} into the faction {2}."),
    COMMAND_JOIN_DESCRIPTION("Join a faction"),
    COMMAND_JOIN_BANNED("&cYou are banned from {0} &c:("),

    COMMAND_KICK_CANDIDATES("Players you can kick: "),
    COMMAND_KICK_CLICKTOKICK("Click to kick "),
    COMMAND_KICK_SELF("&cYou cannot kick yourself."),
    COMMAND_KICK_NONE("That player is not in a faction."),
    COMMAND_KICK_NOTMEMBER("{0}&c is not a member of {1}"),
    COMMAND_KICK_INSUFFICIENTRANK("&cYour rank is too low to kick this player."),
    COMMAND_KICK_NEGATIVEPOWER("&cYou cannot kick that member until their power is positive."),
    COMMAND_KICK_TOKICK("to kick someone from the faction"),
    COMMAND_KICK_FORKICK("for kicking someone from the faction"),
    COMMAND_KICK_FACTION("{0}&e kicked {1}&e from the faction! :O"), //message given to faction members
    COMMAND_KICK_KICKS("&eYou kicked {0}&e from the faction {1}&e!"), //kicker perspective
    COMMAND_KICK_KICKED("{0}&e kicked you from {1}&e! :O"), //kicked player perspective
    COMMAND_KICK_DESCRIPTION("Kick a player from the faction"),

    COMMAND_LIST_FACTIONLIST("Faction List "),
    COMMAND_LIST_TOLIST("to list the factions"),
    COMMAND_LIST_FORLIST("for listing the factions"),
    COMMAND_LIST_ONLINEFACTIONLESS("Online factionless: "),
    COMMAND_LIST_DESCRIPTION("See a list of the factions"),

    COMMAND_LOCK_LOCKED("&eFactions is now locked"),
    COMMAND_LOCK_UNLOCKED("&eFactions in now unlocked"),
    COMMAND_LOCK_DESCRIPTION("Lock all write stuff. Apparently."),

    COMMAND_LOGINS_TOGGLE("&eSet login / logout notifications for Faction members to: &6{0}"),
    COMMAND_LOGINS_DESCRIPTION("Toggle(?) login / logout notifications for Faction members"),

    COMMAND_MAP_TOSHOW("to show the map"),
    COMMAND_MAP_FORSHOW("for showing the map"),
    COMMAND_MAP_UPDATE_ENABLED("&eMap auto update &2ENABLED."),
    COMMAND_MAP_UPDATE_DISABLED("&eMap auto update &4DISABLED."),
    COMMAND_MAP_DESCRIPTION("Show the territory map, and set optional auto update"),

    COMMAND_MAPHEIGHT_DESCRIPTION("&eUpdate the lines that /f map sends"),
    COMMAND_MAPHEIGHT_SET("&eSet /f map lines to &a{0}"),
    COMMAND_MAPHEIGHT_CURRENT("&eCurrent mapheight: &a{0}"),

    COMMAND_MOD_CANDIDATES("Players you can promote: "),
    COMMAND_MOD_CLICKTOPROMOTE("Click to promote "),
    COMMAND_MOD_NOTMEMBER("{0}&c is not a member in your faction."),
    COMMAND_MOD_NOTADMIN("&cYou are not the faction admin."),
    COMMAND_MOD_SELF("&cThe target player musn't be yourself."),
    COMMAND_MOD_TARGETISADMIN("&cThe target player is a faction admin. Demote them first."),
    COMMAND_MOD_REVOKES("&eYou have removed moderator status from {0}&e."),
    COMMAND_MOD_REVOKED("{0}&e is no longer moderator in your faction."),
    COMMAND_MOD_PROMOTES("{0}&e was promoted to moderator in your faction."),
    COMMAND_MOD_PROMOTED("&eYou have promoted {0}&e to moderator."),
    COMMAND_MOD_DESCRIPTION("Give or revoke moderator rights"),

    COMMAND_MODIFYPOWER_ADDED("&eAdded &6{0} &epower to &6{1}. &eNew total rounded power: &6{2}"),
    COMMAND_MODIFYPOWER_DESCRIPTION("Modify the power of a faction/player"),

    COMMAND_MONEY_LONG("&eThe faction money commands."),
    COMMAND_MONEY_DESCRIPTION("Faction money commands"),

    COMMAND_MONEYBALANCE_SHORT("show faction balance"),
    COMMAND_MONEYBALANCE_DESCRIPTION("Show your factions current money balance"),

    COMMAND_MONEYDEPOSIT_DESCRIPTION("Deposit money"),
    COMMAND_MONEYDEPOSIT_DEPOSITED("{0} deposited {1} in the faction bank: {2}"),

    COMMAND_MONEYTRANSFERFF_DESCRIPTION("Transfer f -> f"),
    COMMAND_MONEYTRANSFERFF_TRANSFER("{0} transferred {1} from the faction \"{2}\" to the faction \"{3}\""),

    COMMAND_MONEYTRANSFERFP_DESCRIPTION("Transfer f -> p"),
    COMMAND_MONEYTRANSFERFP_TRANSFER("{0} transferred {1} from the faction \"{2}\" to the player \"{3}\""),

    COMMAND_MONEYTRANSFERPF_DESCRIPTION("Transfer p -> f"),
    COMMAND_MONEYTRANSFERPF_TRANSFER("{0} transferred {1} from the player \"{2}\" to the faction \"{3}\""),

    COMMAND_MONEYWITHDRAW_DESCRIPTION("Withdraw money"),
    COMMAND_MONEYWITHDRAW_WITHDRAW("{0} withdrew {1} from the faction bank: {2}"),

    COMMAND_NEAR_DESCRIPTION("Show nearby faction members"),
    COMMAND_NEAR_PLAYER("&a&l{role-prefix}&8{name} (&d{distance}m&8)"),
    COMMAND_NEAR_PLAYERLIST("&eNear: {players-nearby}"),
    COMMAND_NEAR_NONE("&8None"),

    COMMAND_OPEN_TOOPEN("to open or close the faction"),
    COMMAND_OPEN_FOROPEN("for opening or closing the faction"),
    COMMAND_OPEN_OPEN("open"),
    COMMAND_OPEN_CLOSED("closed"),
    COMMAND_OPEN_CHANGES("{0}&e changed the faction to &d{1}&e."),
    COMMAND_OPEN_CHANGED("&eThe faction {0}&e is now {1}"),
    COMMAND_OPEN_DESCRIPTION("Switch if invitation is required to join"),

    COMMAND_OWNER_DISABLED("&cSorry, but owned areas are disabled on this server."),
    COMMAND_OWNER_LIMIT("&cSorry, but you have reached the server's &dlimit of {0} &cowned areas per faction."),
    COMMAND_OWNER_WRONGFACTION("&cThis land is not claimed by your faction, so you can't set ownership of it."),
    COMMAND_OWNER_NOTCLAIMED("&cThis land is not claimed by a faction. Ownership is not possible."),
    COMMAND_OWNER_NOTMEMBER("{0}&e is not a member of this faction."),
    COMMAND_OWNER_CLEARED("&eYou have cleared ownership for this claimed area."),
    COMMAND_OWNER_REMOVED("&eYou have removed ownership of this claimed land from {0}&e."),
    COMMAND_OWNER_TOSET("to set ownership of claimed land"),
    COMMAND_OWNER_FORSET("for setting ownership of claimed land"),
    COMMAND_OWNER_ADDED("&eYou have added {0}&e to the owner list for this claimed land."),
    COMMAND_OWNER_DESCRIPTION("Set ownership of claimed land"),

    COMMAND_OWNERLIST_DISABLED("&cSorry, but owned areas are disabled on this server."),//dup->
    COMMAND_OWNERLIST_WRONGFACTION("&cThis land is not claimed by your faction."),//eq
    COMMAND_OWNERLIST_NOTCLAIMED("&eThis land is not claimed by any faction, thus no owners."),//eq
    COMMAND_OWNERLIST_NONE("&eNo owners are set here; everyone in the faction has access."),
    COMMAND_OWNERLIST_OWNERS("&eCurrent owner(s) of this land: {0}"),
    COMMAND_OWNERLIST_DESCRIPTION("List owner(s) of this claimed land"),

    COMMAND_PEACEFUL_DESCRIPTION("Set a faction to peaceful"),
    COMMAND_PEACEFUL_YOURS("{0} has {1} your faction"),
    COMMAND_PEACEFUL_OTHER("{0}&e has {1} the faction '{2}&e'."),
    COMMAND_PEACEFUL_GRANT("granted peaceful status to"),
    COMMAND_PEACEFUL_REVOKE("removed peaceful status from"),

    COMMAND_PERM_DESCRIPTION("&6Edit or list your Faction's permissions."),
    COMMAND_PERM_INVALID_RELATION("Invalid relation defined. Try something like 'ally'"),
    COMMAND_PERM_INVALID_ACCESS("Invalid access defined. Try something like 'allow'"),
    COMMAND_PERM_INVALID_ACTION("Invalid action defined. Try something like 'build'"),
    COMMAND_PERM_SET("&aSet permission &e{0} &ato &b{1} &afor relation &c{2}"),
    COMMAND_PERM_INVALID_SET("&cCannot set a locked permission"),
    COMMAND_PERM_TOP("RCT MEM OFF ALLY TRUCE NEUT ENEMY"),

    COMMAND_PERMANENT_DESCRIPTION("Toggles a faction's permanence"),
    COMMAND_PERMANENT_GRANT("added permanent status to"),
    COMMAND_PERMANENT_REVOKE("removed permanent status from"),
    COMMAND_PERMANENT_YOURS("{0} has {0} your faction"),
    COMMAND_PERMANENT_OTHER("{0}&e has {1} the faction '{2}&e'."),
    COMMAND_PROMOTE_TARGET("You've been {0} to {1}"),
    COMMAND_PROMOTE_SUCCESS("You successfully {0} {1} to {2}"),
    COMMAND_PROMOTE_PROMOTED("promoted"),
    COMMAND_PROMOTE_DEMOTED("demoted"),

    COMMAND_PERMANENTPOWER_DESCRIPTION("Toggle faction power permanence"),
    COMMAND_PERMANENTPOWER_GRANT("added permanentpower status to"),
    COMMAND_PERMANENTPOWER_REVOKE("removed permanentpower status from"),
    COMMAND_PERMANENTPOWER_SUCCESS("&eYou {0} &d{1}&e."),
    COMMAND_PERMANENTPOWER_FACTION("{0}&e {1} your faction"),

    COMMAND_PROMOTE_DESCRIPTION("/f promote <name>"),
    COMMAND_PROMOTE_WRONGFACTION("{0} is not part of your faction."),
    COMMAND_NOACCESS("You don't have access to that."),
    COMMAND_PROMOTE_NOTTHATPLAYER("That player cannot be promoted."),
    COMMAND_PROMOTE_NOT_ALLOWED("You can't promote or demote that player."),

    COMMAND_POWER_TOSHOW("to show player power info"),
    COMMAND_POWER_FORSHOW("for showing player power info"),
    COMMAND_POWER_POWER("{0}&6 - Power / Maxpower: &e{1} / {2} {3}"),
    COMMAND_POWER_BONUS(" (bonus: "),
    COMMAND_POWER_PENALTY(" (penalty: "),
    COMMAND_POWER_DESCRIPTION("Show player power info"),

    COMMAND_POWERBOOST_HELP_1("&cYou must specify \"p\" or \"player\" to target a player or \"f\" or \"faction\" to target a faction."),
    COMMAND_POWERBOOST_HELP_2("&cex. /f powerboost p SomePlayer 0.5  -or-  /f powerboost f SomeFaction -5"),
    COMMAND_POWERBOOST_INVALIDNUM("&cYou must specify a valid numeric value for the power bonus/penalty amount."),
    COMMAND_POWERBOOST_PLAYER("Player \"{0}\""),
    COMMAND_POWERBOOST_FACTION("Faction \"{0}\""),
    COMMAND_POWERBOOST_BOOST("&e{0} now has a power bonus/penalty of {1} to min and max power levels."),
    COMMAND_POWERBOOST_BOOSTLOG("{0} has set the power bonus/penalty for {1} to {2}."),
    COMMAND_POWERBOOST_DESCRIPTION("Apply permanent power bonus/penalty to specified player or faction"),

    COMMAND_RELATIONS_ALLTHENOPE("&cNope! You can't."),
    COMMAND_RELATIONS_MORENOPE("&cNope! You can't declare a relation to yourself :)"),
    COMMAND_RELATIONS_ALREADYINRELATIONSHIP("&cYou already have that relation wish set with {0}."),
    COMMAND_RELATIONS_TOMARRY("to change a relation wish"),
    COMMAND_RELATIONS_FORMARRY("for changing a relation wish"),
    COMMAND_RELATIONS_MUTUAL("&eYour faction is now {0}&e to {1}"),
    COMMAND_RELATIONS_PEACEFUL("&eThis will have no effect while your faction is peaceful."),
    COMMAND_RELATIONS_PEACEFULOTHER("&eThis will have no effect while their faction is peaceful."),
    COMMAND_RELATIONS_DESCRIPTION("Set relation wish to another faction"),
    COMMAND_RELATIONS_EXCEEDS_ME("&eFailed to set relation wish. You can only have {0} {1}."),
    COMMAND_RELATIONS_EXCEEDS_THEY("&eFailed to set relation wish. They can only have {0} {1}."),

    COMMAND_RELATIONS_PROPOSAL_1("{0}&e wishes to be your {1}"),
    COMMAND_RELATIONS_PROPOSAL_2("&eType &b/{0} {1} {2}&e to accept."),
    COMMAND_RELATIONS_PROPOSAL_SENT("{0}&e were informed that you wish to be {1}"),

    COMMAND_RELOAD_TIME("&eReloaded &dall configuration files &efrom disk, took &d{0} ms&e."),
    COMMAND_RELOAD_DESCRIPTION("Reload data file(s) from disk"),

    COMMAND_SAFEUNCLAIMALL_DESCRIPTION("Unclaim all safe zone land"),
    COMMAND_SAFEUNCLAIMALL_UNCLAIMED("&eYou unclaimed ALL safe zone land."),
    COMMAND_SAFEUNCLAIMALL_UNCLAIMEDLOG("{0} unclaimed all safe zones."),

    COMMAND_SAVEALL_SUCCESS("&eFactions saved to disk!"),
    COMMAND_SAVEALL_DESCRIPTION("Save all data to disk"),

    COMMAND_SCOREBOARD_DESCRIPTION("Scoreboardy things"),

    COMMAND_SETDEFAULTROLE_DESCRIPTION("/f defaultrole <role> - set your Faction's default role."),
    COMMAND_SETDEFAULTROLE_NOTTHATROLE("You cannot set the default to admin."),
    COMMAND_SETDEFAULTROLE_SUCCESS("Set default role of your faction to {0}"),
    COMMAND_SETDEFAULTROLE_INVALIDROLE("Couldn't find matching role for {0}"),

    COMMAND_SETFWARP_NOTCLAIMED("&eYou can only set warps in your faction territory."),
    COMMAND_SETFWARP_LIMIT("&eYour Faction already has the max amount of warps set &6({0})."),
    COMMAND_SETFWARP_SET("&eSet warp &6{0}&e and password &b'{1}' &eto your location."),
    COMMAND_SETFWARP_TOSET("to set warp"),
    COMMAND_SETFWARP_FORSET("for setting warp"),
    COMMAND_SETFWARP_DESCRIPTION("Set a faction warp"),

    COMMAND_SETHOME_DISABLED("&cSorry, Faction homes are disabled on this server."),
    COMMAND_SETHOME_NOTCLAIMED("&cSorry, your faction home can only be set inside your own claimed territory."),
    COMMAND_SETHOME_TOSET("to set the faction home"),
    COMMAND_SETHOME_FORSET("for setting the faction home"),
    COMMAND_SETHOME_SET("{0}&e set the home for your faction. You can now use:"),
    COMMAND_SETHOME_SETOTHER("&cYou have set the home for the {0}&e faction."),
    COMMAND_SETHOME_DESCRIPTION("Set the faction home"),

    COMMAND_SETMAXVAULTS_DESCRIPTION("Set max vaults for a Faction."),
    COMMAND_SETMAXVAULTS_SUCCESS("&aSet max vaults for &e{0} &ato &b{1}"),

    COMMAND_TNT_TERRITORYONLY("&cCommand can only be run from your faction's territory!"),
    COMMAND_TNT_DEPOSIT_DESCRIPTION("Add to your faction's TNT bank"),
    COMMAND_TNT_DEPOSIT_FAIL_FULL("&cFaction bank already at maximum!"),
    COMMAND_TNT_DEPOSIT_FAIL_NOTENOUGH("&cYou do not have that much TNT!"),
    COMMAND_TNT_DEPOSIT_FAIL_POSITIVE("&cMust deposit at least one!"),
    COMMAND_TNT_DEPOSIT_SUCCESS("&eYour faction now has {0} TNT"),
    COMMAND_TNT_FILL_DESCRIPTION("Fill TNT into nearby dispensers"),
    COMMAND_TNT_FILL_MESSAGE("&eFilled {0} TNT into {1} dispensers. {2} left in the faction bank."),
    COMMAND_TNT_FILL_FAIL_MAXRADIUS("&c{0} is bigger than the maximum radius of {1}"),
    COMMAND_TNT_FILL_FAIL_NOTENOUGH("&cThe faction bank does not have {0} TNT!"),
    COMMAND_TNT_FILL_FAIL_POSITIVE("&cPositive values only!"),
    COMMAND_TNT_INFO_DESCRIPTION("View your faction's TNT bank"),
    COMMAND_TNT_INFO_MESSAGE("&eYour faction has {0} TNT"),
    COMMAND_TNT_SIPHON_DESCRIPTION("Take TNT from nearby dispensers"),
    COMMAND_TNT_SIPHON_MESSAGE("&eAcquired {0} TNT, for a total of {0} in the faction bank."),
    COMMAND_TNT_SIPHON_FAIL_POSITIVE("&cPositive values only!"),
    COMMAND_TNT_SIPHON_FAIL_FULL("&cFaction bank already at maximum!"),
    COMMAND_TNT_SIPHON_FAIL_MAXRADIUS("&c{0} is bigger than the maximum radius of {1}"),
    COMMAND_TNT_WITHDRAW_DESCRIPTION("Withdraw TNT from the faction bank"),
    COMMAND_TNT_WITHDRAW_MESSAGE("&eWithdrew {0} TNT. {1} left in the faction bank."),
    COMMAND_TNT_WITHDRAW_FAIL_NOTENOUGH("&cThe faction bank does not have {0} TNT!"),
    COMMAND_TNT_WITHDRAW_FAIL_POSITIVE("&cPositive values only!"),

    COMMAND_VAULT_DESCRIPTION("/f vault <number> to open one of your Faction's vaults."),
    COMMAND_VAULT_TOOHIGH("&cYou tried to open vault {0} but your Faction only has {1} vaults."),

    COMMAND_SEECHUNK_DESCRIPTION("Show chunk boundaries"),
    COMMAND_SEECHUNK_TOGGLE("&eSeechunk &d{0}"),

    COMMAND_SHOW_NOFACTION_SELF("You are not in a faction"),
    COMMAND_SHOW_NOFACTION_OTHER("That's not a faction"),
    COMMAND_SHOW_TOSHOW("to show faction information"),
    COMMAND_SHOW_FORSHOW("for showing faction information"),
    COMMAND_SHOW_DESCRIPTION("&6Description: &e{0}"),
    COMMAND_SHOW_PEACEFUL("This faction is Peaceful"),
    COMMAND_SHOW_PERMANENT("&6This faction is permanent, remaining even with no members."),
    COMMAND_SHOW_JOINING("&6Joining: &e{0} "),
    COMMAND_SHOW_INVITATION("invitation is required"),
    COMMAND_SHOW_UNINVITED("no invitation is needed"),
    COMMAND_SHOW_NOHOME("n/a"),
    COMMAND_SHOW_POWER("&6Land / Power / Maxpower: &e {0}/{1}/{2} {3}."),
    COMMAND_SHOW_BONUS(" (bonus: "),
    COMMAND_SHOW_PENALTY(" (penalty: "),
    COMMAND_SHOW_DEPRECIATED("({0} depreciated)"),
    COMMAND_SHOW_LANDVALUE("&6Total land value: &e{0} {1}"),
    COMMAND_SHOW_BANKCONTAINS("&6Bank contains: &e{0}"),
    COMMAND_SHOW_ALLIES("Allies: "),
    COMMAND_SHOW_ENEMIES("Enemies: "),
    COMMAND_SHOW_MEMBERSONLINE("Members online: "),
    COMMAND_SHOW_MEMBERSOFFLINE("Members offline: "),
    COMMAND_SHOW_COMMANDDESCRIPTION("Show faction information"),
    COMMAND_SHOW_DEATHS_TIL_RAIDABLE("&eDTR: {0}"),
    COMMAND_SHOW_EXEMPT("&cThis faction is exempt and cannot be seen."),

    COMMAND_SHOWINVITES_PENDING("Players with pending invites: "),
    COMMAND_SHOWINVITES_CLICKTOREVOKE("Click to revoke invite for {0}"),
    COMMAND_SHOWINVITES_DESCRIPTION("Show pending faction invites"),

    COMMAND_STATUS_FORMAT("{0} Power: {1} Last Seen: {2}"),
    COMMAND_STATUS_ONLINE("Online"),
    COMMAND_STATUS_AGOSUFFIX(" ago."),
    COMMAND_STATUS_DESCRIPTION("Show the status of a player"),

    COMMAND_STUCK_TIMEFORMAT("m 'minutes', s 'seconds.'"),
    COMMAND_STUCK_CANCELLED("&6Teleport cancelled because you were damaged"),
    COMMAND_STUCK_OUTSIDE("&6Teleport cancelled because you left &e{0} &6block radius"),
    COMMAND_STUCK_EXISTS("&6You are already teleporting, you must wait &e{0}"),
    COMMAND_STUCK_START("&6Teleport will commence in &e{0}&6. Don't take or deal damage. "),
    COMMAND_STUCK_TELEPORT("&6Teleported safely to {0}, {1}, {2}."),
    COMMAND_STUCK_TOSTUCK("to safely teleport {0} out"),
    COMMAND_STUCK_FORSTUCK("for {0} initiating a safe teleport out"),
    COMMAND_STUCK_DESCRIPTION("Safely teleports you out of enemy faction"),

    COMMAND_TAG_TAKEN("&cThat tag is already taken"),
    COMMAND_TAG_TOCHANGE("to change the faction tag"),
    COMMAND_TAG_FORCHANGE("for changing the faction tag"),
    COMMAND_TAG_FACTION("{0}&e changed your faction tag to {1}"),
    COMMAND_TAG_CHANGED("&eThe faction {0}&e changed their name to {1}."),
    COMMAND_TAG_DESCRIPTION("Change the faction tag"),

    COMMAND_TITLE_TOCHANGE("to change a players title"),
    COMMAND_TITLE_FORCHANGE("for changing a players title"),
    COMMAND_TITLE_CHANGED("{0}&e changed a title: {1}"),
    COMMAND_TITLE_DESCRIPTION("Set or remove a players title"),

    COMMAND_TOGGLEALLIANCECHAT_DESCRIPTION("Toggles whether or not you will see alliance chat"),
    COMMAND_TOGGLEALLIANCECHAT_IGNORE("Alliance chat is now ignored"),
    COMMAND_TOGGLEALLIANCECHAT_UNIGNORE("Alliance chat is no longer ignored"),

    COMMAND_TOGGLESB_DISABLED("You can't toggle scoreboards while they are disabled."),

    COMMAND_TOP_DESCRIPTION("Sort Factions to see the top of some criteria."),
    COMMAND_TOP_TOP("Top Factions by {0}. Page {1}/{2}"),
    COMMAND_TOP_LINE("{0}. &6{1}: &c{2}"), // Rank. Faction: Value
    COMMAND_TOP_INVALID("Could not sort by {0}. Try balance, online, members, power or land."),

    COMMAND_UNBAN_DESCRIPTION("Unban someone from your Faction"),
    COMMAND_UNBAN_NOTBANNED("&7{0} &cisn't banned. Not doing anything."),
    COMMAND_UNBAN_UNBANNED("&e{0} &cunbanned &7{1}"),
    COMMAND_UNBAN_TARGET("&aYou were unbanned from &r{0}"),

    COMMAND_UNCLAIM_SAFEZONE_SUCCESS("&eSafe zone was unclaimed."),
    COMMAND_UNCLAIM_SAFEZONE_NOPERM("&cThis is a safe zone. You lack permissions to unclaim."),
    COMMAND_UNCLAIM_WARZONE_SUCCESS("&eWar zone was unclaimed."),
    COMMAND_UNCLAIM_WARZONE_NOPERM("&cThis is a war zone. You lack permissions to unclaim."),
    COMMAND_UNCLAIM_UNCLAIMED("{0}&e unclaimed some of your land."),
    COMMAND_UNCLAIM_UNCLAIMS("&eYou unclaimed this land."),
    COMMAND_UNCLAIM_WRONGFACTIONOTHER("&cAttempted to unclaim land for incorrect faction"),
    COMMAND_UNCLAIM_LOG("{0} unclaimed land at ({1}) from the faction: {2}"),
    COMMAND_UNCLAIM_WRONGFACTION("&cYou don't own this land."),
    COMMAND_UNCLAIM_TOUNCLAIM("to unclaim this land"),
    COMMAND_UNCLAIM_FORUNCLAIM("for unclaiming this land"),
    COMMAND_UNCLAIM_FACTIONUNCLAIMED("{0}&e unclaimed some land."),
    COMMAND_UNCLAIM_DESCRIPTION("Unclaim the land where you are standing"),

    COMMAND_UNCLAIMALL_TOUNCLAIM("to unclaim all faction land"),
    COMMAND_UNCLAIMALL_FORUNCLAIM("for unclaiming all faction land"),
    COMMAND_UNCLAIMALL_UNCLAIMED("{0}&e unclaimed ALL of your faction's land."),
    COMMAND_UNCLAIMALL_LOG("{0} unclaimed everything for the faction: {1}"),
    COMMAND_UNCLAIMALL_DESCRIPTION("Unclaim all of your factions land"),

    COMMAND_VERSION_VERSION("&eYou are running {0}"),
    COMMAND_VERSION_DESCRIPTION("Show plugin and translation version information"),

    COMMAND_WARUNCLAIMALL_DESCRIPTION("Unclaim all war zone land"),
    COMMAND_WARUNCLAIMALL_SUCCESS("&eYou unclaimed ALL war zone land."),
    COMMAND_WARUNCLAIMALL_LOG("{0} unclaimed all war zones."),


    /**
     * Leaving - This is accessed through a command, and so it MAY need a COMMAND_* slug :s
     */
    LEAVE_PASSADMIN("&cYou must give the admin role to someone else first."),
    LEAVE_NEGATIVEPOWER("&cYou cannot leave until your power is positive."),
    LEAVE_TOLEAVE("to leave your faction."),
    LEAVE_FORLEAVE("for leaving your faction."),
    LEAVE_LEFT("{0}&e left faction {1}&e."),
    LEAVE_DISBANDED("&e{0}&e was disbanded."),
    LEAVE_DISBANDEDLOG("The faction {0} ({1}) was disbanded due to the last player ({2}) leaving."),
    LEAVE_DESCRIPTION("Leave your faction"),

    /**
     * Claiming - Same as above basically. No COMMAND_* because it's not in a command class, but...
     */
    CLAIM_PROTECTED("&cThis land is protected"),
    CLAIM_DISABLED("&cSorry, this world has land claiming disabled."),
    CLAIM_CANTCLAIM("&cYou can't claim land for &d{0}&c."),
    CLAIM_ALREADYOWN("{0}&e already own this land."),
    CLAIM_MUSTBE("&cYou must be &d{0}&c to claim land."),
    CLAIM_MEMBERS("Factions must have at least &d{0}&c members to claim land."),
    CLAIM_SAFEZONE("&cYou can not claim a safe zone."),
    CLAIM_WARZONE("&cYou can not claim a war zone."),
    CLAIM_POWER("&cYou can't claim more land! You need more power!"),
    CLAIM_DTR_LAND("&cYou can't claim more land!"),
    CLAIM_LIMIT("&cLimit reached. You can't claim more land!"),
    CLAIM_ALLY("&cYou can't claim the land of your allies."),
    CLAIM_CONTIGIOUS("&cYou can only claim additional land which is connected to your first claim or controlled by another faction!"),
    CLAIM_FACTIONCONTIGUOUS("&cYou can only claim additional land which is connected to your first claim!"),
    CLAIM_PEACEFUL("{0}&e owns this land. Your faction is peaceful, so you cannot claim land from other factions."),
    CLAIM_PEACEFULTARGET("{0}&e owns this land, and is a peaceful faction. You cannot claim land from them."),
    CLAIM_THISISSPARTA("{0}&e owns this land and is strong enough to keep it."),
    CLAIM_BORDER("&cYou must start claiming land at the border of the territory."),
    CLAIM_TOCLAIM("to claim this land"),
    CLAIM_FORCLAIM("for claiming this land"),
    CLAIM_TOOVERCLAIM("to overclaim this land"),
    CLAIM_FOROVERCLAIM("for over claiming this land"),
    CLAIM_CLAIMED("&d{0}&e claimed land for &d{1}&e from &d{2}&e."),
    CLAIM_CLAIMEDCOMPACT_ANNOUNCE("&d{0} &eclaimed land for &d{1}&e:"),
    CLAIM_CLAIMEDCOMPACT_SUCCESSES("&a    ➥ Successes: &b{0}"),
    CLAIM_CLAIMEDCOMPACT_FAILURES("&c    ➥ Failures: &b{0}"),
    CLAIM_CLAIMEDLOG("{0} claimed land at ({1}) for the faction: {2}"),
    CLAIM_OVERCLAIM_DISABLED("&eOver claiming is disabled on this server."),
    CLAIM_TOOCLOSETOOTHERFACTION("&eYour claim is too close to another Faction. Buffer required is {0}"),
    CLAIM_OUTSIDEWORLDBORDER("&eYour claim is outside the border."),
    CLAIM_OUTSIDEBORDERBUFFER("&eYour claim is outside the border. {0} chunks away world edge required."),
    CLAIM_CLICK_TO_CLAIM("Click to try to claim &2({0}, {1})"),
    CLAIM_YOUAREHERE("You are here"),

    /**
     * More generic, or less easily categorisable translations, which may apply to more than one class
     */
    GENERIC_YOU("you"),
    GENERIC_YOURFACTION("your faction"),
    GENERIC_NOPERMISSION("&cYou don't have permission to {0}."),
    GENERIC_DOTHAT("do that"),  //Ugh nuke this from high orbit
    GENERIC_NOPLAYERMATCH("&cNo player match found for \"&3{0}&c\"."),
    GENERIC_NOPLAYERFOUND("&cNo player \"&3{0}&c\" could not be found."),
    GENERIC_NOFACTIONMATCH("&cNo faction match found for \"&3{0}&c\"."),
    GENERIC_ARGS_TOOFEW("&cToo few arguments. &eUse like this:"),
    GENERIC_ARGS_TOOMANY("&cStrange argument \"&3{0}&c\". &eUse the command like this:"),
    GENERIC_DEFAULTDESCRIPTION("Default faction description :("),
    GENERIC_OWNERS("Owner(s): {0}"),
    GENERIC_PUBLICLAND("Public faction land."),
    GENERIC_FACTIONLESS("factionless"),
    GENERIC_SERVERADMIN("A server admin"),
    GENERIC_DISABLED("disabled"),
    GENERIC_ENABLED("enabled"),
    GENERIC_INFINITY("∞"),
    GENERIC_CONSOLEONLY("This command cannot be run as a player."),
    GENERIC_PLAYERONLY("&cThis command can only be used by ingame players."),
    GENERIC_MEMBERONLY("&cYou are not member of any faction."),
    GENERIC_ASKYOURLEADER("&e Ask your leader to:"),
    GENERIC_YOUSHOULD("&eYou should:"),
    GENERIC_YOUMAYWANT("&eYou may want to: "),
    GENERIC_DISABLEDWORLD("&cFactions is disabled in this world."),
    GENERIC_YOUMUSTBE("&cYou must be &d{0}&c."),
    GENERIC_TRANSLATION_VERSION("Translation: {0}({1},{2}) State: {3}"),
    GENERIC_TRANSLATION_CONTRIBUTORS("Translation contributors: {0}"),
    GENERIC_TRANSLATION_RESPONSIBLE("Responsible for translation: {0}"),
    GENERIC_FACTIONTAG_BLACKLIST("&eThat faction tag is blacklisted."),
    GENERIC_FACTIONTAG_TOOSHORT("&eThe faction tag can't be shorter than &d{0}&e chars."),
    GENERIC_FACTIONTAG_TOOLONG("&eThe faction tag can't be longer than &d{0}&e chars."),
    GENERIC_FACTIONTAG_ALPHANUMERIC("&eFaction tag must be alphanumeric. \"&d{0}&e\" is not allowed."),
    GENERIC_PLACEHOLDER("<This is a placeholder for a message you should not see>"),

    /**
     * Clip placeholder stuff
     */
    PLACEHOLDER_ROLE_NAME("None"),
    PLACEHOLDER_CUSTOM_FACTION("{faction} "),

    /**
     * ASCII compass (for chat map)
     */
    COMPASS_SHORT_NORTH("N"),
    COMPASS_SHORT_EAST("E"),
    COMPASS_SHORT_SOUTH("S"),
    COMPASS_SHORT_WEST("W"),

    /**
     * Chat modes
     */
    CHAT_COLEADER("coleader chat"),
    CHAT_MOD("mod chat"),
    CHAT_FACTION("faction chat"),
    CHAT_ALLIANCE("alliance chat"),
    CHAT_TRUCE("truce chat"),
    CHAT_PUBLIC("public chat"),

    /**
     * Economy stuff
     */

    ECON_OFF("no {0}"), // no balance, no value, no refund, etc
    ECON_FORMAT("###,###.###"),
    ECON_DISABLED("Faction econ is disabled."),
    ECON_OVER_BAL_CAP("&4The amount &e{0} &4is over Essentials' balance cap."),
    ECON_BALANCE("&6{0}'s&e balance is &d{1}&e."),
    ECON_NOPERM("&d{0}&e lacks permission to control &d{0}'s&e money."),
    ECON_CANTAFFORD_TRANSFER("&d{0}<b> can't afford to transfer &d{1}<b> to {2}<b>."),
    ECON_CANTAFFORD_AMOUNT("&d{0}&e can't afford &d{1}&e {2}."),
    ECON_TRANSFER_UNABLE("Unable to transfer {0}<b> to &d{1}<b> from &d{2}<b>."),
    ECON_TRANSFER_NOINVOKER("&d{0}&e was transferred from &d{1}&e to &d{2}&e."),
    ECON_TRANSFER_GAVE("&d{0}&e &dgave {1}&e to &d{2}&e."),
    ECON_TRANSFER_TOOK("&d{0}&e &dtook {1}&e from &d{2}&e."),
    ECON_TRANSFER_TRANSFER("&d{0}&e transferred &d{1}&e from &d{2}&e to &d{3}&e."),
    ECON_GAIN_SUCCESS("&d{0}&e gained &d{1}&e {2}."),
    ECON_GAIN_FAILURE("&d{0}&e would have gained &d{1}&e {2}, but the deposit failed."),
    ECON_LOST_SUCCESS("&d{0}&e lost &d{1}&e {2}."),
    ECON_LOST_FAILURE("&d{0}&e can't afford &d{1}&e {2}."),

    /**
     * Relations
     */
    RELATION_MEMBER_SINGULAR("member"),
    RELATION_MEMBER_PLURAL("members"),
    RELATION_ALLY_SINGULAR("ally"),
    RELATION_ALLY_PLURAL("allies"),
    RELATION_TRUCE_SINGULAR("truce"),
    RELATION_TRUCE_PLURAL("truces"),
    RELATION_NEUTRAL_SINGULAR("neutral"),
    RELATION_NEUTRAL_PLURAL("neutrals"),
    RELATION_ENEMY_SINGULAR("enemy"),
    RELATION_ENEMY_PLURAL("enemies"),

    /**
     * Roles
     */
    ROLE_ADMIN("admin"),
    ROLE_COLEADER("coleader"),
    ROLE_MODERATOR("moderator"),
    ROLE_NORMAL("normal member"),
    ROLE_RECRUIT("recruit"),

    /**
     * Region types.
     */
    REGION_SAFEZONE("safezone"),
    REGION_WARZONE("warzone"),
    REGION_WILDERNESS("wilderness"),

    REGION_PEACEFUL("peaceful territory"),
    /**
     * In the player and entity listeners
     */
    PLAYER_CANTHURT("&eYou may not harm other players in {0}"),
    PLAYER_SAFEAUTO("&eThis land is now a safe zone."),
    PLAYER_WARAUTO("&eThis land is now a war zone."),
    PLAYER_OUCH("&cOuch, that is starting to hurt. You should give it a rest."),
    PLAYER_USE_WILDERNESS("&cYou can't use &d{0}&c in the wilderness."),
    PLAYER_USE_SAFEZONE("&cYou can't use &d{0}&c in a safe zone."),
    PLAYER_USE_WARZONE("&cYou can't use &d{0}&c in a war zone."),
    PLAYER_USE_TERRITORY("&cYou can't &d{0}&c in the territory of &d{1}&c."),
    PLAYER_USE_OWNED("&cYou can't use &d{0}&c in this territory, it is owned by: {1}&c."),
    PLAYER_COMMAND_WARZONE("&cYou can't use the command '{0}' in war zone."),
    PLAYER_COMMAND_NEUTRAL("&cYou can't use the command '{0}' in neutral territory."),
    PLAYER_COMMAND_ENEMY("&cYou can't use the command '{0}' in enemy territory."),
    PLAYER_COMMAND_PERMANENT("&cYou can't use the command '{0}' because you are in a permanent faction."),
    PLAYER_COMMAND_ALLY("&cYou can't use the command '{0}' in ally territory."),
    PLAYER_COMMAND_WILDERNESS("&cYou can't use the command '{0}' in the wilderness."),

    PLAYER_PORTAL_NOTALLOWED("&cDestination portal can't be created there."),

    PLAYER_POWER_NOLOSS_PEACEFUL("&eYou didn't lose any power since you are in a peaceful faction."),
    PLAYER_POWER_NOLOSS_WORLD("&eYou didn't lose any power due to the world you died in."),
    PLAYER_POWER_NOLOSS_WILDERNESS("&eYou didn't lose any power since you were in the wilderness."),
    PLAYER_POWER_NOLOSS_WARZONE("&eYou didn't lose any power since you were in a war zone."),
    PLAYER_POWER_LOSS_WARZONE("&cThe world you are in has power loss normally disabled, but you still lost power since you were in a war zone.\n&eYour power is now &d{0} / {1}"),
    PLAYER_POWER_NOW("&eYour power is now &d{0} / {1}"),

    PLAYER_PVP_LOGIN("&eYou can't hurt other players for {0} seconds after logging in."),
    PLAYER_PVP_REQUIREFACTION("&eYou can't hurt other players until you join a faction."),
    PLAYER_PVP_FACTIONLESS("&eYou can't hurt players who are not currently in a faction."),
    PLAYER_PVP_PEACEFUL("&ePeaceful players cannot participate in combat."),
    PLAYER_PVP_NEUTRAL("&eYou can't hurt neutral factions. Declare them as an enemy."),
    PLAYER_PVP_CANTHURT("&eYou can't hurt {0}&e."),

    PLAYER_PVP_NEUTRALFAIL("&eYou can't hurt {0}&e in their own territory unless you declare them as an enemy."),
    PLAYER_PVP_TRIED("{0}&e tried to hurt you."),

    PERM_BUILD("Building blocks"),
    PERM_DESTROY("Breaking blocks"),
    PERM_PAINBUILD("If allow, can build but hurts to do so"),
    PERM_ITEM("Using items"),
    PERM_CONTAINER("Opening any block that can store items"),
    PERM_BUTTON("Using buttons"),
    PERM_DOOR("Opening doors"),
    PERM_LEVER("Using levers"),
    PERM_PLATE("Using pressure plates"),
    PERM_FROSTWALK("Walking on water with the frostwalk enchantment"),
    PERM_INVITE("Inviting others to join the faction"),
    PERM_KICK("Kicking members from the faction"),
    PERM_BAN("Banning players from the faction"),
    PERM_PROMOTE("Promoting members of the faction"),
    PERM_DISBAND("Disbanding the entire faction"),
    PERM_ECONOMY("Spending faction money"),
    PERM_TERRITORY("Claiming or unclaiming faction territory"),
    PERM_HOME("Visiting the faction home"),
    PERM_SETHOME("Setting the faction home"),
    PERM_SETWARP("Setting and unsetting faction warps"),
    PERM_TNTDEPOSIT("Deposit TNT into faction bank"),
    PERM_TNTWITHDRAW("Withdraw TNT from faction bank"),
    PERM_WARP("Using faction warps"),
    PERM_FLY("Flying in faction territory"),
    PERM_OWNER("Set ownership of land using /f owner in faction territory"),

    PERM_SHORT_BUILD("build"),
    PERM_SHORT_DESTROY("destroy"),
    PERM_SHORT_PAINBUILD("painbuild"),
    PERM_SHORT_ITEM("use items"),
    PERM_SHORT_CONTAINER("open containers"),
    PERM_SHORT_BUTTON("use buttons"),
    PERM_SHORT_DOOR("open doors"),
    PERM_SHORT_LEVER("use levers"),
    PERM_SHORT_PLATE("use pressure plates"),
    PERM_SHORT_FROSTWALK("frostwalk"),
    PERM_SHORT_INVITE("invite"),
    PERM_SHORT_KICK("kick"),
    PERM_SHORT_BAN("ban"),
    PERM_SHORT_PROMOTE("promote"),
    PERM_SHORT_DISBAND("disband"),
    PERM_SHORT_ECONOMY("spend faction money"),
    PERM_SHORT_TERRITORY("manage faction territory"),
    PERM_SHORT_HOME("visit home"),
    PERM_SHORT_SETHOME("set home"),
    PERM_SHORT_SETWARP("set warps"),
    PERM_SHORT_TNTDEPOSIT("deposit TNT"),
    PERM_SHORT_TNTWITHDRAW("withdraw TNT"),
    PERM_SHORT_WARP("use warps"),
    PERM_SHORT_FLY("fly"),
    PERM_SHORT_OWNER("set ownership"),

    PERM_DENIED_WILDERNESS("&cYou can't {0} in the wilderness"),
    PERM_DENIED_SAFEZONE("&c>You can't {0} in a safe zone"),
    PERM_DENIED_WARZONE("&cYou can't {0} in a war zone"),
    PERM_DENIED_TERRITORY("&cYou can't {0} in the territory of {1}"),
    PERM_DENIED_PAINTERRITORY("&cIt is painful to {0} in the territory of {1}"),
    PERM_DENIED_OWNED("&cYou can't {0} in this territory, it is owned by: {1}"),
    PERM_DENIED_PAINOWNED("&cIt is painful to try to {0} in this territory, it is owned by: {1}"),

    GUI_WARPS_ONE_PAGE("{0} warps"),
    GUI_WARPS_PAGE("{0} warps (page {1})"),

    GUI_PERMS_RELATION_NAME("Choose a relation{0}:"),
    GUI_PERMS_RELATION_ONLINEOFFLINEBIT(" ({0})"),

    GUI_PERMS_ACTION_NAME("Permissions: {0}{1}"),
    GUI_PERMS_ACTION_ONLINEOFFLINEBIT(" ({0})"),

    GUI_PERMS_TOGGLE("Toggle online/offline"),
    GUI_PERMS_ONLINE("online"),
    GUI_PERMS_OFFLINE("offline"),

    GUI_BUTTON_NEXT("NEXT"),
    GUI_BUTTON_PREV("PREVIOUS"),
    GUI_BUTTON_BACK("BACK"),

    /**
     * Strings lying around in other bits of the plugins
     */
    NOPAGES("&eSorry. No Pages available."),
    INVALIDPAGE("&eInvalid page. Must be between 1 and {0}"),

    /**
     * The ones here before I started messing around with this
     */
    TITLE("title", "&bFactions &0|&r"),
    WILDERNESS("wilderness", "&2Wilderness"),
    WILDERNESS_DESCRIPTION("wilderness-description", ""),
    WARZONE("warzone", "&4Warzone"),
    WARZONE_DESCRIPTION("warzone-description", "Not the safest place to be."),
    SAFEZONE("safezone", "&6Safezone"),
    SAFEZONE_DESCRIPTION("safezone-description", "Free from pvp and monsters."),
    TOGGLE_SB("toggle-sb", "You now have scoreboards set to {value}"),
    FACTION_LEAVE("faction-leave", "&6Leaving {0}, &6Entering {1}"),
    FACTIONS_ANNOUNCEMENT_TOP("faction-announcement-top", "&d--Unread Faction Announcements--"),
    FACTIONS_ANNOUNCEMENT_BOTTOM("faction-announcement-bottom", "&d--Unread Faction Announcements--"),
    DEFAULT_PREFIX("default-prefix", "{relationcolor}[{faction}] &r"),
    FACTION_LOGIN("faction-login", "&e{0} &9logged in."),
    FACTION_LOGOUT("faction-logout", "&e{0} &9logged out.."),
    NOFACTION_PREFIX("nofactions-prefix", "&6[&ano-faction&6]&r"),
    DATE_FORMAT("date-format", "MM/d/yy h:ma"), // 3/31/15 07:49AM

    /**
     * Raidable is used in multiple places. Allow more than just true/false.
     */
    RAIDABLE_TRUE("raidable-true", "true"),
    RAIDABLE_FALSE("raidable-false", "false"),
    /**
     * Warmups
     */
    WARMUPS_NOTIFY_FLIGHT("&eFlight will enable in &d{0} &eseconds."),
    WARMUPS_NOTIFY_TELEPORT("&eYou will teleport to &d{0} &ein &d{1} &eseconds."),
    WARMUPS_ALREADY("&cYou are already warming up."),
    WARMUPS_CANCELLED("&cYou have cancelled your warmup."),
    /**
     * DTR
     */
    DTR_CANNOT_FROZEN("&cAction denied due to frozen DTR"),
    DTR_KICK_PENALTY("&cPenalty DTR lost due to kicking with frozen DTR"),
    DTR_FROZEN_STATUS_MESSAGE("{0}"),
    DTR_FROZEN_STATUS_TRUE("Frozen"),
    DTR_FROZEN_STATUS_FALSE("Not frozen"),
    DTR_FROZEN_TIME_MESSAGE("{0}"),
    DTR_FROZEN_TIME_NOTFROZEN(""),

    FACTIONS_DATA_LOADING("Server data loading... try again later."),
    FACTIONS_DATA_ALREADY_SAVING("Factions data is already saving... try again later.")

    ;

    private String path;
    private String def;

    private static Path PATH;
    private static FileConfiguration CONFIG;
    private static SimpleDateFormat SIMPLE_DATE_FORMAT;

    private static Formattable<TL> DELEGATE = new IndexedFormatter();

    private static final Map<TL, String> TO_STRINGS = new EnumMap<>(TL.class);

    private static final ReentrantReadWriteLock.ReadLock LOCK = new ReentrantReadWriteLock().readLock();

    public static final TL[] VALUES = values();

    /**
     * Lang enum constructor.
     *
     * @param path  The string path.
     * @param start The default string.
     */
    TL(String path, String start) {
        this.path = path;
        this.def = start;
    }

    /**
     * Lang enum constructor. Use this when your desired path simply exchanges '_' for '.'
     *
     * @param start The default string.
     */
    TL(String start) {
        this.path = TextUtil.replace(this.name(), "_", ".");
        if (this.path.startsWith(".")) {
            path = "root" + path;
        }
        this.def = start;
    }

    public static void inheritFrom(Path path, FileConfiguration fileConfiguration) {
        try {
            LOCK.lock();
            PATH = path;
            CONFIG = fileConfiguration;

            reload();

            SIMPLE_DATE_FORMAT = new SimpleDateFormat(DATE_FORMAT.toString());
        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            LOCK.unlock();
        }
    }

    public TextComponent toComponent() {
        return TextUtil.parseFancy(toString()).build();
    }

    public TextComponent toFormattedComponent(String... args) {
        return TextComponent.of(format(args));
    }

    public String toRawString() {
        return this == TITLE ? TextUtil.parseColorBukkit(CONFIG.getString(this.path, this.def)) + " " : TextUtil.parseColorBukkit(CONFIG.getString(this.path, this.def));
    }

    @Override
    public String toString() {
        return TO_STRINGS.get(this);
    }

    public static String formatDate(Date date) {
        return SIMPLE_DATE_FORMAT.format(date);
    }

    public static String formatDate(long date) {
        return SIMPLE_DATE_FORMAT.format(date);
    }

    public static void reload() {
        TO_STRINGS.clear();
        try {
            DELEGATE.close();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        if (FactionsPlugin.getInstance().conf().lang().getFormatType().equalsIgnoreCase("LOOSE")) {
            if (DELEGATE instanceof IndexedFormatter) {
                DELEGATE = new LooseFormatter();
                CONFIG.set("lang-type", "LOOSE");
                try {
                    CONFIG.save(PATH.toFile());
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            }
        } else {
            if (DELEGATE instanceof LooseFormatter) {
                DELEGATE = new IndexedFormatter();
                CONFIG.set("lang-type", "INDEX");
                try {
                    CONFIG.save(PATH.toFile());
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            }
        }
        for (TL value : VALUES) {
            String string = value.toRawString();
            TO_STRINGS.put(value, string);
            if (DELEGATE.contains(string)) {
                DELEGATE.recache(value, string);
            }
        }
    }

    public static void setTransition(boolean transition) {
        HoconConfigurationLoader loader = Loader.getLoader("main");
        try {
            FactionsPlugin.getInstance().conf().lang().setLangTransitioned(transition);
            CommentedConfigurationNode node = loader.load();
            node.getNode("lang").getNode("langTransitioned").setValue(transition);
            loader.save(node);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public static boolean formatContains(String string) {
        return DELEGATE.contains(string);
    }

    public static Formattable<TL> getParent() {
        return DELEGATE;
    }


    /**
     * Get the default value of the path.
     *
     * @return The default value of the path.
     */
    public String getDefault() {
        return this.def;
    }

    /**
     * Get the path to the string.
     *
     * @return The path to the string.
     */
    public String getPath() {
        return this.path;
    }

    public String format(String... values) {
        return format(this, values);
    }

    @Override
    public boolean contains(String string) {
        return DELEGATE.contains(string);
    }

    @Override
    public String format(TL tl, String... values) {
        return DELEGATE.format(tl, values);
    }

    @Override
    public Component toFormattedComponent(TL tl, String... values) {
        return DELEGATE.toFormattedComponent(tl, values);
    }

    @Override
    public TextComponent.Builder toFormattedComponentBuilder(TL tl, String... values) {
        return DELEGATE.toFormattedComponentBuilder(tl, values);
    }

    @Override
    public void recache(TL tl, String string) {
        DELEGATE.recache(tl, string);
    }

    @Override
    public void close() {
        try {
            DELEGATE.close();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
