package com.massivecraft.factions.cmd;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FactionsPlugin;
import com.massivecraft.factions.perms.PermissibleAction;
import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.util.TL;
import net.kyori.text.TextComponent;
import net.kyori.text.adapter.bukkit.TextAdapter;
import net.kyori.text.event.ClickEvent;
import net.kyori.text.event.HoverEvent;
import net.kyori.text.format.TextColor;

public class CmdInvite extends FCommand {

    public CmdInvite() {
        super();

        this.aliases.add("invite");
        this.aliases.add("inv");

        this.requiredArgs.add("player");

        this.requirements = new CommandRequirements.Builder(Permission.INVITE)
                .memberOnly()
                .withAction(PermissibleAction.INVITE)
                .build();
    }

    @Override
    public void perform(CommandContext context) {
        FPlayer target = context.argAsBestFPlayerMatch(0);
        if (target == null) {
            return;
        }

        if (target.getFaction() == context.faction) {
            context.msg(TL.COMMAND_INVITE_ALREADYMEMBER, target.getName(), context.faction.getTag());
            context.msg(TL.GENERIC_YOUMAYWANT.toString() + FCmdRoot.getInstance().cmdKick.getUsageTemplate(context));
            return;
        }

        // if economy is enabled, they're not on the bypass list, and this command has a cost set, make 'em pay
        if (!context.payForCommand(FactionsPlugin.getInstance().conf().economy().getCostInvite(), TL.COMMAND_INVITE_TOINVITE.toString(), TL.COMMAND_INVITE_FORINVITE.toString())) {
            return;
        }

        if (context.faction.isBanned(target)) {
            context.msg(TL.COMMAND_INVITE_BANNED, target.getName());
            return;
        }

        context.faction.invite(target);
        if (!target.isOnline()) {
            return;
        }

        // Tooltips, colors, and commands only apply to the string immediately before it.
        TextComponent message = TextComponent.of(context.fPlayer.describeTo(target, true))
                .hoverEvent(HoverEvent.showText(TL.COMMAND_INVITE_CLICKTOJOIN.toComponent()))
                .clickEvent(ClickEvent.runCommand("/" + FactionsPlugin.getInstance().conf().getCommandBase().get(0) + " join " + context.faction.getTag()))
                .append(TL.COMMAND_INVITE_INVITEDYOU.toComponent())
                .color(TextColor.YELLOW);

        TextAdapter.sendMessage(target.getPlayer(), message);

        //you.msg("[]<i> invited you to []",context.fPlayer.describeTo(you, true), context.faction.describeTo(you));
        context.faction.msg(TL.COMMAND_INVITE_INVITED, context.fPlayer.describeTo(context.faction, true), target.describeTo(context.faction));
    }

    @Override
    public TL getUsageTranslation() {
        return TL.COMMAND_INVITE_DESCRIPTION;
    }

}
