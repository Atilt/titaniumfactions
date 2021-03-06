package com.massivecraft.factions.cmd;

import com.massivecraft.factions.util.TL;
import com.massivecraft.factions.util.TextUtil;

import java.util.ArrayList;
import java.util.List;

public class CmdAutoHelp extends FCommand {

    public CmdAutoHelp() {
        super();

        this.aliases.add("?");
        this.aliases.add("h");
        this.aliases.add("help");

        this.setHelpShort("");

        this.optionalArgs.put("page", "1");
    }

    @Override
    public void perform(CommandContext context) {
        if (context.commandChain.size() == 0) {
            return;
        }
        FCommand pcmd = context.commandChain.get(context.commandChain.size() - 1);

        List<String> lines = new ArrayList<>(pcmd.helpLong);

        for (FCommand scmd : pcmd.subCommands) {
            if (scmd.visibility == CommandVisibility.VISIBLE) {
                lines.add(scmd.getUsageTemplate(context, true));
            }
            // TODO deal with other visibilities
        }

        context.sendMessage(TextUtil.getPage(lines, context.argAsInt(0, 1), TL.COMMAND_AUTOHELP_HELPFOR.toString() + pcmd.aliases.get(0) + "\""));
    }

    @Override
    public TL getUsageTranslation() {
        return TL.COMMAND_HELP_DESCRIPTION;
    }
}
