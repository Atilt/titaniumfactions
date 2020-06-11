package com.massivecraft.factions.cmd;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.Factions;
import com.massivecraft.factions.FactionsPlugin;
import com.massivecraft.factions.event.FPlayerJoinEvent;
import com.massivecraft.factions.event.FactionCreateEvent;
import com.massivecraft.factions.perms.Role;
import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.util.MiscUtil;
import com.massivecraft.factions.util.TL;
import org.bukkit.Bukkit;

import java.util.List;
import java.util.UUID;


public class CmdCreate extends FCommand {

    public CmdCreate() {
        super();

        this.aliases.add("create");

        this.requiredArgs.add("faction tag");

        this.requirements = new CommandRequirements.Builder(Permission.CREATE)
                .playerOnly()
                .build();
    }

    @Override
    public void perform(CommandContext context) {
        String tag = context.argAsString(0);

        if (context.fPlayer.hasFaction()) {
            context.msg(TL.COMMAND_CREATE_MUSTLEAVE);
            return;
        }

        if (Factions.getInstance().isTagTaken(tag)) {
            context.msg(TL.COMMAND_CREATE_INUSE);
            return;
        }
        UUID owner = FactionsPlugin.getInstance().getReserveManager().getReserveOwner(tag);
        if (owner != null && !owner.equals(context.player.getUniqueId())) {
            context.msg(TL.COMMAND_CREATE_TAG_RESERVED);
            return;
        }

        List<String> tagValidationErrors = MiscUtil.validateTag(tag);
        if (tagValidationErrors.size() > 0) {
            context.sendMessage(tagValidationErrors);
            return;
        }

        // if economy is enabled, they're not on the bypass list, and this command has a cost set, make sure they can pay
        if (!context.canAffordCommand(FactionsPlugin.getInstance().conf().economy().getCostCreate(), TL.COMMAND_CREATE_TOCREATE.toString())) {
            return;
        }

        // then make 'em pay (if applicable)
        if (!context.payForCommand(FactionsPlugin.getInstance().conf().economy().getCostCreate(), TL.COMMAND_CREATE_TOCREATE, TL.COMMAND_CREATE_FORCREATE)) {
            return;
        }

        Faction faction = Factions.getInstance().createFaction();

        // TODO: Why would this even happen??? Auto increment clash??
        if (faction == null) {
            context.msg(TL.COMMAND_CREATE_ERROR);
            return;
        }

        // finish setting up the Faction
        faction.setTag(tag);

        if (owner != null) {
            FactionsPlugin.getInstance().getReserveManager().unreserve(owner);
        }

        // trigger the faction join event for the creator
        FPlayerJoinEvent joinEvent = new FPlayerJoinEvent(FPlayers.getInstance().getByPlayer(context.player), faction, FPlayerJoinEvent.PlayerJoinReason.CREATE);
        Bukkit.getPluginManager().callEvent(joinEvent);
        // join event cannot be cancelled or you'll have an empty faction

        // finish setting up the FPlayer
        context.fPlayer.setRole(Role.ADMIN);
        context.fPlayer.setFaction(faction);

        // trigger the faction creation event
        FactionCreateEvent createEvent = new FactionCreateEvent(context.player, tag, faction);
        Bukkit.getPluginManager().callEvent(createEvent);

        for (FPlayer follower : FPlayers.getInstance().getOnlinePlayers()) {
            follower.msg(TL.COMMAND_CREATE_CREATED, context.fPlayer.describeTo(follower, true), faction.getTag(follower));
        }

        context.msg(TL.COMMAND_CREATE_YOUSHOULD, FCmdRoot.getInstance().cmdDescription.getUsageTemplate(context));

        if (FactionsPlugin.getInstance().conf().logging().isFactionCreate()) {
            FactionsPlugin.getInstance().getPluginLogger().info(context.fPlayer.getName() + TL.COMMAND_CREATE_CREATEDLOG.toString() + tag);
        }
    }

    @Override
    public TL getUsageTranslation() {
        return TL.COMMAND_CREATE_DESCRIPTION;
    }
}
