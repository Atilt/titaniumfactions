package com.massivecraft.factions.cmd.claim;

import com.massivecraft.factions.FLocation;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.FactionsPlugin;
import com.massivecraft.factions.cmd.CommandContext;
import com.massivecraft.factions.cmd.CommandRequirements;
import com.massivecraft.factions.cmd.FCommand;
import com.massivecraft.factions.perms.PermissibleAction;
import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.tasks.CompactSpiralTask;
import com.massivecraft.factions.tasks.SpiralTask;
import com.massivecraft.factions.util.TL;
import net.kyori.text.TextComponent;
import net.kyori.text.adapter.bukkit.TextAdapter;
import net.kyori.text.event.HoverEvent;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;


public class CmdClaim extends FCommand {

    private final Set<UUID> claiming = new HashSet<>();

    private static final int MAX_RADIUS = 100;

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

        if (radius > MAX_RADIUS) {
            context.msg(TL.COMMAND_CLAIM_MAXRADIUS);
            return;
        }

        if (radius < 1) {
            context.msg(TL.COMMAND_CLAIM_INVALIDRADIUS);
            return;
        }
        Faction forFaction = context.argAsFaction(1, context.faction); // Default to own

        if (radius < 2) {
            // single chunk
            context.fPlayer.attemptClaim(forFaction, context.player.getLocation(), true);
        } else {
            // radius claim
            if (!Permission.CLAIM_RADIUS.has(context.sender, false)) {
                context.msg(TL.COMMAND_CLAIM_DENIED);
                return;
            }
            if (!this.claiming.add(context.player.getUniqueId())) {
                context.msg(TL.COMMAND_CLAIM_ALREADY_OCCURING);
                return;
            }
            if (FactionsPlugin.getInstance().conf().factions().claims().shouldOptimizeClaiming()) {
                if (!forFaction.hasAccess(context.fPlayer, PermissibleAction.TERRITORY)) {
                    context.sendMessage(TL.CLAIM_CANTCLAIM.format(forFaction.describeTo(context.fPlayer)));
                    this.claiming.remove(context.player.getUniqueId());
                    return;
                }
                new CompactSpiralTask(context.player.getWorld(), radius, FLocation.wrap(context.player), context.fPlayer, context.faction) {
                    @Override
                    public void onFinish() {
                        claiming.remove(context.player.getUniqueId());
                        List<Player> onlineMembers = forFaction.getOnlinePlayers();

                        TextAdapter.sendMessage(onlineMembers, TL.CLAIM_CLAIMEDCOMPACT_ANNOUNCE.toFormattedComponent(context.fPlayer.getName(), "your faction"));

                        int successSize = this.getSuccessSize();
                        int failureSize  = this.getFailureSize();

                        TextComponent.Builder success = TL.CLAIM_CLAIMEDCOMPACT_SUCCESSES.toFormattedComponent(successSize > 0 ? Integer.toString(successSize) : "None").toBuilder();
                        if (successSize > 0) {
                            success.hoverEvent(HoverEvent.showText(TextComponent.of(String.join("\n", this.getSuccesses().subList(0, Math.min(successSize, MAX_DEBUG))))));
                        }
                        TextComponent.Builder failure = TL.CLAIM_CLAIMEDCOMPACT_FAILURES.toFormattedComponent(failureSize > 0 ? Integer.toString(failureSize) : "None").toBuilder();
                        if (failureSize > 0) {
                            failure.hoverEvent(HoverEvent.showText(TextComponent.of(String.join("\n", this.getFailures().subList(0, Math.min(failureSize, MAX_DEBUG))))));
                        }
                        TextAdapter.sendMessage(onlineMembers, success.build());
                        TextAdapter.sendMessage(onlineMembers, failure.build());
                    }
                }.start();
                return;
            }
            new SpiralTask(FLocation.wrap(context.player), radius, () -> this.claiming.remove(context.player.getUniqueId())) {
                private int failCount = 0;
                private final int limit = FactionsPlugin.getInstance().conf().factions().claims().getRadiusClaimFailureLimit() - 1;

                @Override
                public boolean work() {
                    boolean success = context.fPlayer.attemptClaim(forFaction, this.currentFLocation(), true);
                    if (success) {
                        this.failCount = 0;
                    } else if (this.failCount++ >= this.limit) {
                        this.stop();
                        return false;
                    }
                    return true;
                }
            };
        }
    }

    @Override
    public TL getUsageTranslation() {
        return TL.COMMAND_CLAIM_DESCRIPTION;
    }

}
