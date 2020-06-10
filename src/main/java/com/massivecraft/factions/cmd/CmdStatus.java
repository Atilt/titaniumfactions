package com.massivecraft.factions.cmd;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FactionsPlugin;
import com.massivecraft.factions.landraidcontrol.PowerControl;
import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.util.TL;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.ChatColor;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class CmdStatus extends FCommand {

    public CmdStatus() {
        super();

        this.aliases.add("status");
        this.aliases.add("s");

        this.requirements = new CommandRequirements.Builder(Permission.STATUS)
                .memberOnly()
                .build();
    }

    @Override
    public void perform(CommandContext context) {
        List<String> ret = new ArrayList<>(context.faction.getSize());
        for (FPlayer fp : context.faction.getFPlayers()) {
            String humanized = DurationFormatUtils.formatDurationWords(Instant.now().toEpochMilli() - fp.getLastLoginTime(), true, true) + TL.COMMAND_STATUS_AGOSUFFIX;
            String last = fp.isOnline() ? ChatColor.GREEN + TL.COMMAND_STATUS_ONLINE.toString() : (Instant.now().toEpochMilli() - fp.getLastLoginTime() < 432_000_000 ? ChatColor.YELLOW + humanized : ChatColor.RED + humanized);
            String power = FactionsPlugin.getInstance().getLandRaidControl() instanceof PowerControl ? ChatColor.YELLOW + Double.toString(fp.getPowerRounded()) + " / " + fp.getPowerMaxRounded() + ChatColor.RESET : "n/a";
            ret.add(TL.COMMAND_STATUS_FORMAT.format(ChatColor.GOLD + fp.getRole().getPrefix() + fp.getName() + ChatColor.RESET, power, last).trim());
        }
        context.fPlayer.sendMessage(ret);
    }

    @Override
    public TL getUsageTranslation() {
        return TL.COMMAND_STATUS_DESCRIPTION;
    }

}
