package com.massivecraft.factions.meta.scoreboards.sidebar;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FactionsPlugin;
import com.massivecraft.factions.meta.scoreboards.SidebarTextProvider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class DefaultSidebar implements SidebarTextProvider {

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
            return Collections.emptyList();
        }
        List<String> formatted = new ArrayList<>(lines.size());
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
