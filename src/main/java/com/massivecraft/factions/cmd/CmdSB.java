package com.massivecraft.factions.cmd;

import com.massivecraft.factions.FactionsPlugin;
import com.massivecraft.factions.meta.scoreboards.SidebarProvider;
import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.util.TL;
import com.massivecraft.factions.util.TextUtil;

public class CmdSB extends FCommand {

    public CmdSB() {
        super();

        this.aliases.add("sb");
        this.aliases.add("scoreboard");

        this.requirements = new CommandRequirements.Builder(Permission.SCOREBOARD)
                .playerOnly()
                .build();
    }

    @Override
    public void perform(CommandContext context) {
        if (!FactionsPlugin.getInstance().conf().scoreboard().constant().isEnabled()) {
            context.player.sendMessage(TL.COMMAND_TOGGLESB_DISABLED.toString());
            SidebarProvider.get().untrack(context.fPlayer);
            return;
        }
        if (context.fPlayer.showScoreboard()) {
            context.fPlayer.setShowScoreboard(false);
        } else {
            if (context.fPlayer.getScoreboardTextProvider() == null) {
                context.fPlayer.setTextProvider(SidebarProvider.DEFAULT_SIDEBAR);
            }
            context.fPlayer.setShowScoreboard(true);
        }
        context.player.sendMessage(TextUtil.replace(TL.TOGGLE_SB.toString(), "{value}", Boolean.toString(context.fPlayer.showScoreboard())));
    }

    @Override
    public TL getUsageTranslation() {
        return TL.COMMAND_SCOREBOARD_DESCRIPTION;
    }
}
