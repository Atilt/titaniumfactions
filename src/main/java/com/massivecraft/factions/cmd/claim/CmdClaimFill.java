package com.massivecraft.factions.cmd.claim;

import com.massivecraft.factions.Board;
import com.massivecraft.factions.FLocation;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.FactionsPlugin;
import com.massivecraft.factions.cmd.CommandContext;
import com.massivecraft.factions.cmd.CommandRequirements;
import com.massivecraft.factions.cmd.FCommand;
import com.massivecraft.factions.perms.PermissibleAction;
import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.util.TL;
import org.bukkit.Location;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

public class CmdClaimFill extends FCommand {

    public CmdClaimFill() {

        // Aliases
        this.aliases.add("claimfill");
        this.aliases.add("cf");

        // Args
        this.optionalArgs.put("limit", String.valueOf(FactionsPlugin.getInstance().conf().factions().claims().getFillClaimMaxClaims()));
        this.optionalArgs.put("faction", "you");

        this.requirements = new CommandRequirements.Builder(Permission.CLAIM_FILL)
                .playerOnly()
                .build();
    }

    @Override
    public void perform(CommandContext context) {
        // Args
        final int limit = context.argAsInt(0, FactionsPlugin.getInstance().conf().factions().claims().getFillClaimMaxClaims());

        if (limit > FactionsPlugin.getInstance().conf().factions().claims().getFillClaimMaxClaims()) {
            context.msg(TL.COMMAND_CLAIMFILL_ABOVEMAX, FactionsPlugin.getInstance().conf().factions().claims().getFillClaimMaxClaims());
            return;
        }

        final Faction forFaction = context.argAsFaction(2, context.faction);
        Location location = context.player.getLocation();
        FLocation loc = new FLocation(location);

        Faction currentFaction = Board.getInstance().getFactionAt(loc);

        if (currentFaction.equals(forFaction)) {
            context.msg(TL.CLAIM_ALREADYOWN, forFaction.describeTo(context.fPlayer, true));
            return;
        }

        if (!currentFaction.isWilderness()) {
            context.msg(TL.COMMAND_CLAIMFILL_ALREADYCLAIMED);
            return;
        }

        if (!context.fPlayer.isAdminBypassing() && !forFaction.hasAccess(context.fPlayer, PermissibleAction.TERRITORY)) {
            context.msg(TL.CLAIM_CANTCLAIM, forFaction.describeTo(context.fPlayer));
            return;
        }

        final double distance = FactionsPlugin.getInstance().conf().factions().claims().getFillClaimMaxDistance();
        long startX = loc.getX();
        long startZ = loc.getZ();

        Set<FLocation> toClaim = new HashSet<>();
        Queue<FLocation> queue = new LinkedList<>();
        FLocation currentHead;
        queue.add(loc);
        toClaim.add(loc);
        while (!queue.isEmpty() && toClaim.size() <= limit) {
            currentHead = queue.poll();

            if (Math.abs(currentHead.getX() - startX) > distance || Math.abs(currentHead.getZ() - startZ) > distance) {
                context.msg(TL.COMMAND_CLAIMFILL_TOOFAR, distance);
                return;
            }

            addIf(toClaim, queue, currentHead.getRelative(0, 1));
            addIf(toClaim, queue, currentHead.getRelative(0, -1));
            addIf(toClaim, queue, currentHead.getRelative(1, 0));
            addIf(toClaim, queue, currentHead.getRelative(-1, 0));
        }

        if (toClaim.size() > limit) {
            context.msg(TL.COMMAND_CLAIMFILL_PASTLIMIT);
            return;
        }

        if (toClaim.size() > this.plugin.getLandRaidControl().getPossibleClaimCount(forFaction)) {
            context.msg(TL.COMMAND_CLAIMFILL_NOTENOUGHLANDLEFT, forFaction.describeTo(context.fPlayer), toClaim.size());
            return;
        }

        final int limFail = FactionsPlugin.getInstance().conf().factions().claims().getRadiusClaimFailureLimit();
        int fails = 0;
        for (FLocation currentLocation : toClaim) {
            if (!context.fPlayer.attemptClaim(forFaction, currentLocation, true)) {
                fails++;
            }
            if (fails >= limFail) {
                context.msg(TL.COMMAND_CLAIMFILL_TOOMUCHFAIL, fails);
                return;
            }
        }
    }

    private void addIf(Set<FLocation> toClaim, Queue<FLocation> queue, FLocation examine) {
        if (Board.getInstance().getFactionAt(examine).isWilderness() && !toClaim.contains(examine)) {
            toClaim.add(examine);
            queue.add(examine);
        }
    }

    @Override
    public TL getUsageTranslation() {
        return TL.COMMAND_CLAIMFILL_DESCRIPTION;
    }
}
