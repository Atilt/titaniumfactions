package com.massivecraft.factions.landraidcontrol;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.FactionsPlugin;
import com.massivecraft.factions.cmd.CommandContext;
import com.massivecraft.factions.config.file.MainConfig;
import com.massivecraft.factions.util.TL;
import com.massivecraft.factions.util.WorldUtil;
import org.bukkit.entity.Player;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;

public class DTRControl implements LandRaidControl {

    public static String round(double dtr) {
        return BigDecimal.valueOf(dtr).setScale(conf().getDecimalDigits(), RoundingMode.UP).toPlainString();
    }

    private static MainConfig.Factions.LandRaidControl.DTR conf() {
        return FactionsPlugin.getInstance().conf().factions().landRaidControl().dtr();
    }

    @Override
    public boolean isRaidable(Faction faction) {
        return !faction.isPeaceful() && faction.getDTR() <= 0;
    }

    @Override
    public boolean hasLandInflation(Faction faction) {
        return false; // fail all attempts at claiming
    }

    @Override
    public int getLandLimit(Faction faction) {
        return conf().getLandStarting() + (faction.getSize() * conf().getLandPerPlayer());
    }

    @Override
    public boolean canJoinFaction(Faction faction, FPlayer player, CommandContext context) {
        if (faction.isFrozenDTR() && conf().isFreezePreventsJoin()) {
            context.msg(TL.DTR_CANNOT_FROZEN);
            return false;
        }
        return true;
    }

    @Override
    public boolean canLeaveFaction(FPlayer player) {
        if (player.getFaction().isFrozenDTR() && conf().isFreezePreventsLeave()) {
            player.msg(TL.DTR_CANNOT_FROZEN);
            return false;
        }
        return true;
    }

    @Override
    public boolean canDisbandFaction(Faction faction, CommandContext context) {
        if (faction.isFrozenDTR() && conf().isFreezePreventsDisband()) {
            context.msg(TL.DTR_CANNOT_FROZEN);
            return false;
        }
        return true;
    }

    @Override
    public boolean canKick(FPlayer toKick, CommandContext context) {
        if (toKick.getFaction().isNormal()) {
            Faction faction = toKick.getFaction();
            if (faction.isFrozenDTR() && conf().getFreezeKickPenalty() > 0) {
                faction.setDTR(Math.min(conf().getMinDTR(), faction.getDTR() - conf().getFreezeKickPenalty()));
                context.msg(TL.DTR_KICK_PENALTY);
            }
        }
        return true;
    }

    @Override
    public void onRespawn(FPlayer player) {
        // Handled on death
    }

    @Override
    public void update(FPlayer player) {
        if (player.getFaction().isNormal()) {
            this.updateDTR(player.getFaction());
        }
    }

    @Override
    public void onDeath(Player player) {
        Faction faction = FPlayers.getInstance().getByPlayer(player).getFaction();
        if (!faction.isNormal()) {
            return;
        }
        faction.setDTR(Math.max(conf().getMinDTR(), faction.getDTR() - conf().getLossPerDeath(player.getWorld())));
        faction.setFrozenDTR(Instant.now().toEpochMilli() + (conf().getFreezeTime() * 1000));
    }

    @Override
    public void onQuit(FPlayer player) {
        this.update(player);
    }

    @Override
    public void onJoin(FPlayer player) {
        if (player.getFaction().isNormal()) {
            this.updateDTR(player.getFaction(), 1);
        }
    }

    public void updateDTR(Faction faction) {
        this.updateDTR(faction, 0);
    }

    public void updateDTR(Faction faction, int minusPlayer) {
        long now = Instant.now().toEpochMilli();
        if (faction.getFrozenDTRUntilTime() > now) {
            // Not yet time to regen
            return;
        }
        long millisPassed = now - Math.max(faction.getLastDTRUpdateTime(), faction.getFrozenDTRUntilTime());

        int onlineInEnabledWorlds = 0;
        for (Player onlinePlayer : faction.getOnlinePlayers()) {
            if (!WorldUtil.isEnabled(onlinePlayer.getWorld())) {
                continue;
            }
            onlineInEnabledWorlds++;
        }
        double rate = Math.min(conf().getRegainPerMinuteMaxRate(), Math.max(0, onlineInEnabledWorlds - minusPlayer) * conf().getRegainPerMinutePerPlayer());
        double regain = (millisPassed / (60D * 1000D)) * rate;
        faction.setDTR(Math.min(faction.getDTRWithoutUpdate() + regain, this.getMaxDTR(faction)));
    }

    public double getMaxDTR(Faction faction) {
        return Math.min(conf().getStartingDTR() + (conf().getPerPlayer() * faction.getSize()), conf().getMaxDTR());
    }
}
