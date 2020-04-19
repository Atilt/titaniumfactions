package com.massivecraft.factions.cmd;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.perms.PermissibleAction;
import com.massivecraft.factions.perms.Role;
import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.util.TL;

public class CmdUnban extends FCommand {

    public CmdUnban() {
        super();
        this.aliases.add("unban");

        this.requiredArgs.add("target");

        this.requirements = new CommandRequirements.Builder(Permission.BAN)
                .memberOnly()
                .withAction(PermissibleAction.BAN)
                .withRole(Role.MODERATOR)
                .build();
    }

    @Override
    public void perform(CommandContext context) {
        FPlayer target = context.argAsFPlayer(0);
        if (target == null) {
            return; // the above method sends a message if fails to find someone.
        }

        if (!context.faction.isBanned(target)) {
            context.msg(TL.COMMAND_UNBAN_NOTBANNED, target.getName());
            return;
        }

        context.faction.unban(target);

        context.faction.msg(TL.COMMAND_UNBAN_UNBANNED, context.fPlayer.getName(), target.getName());
        target.msg(TL.COMMAND_UNBAN_TARGET, context.faction.getTag(target));
    }

    @Override
    public TL getUsageTranslation() {
        return TL.COMMAND_UNBAN_DESCRIPTION;
    }
}
