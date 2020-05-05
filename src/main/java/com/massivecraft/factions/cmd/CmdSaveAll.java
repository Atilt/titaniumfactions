package com.massivecraft.factions.cmd;

import com.massivecraft.factions.data.SaveTask;
import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.util.TL;

public class CmdSaveAll extends FCommand {

    public CmdSaveAll() {
        super();

        this.aliases.add("saveall");
        this.aliases.add("save");

        this.requirements = new CommandRequirements.Builder(Permission.SAVE).noDisableOnLock().build();
    }

    @Override
    public void perform(CommandContext context) {
        if (SaveTask.get().isSaving()) {
            context.msg(TL.FACTIONS_DATA_ALREADY_SAVING);
        }
        SaveTask.get().save(result -> context.msg(TL.COMMAND_SAVEALL_SUCCESS));
    }

    @Override
    public TL getUsageTranslation() {
        return TL.COMMAND_SAVEALL_DESCRIPTION;
    }
}