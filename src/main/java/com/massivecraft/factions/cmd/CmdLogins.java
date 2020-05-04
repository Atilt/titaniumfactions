package com.massivecraft.factions.cmd;

import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.util.TL;

public class CmdLogins extends FCommand {

    public CmdLogins() {
        super();
        this.aliases.add("login");
        this.aliases.add("logins");
        this.aliases.add("logout");
        this.aliases.add("logouts");

        this.requirements = new CommandRequirements.Builder(Permission.MONITOR_LOGINS)
                .memberOnly()
                .build();
    }

    @Override
    public void perform(CommandContext context) {
        boolean toggle = !context.fPlayer.isMonitoringJoins();
        context.msg(TL.COMMAND_LOGINS_TOGGLE, Boolean.toString(toggle));
        context.fPlayer.setMonitorJoins(toggle);
    }

    @Override
    public TL getUsageTranslation() {
        return TL.COMMAND_LOGINS_DESCRIPTION;
    }
}
