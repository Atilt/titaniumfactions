package com.massivecraft.factions.listeners;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.FactionsPlugin;
import com.massivecraft.factions.perms.Relation;
import com.massivecraft.factions.perms.Role;
import com.massivecraft.factions.struct.ChatMode;
import com.massivecraft.factions.util.TextUtil;
import com.massivecraft.factions.util.WorldUtil;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.UnknownFormatConversionException;

public class FactionsChatListener implements Listener {

    // this is for handling slashless command usage and faction/alliance chat, set at lowest priority so Factions gets to them first
    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onPlayerEarlyChat(AsyncPlayerChatEvent event) {
        if (!WorldUtil.isEnabled(event.getPlayer().getWorld())) {
            return;
        }

        Player talkingPlayer = event.getPlayer();
        String msg = event.getMessage();
        FPlayer me = FPlayers.getInstance().getByPlayer(talkingPlayer);
        ChatMode chat = me.getChatMode();
        //Is it a MOD chat
        if (chat == ChatMode.MOD) {
            Faction myFaction = me.getFaction();

            String message = String.format(FactionsPlugin.getInstance().conf().factions().chat().getModChatFormat(), ChatColor.stripColor(me.getNameAndTag()), msg);

            //Send to all mods
            for (FPlayer fplayer : FPlayers.getInstance().getOnlinePlayers()) {
                if (myFaction == fplayer.getFaction() && fplayer.getRole().isAtLeast(Role.MODERATOR)) {
                    fplayer.sendMessage(message);
                } else if (fplayer.isSpyingChat() && me != fplayer) {
                    fplayer.sendMessage("[MCspy]: " + message);
                }
            }

            FactionsPlugin.getInstance().getPluginLogger().info(ChatColor.stripColor("[ModChat] " + myFaction.getTag() + ": " + message));

            event.setCancelled(true);
        } else if (chat == ChatMode.FACTION) {
            Faction myFaction = me.getFaction();

            String message = String.format(FactionsPlugin.getInstance().conf().factions().chat().getFactionChatFormat(), me.describeTo(myFaction), msg);
            myFaction.sendMessage(message);

            FactionsPlugin.getInstance().getPluginLogger().info(ChatColor.stripColor("[FactionChat] " + myFaction.getTag() + ": " + message));

            //Send to any players who are spying chat
            for (FPlayer fplayer : FPlayers.getInstance().getOnlinePlayers()) {
                if (fplayer.isSpyingChat() && fplayer.getFaction() != myFaction && me != fplayer) {
                    fplayer.sendMessage("[FCspy] " + myFaction.getTag() + ": " + message);
                }
            }

            event.setCancelled(true);
        } else if (chat == ChatMode.ALLIANCE) {
            Faction myFaction = me.getFaction();

            String message = String.format(FactionsPlugin.getInstance().conf().factions().chat().getAllianceChatFormat(), ChatColor.stripColor(me.getNameAndTag()), msg);

            //Send message to our own faction
            myFaction.sendMessage(message);

            //Send to all our allies
            for (FPlayer fplayer : FPlayers.getInstance().getOnlinePlayers()) {
                if (myFaction.getRelationTo(fplayer) == Relation.ALLY && !fplayer.isIgnoreAllianceChat()) {
                    fplayer.sendMessage(message);
                } else if (fplayer.isSpyingChat() && me != fplayer) {
                    fplayer.sendMessage("[ACspy]: " + message);
                }
            }

            FactionsPlugin.getInstance().getPluginLogger().info(ChatColor.stripColor("[AllianceChat]: " + message));

            event.setCancelled(true);
        } else if (chat == ChatMode.TRUCE) {
            Faction myFaction = me.getFaction();

            String message = String.format(FactionsPlugin.getInstance().conf().factions().chat().getTruceChatFormat(), ChatColor.stripColor(me.getNameAndTag()), msg);

            //Send message to our own faction
            myFaction.sendMessage(message);

            //Send to all our truces
            for (FPlayer fplayer : FPlayers.getInstance().getOnlinePlayers()) {
                if (myFaction.getRelationTo(fplayer) == Relation.TRUCE) {
                    fplayer.sendMessage(message);
                } else if (fplayer.isSpyingChat() && fplayer != me) {
                    fplayer.sendMessage("[TCspy]: " + message);
                }
            }

            FactionsPlugin.getInstance().getPluginLogger().info(ChatColor.stripColor("[TruceChat]: " + message));
            event.setCancelled(true);
        }
    }

