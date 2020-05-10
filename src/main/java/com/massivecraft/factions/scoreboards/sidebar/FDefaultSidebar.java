package com.massivecraft.factions.scoreboards.sidebar;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FactionsPlugin;
import com.massivecraft.factions.scoreboards.SidebarTextProvider;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import it.unimi.dsi.fastutil.objects.ObjectLists;

import java.util.List;

public final class FDefaultSidebar implements SidebarTextProvider {

    @Override
    public String getTitle(FPlayer fPlayer) {
        if (FactionsPlugin.getInstance().conf().scoreboard().constant().isFactionlessEnabled() && !fPlayer.hasFaction()) {
            return SidebarTextProvider.replaceTags(fPlayer, FactionsPlugin.getInstance().conf().scoreboard().constant().getFactionlessTitle());
        }
        return SidebarTextProvider.replaceTags(fPlayer, FactionsPlugin.getInstance().conf().scoreboard().constant().getTitle());
    }

    @Override
    public List<String> getText(FPlayer fPlayer) {
        if (FactionsPlugin.getInstance().conf().scoreboard().constant().isFactionlessEnabled() && !fPlayer.hasFaction()) {
            return format(fPlayer, FactionsPlugin.getInstance().conf().scoreboard().constant().getFactionlessContent());
        }
        return format(fPlayer, FactionsPlugin.getInstance().conf().scoreboard().constant().getContent());
    }

    private List<String> format(FPlayer fPlayer, List<String> lines) {
        if (lines == null || lines.isEmpty()) {
            return ObjectLists.emptyList();
        }
        ObjectList<String> formatted = new ObjectArrayList<>(lines.size());
        for (String line : lines) {
            String replaced = SidebarTextProvider.replaceTags(fPlayer, line);
            if (replaced == null) {
                continue;
            }
            formatted.add(replaced);
        }
        return formatted;
    }
}
