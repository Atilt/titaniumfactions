package com.massivecraft.factions.listeners;

import com.massivecraft.factions.FactionsPlugin;
import com.massivecraft.factions.util.TL;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

public class FactionsDataListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void on(PlayerLoginEvent event) {
        if (!FactionsPlugin.getInstance().isFinishedLoading()) {
            event.disallow(PlayerLoginEvent.Result.KICK_OTHER, TL.isSet() ? TL.FACTIONS_DATA_LOADING.toString() : TL.FACTIONS_DATA_LOADING.getDefault());
        }
    }
}