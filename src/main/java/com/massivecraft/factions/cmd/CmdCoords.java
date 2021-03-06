package com.massivecraft.factions.cmd;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.math.FastMath;
import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.util.TL;
import org.bukkit.Location;

public class CmdCoords extends FCommand {

    public CmdCoords() {
        super();

        this.aliases.add("coords");
        this.aliases.add("coord");

        this.requirements = new CommandRequirements.Builder(Permission.COORDS)
                .memberOnly()
                .build();
    }

    @Override
    public void perform(CommandContext context) {
        Location location = context.player.getLocation();
        String message = TL.COMMAND_COORDS_MESSAGE.format(context.player.getDisplayName(), Integer.toString(FastMath.floor(location.getX())), Integer.toString(FastMath.floor(location.getY())), Integer.toString(FastMath.floor(location.getZ())), location.getWorld().getName());
        for (FPlayer fPlayer : context.faction.getFPlayers()) {
            fPlayer.sendMessage(message);
        }
    }

    @Override
    public TL getUsageTranslation() {
        return TL.COMMAND_COORDS_DESCRIPTION;
    }

}
