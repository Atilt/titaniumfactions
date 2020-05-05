package com.massivecraft.factions.cmd;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.FactionsPlugin;
import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.util.TL;
import net.kyori.text.TextComponent;
import net.kyori.text.event.ClickEvent;
import net.kyori.text.event.HoverEvent;
import net.kyori.text.format.TextColor;
import org.bukkit.Bukkit;

import java.util.UUID;

public class CmdShowInvites extends FCommand {

    public CmdShowInvites() {
        super();

        this.aliases.add("showinvites");

        this.requirements = new CommandRequirements.Builder(Permission.SHOW_INVITES)
                .memberOnly()
                .build();
    }

    @Override
    public void perform(CommandContext context) {
        TextComponent msg = TL.COMMAND_SHOWINVITES_PENDING.toComponent().color(TextColor.GOLD);
        for (UUID id : context.faction.getInvites()) {
            FPlayer fp = FPlayers.getInstance().getById(id);
            String name = fp != null ? fp.getName() : Bukkit.getOfflinePlayer(id).getName();
            msg.append(TextComponent.of(name + " "))
               .color(TextColor.WHITE)
               .hoverEvent(HoverEvent.showText(TL.COMMAND_SHOWINVITES_CLICKTOREVOKE.toFormattedComponent(name)))
               .clickEvent(ClickEvent.runCommand("/" + FactionsPlugin.getInstance().conf().getCommandBase().get(0) + " deinvite " + name));
        }
        context.sendFancyMessage(msg);
    }

    @Override
    public TL getUsageTranslation() {
        return TL.COMMAND_SHOWINVITES_DESCRIPTION;
    }


}
