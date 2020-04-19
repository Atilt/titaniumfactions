package com.massivecraft.factions.data;

import com.massivecraft.factions.Board;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.Factions;
import com.massivecraft.factions.FactionsPlugin;

import java.util.concurrent.atomic.AtomicBoolean;

public class SaveTask implements Runnable {

    private static AtomicBoolean running = new AtomicBoolean(false);

    private FactionsPlugin plugin;

    public SaveTask(FactionsPlugin plugin) {
        this.plugin = plugin;
    }

    public void run() {
        if (!plugin.getAutoSave() || running.get()) {
            return;
        }
        running.set(true);
        Factions.getInstance().forceSave(false, null);
        FPlayers.getInstance().forceSave(false, null);
        Board.getInstance().forceSave(false, result -> running.set(false));
    }
}
