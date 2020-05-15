package com.massivecraft.factions;

import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.Plugin;

import java.util.Set;
import java.util.logging.Logger;

public interface FactionsAPI {

    Logger getPluginLogger();

    default int getAPIVersion() {
        return 5;
    }

    boolean isAnotherPluginHandlingChat();

    void setHandlingChat(Plugin plugin, boolean handling);

    boolean shouldLetFactionsHandleThisChat(AsyncPlayerChatEvent event);

    boolean isPlayerFactionChatting(Player player);

    String getPlayerFactionTag(Player player);

    String getPlayerFactionTagRelation(Player speaker, Player listener);

    String getPlayerTitle(Player player);

    Set<String> getFactionTags();

    Set<String> getPlayersInFaction(String factionTag);

    @Deprecated
    Set<String> getOnlinePlayersInFaction(String factionTag);

    Set<Player> getRawOnlinePlayersInFaction(String factionTag);
}
