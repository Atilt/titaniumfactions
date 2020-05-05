package com.massivecraft.factions.cmd;

import com.massivecraft.factions.Faction;
import com.massivecraft.factions.Factions;
import com.massivecraft.factions.FactionsPlugin;
import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.tag.Tag;
import com.massivecraft.factions.util.TL;
import com.massivecraft.factions.util.TextUtil;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import org.bukkit.Bukkit;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;


public class CmdList extends FCommand {

    public CmdList() {
        super();

        this.aliases.add("list");
        this.aliases.add("ls");

        this.optionalArgs.put("page", "1");

        this.requirements = new CommandRequirements.Builder(Permission.LIST).build();
    }

    @Override
    public void perform(CommandContext context) {
        // if economy is enabled, they're not on the bypass list, and this command has a cost set, make 'em pay
        if (!context.payForCommand(FactionsPlugin.getInstance().conf().economy().getCostList(), "to list the factions", "for listing the factions")) {
            return;
        }

        List<Faction> factionList = Factions.getInstance().getAllFactions();
        factionList.remove(Factions.getInstance().getWilderness());
        factionList.remove(Factions.getInstance().getSafeZone());
        factionList.remove(Factions.getInstance().getWarZone());

        for (Faction faction : factionList) {
            Bukkit.broadcastMessage("Faction: " + faction.getTag() + " (" + faction.getFPlayersWhereOnline(true).size() + " online)");
        }

        // remove exempt factions
        if (!context.sender.hasPermission(Permission.SHOW_BYPASS_EXEMPT.toString())) {
            List<String> exemptFactions = FactionsPlugin.getInstance().conf().commands().show().getExempt();
            factionList.removeIf(next -> exemptFactions.contains(next.getTag()));
        }

        // Sort by total followers first
        factionList.sort(this.compare(Faction::getFPlayers));

        // Then sort by how many members are online now
        factionList.sort(this.compare(f -> f.getFPlayersWhereOnline(true)));

        ObjectList<String> lines = new ObjectArrayList<>();

        factionList.add(0, Factions.getInstance().getWilderness());

        final int pageheight = 9;
        int pagenumber = context.argAsInt(0, 1);
        int pagecount = (factionList.size() / pageheight) + 1;
        if (pagenumber > pagecount) {
            pagenumber = pagecount;
        } else if (pagenumber < 1) {
            pagenumber = 1;
        }
        int start = (pagenumber - 1) * pageheight;
        int end = start + pageheight;
        if (end > factionList.size()) {
            end = factionList.size();
        }


        String header = plugin.conf().commands().list().getHeader();
        String footer = plugin.conf().commands().list().getFooter();

        String pageNumberReadable = Integer.toString(pagenumber);
        String pageCountReadable = Integer.toString(pagecount);

        if (!header.isEmpty()) {
            header = header.replace("{pagenumber}", pageNumberReadable).replace("{pagecount}", pageCountReadable);
            lines.add(TextUtil.parse(header));
        }

        for (Faction faction : factionList.subList(start, end)) {
            System.out.println(faction.getTag());
            if (faction.isWilderness()) {
                System.out.println("is wild");
                lines.add(TextUtil.parse(Tag.parsePlain(faction, plugin.conf().commands().list().getFactionlessEntry())));
                continue;
            }
            System.out.println("reg?");
            lines.add(TextUtil.parse(Tag.parsePlain(faction, context.fPlayer, plugin.conf().commands().list().getEntry())));
            System.out.println(TextUtil.parse(Tag.parsePlain(faction, context.fPlayer, plugin.conf().commands().list().getEntry())));
            System.out.println(Tag.parsePlain(faction, context.fPlayer, plugin.conf().commands().list().getEntry()));
            System.out.println(plugin.conf().commands().list().getEntry());
        }

        if (!footer.isEmpty()) {
            footer = footer.replace("{pagenumber}", pageNumberReadable).replace("{pagecount}", pageCountReadable);
            lines.add(TextUtil.parse(footer));
        }

        context.sendMessage(lines);
    }

    private Comparator<Faction> compare(Function<Faction, ? extends Collection<?>> func) {
        return (f1, f2) -> {
            int f1Size = func.apply(f1).size();
            int f2Size = func.apply(f2).size();
            if (f1Size < f2Size) {
                return 1;
            } else if (f1Size > f2Size) {
                return -1;
            }
            return 0;
        };
    }

    @Override
    public TL getUsageTranslation() {
        return TL.COMMAND_LIST_DESCRIPTION;
    }
}