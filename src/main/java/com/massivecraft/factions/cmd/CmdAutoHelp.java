package com.massivecraft.factions.cmd;

import com.massivecraft.factions.FactionsPlugin;
import com.massivecraft.factions.util.TL;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;

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

        ObjectList<String> lines = new ObjectArrayList<>(pcmd.helpLong);

        for (FCommand scmd : pcmd.subCommands) {
            if (scmd.visibility == CommandVisibility.VISIBLE) {
                lines.add(scmd.getUsageTemplate(context, true));
            }
            // TODO deal with other visibilities
        }

        context.sendMessage(FactionsPlugin.getInstance().txt().getPage(lines, context.argAsInt(0, 1), TL.COMMAND_AUTOHELP_HELPFOR.toString() + pcmd.aliases.get(0) + "\""));
    }

    @Override
    public TL getUsageTranslation() {
        return TL.COMMAND_HELP_DESCRIPTION;
    }
}
