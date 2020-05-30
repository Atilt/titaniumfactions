package com.massivecraft.factions.cmd;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.struct.BanInfo;
import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.util.TL;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;

public class CmdBanlist extends FCommand {

    public CmdBanlist() {
        super();

        this.aliases.add("banlist");
        this.aliases.add("bans");
        this.aliases.add("banl");

        this.optionalArgs.put("faction", "faction");

        this.requirements = new CommandRequirements.Builder(Permission.BAN)
                .playerOnly()
                .build();
    }

    @Override
    public void perform(CommandContext context) {
        Faction target = context.faction;
        if (!context.args.isEmpty()) {
            target = context.argAsFaction(0);
        }

        if (target == null) {
            context.msg(TL.COMMAND_BANLIST_INVALID.format(context.argAsString(0)));
            return;
        }

        if (!target.isNormal()) {
            context.msg(TL.COMMAND_BANLIST_NOFACTION);
            return;
        }

        int size = target.getBannedPlayers().size();
        ObjectList<String> lines = new ObjectArrayList<>(size + 1);
        lines.add(TL.COMMAND_BANLIST_HEADER.format(Integer.toString(size), target.getTag(context.faction)));

        int i = 1;

        for (BanInfo info : target.getBannedPlayers()) {
            FPlayer banned = FPlayers.getInstance().getById(info.getBanned());
            FPlayer banner = FPlayers.getInstance().getById(info.getBanner());
            lines.add(TL.COMMAND_BANLIST_ENTRY.format(Integer.toString(i), banned.getName(), banner.getName(), TL.formatDate(info.getTime())));
            i++;
        }

        for (String s : lines) {
            context.fPlayer.sendMessage(s);
        }
    }

    @Override
    public TL getUsageTranslation() {
        return TL.COMMAND_BANLIST_DESCRIPTION;
    }
}
