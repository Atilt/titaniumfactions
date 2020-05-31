package com.massivecraft.factions.cmd;

import com.massivecraft.factions.Faction;
import com.massivecraft.factions.Factions;
import com.massivecraft.factions.FactionsPlugin;
import com.massivecraft.factions.landraidcontrol.PowerControl;
import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.util.TL;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import org.bukkit.entity.Player;

import java.util.Comparator;
import java.util.List;

//TODO: implement spawner values
public class CmdTop extends FCommand {

    private static final int PAGE_HEIGHT = 9;

    public CmdTop() {
        super();

        this.aliases.add("top");
        this.aliases.add("t");

        this.requiredArgs.add("criteria");
        this.optionalArgs.put("page", "1");

        this.requirements = new CommandRequirements.Builder(Permission.TOP).noDisableOnLock().build();
    }

    @Override
    public void perform(CommandContext context) {
        List<Faction> factionList = Factions.getInstance().getAllNormalFactions();

        String criteria = context.argAsString(0);

        if (criteria.equalsIgnoreCase("members")) {
            factionList.sort(Comparator.comparingInt(Faction::getSize).reversed());
        } else if (criteria.equalsIgnoreCase("start")) {
            factionList.sort(Comparator.comparingLong(Faction::getFoundedDate));
        } else if (criteria.equalsIgnoreCase("power") && FactionsPlugin.getInstance().getLandRaidControl() instanceof PowerControl) {
            factionList.sort(Comparator.comparingInt(Faction::getPowerRounded).reversed());
        } else if (criteria.equalsIgnoreCase("land")) {
            factionList.sort(Comparator.comparingInt(Faction::getLandRounded).reversed());
        } else if (criteria.equalsIgnoreCase("online")) {
            factionList.sort(Comparator.comparingInt(Faction::getTotalOnline).reversed());
        } else if (criteria.equalsIgnoreCase("money") || criteria.equalsIgnoreCase("balance") || criteria.equalsIgnoreCase("bal")) {
            if (!FactionsPlugin.getInstance().conf().economy().isEnabled()) {
                context.msg("<b>Faction economy features are disabled on this server.");
                return;
            }
            factionList.sort(Comparator.comparingDouble(Faction::getTotalMoney).reversed());
        } else {
            context.msg(TL.COMMAND_TOP_INVALID, criteria);
            return;
        }

        int pagecount = (factionList.size() / PAGE_HEIGHT) + 1;
        int pagenumber = Math.max(1, Math.min(context.argAsInt(1, 1), pagecount));

        int start = (pagenumber - 1) * PAGE_HEIGHT;

        int end = Math.min(start + PAGE_HEIGHT, factionList.size());

        List<Faction> factions = factionList.subList(start, end);
        ObjectList<String> lines = new ObjectArrayList<>(factionList.size() + 1);

        lines.add(TL.COMMAND_TOP_TOP.format(criteria.toUpperCase(), Integer.toString(pagenumber), Integer.toString(pagecount)));

        for (int rank = 0; rank < factions.size(); rank++) {
            Faction faction = factions.get(rank);
            lines.add(TL.COMMAND_TOP_LINE.format(Integer.toString(rank + 1), context.sender instanceof Player ? faction.getRelationTo(context.fPlayer).getColor() + faction.getTag() : faction.getTag(), getValue(faction, criteria)));
        }

        context.sendMessage(lines);
    }

    private String getValue(Faction faction, String criteria) {
        if (criteria.equalsIgnoreCase("members")) {
            return Integer.toString(faction.getSize());
        }
        if (criteria.equalsIgnoreCase("start")) {
            return TL.formatDate(faction.getFoundedDate());
        }
        if (criteria.equalsIgnoreCase("power") && FactionsPlugin.getInstance().getLandRaidControl() instanceof PowerControl) {
            return Integer.toString(faction.getPowerRounded());
        }
        if (criteria.equalsIgnoreCase("land")) {
            return Integer.toString(faction.getLandRounded());
        }
        if (criteria.equalsIgnoreCase("online")) {
            return Integer.toString(faction.getTotalOnline());
        }
        if (criteria.equalsIgnoreCase("money") || criteria.equalsIgnoreCase("balance") || criteria.equalsIgnoreCase("bal")) {
            return Double.toString(faction.getTotalMoney());
        }
        return "";
    }

    @Override
    public TL getUsageTranslation() {
        return TL.COMMAND_TOP_DESCRIPTION;
    }
}
