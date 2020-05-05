package com.massivecraft.factions.data;

import com.massivecraft.factions.Board;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.Factions;
import com.massivecraft.factions.FactionsPlugin;
import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.concurrent.atomic.AtomicBoolean;

public final class SaveTask extends BukkitRunnable implements AutoCloseable {

    private final AtomicBoolean saving = new AtomicBoolean(false);
    private int task = -1;

    private static SaveTask instance;

    private SaveTask() {}

    public static SaveTask get() {
        if (instance == null) {
            synchronized (SaveTask.class) {
                if (instance == null) {
                    instance = new SaveTask();
                }
            }
        }
        return instance;
    }

    public void start() {
        long interval = (long) (20 * 60 * FactionsPlugin.getInstance().conf().factions().other().getSaveToFileEveryXMinutes());
        this.task = this.runTaskTimer(FactionsPlugin.getInstance(), interval, interval).getTaskId();
    }

    @Override
    public void close() {
        if (task != -1) {
            Bukkit.getScheduler().cancelTask(this.task);
        }
    }

    public boolean isSaving() {
        return this.saving.get();
    }

    public void save(BooleanConsumer result) {
        saving.set(true);
        Factions.getInstance().forceSave(false, factionsResult -> {
            if (factionsResult) {
                FPlayers.getInstance().forceSave(false, playersResult -> {
                    if (playersResult) {
                        Board.getInstance().forceSave(false, boardResult -> {
                            saving.set(false);
                            if (result != null) {
                                result.accept(true);
                            }
                        });
                        return;
                    }
                    saving.set(false);
                    if (result != null) {
                        result.accept(false);
                    }
                });
                return;
            }
            saving.set(false);
            if (result != null) {
                result.accept(false);
            }
        });
    }

    public void run() {
        if (!FactionsPlugin.getInstance().getAutoSave() || this.saving.get()) {
            return;
        }
        this.save(null);
    }
}
