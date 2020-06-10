package com.massivecraft.factions.cmd.claim;

import com.massivecraft.factions.FLocation;
import com.massivecraft.factions.cmd.CommandContext;
import com.massivecraft.factions.cmd.CommandRequirements;
import com.massivecraft.factions.cmd.FCommand;
import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.util.TL;
import org.bukkit.Bukkit;
import org.bukkit.World;

public class CmdClaimAt extends FCommand {

    public CmdClaimAt() {
        super();
        this.aliases.add("claimat");

        this.requiredArgs.add("world");
        this.requiredArgs.add("x");
        this.requiredArgs.add("z");

        this.requirements = new CommandRequirements.Builder(Permission.CLAIMAT)
                .memberOnly()
                .build();
    }

    @Override
    public void perform(CommandContext context) {
        World world = Bukkit.getWorld(context.argAsString(0));
        if (world == null) {
            context.msg(TL.COMMAND_CLAIMAT_INVALID_WORLD);
            return;
        }
        FLocation location = FLocation.wrap(world.getName(), context.argAsInt(1), context.argAsInt(2));
        context.fPlayer.attemptClaim(context.faction, location, true);
    }

    @Override
    public TL getUsageTranslation() {
        return null;
    }
}
