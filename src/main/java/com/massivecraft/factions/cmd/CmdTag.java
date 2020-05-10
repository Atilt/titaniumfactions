package com.massivecraft.factions.cmd;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.Factions;
import com.massivecraft.factions.FactionsPlugin;
import com.massivecraft.factions.event.FactionRenameEvent;
import com.massivecraft.factions.perms.Role;
import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.util.MiscUtil;
import com.massivecraft.factions.util.TL;
import org.bukkit.Bukkit;

import java.util.List;

public class CmdTag extends FCommand {

    public CmdTag() {
        super();

        this.aliases.add("tag");
        this.aliases.add("rename");

        this.requiredArgs.add("faction tag");

        this.requirements = new CommandRequirements.Builder(Permission.TAG)
                .memberOnly()
                .withRole(Role.MODERATOR)
                .build();
    }

    @Override
    public void perform(CommandContext context) {
        String tag = context.argAsString(0);

        // TODO does not first shouldCancel cover selfcase?
        if (Factions.getInstance().isTagTaken(tag) && !MiscUtil.getComparisonString(tag).equals(context.faction.getComparisonTag())) {
            context.msg(TL.COMMAND_TAG_TAKEN);
            return;
        }

        List<String> errors = MiscUtil.validateTag(tag);
        if (!errors.isEmpty()) {
            context.sendMessage(errors);
            return;
        }

        // if economy is enabled, they're not on the bypass list, and this command has a cost set, make sure they can pay
        if (!context.canAffordCommand(FactionsPlugin.getInstance().conf().economy().getCostTag(), TL.COMMAND_TAG_TOCHANGE.toString())) {
            return;
        }

        // trigger the faction rename event (cancellable)
        FactionRenameEvent renameEvent = new FactionRenameEvent(context.fPlayer, tag);
        Bukkit.getPluginManager().callEvent(renameEvent);
        if (renameEvent.isCancelled()) {
            return;
        }

        // then make 'em pay (if applicable)
        if (!context.payForCommand(FactionsPlugin.getInstance().conf().economy().getCostTag(), TL.COMMAND_TAG_TOCHANGE, TL.COMMAND_TAG_FORCHANGE)) {
            return;
        }

        context.faction.setTag(tag);

        // Inform
        for (FPlayer fplayer : FPlayers.getInstance().getOnlinePlayers()) {
            if (fplayer.getFactionIdRaw() == context.faction.getIdRaw()) {
                fplayer.msg(TL.COMMAND_TAG_FACTION, context.fPlayer.describeTo(context.faction, true), context.faction.getTag(context.faction));
                continue;
            }

            // Broadcast the tag change (if applicable)
            if (FactionsPlugin.getInstance().conf().factions().chat().isBroadcastTagChanges()) {
                Faction faction = fplayer.getFaction();
                fplayer.msg(TL.COMMAND_TAG_CHANGED, context.fPlayer.getColorTo(faction) + context.faction.getTag(), context.faction.getTag(faction));
            }
        }
    }

    @Override
    public TL getUsageTranslation() {
        return TL.COMMAND_TAG_DESCRIPTION;
    }

}
