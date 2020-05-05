package com.massivecraft.factions.cmd;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.FactionsPlugin;
import com.massivecraft.factions.perms.PermissibleAction;
import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.util.TL;
import net.kyori.text.TextComponent;
import net.kyori.text.event.ClickEvent;
import net.kyori.text.event.HoverEvent;
import net.kyori.text.format.TextColor;
import org.bukkit.Bukkit;

import java.util.UUID;

public class CmdDeinvite extends FCommand {

    public CmdDeinvite() {
        super();

        this.aliases.add("deinvite");
        this.aliases.add("deinv");

        this.optionalArgs.put("player", "player");
        //this.optionalArgs.put("", "");

        this.requirements = new CommandRequirements.Builder(Permission.DEINVITE)
                .memberOnly()
                .withAction(PermissibleAction.INVITE)
                .build();
    }

    @Override
    public void perform(CommandContext context) {
        FPlayer you = context.argAsBestFPlayerMatch(0);
        if (you == null) {
            TextComponent msg = TL.COMMAND_DEINVITE_CANDEINVITE.toComponent().color(TextColor.GOLD);
            for (UUID id : context.faction.getInvites()) {
                FPlayer fp = FPlayers.getInstance().getById(id);
                String name = fp != null ? fp.getName() : Bukkit.getOfflinePlayer(id).getName();
                msg.append(TextComponent.of(name + " ").color(TextColor.WHITE).hoverEvent(HoverEvent.showText(TL.COMMAND_DEINVITE_CLICKTODEINVITE.toFormattedComponent(name)))).clickEvent(ClickEvent.runCommand("/" + FactionsPlugin.getInstance().conf().getCommandBase().get(0) + " deinvite " + name));
            }
            context.sendFancyMessage(msg);
            return;
        }

        if (you.getFaction() == context.faction) {
            context.msg(TL.COMMAND_DEINVITE_ALREADYMEMBER, you.getName(), context.faction.getTag());
            context.msg(TL.COMMAND_DEINVITE_MIGHTWANT, FCmdRoot.getInstance().cmdKick.getUsageTemplate(context));
            return;
        }

        context.faction.deinvite(you);

        you.msg(TL.COMMAND_DEINVITE_REVOKED, context.fPlayer.describeTo(you), context.faction.describeTo(you));

        context.faction.msg(TL.COMMAND_DEINVITE_REVOKES, context.fPlayer.describeTo(context.faction), you.describeTo(context.faction));
    }

    @Override
    public TL getUsageTranslation() {
        return TL.COMMAND_DEINVITE_DESCRIPTION;
    }

}
