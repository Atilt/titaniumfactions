package com.massivecraft.factions.cmd;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.event.FPlayerTeleportEvent;
import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.util.TL;
import io.papermc.lib.PaperLib;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class CmdAHome extends FCommand {

    public CmdAHome() {
        super();

        this.aliases.add("ahome");

        this.requiredArgs.add("player");

        this.requirements = new CommandRequirements.Builder(Permission.AHOME).noDisableOnLock().build();
    }

    @Override
    public void perform(CommandContext context) {
        FPlayer target = context.argAsBestFPlayerMatch(0);
        if (target == null) {
            context.msg(TL.GENERIC_NOPLAYERMATCH, context.argAsString(0));
            return;
        }

        Player player = target.getPlayer();
        if (player != null) {
            Faction faction = target.getFaction();
            if (faction.hasHome()) {
                Location destination = faction.getHome();
                FPlayerTeleportEvent tpEvent = new FPlayerTeleportEvent(context.fPlayer, destination, FPlayerTeleportEvent.PlayerTeleportReason.AHOME);
                Bukkit.getPluginManager().callEvent(tpEvent);
                if (tpEvent.isCancelled()) {
                    return;
                }
                PaperLib.teleportAsync(player, destination);
                context.msg(TL.COMMAND_AHOME_SUCCESS, target.getName());
                target.msg(TL.COMMAND_AHOME_TARGET);
            } else {
                context.msg(TL.COMMAND_AHOME_NOHOME, target.getName());
            }
        } else {
            context.msg(TL.COMMAND_AHOME_OFFLINE, target.getName());
        }
    }

    @Override
    public TL getUsageTranslation() {
        return TL.COMMAND_AHOME_DESCRIPTION;
    }
}
