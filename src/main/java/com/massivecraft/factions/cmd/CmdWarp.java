package com.massivecraft.factions.cmd;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FactionsPlugin;
import com.massivecraft.factions.event.FPlayerTeleportEvent;
import com.massivecraft.factions.gui.WarpGUI;
import com.massivecraft.factions.perms.PermissibleAction;
import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.util.TL;
import com.massivecraft.factions.util.WarmUpUtil;
import io.papermc.lib.PaperLib;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class CmdWarp extends FCommand {

    public CmdWarp() {
        super();

        this.aliases.add("warp");
        this.aliases.add("warps");

        this.optionalArgs.put("warp", "warp");
        this.optionalArgs.put("password", "password");

        this.requirements = new CommandRequirements.Builder(Permission.WARP)
                .memberOnly()
                .withAction(PermissibleAction.WARP)
                .build();
    }

    @Override
    public void perform(CommandContext context) {
        // TODO: check if in combat.

        if (!context.faction.hasAccess(context.fPlayer, PermissibleAction.WARP)) {
            context.msg(TL.COMMAND_FWARP_NOACCESS, context.faction.getTag(context.fPlayer));
            return;
        }

        if (context.args.size() == 0) {
            new WarpGUI(context.fPlayer, context.faction).open();
        } else if (context.args.size() > 2) {
            context.msg(TL.COMMAND_FWARP_COMMANDFORMAT);
        } else {
            final String warpName = context.argAsString(0);
            final String passwordAttempt = context.argAsString(1);

            if (context.faction.isWarp(context.argAsString(0))) {
                // Check if requires password and if so, check if valid. CASE SENSITIVE
                if (context.faction.hasWarpPassword(warpName) && !context.faction.isWarpPassword(warpName, passwordAttempt)) {
                    context.fPlayer.msg(TL.COMMAND_FWARP_INVALID_PASSWORD);
                    return;
                }
                FPlayerTeleportEvent tpEvent = new FPlayerTeleportEvent(context.fPlayer, context.fPlayer.getFaction().getWarp(warpName).getLocation(), FPlayerTeleportEvent.PlayerTeleportReason.WARP);
                Bukkit.getPluginManager().callEvent(tpEvent);
                if (tpEvent.isCancelled()) {
                    return;
                }
                // Check transaction AFTER password check.
                if (!transact(context.fPlayer, context)) {
                    return;
                }
                FPlayer fPlayer = context.fPlayer;
                Player player = context.fPlayer.getPlayer();
                context.doWarmUp(WarmUpUtil.Warmup.WARP, TL.WARMUPS_NOTIFY_TELEPORT, warpName, () -> {
                    if (player.isOnline()) {
                        PaperLib.teleportAsync(player, fPlayer.getFaction().getWarp(warpName).getLocation());
                        fPlayer.msg(TL.COMMAND_FWARP_WARPED, warpName);
                    }
                }, this.plugin.conf().commands().warp().getDelay());
            } else {
                context.fPlayer.msg(TL.COMMAND_FWARP_INVALID_WARP, warpName);
            }
        }
    }

    private boolean transact(FPlayer player, CommandContext context) {
        return player.isAdminBypassing() || context.payForCommand(FactionsPlugin.getInstance().conf().economy().getCostWarp(), TL.COMMAND_FWARP_TOWARP.toString(), TL.COMMAND_FWARP_FORWARPING.toString());
    }


    @Override
    public TL getUsageTranslation() {
        return TL.COMMAND_FWARP_DESCRIPTION;
    }
}
