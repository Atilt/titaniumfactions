package com.massivecraft.factions.meta.scoreboards.sidebar;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.FactionsPlugin;
import com.massivecraft.factions.meta.scoreboards.SidebarTextProvider;

import java.util.ArrayList;
import java.util.List;

public final class InfoSidebar implements SidebarTextProvider {

    private Faction faction;

    public InfoSidebar(Faction faction) {
        this.faction = faction;
    }

    @Override
    public String getTitle(FPlayer fPlayer) {
        return SidebarTextProvider.replaceTags(this.faction, fPlayer, FactionsPlugin.getInstance().conf().scoreboard().info().getTitle());
    }

    @Override
    public List<String> getText(FPlayer fPlayer) {
        List<String> content = FactionsPlugin.getInstance().conf().scoreboard().info().getContent();
        List<String> lines = new ArrayList<>(content.size());
        for (String line : content) {
            String replaced = SidebarTextProvider.replaceTags(this.faction, fPlayer, line);
            if (replaced == null) {
                continue;
            }
            lines.add(replaced);
        }
        return lines;
    }

    public void setFaction(Faction faction) {
        this.faction = faction;
    }
}