        // this is for handling insertion of the player's faction tag, set at highest priority to give other plugins a chance to modify chat first
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        if (!WorldUtil.isEnabled(event.getPlayer().getWorld())) {
            return;
        }

        // Are we to insert the Faction tag into the format?
        // If we are not to insert it - we are done.
        if (FactionsPlugin.getInstance().isAnotherPluginHandlingChat()) {
            return;
        }

        Player talkingPlayer = event.getPlayer();
        String msg = event.getMessage();
        String eventFormat = event.getFormat();
        FPlayer me = FPlayers.getInstance().getByPlayer(talkingPlayer);
        int insertIndex = FactionsPlugin.getInstance().conf().factions().chat().getTagInsertIndex();

        boolean padBefore = FactionsPlugin.getInstance().conf().factions().chat().isTagPadBefore();
        boolean padAfter = FactionsPlugin.getInstance().conf().factions().chat().isTagPadAfter();

        if (!FactionsPlugin.getInstance().conf().factions().chat().getTagReplaceString().isEmpty() && eventFormat.contains(FactionsPlugin.getInstance().conf().factions().chat().getTagReplaceString())) {
            // we're using the "replace" method of inserting the faction tags
            eventFormat = TextUtil.replace(eventFormat, "[FACTION_TITLE]", me.getTitle());
            insertIndex = eventFormat.indexOf(FactionsPlugin.getInstance().conf().factions().chat().getTagReplaceString());
            eventFormat = TextUtil.replace(eventFormat, FactionsPlugin.getInstance().conf().factions().chat().getTagReplaceString(), "");
            padBefore = false;
            padAfter = false;
        } else if (!FactionsPlugin.getInstance().conf().factions().chat().getTagInsertAfterString().isEmpty() && eventFormat.contains(FactionsPlugin.getInstance().conf().factions().chat().getTagInsertAfterString())) {
            // we're using the "insert after string" method
            insertIndex = eventFormat.indexOf(FactionsPlugin.getInstance().conf().factions().chat().getTagInsertAfterString()) + FactionsPlugin.getInstance().conf().factions().chat().getTagInsertAfterString().length();
        } else if (!FactionsPlugin.getInstance().conf().factions().chat().getTagInsertBeforeString().isEmpty() && eventFormat.contains(FactionsPlugin.getInstance().conf().factions().chat().getTagInsertBeforeString())) {
            // we're using the "insert before string" method
            insertIndex = eventFormat.indexOf(FactionsPlugin.getInstance().conf().factions().chat().getTagInsertBeforeString());
        } else if (!FactionsPlugin.getInstance().conf().factions().chat().isAlwaysShowChatTag()) {
            return;
        }

        String formatStart = eventFormat.substring(0, insertIndex) + ((padBefore && !me.getChatTag().isEmpty()) ? " " : "");
        String formatEnd = ((padAfter && !me.getChatTag().isEmpty()) ? " " : "") + eventFormat.substring(insertIndex);

        String nonColoredMsgFormat = formatStart + me.getChatTag().trim() + formatEnd;

        // Relation Colored?
        if (FactionsPlugin.getInstance().conf().factions().chat().isTagRelationColored()) {
            for (Player listeningPlayer : event.getRecipients()) {
                FPlayer you = FPlayers.getInstance().getByPlayer(listeningPlayer);
                String yourFormat = formatStart + me.getChatTag(you).trim() + formatEnd;
                try {
                    listeningPlayer.sendMessage(String.format(yourFormat, talkingPlayer.getDisplayName(), msg));
                } catch (UnknownFormatConversionException ex) {

                    FactionsPlugin.getInstance().getPluginLogger().warning("Critical error in chat message formatting!");
                    FactionsPlugin.getInstance().getPluginLogger().warning("NOTE: This can be fixed right now by setting chat tagInsertIndex to 0.");
                    return;
                }
            }

            // Messages are sent to players individually
            // This still leaves a chance for other plugins to pick it up
            event.getRecipients().clear();
        }
        // Message with no relation color.
        event.setFormat(nonColoredMsgFormat);
    }

}
