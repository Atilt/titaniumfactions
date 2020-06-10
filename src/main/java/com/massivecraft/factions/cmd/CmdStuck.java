package com.massivecraft.factions.cmd;

import com.massivecraft.factions.Board;
import com.massivecraft.factions.FLocation;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.FactionsPlugin;
import com.massivecraft.factions.cooldown.StuckCooldown;
import com.massivecraft.factions.event.FPlayerTeleportEvent;
import com.massivecraft.factions.integration.Essentials;
import com.massivecraft.factions.math.FastMath;
import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.tasks.SpiralTask;
import com.massivecraft.factions.util.TL;
import com.massivecraft.factions.util.WorldUtil;
import io.papermc.lib.PaperLib;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.time.Duration;
import java.time.Instant;

public class CmdStuck extends FCommand {

    public CmdStuck() {
        super();

        this.aliases.add("stuck");
        this.aliases.add("halp!"); // halp! c:

        this.requirements = new CommandRequirements.Builder(Permission.STUCK).build();
    }

    @Override
    public void perform(final CommandContext context) {
        Player player = context.fPlayer.getPlayer();
        Location sentAt = player.getLocation();
        FLocation chunk = context.fPlayer.getLastStoodAt();
        // TODO handle delay 0
        long delay = FactionsPlugin.getInstance().conf().commands().stuck().getDelay();
        int radius = FactionsPlugin.getInstance().conf().commands().stuck().getRadius();

        StuckCooldown stuckCooldown = FactionsPlugin.getInstance().getStuckSessions().get(player.getUniqueId());

        if (stuckCooldown != null) {
            context.msg(TL.COMMAND_STUCK_EXISTS, stuckCooldown.getRemainingReadable());
        } else {
            FPlayerTeleportEvent tpEvent = new FPlayerTeleportEvent(context.fPlayer, null, FPlayerTeleportEvent.PlayerTeleportReason.STUCK);
            Bukkit.getPluginManager().callEvent(tpEvent);
            if (tpEvent.isCancelled()) {
                return;
            }

            // if economy is enabled, they're not on the bypass list, and this command has a cost set, make 'em pay
            if (!context.payForCommand(FactionsPlugin.getInstance().conf().economy().getCostStuck(), TL.COMMAND_STUCK_TOSTUCK.format(context.fPlayer.getName()), TL.COMMAND_STUCK_FORSTUCK.format(context.fPlayer.getName()))) {
                return;
            }

           int id = Bukkit.getScheduler().runTaskLater(FactionsPlugin.getInstance(), () -> {
               if (!FactionsPlugin.getInstance().getStuckSessions().containsKey(player.getUniqueId())) {
                   return;
               }

               // check for world difference or radius exceeding
               World world = chunk.getWorld();
               if (!world.equals(player.getWorld()) || sentAt.distanceSquared(player.getLocation()) > radius * radius) {
                   context.msg(TL.COMMAND_STUCK_OUTSIDE.format(Integer.toString(radius)));
                   FactionsPlugin.getInstance().getStuckSessions().remove(player.getUniqueId());
                   return;
               }
               // spiral task to find nearest wilderness chunk
               new SpiralTask(FLocation.wrap(context.player), radius * 2, null) {

                   @Override
                   public boolean work() {
                       FLocation chunk = currentFLocation();
                       if (chunk.isOutsideWorldBorder(FactionsPlugin.getInstance().conf().worldBorder().getBuffer())) {
                           return true;
                       }

                       Faction faction = Board.getInstance().getFactionAt(chunk);
                       if (faction.isWilderness()) {
                           int cx = WorldUtil.chunkToBlock(chunk.getX());
                           int cz = WorldUtil.chunkToBlock(chunk.getZ());
                           int y = world.getHighestBlockYAt(cx, cz);
                           Location tp = new Location(world, cx, y, cz);
                           context.msg(TL.COMMAND_STUCK_TELEPORT, Integer.toString(FastMath.floor(cx)), Integer.toString(y), Integer.toString(FastMath.floor(cz)));
                           FactionsPlugin.getInstance().getStuckSessions().remove(player.getUniqueId());
                           if (!Essentials.handleTeleport(player, tp)) {
                               PaperLib.teleportAsync(player, tp);
                           }
                           this.stop();
                           return false;
                       }
                       return true;
                   }
               };
            }, delay * 20L).getTaskId();
            stuckCooldown = new StuckCooldown(Instant.now(), Duration.ofSeconds(delay));
            stuckCooldown.setTaskId(id);

            FactionsPlugin.getInstance().getStuckSessions().put(player.getUniqueId(), stuckCooldown);

            context.msg(TL.COMMAND_STUCK_START, stuckCooldown.getDurationReadable());
        }
    }

    @Override
    public TL getUsageTranslation() {
        return TL.COMMAND_STUCK_DESCRIPTION;
    }
}
