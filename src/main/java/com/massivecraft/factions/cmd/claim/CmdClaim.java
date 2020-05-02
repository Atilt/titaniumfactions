package com.massivecraft.factions.cmd.claim;

import com.massivecraft.factions.FLocation;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.FactionsPlugin;
import com.massivecraft.factions.cmd.CommandContext;
import com.massivecraft.factions.cmd.CommandRequirements;
import com.massivecraft.factions.cmd.FCommand;
import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.util.SpiralTask;
import com.massivecraft.factions.util.TL;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import it.unimi.dsi.fastutil.objects.ObjectSet;

import java.util.UUID;


public class CmdClaim extends FCommand {

    private final ObjectSet<UUID> claiming = new ObjectOpenHashSet<>();

    public CmdClaim() {
        super();
        this.aliases.add("claim");

        this.optionalArgs.put("radius", "1");
        this.optionalArgs.put("faction", "your");

        this.requirements = new CommandRequirements.Builder(Permission.CLAIM)
                .playerOnly()
                .build();
    }

    @Override
    public void perform(final CommandContext context) {
        // Read and validate input
        int radius = context.argAsInt(0, 1); // Default to 1
        final Faction forFaction = context.argAsFaction(1, context.faction); // Default to own

        if (radius < 1) {
            context.msg(TL.COMMAND_CLAIM_INVALIDRADIUS);
            return;
        }

        if (radius < 2) {
            // single chunk
            context.fPlayer.attemptClaim(forFaction, context.player.getLocation(), true);
        } else {
            // radius claim
            if (!Permission.CLAIM_RADIUS.has(context.sender, false)) {
                context.msg(TL.COMMAND_CLAIM_DENIED);
                return;
            }
            if (this.claiming.add(context.player.getUniqueId())) {
                new SpiralTask(FLocation.wrap(context.player), radius, () -> this.claiming.remove(context.player.getUniqueId())) {
                    private int failCount = 0;
                    private final int limit = FactionsPlugin.getInstance().conf().factions().claims().getRadiusClaimFailureLimit() - 1;

                    @Override
                    public boolean work() {
                        boolean success = context.fPlayer.attemptClaim(forFaction, this.currentFLocation(), true);
                        if (success) {
                            failCount = 0;
                        } else if (failCount++ >= limit) {
                            this.stop();
                            return false;
                        }

                        return true;
                    }
                };
                return;
            }
            context.msg(TL.COMMAND_CLAIM_ALREADY_OCCURING);
        }
    }

    @Override
    public TL getUsageTranslation() {
        return TL.COMMAND_CLAIM_DESCRIPTION;
    }

}
