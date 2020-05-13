package com.massivecraft.factions.cmd;

import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.util.TL;

public class CmdDebug extends FCommand {
    public CmdDebug() {
        super();

        this.aliases.add("debug");
        this.aliases.add("helpme");
        this.optionalArgs.put("mini/full", "full");

        this.requirements = new CommandRequirements.Builder(Permission.DEBUG).build();
    }

    @Override
    public void perform(CommandContext context) {
        context.msg(TL.COMMAND_DEBUG_RUNNING);
    }

    @Override
    public TL getUsageTranslation() {
        return TL.COMMAND_DEBUG_DESCRIPTION;
    }
}
