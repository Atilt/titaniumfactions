package com.massivecraft.factions.util;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FactionsPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public final class WarmUpUtil {

    /**
     * @param player         The player to notify.
     * @param translationKey The translation key used for notifying.
     * @param action         The action, inserted into the notification message.
     * @param runnable       The task to run after the delay. If the delay is 0, the task is instantly ran.
     * @param delay          The time used, in seconds, for the delay.
     *                       <p/>
     *                       note: for translations: {0}/[] = action, {1}/[] = delay
     */
    public static void process(FPlayer player, Warmup warmup, TL translationKey, String action, Runnable runnable, long delay) {
        if (delay > 0) {
            if (player.isWarmingUp()) {
                player.msg(TL.WARMUPS_ALREADY);
            } else {
                player.msg(translationKey.format(action, Long.toString(delay)));
                player.addWarmup(warmup, new BukkitRunnable() {
                    @Override
                    public void run() {
                        player.stopWarmup();
                        runnable.run();
                    }
                }.runTaskLater(FactionsPlugin.getInstance(), delay * 20L).getTaskId());
            }
        } else {
            runnable.run();
        }
    }

    public enum Warmup {
        HOME, WARP, FLIGHT
    }

}
